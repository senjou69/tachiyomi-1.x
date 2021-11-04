/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

@file:JvmName("ImagePainterPlatform")

package tachiyomi.ui.core.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil.annotation.ExperimentalCoilApi

actual typealias ImageRequest = coil.request.ImageRequest

actual typealias ImageMetadata = coil.request.ImageResult.Metadata

actual interface ImageRequestListener : coil.request.ImageRequest.Listener {
  actual override fun onStart(request: ImageRequest)
  actual override fun onCancel(request: ImageRequest)
  actual override fun onError(request: ImageRequest, throwable: Throwable)
  actual override fun onSuccess(request: ImageRequest, metadata: ImageMetadata)
}

actual class ImageRequestBuilder(private val coilBuilder: coil.request.ImageRequest.Builder) {
  actual fun listener(listener: ImageRequestListener?) {
    coilBuilder.listener(listener = listener)
  }
}

@ExperimentalCoilApi
@Composable
internal actual fun platformRememberImagePainter(
  data: Any?,
  builder: ImageRequestBuilder.() -> Unit
): Painter {
  return coil.compose.rememberImagePainter(data = data, builder = {
    builder(ImageRequestBuilder(this))
  })
}
