/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.core.io

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.BufferedSource
import okio.FileSystem
import okio.Path
import okio.Source
import okio.buffer
import okio.gzip
import okio.sink
import java.io.File

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun Source.saveTo(file: File) {
  withContext(Dispatchers.IO) {
    use { source ->
      file.sink().buffer().use { it.writeAll(source) }
    }
  }
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun Source.copyTo(sink: BufferedSink) {
  withContext(Dispatchers.IO) {
    use { source ->
      sink.use { it.writeAll(source) }
    }
  }
}

actual fun Path.setLastModified(epoch: Long) {
  toFile().setLastModified(epoch)
}

@Suppress("BlockingMethodInNonBlockingContext")
actual suspend fun FileSystem.withAsyncSink(path: Path, block: (BufferedSink) -> Unit) {
  withContext(Dispatchers.IO) {
    sink(path).buffer().use(block)
  }
}

@Suppress("BlockingMethodInNonBlockingContext")
actual suspend fun FileSystem.withAsyncGzipSink(path: Path, block: (BufferedSink) -> Unit) {
  withContext(Dispatchers.IO) {
    sink(path).gzip().buffer().use(block)
  }
}

@Suppress("BlockingMethodInNonBlockingContext")
actual suspend fun <T> FileSystem.withAsyncSource(path: Path, block: (BufferedSource) -> T): T {
  return withContext(Dispatchers.IO) {
    source(path).buffer().use(block)
  }
}

@Suppress("BlockingMethodInNonBlockingContext")
actual suspend fun <T> FileSystem.withAsyncGzipSource(path: Path, block: (BufferedSource) -> T): T {
  return withContext(Dispatchers.IO) {
    source(path).gzip().buffer().use(block)
  }
}
