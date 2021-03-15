package com.gw.mvvm.wan.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gw.mvvm.framework.core.appContext
import com.gw.mvvm.wan.data.database.dao.SearchHistoryDao
import com.gw.mvvm.wan.data.model.SearchHistoryInfo

/**
 * App数据库
 * @author play0451
 */
@Database(entities = [SearchHistoryInfo::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        val instance: AppDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { createDatabase() }

        private fun createDatabase(): AppDatabase {
            return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java, "AppDatabase"
            ).build()
        }
    }

    /**
     * 搜索历史DAO
     * @return SearchHistoryDao
     */
    abstract fun searchHistoryDato(): SearchHistoryDao
}