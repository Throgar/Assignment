package com.example.UserApi

import com.example.assignment.ui.User
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import ru.gildor.coroutines.okhttp.await

object UserApi {
    /**
     * @param page is currently static, we want to change it to make pagination
     */
    suspend fun getUsers(page: Int): Result<List<User>> =
        client.httpGet(
            url   = "https://reqres.in/api/users/?page=${page}&per_page=20".toHttpUrl(),
            parse = ::parseUsers)
}

private val client =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

private suspend fun <T> OkHttpClient.httpGet(url: HttpUrl, parse: (String) -> T): Result<T> =
    try {
        val good: Request = Request.Builder().url(url).get().build()
        val result = newCall(good).await()
        val parsed = parse(result.body!!.string())
        Result.success(parsed)
    } catch (e: Exception) {
        Result.failure(e)
    }

private fun parseUsers(json: String): List<User> =
    JSONObject(json).getJSONArray("data").map {
        User(
            id   = it.getString("id"),
            email = it.getString("email"),
            firstName = it.getString("first_name"),
            lastName = it.getString("last_name"),
            avatar = it.getString("avatar")
            )
    }

private fun <T> JSONArray.map(action: (JSONObject) -> T): List<T> =
    (0 until length()).map { action(getJSONObject(it)) }