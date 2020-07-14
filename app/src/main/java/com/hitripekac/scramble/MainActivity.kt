package com.hitripekac.scramble

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val coder = Coder()
    private var clipboard: ClipboardManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clipboard = getSystemService(ClipboardManager::class.java)

        decodedCoordinates.addTextChangedListener(
                editWatcher(decodedCoordinates, encodedCoordinates) { input ->
                    coder.encodeCoordinate(input, cipher)
                })
        decodedCoordinatesContainer.setEndIconOnClickListener(copyListener({ decodedCoordinates.text.toString() }, R.string.decoded_coordinates_copied))

        encodedCoordinates.addTextChangedListener(
                editWatcher(encodedCoordinates, decodedCoordinates) { input ->
                    coder.decodeCoordinate(input, cipher)
                })
        encodedCoordinatesContainer.setEndIconOnClickListener(copyListener({ encodedCoordinates.text.toString() }, R.string.encoded_coordinates_copied))

        cipherSelect.onItemSelectedListener = selectionListener {
            encodedCoordinates.setText(coder.encodeCoordinate(decodedCoordinates.text.toString(), cipher))
        }
    }

    private fun selectionListener(function: (position: Int?) -> Unit): AdapterView.OnItemSelectedListener? {
        return object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) = function(null)

            override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long) = function(position)

        }
    }

    private fun displayToast(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    private fun editWatcher(from: AppCompatEditText, to: AppCompatEditText, transform: (String) -> String): ChangeWatcher {
        return ChangeWatcher {
            if (from.isFocused) to.setText(transform(from.text.toString()))
        }
    }

    private fun copyListener(input: () -> String, resId: Int): (View) -> Unit {
        return {
            val data = ClipData.newPlainText("coordinates", input())
            clipboard?.setPrimaryClip(data)
            displayToast(resId)
        }
    }

    private val cipher get() = cipherSelect.selectedItemPosition

}
