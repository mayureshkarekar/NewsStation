package com.mayuresh.newsstation.utils

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeUtils {
    const val SERVER_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    const val DAY_DATE_TIME_FORMAT = "EEE, dd MMM yyyy"

    /**
     * Converts a string date time to the given format.
     * @param inputDateTime - The input string representing the date time.
     * @param inputFormat - The format of the input date time string.
     * @param outputFormat - The desired format of the output date time string.
     *
     * @return The formatted date time string, or null if there's an error in parsing.
     */
    fun convertDateTime(inputDateTime: String, inputFormat: String, outputFormat: String): String? {
        return try {
            val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
            val outputDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault())

            val date = inputDateFormat.parse(inputDateTime)

            date?.let { outputDateFormat.format(it) }
        } catch (e: ParseException) {
            Timber.e("Failed to convert date time.")
            null
        }
    }
}