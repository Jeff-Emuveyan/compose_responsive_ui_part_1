package com.example.compose_responsive_ui_part_1

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val map: MutableMap<String, String> = mutableMapOf()
        map.put("a", "A")
        map.put("b", "B")
        add(map)
        assertEquals(3, map.size)
    }

    fun add(map: MutableMap<String, String>) {
        map.put("name", "jeff emuveyan")
    }
}