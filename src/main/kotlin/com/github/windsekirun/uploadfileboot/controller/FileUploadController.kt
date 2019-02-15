package com.github.windsekirun.uploadfileboot.controller

import com.github.windsekirun.uploadfileboot.Constants.RESPONSE_OK
import com.github.windsekirun.uploadfileboot.data.ErrorResponse
import com.github.windsekirun.uploadfileboot.data.Response
import com.github.windsekirun.uploadfileboot.exception.StorageException
import com.github.windsekirun.uploadfileboot.service.storage.StorageService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.stream.Collectors
import javax.activation.MimetypesFileTypeMap

@RestController
class FileUploadController @Autowired constructor(private val storageService: StorageService) {
    @ResponseBody
    @GetMapping("/listFile")
    @ApiOperation("Get list of uploaded files")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success to fetch list", response = Response::class),
        ApiResponse(code = 400, message = "Bad request.", response = ErrorResponse::class)
    ])
    fun listFile(): ResponseEntity<Response<MutableList<String>>> {
        val list = storageService.loadAll()
                .map { it.toString() }
                .collect(Collectors.toList())

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(Response(RESPONSE_OK, "OK", list))
    }

    @ResponseBody
    @GetMapping("/files/{fileName:.+}")
    @ApiOperation("Get binary of uploaded file with given fileName")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success to fetch file", response = Resource::class),
        ApiResponse(code = 400, message = "Bad request.", response = ErrorResponse::class)
    ])
    fun serveFile(@PathVariable fileName: String): ResponseEntity<Resource> {
        val resource = storageService.loadAsResource(fileName)
        val contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(resource.file)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"")
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource)
    }

    @ResponseBody
    @PostMapping("/uploadFile")
    @ApiOperation("Request to upload given file")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success to upload.", response = Response::class),
        ApiResponse(code = 400, message = "Bad request.", response = ErrorResponse::class)
    ])
    fun uploadFile(@RequestParam("extension") extension: String, @RequestParam("file") file: MultipartFile): ResponseEntity<Response<String>> {
        val path = storageService.store(file, extension)
        val response = Response(RESPONSE_OK, "OK", path)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(response)
    }

    @ExceptionHandler(StorageException::class)
    fun handleIllegalState(exception: StorageException): ResponseEntity<*> {
        val response = ErrorResponse(message = "Bad request: ${exception.message}")
        return ResponseEntity.badRequest()
                .body(response)
    }
}