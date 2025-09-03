package com.wherefam.kmp.wherefam_kmp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "peer")
data class Peer(
    @PrimaryKey
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
)