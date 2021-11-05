/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.i18n

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

actual object Images {
  @Composable
  actual fun tachi() = ImageVector.vectorResource(R.drawable.ic_tachi)

  @Composable
  actual fun glasses() = ImageVector.vectorResource(R.drawable.ic_glasses)
}
