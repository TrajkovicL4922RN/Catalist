package com.example.myapplication.repository


import com.example.myapplication.model.Cat
import com.example.myapplication.model.CatApi
import com.example.myapplication.model.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val catApi: CatApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    private var currentCatList = emptyList<Cat>()

    suspend fun filter(query: String): List<Cat> {
        fetchAll()
        if (query.isEmpty()) return currentCatList

        return currentCatList.filter { cat ->
            cat.name.startsWith(query, true) ||
                    cat.alternativeName?.startsWith(query, true) == true
        }
    }

    suspend fun findById(id: String): Cat? {
        fetchAll()
        return currentCatList.find { it.id == id }
    }

    suspend fun findAll(): List<Cat> {
        return fetchAll()
    }

    private suspend fun fetchAll(): List<Cat> {
        if (currentCatList.isEmpty()) {
            withContext(ioDispatcher) {
                currentCatList = catApi.getAllBreads()
            }
        }
        return currentCatList
    }
}
