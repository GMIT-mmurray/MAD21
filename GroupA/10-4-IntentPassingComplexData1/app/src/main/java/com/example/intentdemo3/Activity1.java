//Activity1: Invoking a user-defined sub-activity
//sending and receiving results from the sub-activity
package com.example.intentdemo3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Activity1 extends Activity {
	TextView txtTop;
	TextView txtReturnedValues;
	Button btnCallActivity2;
	// arbitrary interprocess communication ID (just a nickname!)
	private final int IPC_ID = (int) (10001 * Math.random());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.main1);
			txtTop = (TextView) findViewById(R.id.txtTop);
			txtReturnedValues = (TextView) findViewById(R.id.txtReturnedValues);
			btnCallActivity2 = (Button) findViewById(R.id.btnCallActivity2);
			btnCallActivity2.setOnClickListener(new Clicker1());
			// for demonstration purposes- show in top label
			txtTop.setText("Activity1   (sending...) "
					+ "\n  RequestCode:  " + IPC_ID   
					+ "\n  myString1:    Hello Android" 
					+ "\n  myDouble1:    3.14 " 
					+ "\n  myIntArray:   {1,2,3} "       
					+ "\n  Obj Person:   Maria Macarena");
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}// onCreate

	private class Clicker1 implements OnClickListener {
		public void onClick(View v) {
			try {
				// create an Intent to talk to Activity2
				Intent myIntentA1A2 = new Intent(Activity1.this,Activity2.class);

				// prepare a Bundle and add the data pieces to be sent
				Bundle myData = new Bundle();
				myData.putInt("myRequestCode", IPC_ID);
				myData.putString("myString1", "Hello Android");
				myData.putDouble("myDouble1", 3.141592);
				int [] myLittleArray = { 1, 2, 3 };
				myData.putIntArray("myIntArray1", myLittleArray);
				
				// creating an object and passing it into the bundle
				Person p1 = new Person("Maria", "Macarena");
				myData.putSerializable("person", p1);
				
				// bind the Bundle and the Intent that talks to Activity2
				myIntentA1A2.putExtras(myData);

				// call Activity2 and wait for results
				startActivityForResult(myIntentA1A2, IPC_ID);
				
			} catch (Exception e) {
				Toast.makeText(getBaseContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}// onClick
	}// Clicker1

	
	// ///////////////////////////////////////////////////////////////////////
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// check that these results are for me
			if (IPC_ID == requestCode) {
			
				// Activity2 is over - see what happened
				if (resultCode == Activity.RESULT_OK) {

					// good - we have some data sent back from Activity2
					Bundle myReturnedData = data.getExtras();
					String myReturnedString1 = myReturnedData
							.getString("myReturnedString1");
					Double myReturnedDouble1 = myReturnedData
							.getDouble("myReturnedDouble1");
					String myReturnedDate = myReturnedData
							.getString("myCurrentDate");
					Person myReturnedPerson = (Person) myReturnedData
							.getSerializable("person");
					
					int[]  myReturnedReversedArray = myReturnedData
												.getIntArray("myReversedArray");
					
					// display in the bottom label
					txtReturnedValues.setText(
						  "\n requestCode:     " + requestCode
						+ "\n resultCode:      " + resultCode  
						+ "\n returnedString1: " + myReturnedString1 
						+ "\n returnedDouble:  " + Double.toString(myReturnedDouble1) 
						+ "\n returnedString2: " + myReturnedDate
						+ "\n returnedPerson:  " + myReturnedPerson.getFullName() 
						+ "\n returnedArray:   " 
						+ Activity1.myConvertArray2String(myReturnedReversedArray));
				} else {
					// user pressed the BACK button
					txtTop.setText("Selection CANCELLED!");
				}
				
			}

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}// try
	}// onActivityResult
	
	
    // ///////////////////////////////////////////////////////////////////
	static String myConvertArray2String(int[] intArray ) {
		if ( intArray == null) 
			return "NULL";
		String array2Str =  "{" + Integer.toString( intArray[0] );
        for (int i=1; i<intArray.length;  i++) {
        	array2Str = array2Str + "," + Integer.toString( intArray[i] );
        }
        return array2Str + "}";
	}

}// AndroIntent1
