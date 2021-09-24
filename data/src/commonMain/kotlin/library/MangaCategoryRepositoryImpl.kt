/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.library

import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseHandler
import tachiyomi.domain.library.model.MangaCategory
import tachiyomi.domain.library.service.MangaCategoryRepository

internal class MangaCategoryRepositoryImpl @Inject constructor(
  private val handler: DatabaseHandler
) : MangaCategoryRepository {

  override suspend fun replaceAll(mangaCategories: List<MangaCategory>) {
    handler.await(inTransaction = true) {
      val mangaIdsToDelete = mangaCategories.asSequence().map { it.mangaId }.distinct()
      for (mangaId in mangaIdsToDelete) {
        deleteForMangaBlocking(mangaId)
      }
      for (mangaCategory in mangaCategories) {
        insertBlocking(mangaCategory)
      }
    }
  }

  override suspend fun deleteForManga(mangaId: Long) {
    handler.await { deleteForMangaBlocking(mangaId) }
  }

  override suspend fun deleteForMangas(mangaIds: List<Long>) {
    handler.await(inTransaction = true) {
      for (mangaId in mangaIds) {
        deleteForMangaBlocking(mangaId)
      }
    }
  }

  private fun Database.deleteForMangaBlocking(mangaId: Long) {
    mangaCategoriesQueries.deleteForManga(mangaId)
  }

  private fun Database.insertBlocking(mangaCategory: MangaCategory) {
    mangaCategoriesQueries.insert(
      mangaId = mangaCategory.mangaId,
      categoryId = mangaCategory.categoryId
    )
  }

}
