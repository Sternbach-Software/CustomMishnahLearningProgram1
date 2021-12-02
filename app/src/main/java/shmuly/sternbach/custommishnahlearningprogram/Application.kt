package shmuly.sternbach.custommishnahlearningprogram

import android.app.Application
import io.objectbox.kotlin.boxFor
import shmuly.sternbach.custommishnahlearningprogram.activities.program
import shmuly.sternbach.custommishnahlearningprogram.activities.programInitialized
import shmuly.sternbach.custommishnahlearningprogram.activities.toDateMap
import shmuly.sternbach.custommishnahlearningprogram.data.CompletedUnit
import shmuly.sternbach.custommishnahlearningprogram.data.Program
import shmuly.sternbach.custommishnahlearningprogram.data.SkippedUnit
import shmuly.sternbach.custommishnahlearningprogram.data.TODOUnit
import shmuly.sternbach.custommishnahlearningprogram.database.ObjectBox
import timber.log.Timber
import java.io.File
import java.nio.file.Files
import java.time.LocalDate

//lateinit var reviewProgram: LearningProgramMaker
lateinit var mishnayos: Mishnayos
lateinit var programFile: File
lateinit var skippedUnitsFile: File
lateinit var completedUnitsFile: File
lateinit var setOfSkipped: MutableSet<SkippedUnit>
lateinit var setOfCompleted: MutableSet<CompletedUnit>
lateinit var setOfTODO: MutableSet<TODOUnit>
class ReviewApplication: Application() {
    val completedBox by lazy { ObjectBox.store.boxFor(CompletedUnit::class) }
    val skippedBox by lazy { ObjectBox.store.boxFor(SkippedUnit::class) }
    val todoBox by lazy { ObjectBox.store.boxFor(TODOUnit::class) }
    val programBox by lazy { ObjectBox.store.boxFor(Program::class) }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        ObjectBox.init(this)
        mishnayos = Mishnayos()
        setOfCompleted = completedBox.all.toMutableSet()
        setOfSkipped = skippedBox.all.toMutableSet()
        setOfTODO = todoBox.all.toMutableSet()
        if(!programBox.isEmpty) programBox.all[0].program?.let {
            program = it
            programInitialized = true
        }
//        programFile.writeText(program.first.joinToString("~", postfix = "\n") { "${it.first}|${it.second}" })
//        programFile.appendText(program.second.joinToString("~") { "${it.first}|${it.second}" })
//        Pair<MutableList<Pair<String, LocalDate>>, MutableList<Pair<String, LocalDate>>>
    }
}