/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.core.io

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.bits.Memory
import io.ktor.utils.io.bits.of

actual suspend fun ByteReadChannel.peek(bytes: Int, buffer: ByteArray): ByteArray {
  val memory = Memory.of(buffer, 0, bytes)
  peekTo(memory, 0, 0, bytes.toLong(), bytes.toLong())
  return buffer
}

