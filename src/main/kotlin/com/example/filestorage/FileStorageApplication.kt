package com.example.filestorage

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.tomcat.util.http.fileupload.FileUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import javax.annotation.PostConstruct

@SpringBootApplication
class FileStorageApplication {
    @Value("\${clearDirectoryAfterStart}")
    private var isDirectoryNeedClear: Boolean = false

    @Value("\${defaultPath}")
    private lateinit var defaultPath: String


    fun clearDirectory() {
        if (isDirectoryNeedClear.not()) return
        val dir = File(defaultPath)
        FileUtils.cleanDirectory(dir)
    }

    @PostConstruct
    private fun postConstructor() {
        startDownloader()
        clearDirectory()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun startDownloader() {
        GlobalScope.launch { DownloadFile(defaultPath).readFiles() }
    }
}

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


class DownloadFile(private val defaultPath: String) {
    private fun download(link: String, path: String) {
        URL(link).openStream().use { input ->
            FileOutputStream(path).use { output ->
                input.copyTo(output)
            }
        }
    }

    fun readFiles() {
        print("Введите ссылку: ")
        val link = readln()
        print("Введите название файла: ")
        val name = readln()

        val path = "$defaultPath${File.separatorChar}$name"
        try {
            download(link, path)
        } catch (e: Exception) {
            println("Some problems")
        }
        println("==================================")
        readFiles()
    }
}

fun main(args: Array<String>) {
    runApplication<FileStorageApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
