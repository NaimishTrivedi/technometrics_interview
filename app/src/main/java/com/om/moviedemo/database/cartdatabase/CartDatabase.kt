package com.om.moviedemo.database.cartdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.om.moviedemo.database.entity.CartModel

@Database(entities = [CartModel::class], version = 1)
abstract class CartDatabase : RoomDatabase() {
    abstract fun getCartDao(): CartDao

    companion object{
        @Volatile
        private var instance: CartDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CartDatabase::class.java,
                "cart_database"
            ).build()
    }
}