package com.hpritch5.give_me_ltc;

import com.google.cloud.backend.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends Activity {
	
	String st_apiKey = "";
	EditText et_apiKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		et_apiKey = (EditText)findViewById(R.id.et_apiKey);
		
		Button bt_scanQR = (Button)findViewById(R.id.btScanQR);
		bt_scanQR.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IntentIntegrator integrator = new IntentIntegrator(HomeActivity.this);
				integrator.initiateScan();
			}
		});
		
		Button bt_loadAccount = (Button)findViewById(R.id.btLoadAccount);
		bt_loadAccount.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loadAccount();
			}
		});
	}
	
	private void loadAccount() {
		st_apiKey =  et_apiKey.getText().toString().replaceAll(" ", "");
		
		if (st_apiKey.equals("")){
			Toast.makeText(getApplicationContext(), "Please enter your API Key",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		PrefManager.setApiKey(getApplicationContext(), st_apiKey);
		Log.v("GMLTC", "apiKey is: " + st_apiKey);
		
		Intent intent = new Intent(HomeActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_about:
	            aboutDialog();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	// Display about dialog
	public void aboutDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle("Give-Me-Ltc - Alpha");
		alert.setMessage("An app that allows GMLTC miners to monitor their account and " +
				"chat with other users.\n\nCreated by: Harry Pritchett" +
				"\nBarcode Scanner: Zxing" +
				"\nApp Base: nninchuu & Simran" + 
				"\n\nwww.give-me-ltc.com");
		
		alert.show();	
	}
	
	// This gets the apiKey from the QR Code. Currently not implemented by give-me-ltc.com
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		  if (scanResult != null) {
		    et_apiKey.setText(scanResult.getContents());
		    st_apiKey = et_apiKey.getText().toString().replaceAll(" ", "");
		    PrefManager.setApiKey(getApplicationContext(), st_apiKey);
		  }
		}
}
