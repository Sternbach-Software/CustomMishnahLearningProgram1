package shmuly.sternbach.custommishnahlearningprogram

import android.app.Application
import io.objectbox.kotlin.boxFor
import shmuly.sternbach.custommishnahlearningprogram.activities.program
import shmuly.sternbach.custommishnahlearningprogram.activities.programInitialized
import shmuly.sternbach.custommishnahlearningprogram.data.units.CompletedUnit
import shmuly.sternbach.custommishnahlearningprogram.data.Program
import shmuly.sternbach.custommishnahlearningprogram.data.molecules.CompletedMolecule
import shmuly.sternbach.custommishnahlearningprogram.data.molecules.SkippedMolecule
import shmuly.sternbach.custommishnahlearningprogram.data.molecules.TODOMolecule
import shmuly.sternbach.custommishnahlearningprogram.data.units.SkippedUnit
import shmuly.sternbach.custommishnahlearningprogram.data.units.TODOUnit
import shmuly.sternbach.custommishnahlearningprogram.database.ObjectBox
import timber.log.Timber

//lateinit var reviewProgram: LearningProgramMaker
lateinit var mishnayos: Mishnayos
val completedUnitsBox by lazy { ObjectBox.store.boxFor(CompletedUnit::class) }
val skippedUnitsBox by lazy { ObjectBox.store.boxFor(SkippedUnit::class) }
val todoUnitsBox by lazy { ObjectBox.store.boxFor(TODOUnit::class) }
val completedMoleculesBox by lazy { ObjectBox.store.boxFor(CompletedMolecule::class) }
val skippedMoleculesBox by lazy { ObjectBox.store.boxFor(SkippedMolecule::class) }
val todoMoleculesBox by lazy { ObjectBox.store.boxFor(TODOMolecule::class) }
val programBox by lazy { ObjectBox.store.boxFor(Program::class) }

class ReviewApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        ObjectBox.init(this)
        mishnayos = Mishnayos()
        if(!programBox.isEmpty) programBox.all[0].program?.let {
            program = it
            programInitialized = true
        }
//        programFile.writeText(program.first.joinToString("~", postfix = "\n") { "${it.first}|${it.second}" })
//        programFile.appendText(program.second.joinToString("~") { "${it.first}|${it.second}" })
//        Pair<MutableList<Pair<String, LocalDate>>, MutableList<Pair<String, LocalDate>>>
    }
}