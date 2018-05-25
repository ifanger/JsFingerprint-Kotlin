package com.example.ifanger.fpmanager

import java.io.InputStream

/**
 * InputStream management.
 */
public class InputStreamManager {

    /**
     * Converts InputStream into String.
     */
    fun inputStreamToString(`is`: InputStream?): String {
        val PKG_SIZE = 1024
        val data = ByteArray(PKG_SIZE)
        val buffer = StringBuilder(PKG_SIZE * 10)
        var size: Int?

        size = `is`?.read(data, 0, data.size)
        while (size!! > 0) {
            val str = String(data, 0, size)
            buffer.append(str)
            size = `is`?.read(data, 0, data.size)
        }
        return buffer.toString()
    }
}