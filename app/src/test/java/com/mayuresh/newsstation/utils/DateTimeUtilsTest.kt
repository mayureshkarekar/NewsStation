package com.mayuresh.newsstation.utils

import com.mayuresh.newsstation.utils.DateTimeUtils.DAY_DATE_TIME_FORMAT
import com.mayuresh.newsstation.utils.DateTimeUtils.SERVER_DATE_TIME_FORMAT
import com.mayuresh.newsstation.utils.DateTimeUtils.convertDateTime
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(value = Parameterized::class)
class DateTimeUtilsTest(
    private val inputDate: String,
    private val expectedDate: String?
) {
    /**
     * Test case for validating date time conversion.
     **/
    @Test
    fun convertDateTime() {
        // Collecting the result.
        val convertedDate = convertDateTime(inputDate, SERVER_DATE_TIME_FORMAT, DAY_DATE_TIME_FORMAT)

        // Validating the result.
        assertEquals(expectedDate, convertedDate)
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{index} : Converting {0} to {1}")
        fun input(): List<Array<Any?>> {
            return listOf(
                // Valid formats.
                arrayOf("2022-01-15T14:30:00", "Sat, 15 Jan 2022"),
                arrayOf("2023-07-05T08:45:20", "Wed, 05 Jul 2023"),
                arrayOf("2024-12-01T19:00:45", "Sun, 01 Dec 2024"),
                arrayOf("2025-09-20T10:15:30", "Sat, 20 Sep 2025"),
                arrayOf("2026-03-10T22:05:55", "Tue, 10 Mar 2026"),

                // Invalid formats.
                arrayOf("13-25-2025", null),
                arrayOf("2023-02-30T08:45", null),
                arrayOf("20250415T127000", null),
                arrayOf("", null)// Fail
            )
        }
    }
}