package com.example.filestorage

import org.apache.tomcat.util.http.fileupload.FileUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.DigestUtils
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootTest
class DownloadFileTests : DownloadFile("tmp") {

    private val absolutePath = Paths.get("").toAbsolutePath().toString()

    fun sha256(data: ByteArray) = DigestUtils.md5DigestAsHex(data)

    fun getFile(fileName: String): URL = this::class.java.getResource(fileName)

    private fun checkDownload(fileName: String) {
        val dir = "$absolutePath/tmp"
        Files.createDirectories(Paths.get(dir))
        val file = getFile("/$fileName")
        val url = file.toURI().toURL()
        val path = "$dir/$fileName"
        download(url.toString(), path)

        val downloadBytes = File(path).readBytes()

        FileUtils.deleteDirectory(File(dir))
        Assertions.assertEquals(sha256(file.readBytes()), sha256(downloadBytes))
    }

    @Test
    fun downloadSmall() {
        checkDownload("text.txt")
    }



    @Test
    fun downloadLarge() {
        checkDownload("video.mp4")
    }

    @Test
    fun fileLength() {
        val file = getFile("/video.mp4")
        val url = file.toURI().toURL()
        Assertions.assertEquals(file.readBytes().size.toLong(), url.fileLength())
    }

}