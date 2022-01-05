package shmuly.sternbach.custommishnahlearningprogram.data

data class Mishnah(
    var masechtaString: String = "",
    var masechtaIndex: Int = -1,
    var perekString: String = "",
    var perekIndex: Int = -1,
    var mishnah: Int = -1
): ProgramType {
    override fun toString(): String {
        return if(masechtaString == "") "" else "$masechtaString $perekString:$mishnah"
    }
}
