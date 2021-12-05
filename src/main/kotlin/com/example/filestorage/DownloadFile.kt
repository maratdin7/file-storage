package com.example.filestorage

import me.tongfei.progressbar.ProgressBar
import me.tongfei.progressbar.ProgressBarStyle
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.time.Duration
import java.time.temporal.ChronoUnit

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