package cis470.matos.filewriteread;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
// read-write an 'internal' resource text file 

public class File2WriteRead extends Activity {

	private final static String FILE_NAME = "notes.txt";
	private EditText txtMsg;
	Context CONTEXT;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_main);
		CONTEXT = getApplicationContext();
		
		txtMsg = (EditText) findViewById(R.id.txtMsg);
		
		//deleteFile();  //keep for debugging
		
		Button btnClose = (Button) findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}// onCreate
	
	// ////////////////////////////////////////////////////////////////// Successfully
	
	private void deleteFile() {
		String path = "/data/data/cis470.matos.filewriteread/files/"
				+ FILE_NAME;

		File internalFile = new File(path);
		Toast.makeText(CONTEXT, "File already exists: " + internalFile.exists(), 1)
				.show();

		boolean success = internalFile.delete();

		if (!success) {
			Toast.makeText(CONTEXT, "Delete op. failed.", 1).show();

		} else {
			Toast.makeText(CONTEXT, "File successfully deleted.", 1).show();

		}
	}

	// /////////////////////////////////////////////////////////////////
	public void onStart() {
		super.onStart();

		try {
			InputStream inputStream = openFileInput(FILE_NAME);

			if (inputStream != null) {
				
				BufferedReader reader = new BufferedReader(new 
						                    InputStreamReader(inputStream));

				String str = "";
				StringBuffer stringBuffer = new StringBuffer();

				while ((str = reader.readLine()) != null) {
					stringBuffer.append(str + "\n");
				}

				inputStream.close();
				txtMsg.setText(stringBuffer.toString());
			}
		} 
		catch ( Exception ex ) {
			Toast.makeText(CONTEXT, ex.getMessage() , 1).show();
		
		}
	}// onStart

	// /////////////////////////////////////////////////////////////////
	public void onPause() {
		super.onPause();

		try {
			OutputStreamWriter out = new OutputStreamWriter(
					                     openFileOutput(FILE_NAME, 0));

			out.write(txtMsg.getText().toString());
			out.close();
		} catch (Throwable t) {
			Toast.makeText(CONTEXT, t.getMessage() , 1).show();
		}
	}// onPause
}
