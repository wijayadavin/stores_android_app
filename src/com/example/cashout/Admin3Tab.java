package com.example.cashout;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Admin3Tab extends Activity {
	Intent inew;
	private String TAG = AdminActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
 
    // URL to get administrators JSON
    private static String url = "http://apilearningpayment.totopeto.com/administrators";
 
    ArrayList<HashMap<String, String>> administratorList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_admin3);
        
        lv = (ListView) findViewById(R.id.list_admin3);
        lv.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
			HashMap<String, String> hm = administratorList.get(position);
			Intent intent = new Intent(Admin3Tab.this, ProfilActivity.class);
			intent.putExtra("user_id", hm.get("id"));
			intent.putExtra("user_name", hm.get("name"));
			intent.putExtra("user_email", hm.get("email"));
			intent.putExtra("account_type", "administrators");
			intent.putExtra("request_from", "admin");
			intent.putExtra("url",url);
			startActivity(intent);
			}
		});
//        
//        badd.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(AdminActivity.this, administratorAdd.class);
//				startActivity(intent);
//			}
//		});
	}
	
	private class Getadministrators extends AsyncTask<Void, Void, Void> {
   	 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Admin3Tab.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            
            // Making a request to URL and getting response
            String jsonStr = sh.makeServiceCall(url);
 
            Log.e(TAG, "Response from url: " + jsonStr);
 
            // Read JSON
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
 
                    // Getting JSON Array node
                    JSONArray administrators = jsonObj.getJSONArray("administrators");
 
                    // looping through All administrators
                    for (int i = 0; i < administrators.length(); i++) {
                        JSONObject c = administrators.getJSONObject(i);
                        
                        String name = c.getString("name");
                        String email = c.getString("email");
                        
                        // tmp hash map for single administrator
                        HashMap<String, String> administrator = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        administrator.put("name", name);
                        administrator.put("email", email);
                        
                        // adding administrator to administrator list
                        administratorList.add(administrator);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    Admin3Tab.this, administratorList,
                    R.layout.list_item, new String[]{"name", "email"}, new int[]{R.id.name, R.id.email});
 
            lv.setAdapter(adapter);
        }
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	administratorList = new ArrayList<HashMap<String, String>>();
    	new Getadministrators().execute();
    }
    public void callAkunBaru(View v) {
		inew = new Intent(this, NewActivity.class);
		inew.putExtra("account_type", "administrators");
		inew.putExtra("session_url", "http://apilearningpayment.totopeto.com/administrators/");
		startActivity(inew);
    }
	
}