/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.library

import tachiyomi.domain.library.model.LibraryManga

internal val libraryMapper =
  { id: Long, sourceId: Long, key: String, title: String, status: Int, cover: String,
    lastUpdate: Long, unread: Long ->

    LibraryManga(id, sourceId, key, title, status, cover, lastUpdate, unread.toInt())
  }

internal val libraryWithTotalMapper =
  { id: Long, sourceId: Long, key: String, title: String, status: Int, cover: String,
    lastUpdate: Long, unread: Long, _: Long? ->

    LibraryManga(id, sourceId, key, title, status, cover, lastUpdate, unread.toInt())
  }
