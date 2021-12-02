package shmuly.sternbach.custommishnahlearningprogram.data

data class ProgramUnit<T>(
    val listOfMaterials: List<T>
) {
    override fun toString(): String {
        return if(listOfMaterials.isEmpty()) "" else listOfMaterials.first().toString() + " - " + listOfMaterials.last().toString()
    }
}
