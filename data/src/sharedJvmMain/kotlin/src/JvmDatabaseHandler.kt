/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

@Singleton
internal class JvmDatabaseHandler @Inject constructor(
  private val db: Database,
  private val driver: SqlDriver,
  private val queryDispatcher: CoroutineDispatcher = Dispatchers.IO,
  internal val transactionDispatcher: CoroutineDispatcher = queryDispatcher
) : DatabaseHandler {

  internal val suspendingTransactionId = ThreadLocal<Int>()

  override suspend fun <T> await(inTransaction: Boolean, block: suspend Database.() -> T): T {
    if (inTransaction) {
      return withTransaction { await(inTransaction = false, block) }
    }
    return dispatchQuery(block)
  }

  override suspend fun <T : Any> awaitList(
    inTransaction: Boolean, block:
    suspend Database.() -> Query<T>
  ): List<T> {
    if (inTransaction) {
      return withTransaction { awaitList(inTransaction = false, block) }
    }
    return dispatchQuery { block(db).executeAsList() }
  }

  override suspend fun <T : Any> awaitOne(
    inTransaction: Boolean, block:
    suspend Database.() -> Query<T>
  ): T {
    if (inTransaction) {
      return withTransaction { awaitOne(inTransaction = false, block) }
    }
    return dispatchQuery { block(db).executeAsOne() }
  }

  override suspend fun <T : Any> awaitOneOrNull(
    inTransaction: Boolean, block:
    suspend Database.() -> Query<T>
  ): T? {
    if (inTransaction) {
      return withTransaction { awaitOneOrNull(inTransaction = false, block) }
    }
    return dispatchQuery { block(db).executeAsOneOrNull() }
  }

  override fun <T : Any> subscribeToList(block: Database.() -> Query<T>): Flow<List<T>> {
    return block(db).asFlow().mapToList(queryDispatcher)
  }

  override fun <T : Any> subscribeToOne(block: Database.() -> Query<T>): Flow<T> {
    return block(db).asFlow().mapToOne(queryDispatcher)
  }

  override fun <T : Any> subscribeToOneOrNull(block: Database.() -> Query<T>): Flow<T?> {
    return block(db).asFlow().mapToOneOrNull(queryDispatcher)
  }

  private suspend fun <T> dispatchQuery(block: suspend Database.() -> T): T {
    // If we're currently in the transaction thread, there's no need to dispatch our query.
    if (driver.currentTransaction() != null) {
      return block(db)
    }

    // Use the transaction dispatcher if we are on a transaction coroutine, otherwise use the
    // database dispatchers.
    val context = coroutineContext[TransactionElement]?.transactionDispatcher ?: queryDispatcher
    return withContext(context) {
      block(db)
    }
  }

  /**
   * Calls the specified suspending [block] in a database transaction. The transaction will be
   * marked as successful unless an exception is thrown in the suspending [block] or the coroutine
   * is cancelled.
   *
   * Room will only perform at most one transaction at a time, additional transactions are queued
   * and executed on a first come, first serve order.
   *
   * Performing blocking database operations is not permitted in a coroutine scope other than the
   * one received by the suspending block. It is recommended that all [Dao] function invoked within
   * the [block] be suspending functions.
   *
   * The dispatcher used to execute the given [block] will utilize threads from Room's query executor.
   */
  private suspend fun <T> withTransaction(block: suspend () -> T): T {
    // Use inherited transaction context if available, this allows nested suspending transactions.
    val transactionContext =
      coroutineContext[TransactionElement]?.transactionDispatcher ?: createTransactionContext()
    return withContext(transactionContext) {
      val transactionElement = coroutineContext[TransactionElement]!!
      transactionElement.acquire()
      try {
        db.transactionWithResult {
          runBlocking(transactionContext) {
            block()
          }
        }
      } finally {
        transactionElement.release()
      }
    }
  }

}
