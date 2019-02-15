package com.github.windsekirun.uploadfileboot.service.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream

interface StorageService {
    fun init()

    fun store(file: MultipartFile): String

    fun loadAll(): Stream<Path>

    fun load(fileName: String): Path

    fun deleteAll()

    fun loadAsResource(fileName: String): Resource
}