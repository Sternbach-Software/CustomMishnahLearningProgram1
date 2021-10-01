package shmuly.sternbach.custommishnahlearningprogram

import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

private const val TAG = "MainActivity"
class MainActivity: AppCompatActivity(), CallbackListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)
        val mishnahPickerDialog = MishnahPickerDialog(this)

        findViewById<AutoCompleteTextView>(R.id.start_autocomplete_text_view).setOnClickListener{
            mishnahPickerDialog.show(supportFragmentManager,"")
        }
        findViewById<AutoCompleteTextView>(R.id.end_autocomplete_text_view).setOnClickListener{
            mishnahPickerDialog.show(supportFragmentManager,"")
        }
        findViewById<TextInputLayout>(R.id.start_text_input_layout).setEndIconOnClickListener{
            mishnahPickerDialog.show(supportFragmentManager,"")
        }
        findViewById<TextInputLayout>(R.id.end_text_input_layout).setEndIconOnClickListener{
            mishnahPickerDialog.show(supportFragmentManager,"")
        }
    }

    override fun onMishnahPicked(seder: String, masechta: String, perek: String, mishnah: String) {
        Log.d("TorahDownloads","Mishnah picked: $seder,$masechta,$perek,$mishnah")
        findViewById<TextView>(R.id.calendar_result).text = getCalendar().toString()
    }
}