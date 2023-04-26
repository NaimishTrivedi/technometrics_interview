package com.om.moviedemo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblcartdata")
data class CartModel(
    @ColumnInfo(name = "movieId")
    var movieId: String,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "seats")
    var seats: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)
