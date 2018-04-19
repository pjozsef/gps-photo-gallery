package com.github.ttaf.gpsphotogallery.model

const val TABLE_PHOTO = "photo"

const val SELECT_ALL_PHOTOS = "SELECT * FROM $TABLE_PHOTO"
const val SELECT_BY_BOUNDING_BOX =
        "SELECT * " +
                "FROM $TABLE_PHOTO " +
                "WHERE lat <= :northEastLat " +
                "AND lat >= :southWestLat " +
                "AND lon <= :northEastLon " +
                "AND lon >= :southWestLon"
const val SELECT_ALL_PATHS = "SELECT path FROM $TABLE_PHOTO"
const val SELECT_LATEST_TIMESTAMP = "SELECT MAX(timestamp) FROM $TABLE_PHOTO"
const val DELETE_ALL_PHOTOS = "DELETE FROM $TABLE_PHOTO"
const val DELETE_BY_PATH = "DELETE FROM $TABLE_PHOTO WHERE path = :path"
