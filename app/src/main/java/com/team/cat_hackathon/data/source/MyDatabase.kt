package com.team.cat_hackathon.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team.cat_hackathon.data.models.User

@Database(entities = [User::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract val myDao: MyDao
}