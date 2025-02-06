package com.sharma.com.sharma.data.jokes

import com.sharma.com.sharma.data.response.JokePaginationResponse
import com.sharma.com.sharma.data.response.JokeResponse

interface JokeDataSource {
    suspend fun addNewJoke(joke: Joke):Boolean
    suspend fun getAllJokes():List<JokeResponse>?
    suspend fun getJokesWithPagination(page:Int,size:Int): JokePaginationResponse
}