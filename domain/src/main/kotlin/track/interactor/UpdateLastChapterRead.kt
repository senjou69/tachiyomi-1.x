/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.domain.track.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tachiyomi.core.util.Optional
import tachiyomi.domain.track.model.TrackStateUpdate
import tachiyomi.domain.track.model.TrackUpdate
import tachiyomi.domain.track.repository.TrackRepository
import tachiyomi.domain.track.services.TrackServices
import javax.inject.Inject

class UpdateLastChapterRead @Inject constructor(
  private val trackRepository: TrackRepository,
  private val trackServices: TrackServices
) {

  suspend fun await(mangaId: Long, lastChapterRead: Float): Result {
    return try {
      val tracks = withContext(Dispatchers.IO) {
        trackRepository.findAll(mangaId)
      }

      for (track in tracks) {
        if (track.lastChapterRead >= lastChapterRead) continue

        val site = trackServices.get(track.siteId) ?: continue
        val currentState = site.getState(track.entryId) ?: continue

        val update = TrackStateUpdate(lastChapterRead = Optional.of(lastChapterRead))
        site.update(track.entryId, update)

        val localUpdate = TrackUpdate(
          id = track.id,
          lastChapterRead = Optional.of(lastChapterRead),
          totalChapters = Optional.of(currentState.totalChapters),
          score = Optional.of(currentState.score), // TODO what happens with different score systems
          status = Optional.of(currentState.status)
        )
        trackRepository.savePartial(localUpdate)
      }
      Result.Success
    } catch (e: Exception) {
      Result.Error(e)
    }
  }

  sealed class Result {
    object Success : Result()
    data class Error(val error: Throwable) : Result()
  }

}
