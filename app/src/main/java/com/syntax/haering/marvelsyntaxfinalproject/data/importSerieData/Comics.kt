package com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)