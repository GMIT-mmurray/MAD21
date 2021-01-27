//package cis470.matos.filesdcard;
//
//import android.os.Bundle;
//import android.app.Activity;
//import android.view.Menu;
//
//public class File3SdCard extends Activity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.file3_sd_card, menu);
//		return true;
//	}
//
//}

package cis470.matos.filesdcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class File3SdCard extends Activity {
	// GUI controls
	private EditText txtData;
	private Button btnWriteSDFile;
	private Button btnReadSDFile;
	private Button btnClearScreen;
	private Button btnClose;
	private String mySdPath;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// find SD card absolute location
		mySdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		
		// bind GUI elements to local controls
		txtData = (EditText) findViewById(R.id.txtData);
		
		btnWriteSDFile = (Button) findViewById(R.id.btnWriteSDFile);
		btnWriteSDFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// WRITE on SD card file data taken from the text box
				try {
					File myFile = new File(mySdPath + "/mysdfile.txt");
								
					OutputStreamWriter myOutWriter = new OutputStreamWriter(
							                         new FileOutputStream(myFile));
					
					myOutWriter.append(txtData.getText());
					myOutWriter.close();

					Toast.makeText(getBaseContext(),
							"Done writing SD 'mysdfile.txt'",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getBaseContext(), e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}// onClick
		}); // btnWriteSDFile

		btnReadSDFile = (Button) findViewById(R.id.btnReadSDFile);
		btnReadSDFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// READ data from SD card show it in the text box
				try {
					
					BufferedReader myReader = new BufferedReader(
												new InputStreamReader(
												new FileInputStream(
												new File(mySdPath + "/mysdfile.txt"))));
							
					String aDataRow = "";
					String aBuffer = "";
					while ((aDataRow = myReader.readLine()) != null) {
						aBuffer += aDataRow + "\n";
					}
					txtData.setText(aBuffer);
					myReader.close();
					Toast.makeText(getApplicationContext(),
							"Done reading SD 'mysdfile.txt'",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}// onClick
		}); // btnReadSDFile

		btnClearScreen = (Button) findViewById(R.id.btnClearScreen);
		btnClearScreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// clear text box
				txtData.setText("");
			}
		}); // btnClearScreen

		btnClose = (Button) findViewById(R.id.btnFinish);
		btnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// terminate app
				Toast.makeText(getApplicationContext(),
						"Adios...",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}); // btnClose

	}// onCreate

}// File3SdCard

