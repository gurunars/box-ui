package com.gurunars.animal_item

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.gurunars.item_list.persistence.PlainDao

@Dao
interface AnimalItemDao : PlainDao<AnimalItem> {
    @Insert
    override fun insert(items: List<AnimalItem>)

    @Update
    override fun update(items: List<AnimalItem>)

    @Delete
    override fun delete(items: List<AnimalItem>)

    @Query("SELECT * FROM AnimalItem ORDER BY position, id")
    override fun all(): List<AnimalItem>

    @Query("DELETE FROM AnimalItem")
    fun truncate()
}