package shmuly.sternbach.custommishnahlearningprogram

import android.app.Application
//lateinit var reviewProgram: LearningProgramMaker
lateinit var mishnayos: Mishnayos
class ReviewApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        mishnayos = Mishnayos()
    }
}