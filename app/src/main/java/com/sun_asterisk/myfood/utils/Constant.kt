package com.sun_asterisk.myfood.utils

object Constant {
    //Default numbers
    const val DEFAULT_VALUE = 0
    const val DEFAULT_PAGE = 1
    const val TIME_DELAY = 1500L
    const val THUMBNAIL_MULTIPLIER = 0.1F
    const val RULE_NEW_FOOD_FOLLOW_DAY = 7
    const val MAX_TIME_DOUBLE_CLICK_EXIT = 2000L

    // Room database
    const val DATABASE_NAME = "My-food-api-database"

    // network setting
    const val READ_TIMEOUT: Long = 30
    const val WRITE_TIMEOUT: Long = 30
    const val CONNECTION_TIMEOUT: Long = 30

    // URL
    const val BASE_URL = "http://192.168.0.110:3000/"
    const val LOCAL_HOST = "localhost"
    const val IP = "192.168.0.110"

    // Character
    const val SLASH = "/"

    // Format
    const val DEFAULT_UNIT_COST = "VND"
    const val ENTER_SPACE_FORMAT = "\n"
    const val KILOMETER = "km"
    const val DATETIME_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val PASSWORD_NUMBER_OF_CHARACTER = 6
}
