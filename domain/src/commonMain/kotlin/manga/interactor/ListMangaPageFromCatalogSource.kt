/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.domain.manga.interactor

import tachiyomi.core.di.Inject
import tachiyomi.domain.manga.model.MangasPage
import tachiyomi.source.CatalogSource
import tachiyomi.source.model.Listing

class ListMangaPageFromCatalogSource @Inject internal constructor(
  private val getOrAddMangaFromSource: GetOrAddMangaFromSource
) {

  suspend fun await(source: CatalogSource, listing: Listing?, page: Int): MangasPage {
    val sourcePage = source.getAnimeList(listing, page)
    val localPage = sourcePage.mangas.map { getOrAddMangaFromSource.await(it, source.id) }

    return MangasPage(page, localPage, sourcePage.hasNextPage)
  }

}
