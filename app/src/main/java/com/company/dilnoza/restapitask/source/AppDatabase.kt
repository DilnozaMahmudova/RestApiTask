package com.company.dilnoza.restapitask.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.company.dilnoza.restapitask.source.room.dao.PostsDao
import com.company.dilnoza.restapitask.source.room.entity.PostData

@Database(entities = [PostData::class],version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun postDao(): PostsDao
    companion object{
        @Volatile
        private var INSTANS: AppDatabase?=null
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance= INSTANS
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance=Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,"app_database").build()
                INSTANS =instance
                return instance
            }
        }
    }
}