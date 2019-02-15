package com.github.windsekirun.uploadfileboot.controller

import com.github.windsekirun.uploadfileboot.Constants.RESPONSE_OK
import com.github.windsekirun.uploadfileboot.data.Response
import com.github.windsekirun.uploadfileboot.service.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.util.stream.Collectors

@Controller
class FileUploadController @Autowired constructor(private val storageService: StorageService) {
    @Value("\${service.location.text}")
    private lateinit var location: String

    @GetMapping("/")
    @ResponseBody
    fun listUploadFiles(): ResponseEntity<Response<MutableList<String>>> {
        val list = storageService.loadAll()
                .map {
                    MvcUriComponentsBuilder.fromMethodName(FileUploadController::class.java,
                            "serveFile", it.fileName.toString()).build().toString()
                }
                .collect(Collectors.toList())


        return ResponseEntity.ok().body(Response(RESPONSE_OK, "OK", list))
    }

    @GetMapping("/files/{fileName:.+}")
    @ResponseBody
    fun serveFile(@PathVariable fileName: String): ResponseEntity<Resource> {
        val file = storageService.loadAsResource(fileName)
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"").body(file)
    }

    @PostMapping("/uploadFile/")
    @ResponseBody
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<Response<String>> {
        storageService.store(file)
        val path = "$location/${file.originalFilename}"
        return ResponseEntity.accepted().body(Response(RESPONSE_OK, "OK", path))
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(exception: IllegalStateException): ResponseEntity<*> {
        return ResponseEntity("Error ${exception.message}", HttpStatus.BAD_REQUEST)
    }
}