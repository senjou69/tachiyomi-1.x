/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.core.http

import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun ByteReadChannel.saveTo(file: File) {
  withContext(Dispatchers.IO) {
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    file.outputStream().use { output ->
      do {
        val read = readAvailable(buffer, 0, DEFAULT_BUFFER_SIZE)
        if (read > 0) {
          output.write(buffer, 0, read)
        }
      } while (read >= 0)
    }
  }
}
