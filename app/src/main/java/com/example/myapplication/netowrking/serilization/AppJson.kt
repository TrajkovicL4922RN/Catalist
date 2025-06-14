package com.example.myapplication.netowrking.serilization

import kotlinx.serialization.json.Json

val AppJson = Json{
    ignoreUnknownKeys = true
    prettyPrint = true
}