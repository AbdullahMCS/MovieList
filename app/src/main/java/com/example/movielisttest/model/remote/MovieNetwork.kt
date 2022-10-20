package com.example.movielisttest.model.remote

import android.net.Uri
import com.example.movielisttest.model.MovieResponse
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MovieNetwork {
    companion object{
        const val BASE_URL = "https://gist.githubusercontent.com/"
        const val ENDPOINT = "AntoninoAN/f3fa4b2260c51a5f80904c747009289e/raw/6576691177f6b093afd3bf2bbc5e936b62d50721/MovieGist"
        val uriPath = Uri.parse("$BASE_URL$ENDPOINT")
        val url = URL(uriPath.toString())
    }

    fun getMovieList(): List<MovieResponse>{
        val httpUrlConnection = url.openConnection() as HttpURLConnection
        httpUrlConnection.readTimeout = 10000
        httpUrlConnection.connectTimeout = 15000
        httpUrlConnection.requestMethod = "GET"
        httpUrlConnection.doInput = true
        httpUrlConnection.connect()

        return httpUrlConnection.inputStream.run {
            deserializeInputStream(this)
        }.let {
            parseToMovieResponse(it)
        }
    }

    fun deserializeInputStream(IS: InputStream): String{
        val builder = StringBuilder()
        val reader = BufferedReader(InputStreamReader(IS))
        var line: String? = reader.readLine()
        while(line != null){
            builder.append(line)
            builder.append("\n")
            line = reader.readLine()
        }

        if (builder.isEmpty()) {
            return "NA"
        }
        return builder.toString()
    }

    fun parseToMovieResponse(deserialize: String): List<MovieResponse>{
        val response = JSONArray(deserialize)
        val listOfMovies = mutableListOf<MovieResponse>()

        for(i in 0 until response.length()){
            val movieElement = response.getJSONObject(i)
            val movieResponse = MovieResponse(
                movieElement.getString("title"),
                movieElement.getString("image"),
                movieElement.getDouble("rating").toFloat(),
                movieElement.getInt("releaseYear"),
                movieElement.getJSONArray("genre").parseJasonArrayToList()
            )
            listOfMovies.add(movieResponse)
        }
        return listOfMovies
    }

    private fun JSONArray.parseJasonArrayToList(): List<String>{
        val result = mutableListOf<String>()
        for (i in 0 until this.length()){
            result.add(this.getString(i))
        }
        return result
    }
}