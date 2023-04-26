package com.om.moviedemo.database.cartdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.om.moviedemo.database.entity.CartModel

@Dao
interface CartDao {
    @Insert
    suspend fun addCart(cartModel: CartModel): Long

    @Query("SELECT EXISTS(SELECT * FROM tblcartdata WHERE movieId = :movieId)")
    suspend fun isAlreadyInCart(movieId : String) : Boolean
}