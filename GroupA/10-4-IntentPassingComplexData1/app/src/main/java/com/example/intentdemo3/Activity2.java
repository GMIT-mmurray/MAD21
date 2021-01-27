// Activity2. This sub-activity receives a bundle of data,
// data types include: primitive, arrays, complex objects.
// Activity2 performs some work on the data and, returns 
// results to Activity1.
package com.example.intentdemo3;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Activity2 extends Activity {
    TextView txtIncomingData;
    TextView spyBox;
    Button   btnCallActivity1;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        //bind UI variables to Java code
        txtIncomingData = (TextView)findViewById(R.id.txtIncomingData);
        spyBox = (TextView)findViewById(R.id.spyBox);
        
        btnCallActivity1 = (Button)findViewById(R.id.btnCallActivity1);
        btnCallActivity1.setOnClickListener(new Clicker1());
        
        // /////////////////////////////////////////////////////////////
        // create a local Intent handler – we have been called!
        Intent myCallerIntentHandler = getIntent();

        // grab the data package with all the pieces sent to us
        Bundle myBundle = myCallerIntentHandler.getExtras();

        // extract the individual data parts from the bundle 
        // observe you know the individual keyNames
        int    paramInt = myBundle.getInt("myRequestCode");
        String paramString = myBundle.getString("myString1");
        double paramDouble = myBundle.getDouble("myDouble1");
        int[]  paramArray = myBundle.getIntArray("myIntArray1");
        Person paramPerson = (Person) myBundle.getSerializable("person");
        String personName = paramPerson.getFullName();
        
        //for debugging purposes - show arriving data 
        txtIncomingData.append("\n------------------------------ "
        		+ "\n Caller's ID: " + paramInt
        		+ "\n myString1:   " + paramString 
        		+ "\n myDouble1:   " + Double.toString(paramDouble)
        		+ "\n myIntArray1: " + Activity1.myConvertArray2String(paramArray)
        		+ "\n Person obj:  " + paramPerson.getFullName()
        	    );
        
     // next method assumes you do not know the data-items keyNames
        String spyData = extractDataFromBundle( myBundle );
        spyBox.append(spyData);
        
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++               
        // do here something with the extracted data. For example,  
        // reverse the values stored in the incoming integer array
      
        //int[] intReversedArray = myIntReverseArray( paramArray );
        int[] intReversedArray = myIntReverseArray( paramArray );
        String strReversedArray = Activity1.myConvertArray2String(intReversedArray);     
        myBundle.putIntArray("myReversedArray", intReversedArray);
        
        // change the person's firstName 
        paramPerson.setFirstName("Carmen" );
        myBundle.putSerializable("person", paramPerson);
        
        // Returning Results.
        // Go back to myActivity1 with some new data made/change here.    
        myBundle.putString("myReturnedString1", "Adios Android");
        myBundle.putDouble("myReturnedDouble1", Math.PI);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        String now = formatter.format(new Date());
        myBundle.putString("myCurrentDate", now );
        
        myCallerIntentHandler.putExtras(myBundle);
        
        // just debugging - show returning data 
        txtIncomingData.append("\n\n Data to be returned  "
        	+ "\n requestCode:       " + paramInt 
        	+ "\n myReturnedString1: " + myBundle.getString("myReturnedString1") 
        	+ "\n myReturnedDouble1: " + myBundle.getDouble("myReturnedDouble1")
        	+ "\n myCurrentDate:     " + myBundle.getString("myCurrentDate")
        	+ "\n reversedArray:     " + strReversedArray
        	+ "\n reversedArray:     " + myBundle.getIntArray("myReversedArray")
        	+ "\n person:            " + ((Person) myBundle
        										 .getSerializable("person"))
        										 .getFullName()  );
        
        
        // all done! 
        setResult(Activity.RESULT_OK, myCallerIntentHandler);
        
    }//onCreate
    
	// ///////////////////////////////////////////////////////////////////
	private class Clicker1 implements OnClickListener {
		public void onClick(View v) {
			//clear Activity2 screen so Activity1 could be seen
			finish();			
		}//onClick    	
    }//Clicker1

    
    
    
    // ///////////////////////////////////////////////////////////////////

	private int[] myIntReverseArray( int[] theArray ) {
		int n = theArray.length;
		int[] reversedArray =  new int[n];
        for (int i=0; i< theArray.length; i++ ) {
        	reversedArray[i] = theArray[n -i -1];
        }
        return reversedArray;
	}
    
    // //////////////////////////////////////////////////////////////////////
    private String extractDataFromBundle(Bundle myBundle) {
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // What if I don't know the key names?
        // what types are in the bundle?. This fragment shows 
        // how to use bundle methods to extract its data. 
        // SOME ANDROID TYPES INCLUDE: 
        // class [I (array integers)
        // class [J (array long)
        // class [D (array doubles)
        // class [F (array floats)
        // class java.lang.xxx   (where xxx= Integer, Double, ...)
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // Remember, the Bundle is a set of <keyName, keyValue> pairs
        String spy = "\nSPY>>\n";
        
        Set<String> myKeyNames = myBundle.keySet();  //get all keyNames 
        for (String keyName : myKeyNames){
     	   Serializable keyValue =  myBundle.getSerializable(keyName);
     	   String keyType = keyValue.getClass().toString();
     	   if (keyType.equals("class java.lang.Integer")){
     		   keyValue = Integer.parseInt(keyValue.toString()); 
     	   }
     	   else if (keyType.equals("class java.lang.Double")){
     		   keyValue = Double.parseDouble(keyValue.toString()); 
     	   }
     	   else if (keyType.equals("class java.lang.Float")){
     		   keyValue = Float.parseFloat(keyValue.toString()); 
     	   }
     	   else if (keyType.equals("class [I")){
     		   int[] arrint = myBundle.getIntArray(keyName);
     		   int n = arrint.length;
     		   keyValue = arrint[n-1]; // show only the last! 
     	   }    	   
     	   else {
     	   keyValue = (String)keyValue.toString();
     	   }
     	   spy +="\n\nkeyName..." + keyName 
     		   + "  \nKeyValue.." + keyValue
     		   + "  \nKeyType..." + keyType ;
        }
        return spy;
	}//extractDataFromBundle




}//Activity2
