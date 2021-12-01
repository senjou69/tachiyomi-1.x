/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.source

import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import tachiyomi.source.model.EpisodeInfo
import tachiyomi.source.model.Filter
import tachiyomi.source.model.FilterList
import tachiyomi.source.model.ImageUrl
import tachiyomi.source.model.Listing
import tachiyomi.source.model.AnimeInfo
import tachiyomi.source.model.AnimesPageInfo
import tachiyomi.source.model.Video

class TestSource : CatalogSource {
  override val id = 1L

  override val name = "Test source"
  override val lang get() = "en"
  override suspend fun getMangaDetails(manga: AnimeInfo): AnimeInfo {
    delay(1000)
    val noHipstersOffset = 10
    val picId = manga.title.split(" ")[1].toInt() + noHipstersOffset
    return manga.copy(cover = "https://picsum.photos/300/400/?image=$picId")
  }

  override suspend fun getAnimeList(sort: Listing?, page: Int): AnimesPageInfo {
    delay(1000)
    return AnimesPageInfo(getTestManga(page), true)
  }

  override suspend fun getAnimeList(filters: FilterList, page: Int): AnimesPageInfo {
    var mangaList = getTestManga(page)

    filters.forEach { filter ->
      if (filter is Filter.Title) {
        mangaList = mangaList.filter { filter.value in it.title }
      }
    }

    return AnimesPageInfo(mangaList, true)
  }

  override suspend fun getChapterList(manga: AnimeInfo): List<EpisodeInfo> {
    delay(1000)
    return getTestChapters()
  }

  override suspend fun getPageList(chapter: EpisodeInfo): List<Video> {
    delay(1000)
    return getTestPages()
  }

  class Alphabetically : Listing("Alphabetically")

  class Latest : Listing("Latest")

  override fun getListings(): List<Listing> {
    return listOf(Alphabetically(), Latest())
  }

  override fun getFilters(): FilterList {
    return listOf(
      Filter.Title(),
      Filter.Author(),
      Filter.Artist(),
      GenreList(getGenreList())
    )
  }

  //  private class Status : Filter.Check("Completed")
//  private class StatusValue(filter: Status) : Filter.Check(filter, null)
//  private class Author : Filter.Text("Author")
//  private class Genre(name: String) : Filter.TriState(name)
  private class GenreList(genres: List<Filter.Genre>) : Filter.Group("Genres", genres)

  private fun getGenreList() = listOf(
    Filter.Genre("4-koma"),
    Filter.Genre("Action"),
    Filter.Genre("Adventure"),
    Filter.Genre("Award Winning"),
    Filter.Genre("Comedy"),
    Filter.Genre("Cooking"),
    Filter.Genre("Doujinshi"),
    Filter.Genre("Drama"),
    Filter.Genre("Ecchi"),
    Filter.Genre("Fantasy"),
    Filter.Genre("Gender Bender"),
    Filter.Genre("Harem"),
    Filter.Genre("Historical"),
    Filter.Genre("Horror"),
    Filter.Genre("Josei"),
    Filter.Genre("Martial Arts"),
    Filter.Genre("Mecha"),
    Filter.Genre("Medical"),
    Filter.Genre("Music"),
    Filter.Genre("Mystery"),
    Filter.Genre("Oneshot"),
    Filter.Genre("Psychological"),
    Filter.Genre("Romance"),
    Filter.Genre("School Life"),
    Filter.Genre("Sci-Fi"),
    Filter.Genre("Seinen"),
    Filter.Genre("Shoujo"),
    Filter.Genre("Shoujo Ai"),
    Filter.Genre("Shounen"),
    Filter.Genre("Shounen Ai"),
    Filter.Genre("Slice of Life"),
    Filter.Genre("Smut"),
    Filter.Genre("Sports"),
    Filter.Genre("Supernatural"),
    Filter.Genre("Tragedy"),
    Filter.Genre("Webtoon"),
    Filter.Genre("Yaoi"),
    Filter.Genre("Yuri"),
    Filter.Genre("[no chapters]"),
    Filter.Genre("Game"),
    Filter.Genre("Isekai")
  )

  private fun getTestManga(page: Int): List<AnimeInfo> {
    val list = mutableListOf<AnimeInfo>()
    val id = (page - 1) * 20 + 1
    val manga1 = AnimeInfo(
      "$id",
      "Manga $id",
      "Artist $id",
      "Author $id",
      "Lorem ipsum",
      listOf("Foo", "Bar"),
      0,
      ""
    )
    list += manga1

    for (i in 1..19) {
      list += manga1.copy(key = "${id + i}", title = "Manga ${id + i}")
    }

    return list
  }

  private fun getTestChapters(): List<EpisodeInfo> {
    val chapter1 = EpisodeInfo(
      "1",
      "Chapter 1",
      Clock.System.now().toEpochMilliseconds()
    )
    val chapter2 = chapter1.copy(key = "2", name = "Chapter2")
    val chapter3 = chapter1.copy(key = "3", name = "Chapter3")

    return listOf(chapter1, chapter2, chapter3)
  }

  private fun getTestPages(): List<Video> {
    return listOf(
      ImageUrl("imageUrl1"),
      ImageUrl("imageUrl2")
    )
  }

}
