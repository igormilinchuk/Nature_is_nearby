package com.example.nature.data.remote

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PhotoUrlTest {

    @Test
    fun httpIsUpgradedToHttps() {
        assertEquals(
            "https://static.inaturalist.org/photos/1/medium.jpg",
            "http://static.inaturalist.org/photos/1/square.jpg".toPhotoUrl()
        )
    }

    @Test
    fun protocolRelativeUrlGetsHttps() {
        assertEquals(
            "https://inaturalist-open-data.s3.amazonaws.com/photos/2/medium.jpeg",
            "//inaturalist-open-data.s3.amazonaws.com/photos/2/square.jpeg".toPhotoUrl()
        )
    }

    @Test
    fun blankOrBrokenUrlBecomesNull() {
        assertNull(null.toPhotoUrl())
        assertNull("   ".toPhotoUrl())
        assertNull("photos/3/square.jpg".toPhotoUrl())
    }
}
