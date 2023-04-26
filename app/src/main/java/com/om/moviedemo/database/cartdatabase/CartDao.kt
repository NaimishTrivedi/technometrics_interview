package com.om.moviedemo.database.cartdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.om.moviedemo.database.entity.CartModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert
    suspend fun addCart(cartModel: CartModel): Long

    @Query("SELECT EXISTS(SELECT * FROM tblcartdata WHERE movieId = :movieId)")
    suspend fun isAlreadyInCart(movieId : String) : Boolean

    @Query("SELECT * FROM tblcartdata")
    fun getCartList() : Flow<List<CartModel>>
}