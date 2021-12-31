package shmuly.sternbach.custommishnahlearningprogram.data

data class ProgramUnitMolecule(
    val material: Map<String, Int> //e.g. "Brachos 1:1" to CompletionStatus.COMPLETED
) {
    var _mutableMaterial: MutableMap<String, Int>? = null
    val mutableMaterial: MutableMap<String, Int>
        get() = _mutableMaterial ?: material.toMutableMap().also { _mutableMaterial = it }
}