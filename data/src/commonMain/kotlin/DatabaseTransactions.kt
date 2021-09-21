/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data

import kotlinx.coroutines.withContext
import tachiyomi.core.coroutines.runBlocking
import tachiyomi.core.db.Transactions
import tachiyomi.core.di.Inject

internal class DatabaseTransactions @Inject constructor(private val db: Database) : Transactions {

  override suspend fun <R> run(action: suspend () -> R): R {
    return withContext(DatabaseDispatcher) {
      db.transactionWithResult {
        runBlocking {
          action()
        }
      }
    }
  }

}
