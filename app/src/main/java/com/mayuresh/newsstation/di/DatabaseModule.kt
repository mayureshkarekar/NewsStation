package com.mayuresh.newsstation.di

import android.content.Context
import androidx.room.Room
import com.mayuresh.newsstation.database.NEWS_DATABASE_NAME
import com.mayuresh.newsstation.database.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(context, NewsDatabase::class.java, NEWS_DATABASE_NAME).build()
    }
}