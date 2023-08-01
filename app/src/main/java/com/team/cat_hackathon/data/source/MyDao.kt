package com.team.cat_hackathon.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.team.cat_hackathon.data.models.User

@Dao
interface MyDao {

    //todo: refactor this interface
    @Query("select * from User where name = :name")
    fun getModelByName(name: String): List<User>

    @Query("SELECT * FROM User ")
    fun getAllModels(): List<User>

    @Delete
    suspend fun deleteModel(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModel(user: User)

    @Update
    suspend fun updateModel(user: User)
}