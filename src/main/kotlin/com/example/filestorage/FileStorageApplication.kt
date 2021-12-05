package com.example.filestorage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.tomcat.util.http.fileupload.FileUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Paths
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
class FileStorageApplication {
    @Value("\${clearDirectoryAfterStart}")
    private var isDirectoryNeedClear: Boolean = false

    @Value("\${defaultPath}")
    private lateinit var defaultPath: String
    @PostConstruct
    fun clearDirectory() {
        if (isDirectoryNeedClear.not()) return
        val dir = File(defaultPath)
        FileUtils.cleanDirectory(dir)
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
  val defaultPath = Properties().run {
        val stream = ClassLoader.getSystemResourceAsStream("application.properties")
        load(stream)
        getProperty("defaultPath", "/downloads")
    }

    runBlocking<Unit> {
        launch(Dispatchers.IO) { DownloadFile(defaultPath).readFiles() }
        launch {
            runApplication<FileStorageApplication>(*args) {
                setBannerMode(Banner.Mode.OFF)
            }
        }
    }
}
