package com.seeker.myapplication.offline

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.seeker.myapplication.model.TweetModel
import com.seeker.myapplication.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(TweetModel::class), version = 1, exportSchema = false)
public abstract class TweetDatabase : RoomDatabase() {

    abstract fun tweetDao(): TweetDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: TweetDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TweetDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TweetDatabase::class.java,
                    "tweet_database"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }


    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    /*tweetDao.deleteAll()

                    // Add sample words.
                    var word = Word("Hello")
                    tweetDao.insert(word)*/
                }
            }
        }

    }
}