package com.team.cat_hackathon.di

import android.content.Context
import androidx.room.Room
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.data.repositories.AuthRepository
import com.team.cat_hackathon.data.repositories.HomeRepositoryImpl
import com.team.cat_hackathon.data.repositories.TeamsRepository
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
        @ApplicationContext context: Context,
        dataStoreImpl: DataStoreImpl
    ): HomeRepositoryImpl {
        return HomeRepositoryImpl(db.myDao, context, dataStoreImpl)
    }

    @Provides
    @Singleton
    fun provideTeamsRepository(
        db: MyDatabase,
        @ApplicationContext context: Context,
        dataStoreImpl: DataStoreImpl
    ): TeamsRepository {
        return TeamsRepository(dataStoreImpl)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        dataStoreImpl: DataStoreImpl
    ): AuthRepository {
        return AuthRepository(dataStoreImpl)
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