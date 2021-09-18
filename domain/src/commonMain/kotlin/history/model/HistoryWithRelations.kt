/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.domain.history.model

data class HistoryWithRelations(
  val mangaId: Long,
  val chapterId: Long,
  val readAt: Long,
  val mangaTitle: String,
  val sourceId: Long,
  val cover: String,
  val favorite: Boolean,
  val chapterNumber: Float,
  val date: String
)
