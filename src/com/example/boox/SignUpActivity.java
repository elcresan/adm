package com.example.boox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity {
	
	Context context = this;
	final int mode = Activity.MODE_PRIVATE;
	public static final String myPrefs = "prefs";
	
	ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // This must be called before ANY content is created
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        
        setContentView(R.layout.activity_sign_up);
        
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_sign_up, menu);
        return true;
    }
    
	public void onPressSignUp(View view) {
		
        setProgressBarIndeterminateVisibility(true);
        progressDialog = ProgressDialog.show(context, 
        		getResources().getString(R.string.signup_sending),
        		getResources().getString(R.string.signup_pleasewait));

		AsyncAdd add = new AsyncAdd();
		add.execute(null, null, null);
    }
    
    
    public class AsyncAdd extends AsyncTask<Void, Void, Integer> {
    	
    	String pass;
    	String cp;
    	String full;
    	String username;
    	int flag = 0;
    	//1 -> contraseña dup, 2 -> internet, 3 -> username ya existe
    	
		@Override
		protected void onPreExecute(){
			EditText ptxt = (EditText) findViewById(R.id.signup_password);
			EditText ctxt = (EditText) findViewById(R.id.signup_confirm_password);
			pass = ptxt.getText().toString();
            EditText utxt = (EditText) findViewById(R.id.signup_username);
            EditText cptxt = (EditText) findViewById(R.id.signup_zipcode);
            EditText fulltxt = (EditText) findViewById(R.id.signup_fullname);
            
            username = utxt.getText().toString();
            cp = cptxt.getText().toString();
            full = fulltxt.getText().toString();
			String pass2 = ctxt.getText().toString();
			Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
			Matcher m = p.matcher(username);
			if(!m.find())
			  flag = 7;
			else {
				m = p.matcher(pass);
				if(!m.find())
					flag = 8;
				else if(username.equals(""))
					flag = 4;
				else if(pass.length() == 0)
					flag = 5;
				else if (full.equals(""))
					flag = 6;
				else if(!pass.equals(pass2))
					flag = 1;
			}

		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(flag == 0){
				HttpParams httpParameters = new BasicHttpParams();

				int timeoutConnection = 1500;
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

				int timeoutSocket = 1500;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

				DefaultHttpClient client = new DefaultHttpClient(httpParameters); 
	            HttpPost httppost = new HttpPost("http://boox.eu01.aws.af.cm/users"); 

	           
	            //String uname ="{\"uname\":\"" + username + "\"}";

	            JSONObject json = new JSONObject();
	            
	            try {
					json.put("uname", username);
					json.put("pass", pass);
					json.put("postal", cp);
					json.put("fullname", full);
	                StringEntity se;
					se = new StringEntity(json.toString());
	 
	                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	                httppost.setEntity(se);
	                HttpResponse response;
					response = client.execute(httppost);

	                HttpEntity responseEntity = response.getEntity();
					InputStream stream = responseEntity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(stream));
					String line = null;
					line = reader.readLine();
	                if(line.equals("Error")){
	                	flag = 3;
	                }
	            	} catch (ConnectTimeoutException e) {
	            		flag = 2;
	            		e.printStackTrace();
					} catch (JSONException e) {
						flag = 2;
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						flag = 2;
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						flag = 2;
						e.printStackTrace();
					} catch (IOException e) {
						flag = 2;
						e.printStackTrace();
					}
			}
  
			return flag;
		}

		@Override
		protected void onPostExecute(Integer result) {

			// Stop the indeterminate progress bar and close dialog
	        setProgressBarIndeterminateVisibility(false);
	        progressDialog.dismiss();
			
			if(result == 1){
				Toast toast = Toast.makeText(
						getApplicationContext(), 
						getResources().getString(R.string.signup_confirm_pass_error), 
						Toast.LENGTH_SHORT);
				toast.show();
			}
			else if(result == 2){
				Toast toast = Toast.makeText(
						getApplicationContext(), 
						getResources().getString(R.string.signup_internet_error), 
						Toast.LENGTH_SHORT);
				toast.show();
			}
			else if(result == 3){
				Toast toast = Toast.makeText(
						getApplicationContext(), 
						getResources().getString(R.string.signup_already_exists_error), 
						Toast.LENGTH_SHORT);
				toast.show();
			}
			else if(result == 5){
				Toast toast = Toast.makeText(
						getApplicationContext(), 
						getResources().getString(R.string.signup_password_length_error), 
						Toast.LENGTH_SHORT);
				toast.show();
			}
			else if(result == 4){
				Toast toast = Toast.makeText(
						getApplicationContext(), 
						getResources().getString(R.string.signup_empty_user_error), 
						Toast.LENGTH_SHORT);
				toast.show();
			}
			else if(result == 6){
				Toast toast = Toast.makeText(
						getApplicationContext(), 
						getResources().getString(R.string.signup_empty_fullname_error), 
						Toast.LENGTH_SHORT);
				toast.show();
			}
			else if(result == 7){
				Toast toast = Toast.makeText(
						getApplicationContext(), 
						getResources().getString(R.string.signup_username_alphanum_error), 
						Toast.LENGTH_SHORT);
				toast.show();
			}
			else if(result == 8){
				Toast toast = Toast.makeText(
						getApplicationContext(), 
						getResources().getString(R.string.signup_pass_alphanum_error), 
						Toast.LENGTH_SHORT);
				toast.show();
			}
			else {
				Toast toast = Toast.makeText(
						getApplicationContext(), 
						getResources().getString(R.string.signup_user_created), 
						Toast.LENGTH_SHORT);
				toast.show();
				SharedPreferences mySharedPreferences = getSharedPreferences(myPrefs,
						mode);
				SharedPreferences.Editor myEditor = mySharedPreferences.edit();

				myEditor.putString("username", username);
				myEditor.commit();
		        Intent intent = new Intent(context, TabsActivity.class);
		       	startActivity(intent);
			}

		}
	
	}
    
}
