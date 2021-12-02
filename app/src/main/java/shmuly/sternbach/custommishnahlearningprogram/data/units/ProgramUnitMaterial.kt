package shmuly.sternbach.custommishnahlearningprogram.data.units

data class ProgramUnitMaterial<T>(
    val listOfMaterials: List<T>
) {
    override fun toString(): String {
        return if(listOfMaterials.isEmpty()) "" else listOfMaterials.first().toString() + " - " + listOfMaterials.last().toString()
    }

    override fun hashCode(): Int {
        return listOfMaterials.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProgramUnitMaterial<*>

        if (listOfMaterials != other.listOfMaterials) return false

        return true
    }
}
