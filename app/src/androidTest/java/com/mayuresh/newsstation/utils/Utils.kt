package com.mayuresh.newsstation.utils

import java.io.InputStreamReader

object Utils {
    /**
     * This function returns the text content of the file stored in resources.
     * @param fileName - Name of the file which is to be read.
     **/
    fun readResourceFile(fileName: String): String {
        val inputStream = Utils::class.java.getResourceAsStream(fileName)
        val builder = StringBuilder()
        val reader = InputStreamReader(inputStream, "UTF-8")

        reader.readLines().forEach {
            builder.append(it)
        }

        return builder.toString()
    }
}