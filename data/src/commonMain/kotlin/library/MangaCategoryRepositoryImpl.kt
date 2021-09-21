/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.library

import kotlinx.coroutines.withContext
import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseDispatcher
import tachiyomi.domain.library.model.MangaCategory
import tachiyomi.domain.library.service.MangaCategoryRepository

class MangaCategoryRepositoryImpl @Inject constructor(
  private val db: Database
) : MangaCategoryRepository {

  override suspend fun replaceAll(mangaCategories: List<MangaCategory>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        val mangaIdsToDelete = mangaCategories.asSequence().map { it.mangaId }.distinct()
        for (mangaId in mangaIdsToDelete) {
          deleteForMangaBlocking(mangaId)
        }
        for (mangaCategory in mangaCategories) {
          insertBlocking(mangaCategory)
        }
      }
    }
  }

  override suspend fun deleteForManga(mangaId: Long) {
    withContext(DatabaseDispatcher) {
      deleteForMangaBlocking(mangaId)
    }
  }

  override suspend fun deleteForMangas(mangaIds: List<Long>) {
    withContext(DatabaseDispatcher) {
      for (mangaId in mangaIds) {
        deleteForMangaBlocking(mangaId)
      }
    }
  }

  private fun deleteForMangaBlocking(mangaId: Long) {
    db.mangaCategoriesQueries.deleteForManga(mangaId)
  }

  private fun insertBlocking(mangaCategory: MangaCategory) {
    db.mangaCategoriesQueries.insert(
      mangaId = mangaCategory.mangaId,
      categoryId = mangaCategory.categoryId
    )
  }

}
