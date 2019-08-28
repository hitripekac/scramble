package com.hitripekac.scramble

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.logging.Logger


class ChangeWatcher(private val runner: (String) -> Unit) : TextWatcher {
    override fun afterTextChanged(p0: Editable?) = Unit
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
    override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) = runner(sequence.toString())
}


class MainActivity : AppCompatActivity() {
    private val logger = Logger.getLogger(javaClass.name)
    private val coder = Coder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?

        decodedCoordinates.addTextChangedListener(ChangeWatcher { coordinates ->
            logger.info("input: $coordinates")
            val encoded = coder.encodeCoordinate(coordinates, cipher)
            if (decodedCoordinates.isFocused) {
                encodedCoordinates.setText(encoded)
            }
        })

        decodedCoordinatesContainer.setEndIconOnClickListener {
            val data = ClipData.newPlainText("decodedCoordinates", decodedCoordinates.text)
            myClipboard?.setPrimaryClip(data)
            Toast.makeText(this, getString(R.string.decoded_coordinates_copied), Toast.LENGTH_SHORT).show()

        }

        encodedCoordinates.addTextChangedListener(ChangeWatcher { coordinates ->
            logger.info("input: $coordinates")
            val decoded = coder.decodeCoordinate(coordinates, cipher)
            if (encodedCoordinates.isFocused) {
                decodedCoordinates.setText(decoded)
            }
        })

        encodedCoordinatesContainer.setEndIconOnClickListener {
            val data = ClipData.newPlainText("encodedCoordinates", encodedCoordinates.text)
            myClipboard?.setPrimaryClip(data)
            Toast.makeText(this, getString(R.string.encoded_coordinates_copied), Toast.LENGTH_SHORT).show()
        }
    }


    private val cipher get() = cipherSelect.selectedItemPosition

}
