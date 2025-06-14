package com.example.myapplication.main

sealed class MainScreenEvent {
    data class FilterList(val query: String) : MainScreenEvent()
}