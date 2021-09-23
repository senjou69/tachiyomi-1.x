/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data

import tachiyomi.core.db.Transactions
import tachiyomi.core.di.Inject

internal class DatabaseTransactions @Inject constructor(
  private val handler: DatabaseHandler
) : Transactions {

  override suspend fun <T> run(action: suspend () -> T): T {
    return handler.await(inTransaction = true) {
      action()
    }
  }

}
