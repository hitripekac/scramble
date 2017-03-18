package com.hitripekac.scramble;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public static String[] encodingMap;
    public static String[] decodingMap;

    static {
        encodingMap = new String[10];
        encodingMap[0] = "0357198624"; // A1
        encodingMap[1] = "0789123456"; // B2
        encodingMap[2] = "0987645321"; // C3
        encodingMap[3] = "0465798321"; // D4
        encodingMap[4] = "0852962147"; // E5    // fali 3?
        encodingMap[5] = "0864897213"; // A6
        encodingMap[6] = "0258147397"; // B7    // fali 6?
        encodingMap[7] = "0645978312"; // C8
        encodingMap[8] = "3078945612"; // D9
        encodingMap[9] = "0145678923"; // E10
        decodingMap = new String[10];
        decodingMap[0] = "0357198624"; // A1
        decodingMap[1] = "0456789123"; // B2
        decodingMap[2] = "0987564321"; // C3
        decodingMap[3] = "0987192465"; // D4    // fali 3
        decodingMap[4] = "7368259140"; // E5
        decodingMap[5] = "0879312645"; // A6
        decodingMap[6] = "041752 338"; // B7    // ???
        decodingMap[7] = "0897231564"; // C8
        decodingMap[8] = "3890567234"; // D9    // ???
        decodingMap[9] = "0189234567"; // E10
    }

    private TextView coordinateInput;
    private TextView resultText;
    private Spinner cipherSelect;
    private Button showButton;
    private Button copyButton;
    private Button decodeButton;
    private Button encodeButton;


    private static String encodeCoordinate(String coordinate, Integer cipher) {
        return transform(coordinate, encodingMap[cipher]);
    }

    private static String decodeCoordinate(String coordinate, int cipher) {
        return transform(coordinate, decodingMap[cipher]);
    }

    private static String transform(String coordinates, String cipher) {
        StringBuilder result = new StringBuilder();
        for (char single : coordinates.toCharArray()) {
            int position = single - '0';
            if ((0 <= position) && (position < 10)) {
                result.append(cipher.charAt(position));
            } else {
                result.append(single);
            }
        }
        return result.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinateInput = (TextView) findViewById(R.id.coordinateInput);
        resultText = (TextView) findViewById(R.id.resultText);
        cipherSelect = (Spinner) findViewById(R.id.cipherSelect);
        encodeButton = (Button) findViewById(R.id.encodeButton);
        decodeButton = (Button) findViewById(R.id.decodeButton);
        copyButton = (Button) findViewById(R.id.copyButton);
        showButton = (Button) findViewById(R.id.showButton);

        coordinateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enableButtons = !s.toString().isEmpty();
                encodeButton.setEnabled(enableButtons);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        resultText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enableButtons = !s.toString().isEmpty();
                copyButton.setEnabled(enableButtons);
                showButton.setEnabled(enableButtons);
                decodeButton.setEnabled(enableButtons);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        coordinateInput.setText("");
        resultText.setText("");


    }

    public void showOnMap(View view) {
        String text = resultText.getText().toString().replace(" ", ",");
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + text);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    public void resultClickHandle(View v) {
        Context context = getApplicationContext();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("coordinates", resultText.getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, R.string.result_copied, Toast.LENGTH_SHORT).show();
    }

    public void transferHandle(View button) {
        String text = coordinateInput.getText().toString();
        coordinateInput.setText(resultText.getText());
        resultText.setText(text);
    }

    public void clickHandle(View button) {
        String text = coordinateInput.getText().toString();
        int cipher = cipherSelect.getSelectedItemPosition();
        if (text.isEmpty()) {
            return;
        }
        switch (button.getId()) {
            case R.id.encodeButton:
                resultText.setText(encodeCoordinate(text, cipher));
                break;
            case R.id.decodeButton:
                resultText.setText(decodeCoordinate(text, cipher));
                break;
        }
    }
}
