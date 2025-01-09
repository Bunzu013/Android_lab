package com.example.lab1

import android.content.Context
import androidx.room.Room
import com.example.lab1.db.PlayerDao
import com.example.lab1.db.PlayerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePlayerDao(db: AppDatabase): PlayerDao {
        return db.playerDao()
    }

    @Provides
    @Singleton
    fun providePlayerRepository(playerDao: PlayerDao): PlayerRepository {
        return PlayerRepository(playerDao)
    }
}
