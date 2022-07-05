package shmuly.sternbach.custommishnahlearningprogram

import android.app.Application
import android.content.SharedPreferences
import shmuly.sternbach.custommishnahlearningprogram.database.WordRepository
import com.example.android.roomwordssample.WordRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import shmuly.sternbach.custommishnahlearningprogram.activities.programInitialized
import shmuly.sternbach.custommishnahlearningprogram.logic.Mishnayos
import timber.log.Timber

lateinit var mishnayos: Mishnayos
lateinit var sharedPreferences: SharedPreferences
lateinit var mcoroutineScope: CoroutineScope
class ReviewApplication: Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { WordRoomDatabase.getDatabase(this/*, applicationScope*/) }
    val repository by lazy { WordRepository(database.wordDao()) }

    override fun onCreate() {
        super.onCreate()
        mcoroutineScope = CoroutineScope(SupervisorJob())
        Timber.plant(Timber.DebugTree())
        mishnayos = Mishnayos()
        sharedPreferences = getSharedPreferences("custom_mishnayos_program", MODE_PRIVATE)
        programInitialized = sharedPreferences.contains("program_created")
//        if(!programBox.isEmpty) programBox.all[0].program?.let {
//            program = it
//            programInitialized = true
//        }
    }
}