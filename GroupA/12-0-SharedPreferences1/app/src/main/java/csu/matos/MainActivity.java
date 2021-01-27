package csu.matos;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.util.Date;

import csu.matos.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
// allow user to make choices about GUI and save selection in a 
// 'shared preferences' file.
public class MainActivity extends Activity implements OnClickListener {
	
	//GUI controls
	Button btnSimpleOption;
	Button btnFancyOption;
	TextView txtMsg;
	
	final int MY_PREFS_PRIV_MODE = Activity.MODE_PRIVATE;
	final String MY_PREFS_FILE = "my_gui_preferences";
	
	// create a reference to the shared preferences object
	SharedPreferences mySharedPreferences;
	// obtain an editor to add data to my SharedPreferences object
	SharedPreferences.Editor myEditor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	

		txtMsg = (TextView) findViewById(R.id.txtMsg);
		txtMsg.setText("This is a sample line \n"
				+ "suggesting the way the UI looks \n"
				+ "after you choose your preference");

		// create a reference to the SharedPreferences file 
		mySharedPreferences = getSharedPreferences(MY_PREFS_FILE, MY_PREFS_PRIV_MODE);
		// obtain an editor to add data to (my)SharedPreferences object
		myEditor = mySharedPreferences.edit();
		// has a Preferences file been already created?
		if (mySharedPreferences != null ) {
			applySavedPreferences();
		} else {
			Toast.makeText(getApplicationContext(), 
					"No Preferences found", Toast.LENGTH_SHORT).show();
		}

		btnSimpleOption = (Button) findViewById(R.id.btnPrefSimple);
		btnSimpleOption.setOnClickListener(this);

		btnFancyOption = (Button) findViewById(R.id.btnPrefFancy);
		btnFancyOption.setOnClickListener(this);

//		usingPreferences();
		
	}// onCreate

	@Override
	public void onClick(View v) {
		// clear all previous selections
		myEditor.clear();

		// what button has been clicked?
		if (v.getId() == btnSimpleOption.getId()) {
			myEditor.putInt("chosenBackgColor", Color.BLACK);	// black backgnd
			myEditor.putInt("chosenTextSize", 12); 		  		// small font
			myEditor.putInt("chosenTextColor", Color.WHITE);	// white text
		} else { // case btnFancyPref
			myEditor.putInt("chosenTextColor", Color.BLUE); 	// fancy yellow txt
			myEditor.putInt("chosenTextSize", 20); 		  		// fancy big
			myEditor.putString("chosenTextStyle", "BOLD");  	// fancy bold
			myEditor.putInt("chosenBackColor", Color.GREEN);	// fancy green
		}
		myEditor.commit();
		applySavedPreferences();
	}

	@Override
	protected void onPause() {
		// warning: activity is on its last state of visibility!
		// It's on the edge of been killed! Better save all current 
		// state data into Preference object (be quick!)
		myEditor.putString("DateLastExecution", new Date().toString());
		myEditor.commit();
		super.onPause();
	}

	public void applySavedPreferences() {
		// the following setup applies to the (txtMsg) TextView control
		// get <key/value> pairs, use default params for missing data
		int chosenBackColor = mySharedPreferences.getInt("chosenBackColor",Color.BLACK);
		int chosenTextSize = mySharedPreferences.getInt("chosenTextSize", 12);
		String chosenTextStyle = mySharedPreferences.getString("chosenTextStyle", "NORMAL");
		int chosenTextColor = mySharedPreferences.getInt("chosenTextColor", Color.DKGRAY);
		
		String msg = "color " + chosenBackColor + "\n" + "size " + chosenTextSize
				+ "\n" + "style " + chosenTextStyle;
		Toast.makeText(getApplicationContext(), msg, 1).show();
		
		txtMsg.setBackgroundColor(chosenBackColor);
		txtMsg.setTextSize(chosenTextSize);
		txtMsg.setTextColor(chosenTextColor);
		if (chosenTextStyle.compareTo("NORMAL")==0){
			txtMsg.setTypeface(Typeface.SERIF,Typeface.NORMAL);
		}
		else {
			txtMsg.setTypeface(Typeface.SERIF,Typeface.BOLD);
		}
		
	}// applySavedPreferences
	// ---------------------------------------------------------------------------
	 private void usingPreferences(){
		   // Save data in a SharedPreferences container
		   // We need an Editor object to make preference changes.

		   SharedPreferences settings = getSharedPreferences("my_preferred_choices", 
				                                             Activity.MODE_PRIVATE );
		   SharedPreferences.Editor editor = settings.edit();
		           editor.putString("favorite_color", "#ff0000ff");
		           editor.putInt("favorite_number", 101);
		   editor.commit();
		        
		   // retrieving data from SharedPreferences container
		   String favColor = settings.getString("favorite_color", "default black");
		   int favNumber = settings.getInt("favorite_number", 0);

		   Toast.makeText(this, favColor + " " + favNumber, 1).show();
		        
		 }

	

}