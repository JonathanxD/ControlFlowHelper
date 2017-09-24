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
package com.github.jonathanxd.controlflowhelper

import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.TryCatchBlockNode

interface EdgeType


object EdgeTypes {
    val normal = NormalEdgeType
    val trueType = TrueEdgeType
    val falseType = FalseEdgeType

    fun jump(node: JumpInsnNode): EdgeType =
            JumpEdgeType(node)

    fun exception(node: TryCatchBlockNode): EdgeType =
            ExceptionEdgeType(node)


}

object NormalEdgeType : EdgeType
object TrueEdgeType : EdgeType
object FalseEdgeType : EdgeType

data class JumpEdgeType(val node: JumpInsnNode) : EdgeType

data class ExceptionEdgeType(val node: TryCatchBlockNode): EdgeType