package tachiyomi.ui.updates

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import tachiyomi.domain.updates.interactor.GetUpdatesGroupByDate
import tachiyomi.domain.updates.model.UpdatesManga
import tachiyomi.ui.core.viewmodel.BaseViewModel
import java.util.Date
import javax.inject.Inject

typealias UpdateMangaByDate = Map.Entry<Date, List<UpdatesManga>>

class UpdatesViewModel @Inject constructor(
  getUpdatesGroupByDate: GetUpdatesGroupByDate
) : BaseViewModel() {

  val updatesMap by getUpdatesGroupByDate.subscribeAll().asState(emptyMap())

  var selectedManga = mutableStateListOf<Long>()
    private set
  val selectionMode by derivedStateOf { selectedManga.isNotEmpty() }

  fun unselectAll() {
    selectedManga.clear()
  }

  fun selectAll() {
    val allChapterIds = updatesMap
      .flatMap(UpdateMangaByDate::value)
      .map(UpdatesManga::chapterId)
    selectedManga.clear()
    selectedManga.addAll(allChapterIds)
  }

  fun flipSelection() {
    val notSelectedIds = updatesMap
      .flatMap(UpdateMangaByDate::value)
      .map(UpdatesManga::chapterId)
      .filterNot { id -> id in selectedManga }
    selectedManga.clear()
    selectedManga.addAll(notSelectedIds)
  }

  fun updateLibrary() {
    // TODO
  }

  fun toggleManga(id: Long) {
    if (id in selectedManga) {
      selectedManga.remove(id)
    } else {
      selectedManga.add(id)
    }
  }
}