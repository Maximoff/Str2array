package ru.maximoff.str2array;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		final String[] charsets = getResources().getStringArray(R.array.charsets);
		final Spinner spinner = findViewById(R.id.mainSpinner1);
		final EditText string = findViewById(R.id.mainEditText1);
		final EditText array = findViewById(R.id.mainEditText2);
		Button convert = findViewById(R.id.mainButton1);
		Button reverse = findViewById(R.id.mainButton2);
		convert.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					try {
						String hexArray = bytes2hex(string.getText().toString().getBytes(charsets[spinner.getSelectedItemPosition()]));
						array.setText(hexArray);
					} catch (Exception e) {
						Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		reverse.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					try {
						byte[] bytesArray = hex2bytes(array.getText().toString().split("\n"));
						string.setText(new String(bytesArray, charsets[spinner.getSelectedItemPosition()]));
					} catch (Exception e) {
						Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			});
    }

	private String bytes2hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append("0x" + String.format("%x", b) + "\n");
		}
		return sb.toString();
	}

	private byte[] hex2bytes(String[] hex) {
		byte[] array = new byte[hex.length];
		for (int i = 0; i < hex.length; i++) {
			array[i] = (byte) Integer.parseInt(hex[i].trim().substring(2), 16);
		}
		return array;
	}
}
