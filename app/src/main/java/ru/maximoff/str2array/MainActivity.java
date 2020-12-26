package ru.maximoff.str2array;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import ru.maximoff.str2array.R;

public class MainActivity extends Activity {
	private EditText string;
	private EditText array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		string = findViewById(R.id.mainEditText1);
		array = findViewById(R.id.mainEditText2);
		final String[] charsets = getResources().getStringArray(R.array.charsets);
		final Spinner spinner = findViewById(R.id.mainSpinner1);
		final ImageView copyStr = findViewById(R.id.mainImageView1);
		final ImageView copyArr = findViewById(R.id.mainImageView2);
		final Button convert = findViewById(R.id.mainButton1);
		final Button reverse = findViewById(R.id.mainButton2);
		convert.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					try {
						String hexArray = bytes2hex(string.getText().toString().getBytes(charsets[spinner.getSelectedItemPosition()]));
						array.setText(hexArray);
					} catch (Exception e) {
						Toast.makeText(MainActivity.this, getString(R.string.error, e.getMessage()), Toast.LENGTH_SHORT).show();
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
						Toast.makeText(MainActivity.this, getString(R.string.error, e.getMessage()), Toast.LENGTH_SHORT).show();
					}
				}
			});
		copyStr.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					setClipboard(string.getText().toString());
				}
			});
		copyArr.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					setClipboard(array.getText().toString());
				}
			});
		showKeyboard();
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

	private void setClipboard(String text) {
		if (text.equals("")) {
			return;
		}
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("Copied Text", text);
		clipboard.setPrimaryClip(clip);
		Toast.makeText(this, getString(R.string.copied), Toast.LENGTH_SHORT).show();
	}
	
	private void showKeyboard() {
		new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					string.requestFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(string, InputMethodManager.SHOW_IMPLICIT);
				}
			}, 100);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.refresh:
				string.setText("");
				array.setText("");
				showKeyboard();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
