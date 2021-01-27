package csu.matos.asyntaskdemo;

import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	Button btnSlowWork;
	Button btnQuickWork;
	EditText txtMsg;
	Long startingMillis;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtMsg = (EditText) findViewById(R.id.txtMsg);

		// slow work...for example: delete all data from a database
		btnSlowWork = (Button) findViewById(R.id.btnSlow);
		this.btnSlowWork.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				new VerySlowTask().execute("dummy1", "dummy2", "Dummy3");
			}
		});

		btnQuickWork = (Button) findViewById(R.id.btnQuick);
		// delete all data from database (when delete button is clicked)
		this.btnQuickWork.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				txtMsg.setText((new Date()).toString());
			}
		});
	}// onCreate

	private class VerySlowTask extends AsyncTask<String, Long, Void> {
		String waitMsg = "Wait\nSome SLOW job is being done... ";
		private final ProgressDialog dialog = new ProgressDialog(
				MainActivity.this);

		// can use UI thread here
		protected void onPreExecute() {
			startingMillis = System.currentTimeMillis();
			txtMsg.setText("Start Time: " + startingMillis);
			this.dialog.setMessage(waitMsg);
			this.dialog.setCancelable(false); //don't dismiss on outside touches 
			this.dialog.show();
		}

		// automatically done on worker thread (separate from UI thread)
		protected Void doInBackground(final String... args) {
			// show on Log.e the supplied dummy arguments
			Log.e("doInBackground>>", "Total args: " + args.length );
			 for (String s : args) {
				 Log.e("doInBackground>>", "args = " + s );
			 }
			try {
				// simulate here the slow activity
				for (Long i = 0L; i < 5L; i++) {
					Thread.sleep(10000);
					publishProgress((Long) i);
				}
			} catch (InterruptedException e) {
				Log.e("slow-job interrupted", e.getMessage());
			}
			return null;
		}

		// periodic updates - it is OK to change UI
		@Override
		protected void onProgressUpdate(Long... value) {
			super.onProgressUpdate(value);
			dialog.setMessage(waitMsg + value[0]);
			txtMsg.append("\nworking..." + value[0]);
		}

		// can use UI thread here
		protected void onPostExecute(final Void unused) {

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			// cleaning-up, all done
			txtMsg.append("\nEnd Time:"
					+ (System.currentTimeMillis() - startingMillis) / 1000);
			txtMsg.append("\ndone!");
		}

	}// AsyncTask

}// MainActivity
