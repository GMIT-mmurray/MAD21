
package cis493.demos;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyServiceDriver3 extends Activity implements OnClickListener {
    TextView txtMsg;
    //Button btnStopService;
    ComponentName service;
    Intent intentMyService3;
    
    BroadcastReceiver receiver;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        txtMsg = (TextView) findViewById(R.id.txtMsg);                
        
        intentMyService3 = new Intent(this, MyService3.class);        
        service = startService(intentMyService3);   
                
        txtMsg.setText("MyService3 started - (see LogCat)");
        findViewById(R.id.btnStopService).setOnClickListener(this);
        

		// register & define filter for local listener
        IntentFilter mainFilter = new IntentFilter("matos.action.GOSERVICE3");
		receiver = new MyMainLocalReceiver();
		registerReceiver(receiver, mainFilter);
		
		
    }//onCreate
    
    // ////////////////////////////////////////////////////////////////////////
	public void onClick(View v) {
		// assume:  v.getId() == R.id.btnStopService
		try {

			stopService(intentMyService3);
			txtMsg.setText("After stoping Service: \n" + 
					        service.getClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}				
	}     

    // ////////////////////////////////////////////////////////////////////////
    @Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			stopService(intentMyService3);
			unregisterReceiver(receiver);
		} catch (Exception e) {
			
			Log.e ("MAIN3-DESTROY>>>", e.getMessage() );
		}
		Log.e ("MAIN3-DESTROY>>>" , "Adios" );
	}

    
	//////////////////////////////////////////////////////////////////////
	// local RECEIVER
	public class MyMainLocalReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context localContext, Intent callerIntent) {
			String serviceData = callerIntent.getStringExtra("service3Data");
					
			Log.e ("MAIN>>>", "Data received from Service3: " + serviceData);
			
			String now = "\nService3Data: > " + serviceData;
			txtMsg.append(now);
		}		
	}//MyMainLocalReceiver

}//MyServiceDriver4