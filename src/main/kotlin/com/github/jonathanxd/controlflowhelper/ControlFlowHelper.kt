/**
 *
 *  Copyright 2012 Tobias Gierke <tobias.gierke@code-sourcery.de>
 *
 *  Original project: https://github.com/toby1984/controlflow
 *
 *  2017 Rewrite in Kotlin by JonathanxD <https://github.com/JonathanxD>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.jonathanxd.controlflowhelper

import com.github.jonathanxd.controlflowhelper.util.*
import com.github.jonathanxd.iutils.iterator.IteratorUtil
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

object ControlFlowHelper {

    fun analyze(mn: MethodNode): List<BasicBlock> {
        val insns = mn.instructions.toArray()
        val iterator = IteratorUtil.listIteratorOfArray(*insns)

        val blocks = BlockList()

        val mentry = MethodEntry(0, mn.instructions.size())
        val methodExit = MethodExit(mn.instructions.size(), mn.instructions.size())

        var last: BasicBlock? = null
        var edgeType: EdgeType? = null

        while (iterator.hasNext()) {
            val index = iterator.nextIndex()
            val next = iterator.next()

            val current = blocks.getBlock(index)

            if (last != null) {
                last.addSuccessor(current, edgeType ?: EdgeTypes.normal)
                current.addPredecessor(last, edgeType ?: EdgeTypes.normal)
                edgeType = null
            }

            last = current

            if (next is InsnNode) {
                val opcode = next.getOpcode()

                when (opcode) {
                    Opcodes.ARETURN, Opcodes.IRETURN, Opcodes.DRETURN, Opcodes.FRETURN, Opcodes.LRETURN, Opcodes.RET -> {
                        current.addSuccessor(methodExit, EdgeTypes.normal)
                        methodExit.addPredecessor(current, EdgeTypes.normal)
                        last = null
                    }
                    Opcodes.RETURN, Opcodes.ATHROW -> {
                        last = null
                    }
                    else -> {
                    }
                }
            } else if (next is JumpInsnNode) {
                val target = insns.indexOfFirst { it is LabelNode && it.label == next.label.label }

                val jumpExpr = next.opcode.isValidIfExprJmp()
                val gotoExpr = next.opcode.isGoto()

                if (jumpExpr) {
                    edgeType = EdgeTypes.falseType
                }

                val targetBlock = blocks.getBlock(target)

                targetBlock.addPredecessor(current, EdgeTypes.normal)

                val type = when {
                    jumpExpr -> EdgeTypes.trueType
                    gotoExpr -> EdgeTypes.jump(next)
                    else -> EdgeTypes.normal
                }

                current.addSuccessor(targetBlock, type)

                if (target == Opcodes.GOTO) {
                    last = null
                }
            }


            if (!iterator.hasNext()) {
                current.addSuccessor(methodExit, EdgeTypes.normal)
                methodExit.addPredecessor(current, EdgeTypes.normal)
            }
        }

        // TryCatch
        @Suppress("UNCHECKED_CAST")
        val tryBlocks = mn.tryCatchBlocks as List<TryCatchBlockNode>

        for (node in tryBlocks) {
            val startTarget = insns.indexOfFirst { it is LabelNode && it.label == node.start.label }

            val endTarget = insns.indexOfFirst { it is LabelNode && it.label == node.end.label }

            val handlerTarget =
                insns.indexOfFirst { it is LabelNode && it.label == node.handler.label }

            val handler = blocks.getBlock(handlerTarget)

            (startTarget..endTarget)
                .filter { it != handlerTarget }
                .forEach { blocks.getBlock(it).addSuccessor(handler, EdgeTypes.exception(node)) }
        }

        val newBlocks = mergeBlocks(blocks, mn).toMutableList()

        val lowest = blocks.map { it.entryPoint }.min()
                ?: Integer.MAX_VALUE

        val firstBlock = blocks.first { it.entryPoint == lowest }
        if (firstBlock.hasRegularPredecessor()) {
            throw IllegalStateException(firstBlock.toString() + " that constrains first instruction has a predecessor?")
        }

        mentry.addSuccessor(firstBlock, EdgeTypes.normal)
        firstBlock.addPredecessor(mentry, EdgeTypes.normal)
        newBlocks.add(0, mentry)

        newBlocks.add(methodExit)

        return newBlocks.toList()
    }

    private fun mergeBlocks(blocks: BlockList, mn: MethodNode): List<BasicBlock> {
        val instructions = mn.instructions.toArray()

        val lines = blocks.map { it.entryPoint }.sorted()
        if (lines.isEmpty()) {
            throw IllegalStateException("Method with no lines?")
        }

        (0 until lines.size - 2)
            .filter { lines[it] + 1 != lines[it + 1] }
            .forEach { throw IllegalStateException("Missing line " + (lines[it] + 1)) }

        val sorted = arrayOfNulls<Block>(lines.size)

        for (bl in blocks) {
            val lineNo = bl.entryPoint
            val block = bl

            sorted[lineNo] = block
            block.addInsn(instructions[lineNo])
        }

        val sortedList = sorted.requireNoNulls().toMutableList()

        var merged = false
        do {
            merged = false
            var i = 0
            while (!merged && sortedList.size > 1 && i + 1 < sortedList.size) {
                val current = sortedList[i]
                val next = sortedList[i + 1]

                val predecessors = next.predecessors

                if (getSuccessorCountIgnoringEndBlock(current) == 1 && predecessors.size == 1) {
                    if (getSuccessorIgnoringEndBlock(current) === next && next.regularPredecessor === current) {

                        merged = true
                        sortedList.removeAt(i + 1)
                        i--

                        current.addInsn(next)

                        current.removeRegularSuccessor(next)

                        next.successors.forEach {
                            val succ = it.dest
                            succ.blockReplaced(next, current)
                            current.addSuccessor(succ, it.type)
                        }

                        next.predecessors.forEach {
                            val pred = it.src
                            if (pred !== current) {
                                pred.blockReplaced(next, current)
                            }
                        }
                    }
                }
                i++
            }
        } while (merged)

        return sortedList
    }

    private fun getSuccessorCountIgnoringEndBlock(block: BasicBlock): Int =
        block.regularSuccessors.count { it !is MethodExit }

    private fun getSuccessorIgnoringEndBlock(block: BasicBlock): BasicBlock? {
        val successors = block.regularSuccessors
        if (successors.size >= 0) {
            if (successors.size > 2) {
                return block.regularSuccessor
            }

            return successors.firstOrNull { it !is MethodExit }
        }
        return null
    }
}