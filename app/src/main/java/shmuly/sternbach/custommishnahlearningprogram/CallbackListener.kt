package shmuly.sternbach.custommishnahlearningprogram

interface CallbackListener {
    fun onMishnahPicked(seder: String, masechta: String, perek: String, mishnah: String)
}