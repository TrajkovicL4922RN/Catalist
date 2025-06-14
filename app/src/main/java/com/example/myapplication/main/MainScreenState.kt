package com.example.myapplication.main

import com.example.myapplication.model.Cat

data class MainScreenState(
    val query: String = "", //ono sto se kuca u search-u
    val listCats: List<Cat> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null
)

