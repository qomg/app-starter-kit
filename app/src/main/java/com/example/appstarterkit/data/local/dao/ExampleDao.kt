package com.example.appstarterkit.data.local.dao

import androidx.room.*
import com.example.appstarterkit.data.local.ExampleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExampleDao {

    @Query("SELECT * FROM example_table ORDER BY timestamp DESC")
    fun getAllExamples(): Flow<List<ExampleEntity>>

    @Query("SELECT * FROM example_table WHERE id = :id")
    fun getExampleById(id: String): Flow<ExampleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExample(example: ExampleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamples(examples: List<ExampleEntity>)

    @Update
    suspend fun updateExample(example: ExampleEntity)

    @Delete
    suspend fun deleteExample(example: ExampleEntity)

    @Query("DELETE FROM example_table WHERE id = :id")
    suspend fun deleteExampleById(id: String)

    @Query("DELETE FROM example_table")
    suspend fun deleteAllExamples()
}
