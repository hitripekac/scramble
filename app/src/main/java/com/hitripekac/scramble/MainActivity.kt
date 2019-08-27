package com.hitripekac.scramble

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.logging.Logger

class MyTextWatcher(private val runner: (String) -> Unit) : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
        runner(sequence.toString())
    }
}


class MainActivity : AppCompatActivity() {
    private val logger = Logger.getLogger(javaClass.name)
    private val coder = Coder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        decodedCoordinates.addTextChangedListener(MyTextWatcher { coordinates ->
            logger.info("input: $coordinates")
            val encoded = coder.encodeCoordinate(coordinates, cipher)
            if (decodedCoordinates.isFocused) {
                encodedCoordinates.setText(encoded)
            }
        })

        encodedCoordinates.addTextChangedListener(MyTextWatcher { coordinates ->
            logger.info("input: $coordinates")
            val decoded = coder.decodeCoordinate(coordinates, cipher)
            if (encodedCoordinates.isFocused) {
                decodedCoordinates.setText(decoded)
            }
        })
    }

//    fun resultClickHandle(_view: View) {
//        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        val clip = ClipData.newPlainText("coordinates", resultText.text)
//        clipboard.setPrimaryClip(clip)
//        Toast.makeText(this, R.string.result_copied, Toast.LENGTH_SHORT).show()
//    }

    private val cipher get() = cipherSelect.selectedItemPosition

}
