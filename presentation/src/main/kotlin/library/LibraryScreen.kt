/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.twotone.FileDownload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tachiyomi.domain.library.model.CategoryWithCount
import tachiyomi.domain.library.model.DisplayMode
import tachiyomi.domain.library.model.LibraryManga
import tachiyomi.ui.categories.visibleName
import tachiyomi.ui.core.theme.CustomColors
import tachiyomi.ui.core.viewmodel.viewModel
import tachiyomi.ui.main.Route

@Composable
fun LibraryScreen(
  navController: NavController,
  requestHideBottomNav: (Boolean) -> Unit
) {
  val vm = viewModel<LibraryViewModel, LibraryState>(
    initialState = { LibraryState() },
    saver = LibraryState.Saver
  )
  val scope = rememberCoroutineScope()

  LaunchedEffect(vm.selectionMode, vm.showSheet) {
    requestHideBottomNav(vm.selectionMode || vm.showSheet)
  }

  val columns by vm.getLibraryColumns()

  LibrarySheetLayout(
    showSheet = vm.showSheet,
    currentPage = vm.sheetPage,
    onSheetDismissed = { vm.setSheetVisibility(false) },
    onPageChanged = { vm.setSheetPage(it) }
  ) {
    Box {
      Column {
        LibraryToolbar(
          selectedCategory = vm.selectedCategory,
          selectedManga = vm.selectedManga,
          showCategoryTabs = vm.showCategoryTabs,
          showCountInCategory = vm.showCountInCategory,
          selectionMode = vm.selectionMode,
          searchMode = vm.searchMode,
          searchQuery = vm.searchQuery,
          onClickSearch = { vm.openSearch() },
          onClickFilter = { vm.setSheetVisibility(true) },
          onClickRefresh = { vm.updateLibrary() },
          onClickCloseSelection = { vm.unselectAll() },
          onClickCloseSearch = { vm.closeSearch() },
          onClickSelectAll = { vm.selectAllInCurrentCategory() },
          onClickUnselectAll = { vm.flipAllInCurrentCategory() },
          onChangeSearchQuery = { vm.updateQuery(it) }
        )
        LibraryContent(
          scope = scope,
          categories = vm.categories,
          displayMode = vm.displayMode,
          currentPage = vm.selectedCategoryIndex,
          showPageTabs = vm.showCategoryTabs,
          showCountInCategory = vm.showCountInCategory,
          selectedManga = vm.selectedManga,
          columns = columns,
          getLibraryForPage = { vm.getLibraryForCategoryIndex(it) },
          onClickManga = { manga ->
            if (!vm.selectionMode) {
              navController.navigate("${Route.LibraryManga.id}/${manga.id}")
            } else {
              vm.toggleManga(manga)
            }
          },
          onLongClickManga = { vm.toggleManga(it) },
          onPageChanged = { vm.setSelectedPage(it) },
        )
      }
      LibrarySelectionBar(
        visible = vm.selectionMode,
        modifier = Modifier.align(Alignment.BottomCenter),
        onClickChangeCategory = { vm.changeCategoriesForSelectedManga() },
        onClickDownload = { vm.downloadSelectedManga() },
        onClickMarkAsRead = { vm.toggleReadSelectedManga(read = true) },
        onClickMarkAsUnread = { vm.toggleReadSelectedManga(read = false) },
        onClickDeleteDownloads = { vm.deleteDownloadsSelectedManga() }
      )
    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LibrarySheetLayout(
  showSheet: Boolean,
  currentPage: Int,
  onSheetDismissed: () -> Unit,
  onPageChanged: (Int) -> Unit,
  content: @Composable () -> Unit
) {
  val sheetState = remember { ModalBottomSheetState(ModalBottomSheetValue.Hidden) }

  // Check whether the sheet needs to be shown or hidden.
  LaunchedEffect(showSheet) {
    if (showSheet) {
      sheetState.show()
    } else if (sheetState.currentValue != ModalBottomSheetValue.Hidden) {
      sheetState.hide()
    }
  }
  // Notify when the sheet has been dismissed
  LaunchedEffect(sheetState) {
    snapshotFlow { sheetState.currentValue == ModalBottomSheetValue.Hidden }
      .collect { hide -> if (hide) onSheetDismissed() }
  }

  ModalBottomSheetLayout(
    sheetState = sheetState,
    sheetContent = { LibrarySheet(currentPage, onPageChanged) },
    content = content
  )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
private fun LibraryContent(
  scope: CoroutineScope,
  categories: List<CategoryWithCount>,
  displayMode: DisplayMode,
  currentPage: Int,
  showPageTabs: Boolean,
  showCountInCategory: Boolean,
  selectedManga: List<Long>,
  columns: Int,
  getLibraryForPage: @Composable (Int) -> State<List<LibraryManga>>,
  onClickManga: (LibraryManga) -> Unit,
  onLongClickManga: (LibraryManga) -> Unit,
  onPageChanged: (Int) -> Unit
) {
  if (categories.isEmpty()) return

  val infiniteLoop = categories.size > 2
  val state = remember(infiniteLoop) {
    PagerState(categories.size, currentPage, infiniteLoop = infiniteLoop)
  }.apply {
    pageCount = categories.size
  }

  LaunchedEffect(state) {
    snapshotFlow { state.currentPage }.collect {
      onPageChanged(it)
    }
  }

  LibraryTabs(
    state = state,
    visible = showPageTabs,
    categories = categories,
    showCount = showCountInCategory,
    onClickTab = { scope.launch { state.animateScrollToPage(it) } }
  )
  LibraryPager(
    state = state,
    displayMode = displayMode,
    selectedManga = selectedManga,
    columns = columns,
    getLibraryForPage = getLibraryForPage,
    onClickManga = onClickManga,
    onLongClickManga = onLongClickManga,
  )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
private fun LibraryTabs(
  state: PagerState,
  visible: Boolean,
  categories: List<CategoryWithCount>,
  showCount: Boolean,
  onClickTab: (Int) -> Unit
) {
  AnimatedVisibility(
    visible = visible,
    enter = expandVertically(),
    exit = shrinkVertically()
  ) {
    ScrollableTabRow(
      selectedTabIndex = state.currentPage,
      backgroundColor = CustomColors.current.bars,
      contentColor = CustomColors.current.onBars,
      edgePadding = 0.dp,
      indicator = { TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(state, it)) }
    ) {
      categories.forEachIndexed { i, category ->
        Tab(
          selected = state.currentPage == i,
          onClick = { onClickTab(i) },
          text = {
            Text(
              category.visibleName + if (!showCount) {
                ""
              } else {
                " (${category.mangaCount})"
              }
            )
          }
        )
      }
    }
  }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun LibraryPager(
  state: PagerState,
  displayMode: DisplayMode,
  selectedManga: List<Long>,
  columns: Int,
  getLibraryForPage: @Composable (Int) -> State<List<LibraryManga>>,
  onClickManga: (LibraryManga) -> Unit,
  onLongClickManga: (LibraryManga) -> Unit
) {
  HorizontalPager(state = state) { page ->
    val library by getLibraryForPage(page)
    when (displayMode) {
      DisplayMode.CompactGrid -> LibraryMangaCompactGrid(
        library = library,
        selectedManga = selectedManga,
        columns = columns,
        onClickManga = onClickManga,
        onLongClickManga = onLongClickManga
      )
      DisplayMode.ComfortableGrid -> LibraryMangaComfortableGrid(
        library = library,
        selectedManga = selectedManga,
        columns = columns,
        onClickManga = onClickManga,
        onLongClickManga = onLongClickManga
      )
      DisplayMode.List -> LibraryMangaList(
        library = library,
        selectedManga = selectedManga,
        onClickManga = onClickManga,
        onLongClickManga = onLongClickManga
      )
    }
  }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun LibrarySelectionBar(
  visible: Boolean,
  onClickChangeCategory: () -> Unit,
  onClickDownload: () -> Unit,
  onClickMarkAsRead: () -> Unit,
  onClickMarkAsUnread: () -> Unit,
  onClickDeleteDownloads: () -> Unit,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = expandVertically(),
    exit = shrinkVertically()
  ) {
    Surface(
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 32.dp)
        .fillMaxWidth(),
      shape = MaterialTheme.shapes.medium,
      color = CustomColors.current.bars,
      contentColor = CustomColors.current.onBars,
      elevation = 4.dp
    ) {
      Row(
        modifier = Modifier.padding(4.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        IconButton(onClick = onClickChangeCategory) {
          Icon(Icons.Outlined.Label, contentDescription = null)
        }
        IconButton(onClick = onClickDownload) {
          Icon(Icons.TwoTone.FileDownload, contentDescription = null)
        }
        IconButton(onClick = onClickMarkAsRead) {
          Icon(Icons.Default.Check, contentDescription = null)
        }
        // TODO(inorichi): outlined check is not really outlined, we'll need to add a custom icon
        IconButton(onClick = onClickMarkAsUnread) {
          Icon(Icons.Outlined.Check, contentDescription = null)
        }
        IconButton(onClick = onClickDeleteDownloads) {
          Icon(Icons.Outlined.Delete, contentDescription = null)
        }
      }
    }
  }
}
