/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.domain.history.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import tachiyomi.domain.history.model.HistoryWithRelations
import tachiyomi.domain.history.service.HistoryRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetHistoryByDate @Inject constructor(
  private val historyRepository: HistoryRepository
) {

  fun subscribeAll(): Flow<Map<Date, List<HistoryWithRelations>>> {
    val formatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
    return historyRepository.subscribeAll()
      .mapLatest { history ->
        history
          .groupBy { formatter.parse(it.date) }
          .toSortedMap(reverseOrder())
      }
  }
}
