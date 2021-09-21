/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.library

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseDispatcher
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
  private val db: Database
) : LibraryRepository {

  private val queries = db.libraryQueries

  override suspend fun findAll(sort: LibrarySort): List<LibraryManga> {
    return withContext(DatabaseDispatcher) {
      findAllQuery(sort).executeAsList().ordered(sort)
    }
  }

  override suspend fun findUncategorized(sort: LibrarySort): List<LibraryManga> {
    return withContext(DatabaseDispatcher) {
      findUncategorizedQuery(sort).executeAsList().ordered(sort)
    }
  }

  override suspend fun findForCategory(categoryId: Long, sort: LibrarySort): List<LibraryManga> {
    return withContext(DatabaseDispatcher) {
      findAllInCategoryQuery(categoryId, sort).executeAsList().ordered(sort)
    }
  }

  override suspend fun findFavoriteSourceIds(): List<Long> {
    return withContext(DatabaseDispatcher) {
      queries.findFavoriteSourceIds().executeAsList()
    }
  }

  override fun subscribeAll(sort: LibrarySort): Flow<List<LibraryManga>> {
    return findAllQuery(sort).asFlow().mapToList(DatabaseDispatcher)
  }

  override fun subscribeUncategorized(sort: LibrarySort): Flow<List<LibraryManga>> {
    return findUncategorizedQuery(sort).asFlow().mapToList(DatabaseDispatcher)
  }

  override fun subscribeToCategory(categoryId: Long, sort: LibrarySort): Flow<List<LibraryManga>> {
    return findAllInCategoryQuery(categoryId, sort).asFlow().mapToList(DatabaseDispatcher)
  }

  private fun findAllQuery(sort: LibrarySort): Query<LibraryManga> {
    return when (sort.type) {
      Title -> queries.findAll("title", libraryMapper)
      LastRead -> queries.findAll("lastRead", libraryMapper)
      LastUpdated -> queries.findAll("lastUpdated", libraryMapper)
      Unread -> queries.findAll("unread", libraryMapper)
      TotalChapters -> queries.findAllWithTotalChapters(libraryWithTotalMapper)
      Source -> queries.findAll("source", libraryMapper)
    }
  }

  private fun findUncategorizedQuery(sort: LibrarySort): Query<LibraryManga> {
    return when (sort.type) {
      Title -> queries.findUncategorized("title", libraryMapper)
      LastRead -> queries.findUncategorized("lastRead", libraryMapper)
      LastUpdated -> queries.findUncategorized("lastUpdated", libraryMapper)
      Unread -> queries.findUncategorized("unread", libraryMapper)
      TotalChapters -> queries.findUncategorizedWithTotalChapters(libraryWithTotalMapper)
      Source -> queries.findUncategorized("source", libraryMapper)
    }
  }

  private fun findAllInCategoryQuery(categoryId: Long, sort: LibrarySort): Query<LibraryManga> {
    return when (sort.type) {
      Title -> queries.findAllInCategory(categoryId, "title", libraryMapper)
      LastRead -> queries.findAllInCategory(categoryId, "lastRead", libraryMapper)
      LastUpdated -> queries.findAllInCategory(categoryId, "lastUpdated", libraryMapper)
      Unread -> queries.findAllInCategory(categoryId, "unread", libraryMapper)
      TotalChapters -> queries.findAllInCategoryWithTotalChapters(
        categoryId, libraryWithTotalMapper
      )
      Source -> queries.findAllInCategory(categoryId, "source", libraryMapper)
    }
  }

  private fun List<LibraryManga>.ordered(sort: LibrarySort): List<LibraryManga> {
    return if (sort.isAscending) this else asReversed()
  }

}
