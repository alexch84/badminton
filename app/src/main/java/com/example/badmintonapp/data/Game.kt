package com.example.badmintonapp.data

import com.google.firebase.database.Exclude

data class Game(
    @get:Exclude
    var id: String? = null,
    var alexScore: Int? = null,
    var yuriyScore: Int? = null,
    var date: String? = null
)
