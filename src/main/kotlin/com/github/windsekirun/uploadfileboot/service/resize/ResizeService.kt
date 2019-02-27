package com.github.windsekirun.uploadfileboot.service.resize

import java.io.File

interface ResizeService {

    fun resizeImage(imgLocation: String, width: Int): File

    fun resizeImage(imgLocation: String, widths: List<Int>): List<File>
}