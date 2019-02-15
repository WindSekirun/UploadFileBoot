package com.github.windsekirun.uploadfileboot.service.storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream

@Service
class FileStorageService : StorageService {

    @Value("\${service.location.text}")
    private lateinit var location: String

    private val rootLocation: Path by lazy { Paths.get(location) }

    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (e: IOException) {
            throw IllegalStateException("Could not initialize storage", e)
        }
    }

    override fun store(file: MultipartFile) {
        val fileName = StringUtils.cleanPath(file.originalFilename ?: "")
        try {
            if (file.isEmpty) throw IllegalStateException("Failed to store empty file $fileName")

            if (fileName.contentEquals("")) {
                throw IllegalStateException("Cannot store file with relative path outside currnet directory $fileName")
            }

            file.inputStream.use { Files.copy(it, this.rootLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING) }
        } catch (e: IOException) {
            throw IllegalStateException("Failed to store file $fileName", e)
        }
    }

    override fun loadAll(): Stream<Path> {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter { it != this.rootLocation }
                    .map { this.rootLocation.relativize(it) }
        } catch (e: IOException) {
            throw IllegalStateException("Failed to read stored files ${e.message}", e)
        }
    }

    override fun load(fileName: String): Path {
        return rootLocation.resolve(fileName)
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun loadAsResource(fileName: String): Resource {
        try {
            val file = load(fileName)
            val resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                return resource
            } else {
                throw IllegalStateException("Could not read file $fileName")
            }
        } catch (e: MalformedURLException) {
            throw IllegalStateException("Col")
        }
    }
}