/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data

import com.squareup.sqldelight.Query
import kotlinx.coroutines.flow.Flow

/**
 * Database handler used to make SqlDelight queries suspending while making them transaction
 * aware so that the database is not locked if the continuations resume on another thread.
 */
interface DatabaseHandler {

  suspend fun <T> await(inTransaction: Boolean = false, block: suspend Database.() -> T): T

  suspend fun <T : Any> awaitList(
    inTransaction: Boolean = false,
    block: suspend Database.() -> Query<T>
  ): List<T>

  suspend fun <T : Any> awaitOne(
    inTransaction: Boolean = false,
    block: suspend Database.() -> Query<T>
  ): T

  suspend fun <T : Any> awaitOneOrNull(
    inTransaction: Boolean = false, block:
    suspend Database.() -> Query<T>
  ): T?

  fun <T : Any> subscribeToList(block: Database.() -> Query<T>): Flow<List<T>>

  fun <T : Any> subscribeToOne(block: Database.() -> Query<T>): Flow<T>

  fun <T : Any> subscribeToOneOrNull(block: Database.() -> Query<T>): Flow<T?>

}
