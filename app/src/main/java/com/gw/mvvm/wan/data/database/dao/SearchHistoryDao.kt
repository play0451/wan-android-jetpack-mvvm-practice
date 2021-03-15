package com.gw.mvvm.wan.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gw.mvvm.wan.data.model.SearchHistoryInfo

/**
 * 搜索历史DAO
 * @author play0451
 */
@Dao
interface SearchHistoryDao {

    @Insert
    fun insertHistories(vararg histories: SearchHistoryInfo): List<Long>

    @Update
    fun updateHistories(vararg histories: SearchHistoryInfo)

    @Delete
    fun deleteHistories(vararg histories: SearchHistoryInfo)

    @Query("SELECT * FROM SearchHistoryInfo ORDER BY date DESC")
    fun getAllHistories(): LiveData<List<SearchHistoryInfo>>

    @Query("SELECT * FROM Searchhistoryinfo WHERE id=:id")
    fun getHistoryById(id: Long): SearchHistoryInfo?

    @Query("DELETE FROM Searchhistoryinfo WHERE id=:id")
    fun deleteHistoryById(id: Long)

    @Query("DELETE FROM Searchhistoryinfo")
    fun deleteAllHistories()

    @Query("SELECT * FROM searchhistoryinfo WHERE name=:name")
    fun getHistoryByName(name: String): SearchHistoryInfo?
}