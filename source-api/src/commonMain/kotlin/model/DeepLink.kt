/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.source.model

sealed class DeepLink {
  abstract val key: String

  data class Manga(override val key: String) : DeepLink()
  data class Chapter(override val key: String) : DeepLink()
}
