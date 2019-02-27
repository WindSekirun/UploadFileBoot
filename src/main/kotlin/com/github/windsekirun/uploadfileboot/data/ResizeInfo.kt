package com.github.windsekirun.uploadfileboot.data

data class ResizeInfo(val resized: Boolean, val origin: String, val resizedPaths: List<Pair<Int, String>>)