package com.example.filestorage

import org.apache.tomcat.util.http.fileupload.FileUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootTest
class FileStorageApplicationTests : FileStorageApplication() {

    private val absolutePath = Paths.get("").toAbsolutePath().toString()

    private fun clearDir(num: Int) {
        val dir = "$absolutePath/tmp"
        Files.createDirectories(Paths.get(dir))
        for (i in 0 until num)
            FileOutputStream(File("$dir/$i")).write(i.toString().toByteArray())
        isDirectoryNeedClear = true
        clearDirectory(dir)
        val numFiles = File(dir).listFiles().size
        FileUtils.deleteDirectory(File(dir))
        assertEquals(0, numFiles)
    }

    @Test
    fun clearDirectory() {
        clearDir(10)
    }

    @Test
    fun clearEmptyDirectory() {
        clearDir(0)
    }

}
