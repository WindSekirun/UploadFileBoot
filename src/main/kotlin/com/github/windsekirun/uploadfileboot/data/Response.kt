package com.github.windsekirun.uploadfileboot.data

data class Response<T>(val code: Int, val message: String, val data: T?)