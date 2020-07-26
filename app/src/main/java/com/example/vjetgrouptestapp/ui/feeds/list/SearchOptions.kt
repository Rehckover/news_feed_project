package com.example.vjetgrouptestapp.ui.feeds.list

data class SearchOptions(
    var source: String,
    var dateFrom: String? = null,
    var dateTo: String? = null,
    var sortBy: SortByType? = SortByType.DATE
)

enum class SortByType(val sortValue:String) {
    DATE("publishedAt"),
    POPULARITY("popularity")
}