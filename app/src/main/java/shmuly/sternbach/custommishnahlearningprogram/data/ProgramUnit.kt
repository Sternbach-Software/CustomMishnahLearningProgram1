package shmuly.sternbach.custommishnahlearningprogram.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "units_table")
data class ProgramUnit(
    @PrimaryKey
    @ColumnInfo(name = "material")
    val material: String,

    val date: LocalDate,
    val isReview: Boolean,
    val programID: Int,//TODO make this a string of the name of the program
    val group: Int/*if 20th unit in program, will be 20, so that reviews are sorted chronologically*/,
    val positionInGroup: Int,
    var completedStatus: Int
) {
    val isCompleted: Boolean
        get() = completedStatus == CompletionStatus.COMPLETED

    val isSkipped: Boolean
        get() = completedStatus == CompletionStatus.SKIPPED

    val isTODO: Boolean
        get() = completedStatus == CompletionStatus.TODO
}
