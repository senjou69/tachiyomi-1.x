/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.updates

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import tachiyomi.core.di.Inject
import tachiyomi.data.DatabaseHandler
import tachiyomi.domain.updates.model.UpdatesManga
import tachiyomi.domain.updates.service.UpdatesRepository
import java.util.Date

class UpdatesRepositoryImpl @Inject constructor(
  private val handler: DatabaseHandler
) : UpdatesRepository {

  override fun subscribeAll(): Flow<Map<Date, List<UpdatesManga>>> {
    // TODO introduce multiplatform dates library?
//    val formatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
    return handler
      .subscribeToList { updatesQueries.findAll(updatesMapper) }
      .mapLatest { updates ->
        updates
          .groupBy { Date(0) }
//          .groupBy { formatter.parse(it.date) }
//          .toSortedMap(reverseOrder())
      }
  }

}
