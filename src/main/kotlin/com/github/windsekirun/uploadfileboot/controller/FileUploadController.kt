package com.github.windsekirun.uploadfileboot.controller

import com.github.windsekirun.uploadfileboot.Constants.RESPONSE_OK
import com.github.windsekirun.uploadfileboot.data.ErrorResponse
import com.github.windsekirun.uploadfileboot.data.ResizeInfo
import com.github.windsekirun.uploadfileboot.data.Response
import com.github.windsekirun.uploadfileboot.exception.StorageException
import com.github.windsekirun.uploadfileboot.service.resize.ResizeService
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

@RestController
class FileUploadController @Autowired constructor(private val storageService: StorageService,
                                                  private val resizeService: ResizeService) {
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
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .body(resource)
    }

    @ResponseBody
    @PostMapping("/uploadFile")
    @ApiOperation("Request to upload given file")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Success to upload.", response = Response::class),
        ApiResponse(code = 400, message = "Bad request.", response = ErrorResponse::class)
    ])
    fun uploadFile(@RequestParam("file") file: MultipartFile,
                   @RequestParam("thumb", defaultValue = "0", required = false) thumb: String): ResponseEntity<Response<ResizeInfo>> {
        val path = storageService.store(file)

        val list = if (thumb == "1") {
            resizeService.resizeImage(path, WIDTH_LIST)
                    .mapIndexed { index, dest -> WIDTH_LIST[index] to dest.name }
        } else {
            listOf<Pair<Int, String>>()
        }

        val resizeInfo = ResizeInfo(!list.isEmpty(), path, list)
        val response = Response(RESPONSE_OK, "OK", resizeInfo)
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

    companion object {
        private val WIDTH_LIST = listOf(100, 240, 480, 720, 1080)
    }
}