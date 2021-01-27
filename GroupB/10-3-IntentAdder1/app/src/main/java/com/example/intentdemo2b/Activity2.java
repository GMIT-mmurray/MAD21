package com.example.intentdemo2b;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Activity2 extends Activity implements OnClickListener{
	EditText dataReceived;
	Button  btnDone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
		dataReceived = (EditText) findViewById(R.id.etDataReceived);
		btnDone = (Button) findViewById(R.id.btnDone);
		btnDone.setOnClickListener(this);
		
		// pick call made to Activity2 via Intent
		Intent myLocalIntent = getIntent();
		
		// look into the bundle sent to Activity2 for data items
		Bundle myBundle =  myLocalIntent.getExtras();
		Double v1 = myBundle.getDouble("val1");
		Double v2 = myBundle.getDouble("val2");
		
		// operate on the input data
		Double vResult =  v1 + v2;
		
		// for illustration purposes. show data received & result
		dataReceived.setText("Data received is \n"
				+ "val1= " + v1 + "\nval2= " + v2 
				+ "\n\nresult= " + vResult);		
		
		// add to the bundle the computed result  
		myBundle.putDouble("vresult", vResult);
		
		// attach updated bumble to invoking intent
		myLocalIntent.putExtras(myBundle);
		
		// return sending an OK signal to calling activity
		setResult(Activity.RESULT_OK, myLocalIntent);
		
		// experiment: remove comment
		 finish();
	
	}//onCreate

	@Override
	public void onClick(View v) {
		    // close current screen - terminate Activity2
			finish();		
	}
	
}
