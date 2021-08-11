package tachiyomi.ui.updates

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import tachiyomi.domain.updates.model.UpdatesManga
import java.util.Date

@Stable
interface UpdatesState {

  val updates: Map<Date, List<UpdatesManga>>
  val selection: List<Long>
  val hasSelection: Boolean
  val isLoading: Boolean
}

fun UpdatesState(): UpdatesState {
  return UpdatesStateImpl()
}

class UpdatesStateImpl : UpdatesState {

  override var updates: Map<Date, List<UpdatesManga>> by mutableStateOf(emptyMap())
  override val selection: SnapshotStateList<Long> = mutableStateListOf()
  override val hasSelection: Boolean by derivedStateOf { selection.isNotEmpty() }
  override val isLoading: Boolean by derivedStateOf { updates.isEmpty() }
}