//Main class, service, and broadcast receiver defined in separated classes
package cis493.demos;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MyServiceDriver2 extends Activity {
    TextView txtMsg;
    Button btnStopService;
    ComponentName service;
    Intent intentMyService2;
    MyBroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        txtMsg = (TextView) findViewById(R.id.txtMsg);                

        IntentFilter mainFilter = new IntentFilter("matos.action.GOSERVICE2");
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver,mainFilter);

        intentMyService2 = new Intent(this, MyService2.class);      
        
        service = startService(intentMyService2);   
                
        txtMsg.setText("MyService2 started - (see LogCat)");
        
        btnStopService = (Button) findViewById(R.id.btnStopService);
        btnStopService.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					stopService(intentMyService2);
					txtMsg.setText("After stoping Service: \n" + 
							        service.getClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
        	
        });
   
    }//onCreate

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            stopService(intentMyService2);
            unregisterReceiver(receiver);
        } catch (Exception e) {

            Log.e ("MAIN2-DESTROY>>>", e.getMessage() );
        }
        Log.e ("MAIN2-DESTROY>>>" , "Adios" );
    }



}