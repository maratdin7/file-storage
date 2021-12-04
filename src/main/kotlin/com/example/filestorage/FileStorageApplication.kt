package com.example.filestorage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.io.File
import java.io.FileOutputStream
import java.net.URL

@SpringBootApplication
class FileStorageApplication

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
    val defaultPath = if (args.isNotEmpty()) args[0] else return
    runBlocking<Unit> {
        launch(Dispatchers.IO) { DownloadFile(defaultPath).readFiles() }
    }
}
