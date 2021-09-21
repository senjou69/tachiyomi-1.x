/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.catalog

import tachiyomi.domain.catalog.model.CatalogRemote

internal val catalogRemoteMapper =
  { sourceId: Long, name: String, description: String, pkgName: String, versionName: String,
    versionCode: Int, lang: String, pkgUrl: String, iconUrl: String, nsfw: Boolean ->

    CatalogRemote(
      name = name,
      description = description,
      sourceId = sourceId,
      pkgName = pkgName,
      versionName = versionName,
      versionCode = versionCode,
      lang = lang,
      pkgUrl = pkgUrl,
      iconUrl = iconUrl,
      nsfw = nsfw
    )
  }
