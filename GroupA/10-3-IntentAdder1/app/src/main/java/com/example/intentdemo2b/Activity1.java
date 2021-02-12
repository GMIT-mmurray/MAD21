package com.example.intentdemo2b;
// Multi-Activity Application
// Activity1: collects two data items from the user's UI, places
//			  them into a Bundle, and calls Activity2
// Activity2: accepts two data items, adds them, returns result

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity1 extends Activity {
    EditText txtValue1;
    EditText txtValue2;
    TextView txtResult;
    Button   btnAdd;    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);
        txtValue1 = (EditText)findViewById(R.id.EditText01);
        txtValue2 = (EditText)findViewById(R.id.EditText02);
        txtResult = (TextView) findViewById(R.id.txtResult);
        
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// get values from the UI
				Double v1 = Double.parseDouble(txtValue1.getText().toString());
				Double v2 = Double.parseDouble(txtValue2.getText().toString());
				
				// create intent to call Activity2
				Intent myIntentA1A2 = new Intent (Activity1.this,Activity2.class);
				// create a Bundle (MAP) container to ship data
				Bundle myDataBundle = new Bundle();
				
				// add <key,value> data items to the container
				myDataBundle.putDouble("val1", v1);
				myDataBundle.putDouble("val2", v2);
				
				// attach the container to the intent
				myIntentA1A2.putExtras(myDataBundle);
				
				// call Activity2, tell your local listener to wait a 
				// response sent to a listener known as 101
				startActivityForResult(myIntentA1A2, 101);
				
			}
				
		});
    }//onCreate

    //////////////////////////////////////////////////////////////////////////////
    // local listener receives callbacks from other activities
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		try	{
			if ((requestCode == 101 ) && (resultCode == Activity.RESULT_OK)){
				Bundle myResultBundle = data.getExtras();
				Double myResult = myResultBundle.getDouble("vresult");
				txtResult.setText("Sum is " + myResult);
			}
		}
		catch (Exception e) {
			txtResult.setText("Problems - " + requestCode + " " + resultCode);
		}
	}//onActivityResult
    
}//Activity1