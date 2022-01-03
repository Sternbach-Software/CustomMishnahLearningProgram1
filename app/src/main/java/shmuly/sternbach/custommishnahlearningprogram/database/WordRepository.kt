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
package shmuly.sternbach.custommishnahlearningprogram.database

import androidx.annotation.WorkerThread
import com.example.android.roomwordssample.WordDao
import kotlinx.coroutines.flow.Flow
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnit

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
//    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()
    val allTimelines: Flow<List<ProgramUnit>> = wordDao.getAllTimelines()
    val todaysMaterial: Flow<List<ProgramUnit>> = wordDao.getTodaysMaterial()
    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(units: List<ProgramUnit>) {
        wordDao.insertAll(*units.toTypedArray())
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(unit: ProgramUnit) {
        wordDao.insert(unit)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(unit: ProgramUnit) {
        wordDao.update(unit)
    }
}
