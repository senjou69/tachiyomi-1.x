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
import kotlinx.coroutines.flow.map
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
    return handler.awaitList { findAllQuery(sort) }.ordered(sort)
  }

  override suspend fun findUncategorized(sort: LibrarySort): List<LibraryManga> {
    return handler.awaitList { findUncategorizedQuery(sort) }.ordered(sort)
  }

  override suspend fun findForCategory(categoryId: Long, sort: LibrarySort): List<LibraryManga> {
    return handler.awaitList { findAllInCategoryQuery(categoryId, sort) }.ordered(sort)
  }

  override suspend fun findFavoriteSourceIds(): List<Long> {
    return handler.awaitList { libraryQueries.findFavoriteSourceIds() }
  }

  override fun subscribeAll(sort: LibrarySort): Flow<List<LibraryManga>> {
    return handler.subscribeToList { findAllQuery(sort) }.map { it.ordered(sort) }
  }

  override fun subscribeUncategorized(sort: LibrarySort): Flow<List<LibraryManga>> {
    return handler.subscribeToList { findUncategorizedQuery(sort) }.map { it.ordered(sort) }
  }

  override fun subscribeToCategory(categoryId: Long, sort: LibrarySort): Flow<List<LibraryManga>> {
    return handler.subscribeToList { findAllInCategoryQuery(categoryId, sort) }
      .map { it.ordered(sort) }
  }

  private fun Database.findAllQuery(sort: LibrarySort): Query<LibraryManga> {
    return when (sort.type) {
      Title -> libraryQueries.findAll("title", libraryMapper)
      LastRead -> libraryQueries.findAll("lastRead", libraryMapper)
      LastUpdated -> libraryQueries.findAll("lastUpdated", libraryMapper)
      Unread -> libraryQueries.findAll("unread", libraryMapper)
      TotalChapters -> libraryQueries.findAllWithTotalChapters(libraryWithTotalMapper)
      Source -> libraryQueries.findAll("source", libraryMapper)
    }
  }

  private fun Database.findUncategorizedQuery(sort: LibrarySort): Query<LibraryManga> {
    return when (sort.type) {
      Title -> libraryQueries.findUncategorized("title", libraryMapper)
      LastRead -> libraryQueries.findUncategorized("lastRead", libraryMapper)
      LastUpdated -> libraryQueries.findUncategorized("lastUpdated", libraryMapper)
      Unread -> libraryQueries.findUncategorized("unread", libraryMapper)
      TotalChapters -> libraryQueries.findUncategorizedWithTotalChapters(libraryWithTotalMapper)
      Source -> libraryQueries.findUncategorized("source", libraryMapper)
    }
  }

  private fun Database.findAllInCategoryQuery(
    categoryId: Long,
    sort: LibrarySort
  ): Query<LibraryManga> {
    return when (sort.type) {
      Title -> libraryQueries.findAllInCategory(categoryId, "title", libraryMapper)
      LastRead -> libraryQueries.findAllInCategory(categoryId, "lastRead", libraryMapper)
      LastUpdated -> libraryQueries.findAllInCategory(categoryId, "lastUpdated", libraryMapper)
      Unread -> libraryQueries.findAllInCategory(categoryId, "unread", libraryMapper)
      TotalChapters -> libraryQueries.findAllInCategoryWithTotalChapters(
        categoryId, libraryWithTotalMapper
      )
      Source -> libraryQueries.findAllInCategory(categoryId, "source", libraryMapper)
    }
  }

  private fun List<LibraryManga>.ordered(sort: LibrarySort): List<LibraryManga> {
    return if (sort.isAscending) this else asReversed()
  }

}
