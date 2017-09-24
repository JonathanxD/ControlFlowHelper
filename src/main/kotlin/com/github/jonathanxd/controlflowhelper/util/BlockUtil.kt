/**
 *
 *  Copyright 2012 Tobias Gierke <tobias.gierke@code-sourcery.de>
 *
 *  Original project:
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
package com.github.jonathanxd.controlflowhelper.util

import com.github.jonathanxd.controlflowhelper.*

fun BasicBlock.hasRegularSuccessor(): Boolean =
        this.successors.any { isRegularSuccessor(it) }

fun Edge.isSuccessor(block: BasicBlock) =
        this.block == block

fun Edge.isPredecessor(block: BasicBlock) =
        this.block == block

private fun BasicBlock.isRegularSuccessor(e: Edge): Boolean = isRegular(e) && e.isSuccessor(this)

private fun isRegular(e: Edge): Boolean =
        when (e.type) {
            is NormalEdgeType,
            is TrueEdgeType,
            is FalseEdgeType -> true
            else -> false
        }

private fun BasicBlock.isRegularPredecessor(e: Edge): Boolean =
        isRegular(e) && e.isPredecessor(this)

fun BasicBlock.hasRegularPredecessor(): Boolean =
        this.predecessors.any { isRegularPredecessor(it) }

val BasicBlock.regularSuccessor: BasicBlock get() = this.regularSuccessors.single()

val BasicBlock.regularPredecessor: BasicBlock get() = this.regularPredecessors.single()

val BasicBlock.regularSuccessors: Set<BasicBlock>
    get() = this.successors
            .filter { isRegularSuccessor(it) }
            .map { it.block }
            .toMutableSet()

val BasicBlock.regularPredecessors: Set<BasicBlock>
    get() =
        this.predecessors
                .filter { isRegularPredecessor(it) }
                .map { it.block }
                .toMutableSet()