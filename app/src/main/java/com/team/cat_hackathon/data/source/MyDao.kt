package com.team.cat_hackathon.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.team.cat_hackathon.data.models.Model

@Dao
interface MyDao {
    @Query("select * from Model where name = :name")
    fun getModelByName(name: String): List<Model>

    @Query("SELECT * FROM Model ")
    fun getAllModels(): List<Model>

    @Delete
    suspend fun deleteModel(model: Model)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModel(model: Model)

    @Update
    suspend fun updateModel(model: Model)
}