package com.example.filestorage

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.tomcat.util.http.fileupload.FileUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File
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

fun main(args: Array<String>) {
    runApplication<FileStorageApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
