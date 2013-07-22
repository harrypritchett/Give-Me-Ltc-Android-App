package com.hpritch5.give_me_ltc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.backend.android.R;

public class MainActivity extends Activity {
	
	TextView mUsername, mConfirmedRewards, mRoundEstimate, mTotalHashrate, mPayoutHistory, mRoundShares;
	HttpClient client;
	
	final static String URL_STRING = "https://give-me-ltc.com/api?api_key=";
	String st_apiKey = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mUsername = (TextView)findViewById(R.id.mUsername);
		mPayoutHistory = (TextView)findViewById(R.id.mPayoutHistory);
		mConfirmedRewards = (TextView)findViewById(R.id.mConfirmedRewards);
		mRoundShares = (TextView)findViewById(R.id.mRoundShares);
		mRoundEstimate = (TextView)findViewById(R.id.mRoundEstimate);
		mTotalHashrate = (TextView)findViewById(R.id.mTotalHashrate);
		
		
		Button refreshButton = (Button)findViewById(R.id.btRefresh);
		refreshButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				refreshView();
			}
		});
		
		// Initialize
		refreshView();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_options:
	            apiKeyDialog();
	            return true;
	        case R.id.action_about:
	            aboutDialog();
	            return true;
	        case R.id.action_chat:
	        	Intent intent = new Intent(MainActivity.this, ChatActivity.class);
	        	startActivity(intent);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void aboutDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle("Give-Me-Ltc  Alpha");
		alert.setMessage("An app that allows GMLTC miners to monitor their account and " +
				"chat with other users.\n\nCreated by: Harry Pritchett" +
				"\nBarcode Scanner: Zxing" +
				"\nApp Base: nninchuu & Simran" + 
				"\n\nwww.give-me-ltc.com");
		
		alert.show();	
	}
	
	public void apiKeyDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle("API Key Required");
		alert.setMessage("Please enter your give-me-ltc API Key:");
		
		final EditText input = new EditText(this);
		alert.setView(input);
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
			  	st_apiKey = input.getText().toString().replaceAll(" ", "");
			  	
			  	if (!st_apiKey.equals("")) {
			  		PrefManager.setApiKey(getApplicationContext(), st_apiKey);
			  		refreshView();
			  	}
			}
		});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Cancelled.
			  }
		});
		
		alert.show();
	}
	
	// Get JSON data
	public class GetMinerData extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... url) {
			
			String JSONString = "";
			JSONObject result = new JSONObject();
			InputStream is = null;
			
			try {
				String mURL = URL_STRING + st_apiKey;
				Log.v("GMLTC", "Url is: " + mURL);

				HttpGet get = new HttpGet(mURL);
				client = new DefaultHttpClient();
				HttpResponse httpResponse = client.execute(get);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			
			} catch (ClientProtocolException e) {
				setError("Error: No internet connection!!");
				e.printStackTrace();
			} catch (IOException e) {
				setError("Error: No internet connection!!");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				JSONString = sb.toString();
				Log.v("GMLTC", "JSONString is:\n" + JSONString);
				
				if (!JSONString.equals("Invalid Key.\n")){
					result = new JSONObject(JSONString);
				}
			} catch (Exception e) {
				setError("Error: Invalid API Key!");
				e.printStackTrace();
			}

			Log.v("GMLTC", "Result:\n" + result);
			return result;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			
			try {
				mUsername.setText(result.get("username").toString());
				String payoutHistory = new DecimalFormat("#.##").format(result.getDouble("payout_history"));
				mPayoutHistory.setText(payoutHistory + " LTC");
				String rewards = new DecimalFormat("#.####").format(result.getDouble("confirmed_rewards"));
				mConfirmedRewards.setText(rewards + " LTC");
				String roundEstimate = new DecimalFormat("#.####").format(result.getDouble("round_estimate"));
				mRoundShares.setText(result.get("round_shares").toString());
				mRoundEstimate.setText(roundEstimate + " LTC");
				
				int totalHashrate = result.getInt("total_hashrate");
				if (totalHashrate == 0) {
					mTotalHashrate.setTextColor(Color.RED);
				} else {
					mTotalHashrate.setTextColor(Color.WHITE);
				}
				
				mTotalHashrate.setText(totalHashrate + " kH/s");
				
			} catch (JSONException e) {
				setError("Error: Invalid API Key");
				e.printStackTrace();
			}
		}
		
		private void setError(String error) {
			Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		
	}
	
	public void refreshView() {
		
		st_apiKey = PrefManager.getApiKey(getApplicationContext());
		Log.v("GMLTC", "apiKey: " + st_apiKey);
		
		if (st_apiKey.equals("")) {
			Toast.makeText(getApplicationContext(), "No API Key is saved.", Toast.LENGTH_LONG).show();
			apiKeyDialog();
			return;
		}
		
		GetMinerData  data = new GetMinerData();
		data.execute();
		
	}
}
