/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.manga

import com.squareup.sqldelight.ColumnAdapter

internal val mangaGenresConverter = object : ColumnAdapter<List<String>, String> {
  override fun decode(databaseValue: String): List<String> {
    return if (databaseValue.isEmpty()) listOf() else databaseValue.split(";")
  }

  override fun encode(value: List<String>): String {
    return value.joinToString(separator = ";")
  }
}
