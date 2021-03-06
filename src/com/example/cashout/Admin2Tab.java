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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Admin2Tab extends Activity {
	Intent inew;
	private String TAG = AdminActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
 
    // URL to get members JSON
    private static String url = "http://apilearningpayment.totopeto.com/members/";
 
    ArrayList<HashMap<String, String>> membersList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_admin2);
        
        lv = (ListView) findViewById(R.id.list_admin2);
        lv.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
			HashMap<String, String> hm = membersList.get(position);
			Intent intent = new Intent(Admin2Tab.this, ProfilActivity.class);
			intent.putExtra("user_id", hm.get("id"));
			intent.putExtra("user_name", hm.get("name"));
			intent.putExtra("user_email", hm.get("email"));
			intent.putExtra("user_phone_number", hm.get("user_phone_number"));
			intent.putExtra("account_type", "members");
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
//				Intent intent = new Intent(AdminActivity.this, memberAdd.class);
//				startActivity(intent);
//			}
//		});
	}
	
	private class Getmembers extends AsyncTask<Void, Void, Void> {
   	 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Admin2Tab.this);
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
                    JSONArray members = jsonObj.getJSONArray("members");
 
                    // looping through All members
                    for (int i = 0; i < members.length(); i++) {
                        JSONObject c = members.getJSONObject(i);
                        
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String phone_number = c.getString("phone_number");
                        
                        // tmp hash map for single member
                        HashMap<String, String> member = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        member.put("name", name);
                        member.put("email", email);
                        member.put("phone_number", phone_number);                        
                        // adding member to member list
                        membersList.add(member);
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
                    Admin2Tab.this, membersList,
                    R.layout.list_item, new String[]{"name", "email","phone_number" }, new int[]{R.id.name, R.id.email, R.id.phone_number});
 
            lv.setAdapter(adapter);
        }
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	membersList = new ArrayList<HashMap<String, String>>();
    	new Getmembers().execute();
    }
    public void callAkunBaru(View v) {
		inew = new Intent(this, NewActivity.class);
		inew.putExtra("account_type", "members");
		inew.putExtra("session_url", "http://apilearningpayment.totopeto.com/members/ ");
		startActivity(inew);
    }
	
}