package com.example.localdatabase.di

import android.content.Context
import androidx.room.Room
import com.example.localdatabase.util.MyDatabase
import com.example.localdatabase.util.RoomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomDatabaseModule {
    @Singleton
    @Provides
    fun provideBlogDb(@ApplicationContext context: Context): MyDatabase {
        return Room
            .databaseBuilder(
                context,
                MyDatabase::class.java,
                MyDatabase.DATABASE_NAME)
            .build()
    }
    @Singleton
    @Provides
    fun provideBlogDAO(myDatabase: MyDatabase): RoomDao {
        return myDatabase.roomDao()
    }
}