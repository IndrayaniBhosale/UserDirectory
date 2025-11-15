package com.example.userdirectory.data

import com.example.userdirectory.data.local.UserDao
import com.example.userdirectory.data.remote.ApiService
import com.example.userdirectory.data.remote.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(
    private val api: ApiService,
    private val dao: UserDao
) {

    fun usersFlow(): Flow<List<com.example.userdirectory.data.local.UserEntity>> =
        dao.getAll()

    fun searchFlow(query: String): Flow<List<com.example.userdirectory.data.local.UserEntity>> =
        if (query.isBlank()) dao.getAll() else dao.search(query)

    suspend fun refreshUsers() = withContext(Dispatchers.IO) {
        println("🔵 refreshUsers CALLED")
        runCatching {
            println("🔵 Calling API…")
            val users = api.getUsers()
            println("🟢 API returned ${users.size} users")

            val mapped = users.map { it.toEntity() }

            dao.upsertAll(mapped)
            println("🟢 Database insert DONE")
        }.onFailure { e ->
            println("🔴 ERROR: ${e.message}")
            e.printStackTrace()
        }
    }
}
