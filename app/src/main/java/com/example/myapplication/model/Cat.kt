package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cat(
    val id: String,
    val name: String,
    @SerialName("alt_names")
    val alternativeName: String? = null,
    val description: String,
    val origin: String,
    val temperament: String,
    @SerialName("life_span")
    val lifeSpan: String,
    val weight: Weight,
    val adaptability: Int,
    @SerialName("affection_level")
    val affectionLevel: Int,
    @SerialName("child_friendly")
    val childFriendly: Int,
    @SerialName("dog_friendly")
    val dogFriendly: Int,
    @SerialName("energy_level")
    val energyLevel: Int,
    val intelligence: Int,
    val rare: Int,
    val image:CatImage? = null,
    @SerialName("wikipedia_url")
    val wikipediaUrl: String? = null
){
    fun allTemperaments(): List<String> {
        return this.temperament.replace(" ", "").split(",")
    }
}
@Serializable
data class CatImage(
    val width: Int,
    val height: Int,
    val url: String
)
@Serializable
data class Weight(
    val imperial: String,
    val metric: String
)