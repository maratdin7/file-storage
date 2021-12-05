package com.example.filestorage

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files
import java.nio.file.Paths

@RestController
class Controller {
    @Value("\${defaultPath}")
    private lateinit var defaultPath: String

    @GetMapping("/download/{name}")
    fun upload(@PathVariable("name") name: String): ByteArray {
        val path = Paths.get(defaultPath, name)
        return Files.readAllBytes(path)
    }
}