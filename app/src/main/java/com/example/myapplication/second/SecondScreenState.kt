package com.example.myapplication.second

import com.example.myapplication.model.Cat

data class SecondScreenState(
    val cat: Cat? = null,
    val loading: Boolean = true,
    val error: String? = null
)