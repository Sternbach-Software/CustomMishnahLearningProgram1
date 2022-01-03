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

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import shmuly.sternbach.custommishnahlearningprogram.adapters.ld
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnit
import shmuly.sternbach.custommishnahlearningprogram.database.WordRepository
import java.time.LocalDate

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

class ProgramUnitsViewModel(private val repository: WordRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
//    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    val todaysMaterial: LiveData<List<Triple<LocalDate, List<ProgramUnit>/*new material*/, List<ProgramUnit>/*reviews*/>>> =
        repository
            .todaysMaterial
            .asLiveData()
            .listOfProgramUnitsMappedToRecyclerViewDataStructure()

    val allTimelines: LiveData<List<Triple<LocalDate, List<ProgramUnit>/*new material*/, List<ProgramUnit>/*reviews*/>>> =
        repository
            .allTimelines
            .asLiveData()
            .listOfProgramUnitsMappedToRecyclerViewDataStructure()

    private fun LiveData<List<ProgramUnit>>.listOfProgramUnitsMappedToRecyclerViewDataStructure(): LiveData<List<Triple<LocalDate, List<ProgramUnit>, List<ProgramUnit>>>> =
        Transformations.map(this) {
            val map = mutableListOf<Triple<LocalDate, List<ProgramUnit>, List<ProgramUnit>>>()
            if (it.isEmpty()) map.also { ld("List of program units recieved in live data was empty.") } else {
                ld("List from live data was not empty")
                var unit = it.first()
                var triple =
                    Triple(unit.date, mutableListOf<ProgramUnit>(), mutableListOf<ProgramUnit>())
                if (unit.isReview) triple.third.add(unit)
                else triple.second.add(unit)

                for (index in 1 until it.size) {
                    unit = it[index]
                    if (unit.date == triple.first) {
                        if (unit.isReview) triple.third.add(unit)
                        else triple.second.add(unit)
                    } else {
                        map.add(triple)
                        triple = Triple(unit.date, mutableListOf(), mutableListOf())
                        if (unit.isReview) triple.third.add(unit)
                        else triple.second.add(unit)
                    }
                }
                map
            }
        }
//    val listOfDateToContentToReview: Map<LocalDate, Pair<List<ProgramUnit>/*new material*/, List<ProgramUnit>/*reviews*/>>
//    val intervalSize: Int
//    val repository: WordRepository
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun update(unit: ProgramUnit) = viewModelScope.launch {
        repository.update(unit)
    }
    fun insertAll(units: List<ProgramUnit>) = viewModelScope.launch {
        repository.insertAll(units)
    }
}

class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProgramUnitsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProgramUnitsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
