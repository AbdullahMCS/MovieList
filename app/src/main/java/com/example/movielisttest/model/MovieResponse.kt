package com.example.movielisttest.model

/**
 * in-memory data representation
 * toString()
 * hashCode()
 * equals()
 * copy()
 * It can't be inherited
 * It needs at least 1 field/property
 */
data class MovieResponse(
    val title: String,
    val image: String,
    val rating: Float,
    val releaseYear: Int,
    val genre: List<String>
)

class Response: ArrayList<MovieResponse>()
