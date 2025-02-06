package com.sharma.com.sharma.data.jokes

import com.sharma.com.sharma.data.response.JokePaginationResponse
import com.sharma.com.sharma.data.response.JokeResponse
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoJokeDataSource(db: CoroutineDatabase) : JokeDataSource {
    private val collection = db.getCollection<Joke>()
    override suspend fun addNewJoke(joke: Joke): Boolean {
        val oldJokeResponse = joke.jokeId.let { collection.findOne(Joke::jokeId eq it) }
        return if (oldJokeResponse != null) {
            false
        } else {
            collection.insertOne(joke).wasAcknowledged()
        }
    }

    override suspend fun getAllJokes(): List<JokeResponse> {
        val list = collection.find().toList()

        return list.map {
            JokeResponse(
                type = it.type,
                setup = it.setup,
                delivery = it.delivery,
                joke = it.joke,
                jokeId = it.jokeId
            )
        }
    }

    override suspend fun getJokesWithPagination(page: Int, size: Int): JokePaginationResponse {
        val skip = (page - 1) * size
        val jokes = collection.find().skip(skip).limit(size).ascendingSort(Joke::jokeId).toList().map {
            JokeResponse(
                type = it.type,
                setup = it.setup,
                delivery = it.delivery,
                joke = it.joke,
                jokeId = it.jokeId
            )
        }
        val totalJokes = collection.countDocuments()

        return JokePaginationResponse(
            status = jokes.isNotEmpty(),
            page = page,
            totalJokes = totalJokes,
            size = size,
            jokes = jokes,
            totalPages = (totalJokes / size) + if (totalJokes % size == 0L) 0 else 1
        )
    }
}