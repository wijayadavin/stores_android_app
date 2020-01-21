package com.example.cashout;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends Activity {
	
	private String TAG = NewActivity.class.getSimpleName();
    private ProgressDialog pDialog;
	Intent oldIntent;
	Button bsimpan;
	String url="";
	EditText ename, eemail, ephone;
	TextView PhoneTextView, accountType;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_edit);
	oldIntent = getIntent();
	url = oldIntent.getStringExtra("session_url");
	//3 data Profil
	ename = (EditText) findViewById(R.id.edit_profil_nama);
	eemail = (EditText) findViewById(R.id.edit_profil_email);
	ephone = (EditText) findViewById(R.id.edit_profil_phone);
	//TV Phone
	PhoneTextView = (TextView) findViewById(R.id.textView_phone);
	//menangkap tipe akun dari halaman sebelumnya
	accountType = (TextView) findViewById(R.id.textView_accountType);
	accountType.setText(oldIntent.getStringExtra("account_type"));
	//khusus account admin
	if(accountType.getText().toString().equals("administrators")) {

	}
	//khusus account members
	if(accountType.getText().toString().equals("members")) {
		ephone.setVisibility(View.GONE);
		PhoneTextView.setVisibility(View.GONE);
	}
	//khusus account tenants
	if(accountType.getText().toString().equals("tenants")) {

	}		
	bsimpan = (Button) findViewById(R.id.button_save_edit);
	bsimpan.setOnClickListener(new View.OnClickListener() {	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Edit().execute();
			finish();
		}
	});
}	

private class Edit extends AsyncTask<Void, Void, Void> {
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(EditActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    
    @Override
    protected Void doInBackground(Void... arg0) {
        String put_params = null;
        JSONObject params = new JSONObject();

        try {
        	params.put("name", ename.getText().toString());
        	params.put("email", eemail.getText().toString());
        	params.put("phone", ephone.getText().toString());
        	put_params = params.toString();
        	
        } catch (JSONException e) {
        	e.printStackTrace();
        }
        
        HttpHandler data = new HttpHandler();
        String jsonStr = data.makePutRequest(url, put_params);
        Log.e(TAG, "Response from url: " + jsonStr);
        
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
    }
}
	public void callToPrevious(View v) {
		finish();
	}
}