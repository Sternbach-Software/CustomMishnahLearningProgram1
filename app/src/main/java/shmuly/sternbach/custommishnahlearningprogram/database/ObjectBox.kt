package shmuly.sternbach.custommishnahlearningprogram.database

import android.content.Context
import io.objectbox.BoxStore
import shmuly.sternbach.custommishnahlearningprogram.data.MyObjectBox

object ObjectBox {
    lateinit var store: BoxStore

    fun init(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }
}