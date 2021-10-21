/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.core.components

import androidx.compose.runtime.Composable

// TODO(inorichi): default parameters are not working properly on kotlin 1.5.31. "enabled" is set
//  to false even if it's explicitly set as true, so we need two methods for now
@Composable
expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)

@Composable
expect fun BackHandler(onBack: () -> Unit)
