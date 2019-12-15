package com.sun_asterisk.myfood.utils

object Constant {
    //Default numbers
    const val DEFAULT_VALUE = 0
    const val INVALID_VALUE = -1
    const val DEFAULT_PAGE = 1
    const val TIME_DELAY = 1500L
    const val TIME_DELAY_SHORT = 500L
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
    const val BASE_URL = "http://192.168.0.103:3000/"
    const val LOCAL_HOST = "localhost"
    const val IP = "192.168.0.103"

    // Character
    const val SLASH = "/"

    // Format
    const val DEFAULT_UNIT_COST = "VND"
    const val ENTER_SPACE_FORMAT = "\n"
    const val KILOMETER = "km"
    const val DATETIME_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val DATETIME_FORMAT_FULL = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    const val PASSWORD_NUMBER_OF_CHARACTER = 6
    const val SHIFT_AM = "AM"
    const val SHIFT_PM = "PM"
    const val SHIFT_AM_HOUR_RULE = 9
    const val SHIFT_PM_HOUR_RULE = 15

    // Image
    const val REQUEST_IMAGE_CAPTURE = 0
    const val REQUEST_GALLERY_IMAGE = 1
    const val REQUEST_SETTING = 101
    const val ASPECT_RATIO_X = 1F
    const val ASPECT_RATIO_Y = 1F
    const val IMAGE_COMPRESSION_QUALITY = 80
    const val BITMAP_MAX_WIDTH = 1000
    const val BITMAP_MAX_HEIGHT = 1000
    const val CHILD_CAMERA = "camera"
    const val SCHEME_PACKAGE = "package"

    const val IMAGE_TYPE = "image/*"
    const val PART_FILE = "file"
    const val IMAGE_FILE_FORMAT = ".jpg"
    const val PHONE_LENGTH = 10
    const val REQUEST_LOCATION = 1000
    const val TELL = "tel:"
}
