/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.roomwordssample

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnit
import java.time.LocalDate

// The flow always holds/caches latest version of data. Notifies its observers when the
// data has changed.


/**
 * The Room Magic is in this file, where you map a method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
interface WordDao {
    companion object {
        const val sortOrder = "ORDER BY date ASC, isReview DESC, programID DESC, `group` ASC, positionInGroup ASC"
    }

//    @Query("SELECT * FROM word_table ORDER BY word ASC")
//    fun getAlphabetizedWords(): Flow<List<Word>>

    @Query("SELECT * FROM units_table WHERE date = :day $sortOrder")
    fun getMaterialByDay(day: String): Flow<List<ProgramUnit>>

    @Query("SELECT * from units_table WHERE programID = :programID $sortOrder")
    fun getTimeline(programID: Int): Flow<List<ProgramUnit>>

    /**
     * Returns all units from all programs, sorted by date
     * */
    @Query("SELECT * from units_table $sortOrder")
    fun getAllTimelines(): Flow<List<ProgramUnit>>
    /**
     * Returns all units from all programs, sorted by date
     * */
    @Query("SELECT * from units_table $sortOrder")
    fun getAllTimelinesList(): List<ProgramUnit>

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insert(word: Word)

//    @Query("DELETE FROM word_table")
//    suspend fun deleteAll()

    @Query("DELETE FROM units_table WHERE programID = :programID")
    suspend fun deleteTimeline(programID: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(unit: ProgramUnit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(units: List<ProgramUnit>)

    @Update
    suspend fun update(unit: ProgramUnit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg units: ProgramUnit)
}
