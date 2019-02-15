package com.github.windsekirun.uploadfileboot.exception

import java.lang.Exception

class StorageException(message: String, exception: Exception? = null): IllegalStateException(message, exception)