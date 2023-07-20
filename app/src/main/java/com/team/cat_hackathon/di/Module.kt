package com.team.cat_hackathon.di

import android.content.Context
import androidx.room.Room
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.data.repositories.BaseRepositoryImpl
import com.team.cat_hackathon.data.source.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun provideMyDatabase(
        @ApplicationContext appContext: Context
    ): MyDatabase {
        return Room.databaseBuilder(
            appContext,
            MyDatabase::class.java,
            "db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBaseRepository(
        db: MyDatabase,
        @ApplicationContext context: Context
    ): BaseRepositoryImpl {
        return BaseRepositoryImpl(db.myDao, context)
    }

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext appContext: Context
    ): DataStoreImpl {
        return DataStoreImpl(appContext)
    }

    @Provides
    @Singleton
    fun provideContext(
        @ApplicationContext appContext: Context
    ): Context {
        return appContext
    }

}