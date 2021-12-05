package com.example.filestorage

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.tongfei.progressbar.ProgressBar
import me.tongfei.progressbar.ProgressBarStyle
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
import java.time.Duration
import java.time.temporal.ChronoUnit
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


class SimpleProgressBar(task: String, length: Long) : ProgressBar(
    task,
    length,
    10,
    System.out,
    ProgressBarStyle.COLORFUL_UNICODE_BLOCK,
    "",
    1,
    false,
    null,
    ChronoUnit.SECONDS,
    0L,
    Duration.ZERO
)

class DownloadFile(private val defaultPath: String) {

    private fun URL.download(path: String, updateProgress: (n: Long) -> Unit) {
        openStream().use { input ->
            FileOutputStream(File(path)).use { output ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var bytesRead = input.read(buffer)
                while (bytesRead >= 0) {
                    updateProgress(bytesRead.toLong())
                    output.write(buffer, 0, bytesRead)
                    bytesRead = input.read(buffer)
                }
            }
        }
    }

    private fun download(link: String, path: String) {
        val url = URL(link)
        val length = url.openConnection().run {
            connect()
            contentLength.toLong()
        }
        SimpleProgressBar("Скачивание", length).use {
            url.download(path, it::stepBy)
            it.extraMessage = "Успешно!"
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
            println("Какие-то проблемы")
        }
        readFiles()
    }
}

fun main(args: Array<String>) {
    runApplication<FileStorageApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
