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
package com.github.jonathanxd.controlflowhelper.util

import org.objectweb.asm.Opcodes.*

/**
 * Returns true if this is an valid jmp.
 *
 * From Kores-BytecodeReader OpcodeUtil
 */
fun Int.isValidIfExprJmp() = when (this) {
    IFEQ, IF_ICMPEQ, IF_ACMPEQ, IFNULL,
    IFNE, IF_ICMPNE, IF_ACMPNE, IFNONNULL,
    IFLT, IF_ICMPLT, IFLE, IF_ICMPLE,
    IFGT, IF_ICMPGT, IFGE, IF_ICMPGE -> true
    else -> false
}

/**
 * Returns true if this is an valid jmp.
 *
 * From Kores-BytecodeReader OpcodeUtil
 */
fun Int.isGoto() = this == GOTO
