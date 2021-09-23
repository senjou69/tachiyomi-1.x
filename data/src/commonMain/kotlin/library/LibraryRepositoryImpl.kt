/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.library

import com.squareup.sqldelight.Query
import kotlinx.coroutines.flow.Flow
import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseHandler
import tachiyomi.domain.library.model.LibraryManga
import tachiyomi.domain.library.model.LibrarySort
import tachiyomi.domain.library.model.LibrarySort.Type.LastRead
import tachiyomi.domain.library.model.LibrarySort.Type.LastUpdated
import tachiyomi.domain.library.model.LibrarySort.Type.Source
import tachiyomi.domain.library.model.LibrarySort.Type.Title
import tachiyomi.domain.library.model.LibrarySort.Type.TotalChapters
import tachiyomi.domain.library.model.LibrarySort.Type.Unread
import tachiyomi.domain.library.service.LibraryRepository

class LibraryRepositoryImpl @Inject constructor(
  private val handler: DatabaseHandler
) : LibraryRepository {

  override suspend fun findAll(sort: LibrarySort): List<LibraryManga> {
    return handler.awaitList { findAllQuery(sort) }
  }

  override suspend fun findUncategorized(sort: LibrarySort): List<LibraryManga> {
    return handler.awaitList { findUncategorizedQuery(sort) }
  }

  override suspend fun findForCategory(categoryId: Long, sort: LibrarySort): List<LibraryManga> {
    return handler.awaitList { findAllInCategoryQuery(categoryId, sort) }
  }

  override suspend fun findFavoriteSourceIds(): List<Long> {
    return handler.awaitList { libraryQueries.findFavoriteSourceIds() }
  }

  override fun subscribeAll(sort: LibrarySort): Flow<List<LibraryManga>> {
    return handler.subscribeToList { findAllQuery(sort) }
  }

  override fun subscribeUncategorized(sort: LibrarySort): Flow<List<LibraryManga>> {
    return handler.subscribeToList { findUncategorizedQuery(sort) }
  }

  override fun subscribeToCategory(categoryId: Long, sort: LibrarySort): Flow<List<LibraryManga>> {
    return handler.subscribeToList { findAllInCategoryQuery(categoryId, sort) }
  }

  private fun Database.findAllQuery(sort: LibrarySort): Query<LibraryManga> {
    return when (sort.type) {
      TotalChapters -> libraryQueries.findAllWithTotalChapters(libraryWithTotalMapper)
      else -> libraryQueries.findAll(sort.parameter, libraryMapper)
    }
  }

  private fun Database.findUncategorizedQuery(sort: LibrarySort): Query<LibraryManga> {
    return when (sort.type) {
      TotalChapters -> libraryQueries.findUncategorizedWithTotalChapters(libraryWithTotalMapper)
      else -> libraryQueries.findUncategorized(sort.parameter, libraryMapper)
    }
  }

  private fun Database.findAllInCategoryQuery(
    categoryId: Long,
    sort: LibrarySort
  ): Query<LibraryManga> {
    return when (sort.type) {
      TotalChapters -> libraryQueries.findAllInCategoryWithTotalChapters(
        categoryId, libraryWithTotalMapper
      )
      else -> libraryQueries.findAllInCategory(categoryId, sort.parameter, libraryMapper)
    }
  }

  // Returns the selected sorting as a query parameter
  private val LibrarySort.parameter: String
    get() {
      val sort = when (type) {
        Title -> "title"
        LastRead -> "lastRead"
        LastUpdated -> "lastUpdated"
        Unread -> "unread"
        TotalChapters -> "totalChapters"
        Source -> "source"
      }
      return if (isAscending) sort else sort + "Desc"
    }

}
