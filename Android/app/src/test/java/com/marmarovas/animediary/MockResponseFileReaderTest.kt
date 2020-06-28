package com.marmarovas.animediary

import junit.framework.Assert.assertEquals
import org.junit.Test

class MockResponseFileReaderTest {
    @Test
    fun `read simple file`(){
        val reader = MockResponseFileReader("test")
        assertEquals(reader.getContent(), "success")
    }
}