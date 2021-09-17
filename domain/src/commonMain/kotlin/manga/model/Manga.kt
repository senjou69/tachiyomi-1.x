/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.domain.manga.model

data class Manga(
  override val id: Long = 0,
  override val sourceId: Long,
  override val key: String,
  override val title: String,
  val artist: String = "",
  val author: String = "",
  val description: String = "",
  val genres: List<String> = emptyList(),
  val status: Int = 0,
  val cover: String = "",
  val customCover: String = "",
  val favorite: Boolean = false,
  val lastUpdate: Long = 0,
  val lastInit: Long = 0,
  val dateAdded: Long = 0,
  val viewer: Int = 0,
  val flags: Int = 0
) : MangaBase
