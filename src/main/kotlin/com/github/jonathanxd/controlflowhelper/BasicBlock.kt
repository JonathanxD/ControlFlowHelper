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

import com.github.jonathanxd.controlflowhelper.util.LateInt
import com.github.jonathanxd.controlflowhelper.util.isRegularSuccessor

/**
 * <a href=https://en.wikipedia.org/wiki/Basic_block>Basic Block</a>
 */
abstract class BasicBlock(val entryPoint: Int,
                          private val lateEndPoint: LateInt) {

    val successors: MutableList<Edge> = mutableListOf()
    val predecessors: MutableList<Edge> = mutableListOf()

    constructor(entryPoint: Int) : this(entryPoint, LateInt())
    constructor(entryPoint: Int, endPoint: Int) : this(entryPoint, LateInt(endPoint))

    val endPoint: Int
        get() = lateEndPoint.value

    fun addSuccessor(edge: Edge) {
        this.successors.add(edge)
    }

    fun addSuccessor(out: BasicBlock, type: EdgeType) {
        this.addSuccessor(Edge(this, out, type))
    }

    fun addPredecessor(edge: Edge) {
        this.predecessors.add(edge)
    }

    fun addPredecessor(`in`: BasicBlock, type: EdgeType) {
        this.addPredecessor(Edge(`in`, this, type))
    }

    fun initEndPoint(end: Int) {
        lateEndPoint.init(end)
    }

    fun blockReplaced(oldBlock: BasicBlock, newBlock: BasicBlock) {
        this.successors.replaceAll {
            when (oldBlock) {
                it.src -> it.copy(src = newBlock)
                it.dest -> it.copy(dest = oldBlock)
                else -> it
            }
        }
        this.predecessors.replaceAll {
            when (oldBlock) {
                it.src -> it.copy(src = newBlock)
                it.dest -> it.copy(dest = oldBlock)
                else -> it
            }
        }
    }

    fun removeRegularSuccessor(block: BasicBlock): Boolean {
        val iter = this.successors.iterator()

        while (iter.hasNext()) iter.next().let {
            this.isRegularSuccessor(it) && it.dest == block
            iter.remove()
            return true
        }

        return false
    }

}