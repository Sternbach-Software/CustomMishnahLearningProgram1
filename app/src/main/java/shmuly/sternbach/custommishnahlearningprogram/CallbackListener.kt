package shmuly.sternbach.custommishnahlearningprogram

interface CallbackListener {
    fun onMishnahPicked(
        adapterPosition: Int,
        startSelected: Boolean,
        masechtaString: String,
        masechtaIndex: Int,
        perekString: String,
        perekIndex: Int,
        mishnah: Int
    )
}