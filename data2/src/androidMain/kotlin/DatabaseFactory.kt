/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data

import android.app.Application
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
actual class DatabaseDriverFactory @Inject constructor(
  private val app: Application
) {

  actual fun create(): SqlDriver {
    return AndroidSqliteDriver(
      schema = Database.Schema,
      context = app,
      name = "tachiyomi.db",
      factory = RequerySQLiteOpenHelperFactory()
    )
  }

}
