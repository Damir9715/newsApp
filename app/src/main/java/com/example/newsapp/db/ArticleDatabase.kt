package com.example.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.model.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract val articleDao: ArticleDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: ArticleDatabase

        fun getInstance(context: Context): ArticleDatabase {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ArticleDatabase::class.java,
                        "news"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}