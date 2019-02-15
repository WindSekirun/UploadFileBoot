package com.github.windsekirun.uploadfileboot.controller

import com.github.windsekirun.uploadfileboot.Constants
import com.github.windsekirun.uploadfileboot.Constants.RESPONSE_OK
import com.github.windsekirun.uploadfileboot.data.Response
import com.github.windsekirun.uploadfileboot.exception.StorageException
import com.github.windsekirun.uploadfileboot.service.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.stream.Collectors

@Controller
class FileUploadController @Autowired constructor(private val storageService: StorageService) {
    @GetMapping("/")
    @ResponseBody
    fun listFile(): ResponseEntity<Response<MutableList<String>>> {
        val list = storageService.loadAll()
                .map { it.toAbsolutePath().toUri().path }
                .collect(Collectors.toList())

        return ResponseEntity.ok().body(Response(RESPONSE_OK, "OK", list))
    }

    @GetMapping("/files/{fileName:.+}")
    @ResponseBody
    fun serveFile(@PathVariable fileName: String): ResponseEntity<Resource> {
        val file = storageService.loadAsResource(fileName)
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"").body(file)
    }

    @PostMapping("/uploadFile")
    @ResponseBody
    fun uploadFile(@RequestParam("extension") extension: String, @RequestParam("file") file: MultipartFile): ResponseEntity<Response<String>> {
        val path = storageService.store(file, extension)
        return ResponseEntity.accepted().body(Response(RESPONSE_OK, "OK", path))
    }

    @ExceptionHandler(StorageException::class)
    fun handleIllegalState(exception: StorageException): ResponseEntity<*> {
        return ResponseEntity.badRequest().body(Response(Constants.RESPONSE_FAILED_REQUEST, "Bad Request: ${exception.message}", null))
    }
}