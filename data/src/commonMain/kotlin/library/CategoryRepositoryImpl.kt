/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.category

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseDispatcher
import tachiyomi.data.library.categoryMapper
import tachiyomi.data.library.categoryWithCountMapper
import tachiyomi.domain.library.model.Category
import tachiyomi.domain.library.model.CategoryUpdate
import tachiyomi.domain.library.model.CategoryWithCount
import tachiyomi.domain.library.service.CategoryRepository

class CategoryRepositoryImpl @Inject constructor(
  private val db: Database
) : CategoryRepository {

  override fun subscribeAll(): Flow<List<Category>> {
    return db.categoryQueries.findAll(categoryMapper).asFlow().mapToList(DatabaseDispatcher)
  }

  override fun subscribeWithCount(): Flow<List<CategoryWithCount>> {
    return db.categoryQueries.findAllWithCount(categoryWithCountMapper).asFlow()
      .mapToList(DatabaseDispatcher)
  }

  override fun subscribeCategoriesOfManga(mangaId: Long): Flow<List<Category>> {
    return db.categoryQueries.findForManga(mangaId, categoryMapper).asFlow()
      .mapToList(DatabaseDispatcher)
  }

  override suspend fun findAll(): List<Category> {
    return withContext(DatabaseDispatcher) {
      db.categoryQueries.findAll(categoryMapper).executeAsList()
    }
  }

  override suspend fun find(categoryId: Long): Category? {
    return withContext(DatabaseDispatcher) {
      db.categoryQueries.findById(categoryId, categoryMapper).executeAsOneOrNull()
    }
  }

  override suspend fun findCategoriesOfManga(mangaId: Long): List<Category> {
    return withContext(DatabaseDispatcher) {
      db.categoryQueries.findForManga(mangaId, categoryMapper).executeAsList()
    }
  }

  override suspend fun insert(category: Category) {
    withContext(DatabaseDispatcher) {
      insertBlocking(category)
    }
  }

  override suspend fun insert(categories: List<Category>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        for (category in categories) {
          insertBlocking(category)
        }
      }
    }
  }

  override suspend fun updatePartial(update: CategoryUpdate) {
    withContext(DatabaseDispatcher) {
      updatePartialBlocking(update)
    }
  }

  override suspend fun updatePartial(updates: Collection<CategoryUpdate>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        for (update in updates) {
          updatePartialBlocking(update)
        }
      }
    }
  }

  override suspend fun delete(categoryId: Long) {
    return withContext(DatabaseDispatcher) {
      db.categoryQueries.deleteById(categoryId)
    }
  }

  override suspend fun delete(categoryIds: Collection<Long>) {
    return withContext(DatabaseDispatcher) {
      db.transaction {
        for (categoryId in categoryIds) {
          db.categoryQueries.deleteById(categoryId)
        }
      }
    }
  }

  private fun insertBlocking(category: Category) {
    db.categoryQueries.insert(
      name = category.name,
      sort = category.order,
      updateInterval = category.updateInterval
    )
  }

  private fun updatePartialBlocking(update: CategoryUpdate) {
    db.categoryQueries.update(
      update.name,
      update.order?.toLong(),
      update.updateInterval?.toLong(),
      id = update.id
    )
  }

}
