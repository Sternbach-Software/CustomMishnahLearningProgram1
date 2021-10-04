package shmuly.sternbach.custommishnahlearningprogram

data class Mishnah(
    var masechtaString: String = "",
    var masechtaIndex: Int = -1,
    var perekString: String = "",
    var perekIndex: Int = -1,
    var mishnah: Int = -1
) {
    override fun toString(): String {
        return "$masechtaString $perekString:$mishnah"
    }
}
