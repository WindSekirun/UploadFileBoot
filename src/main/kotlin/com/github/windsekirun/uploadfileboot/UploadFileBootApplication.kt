package com.github.windsekirun.uploadfileboot

import com.github.windsekirun.uploadfileboot.service.storage.StorageService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class UploadFileBootApplication {
    @Bean
    fun init(storageService: StorageService): CommandLineRunner {
        return CommandLineRunner {
            storageService.init()
        }
    }
}

fun main(args: Array<String>) {
    runApplication<UploadFileBootApplication>(*args)
}