package com.github.windsekirun.uploadfileboot.data

import com.github.windsekirun.uploadfileboot.Constants

data class ErrorResponse(val code: Int = Constants.RESPONSE_FAILED_REQUEST, val message: String)