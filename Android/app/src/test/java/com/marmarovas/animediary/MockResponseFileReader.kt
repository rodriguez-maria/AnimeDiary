package com.marmarovas.animediary

import java.io.InputStreamReader

class MockResponseFileReader(path: String){
    private val content: String

    init {
        val reader = InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    }

    fun getContent() : String {
        return content
    }
}