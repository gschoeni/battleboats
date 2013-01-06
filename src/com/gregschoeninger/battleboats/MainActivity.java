package com.gregschoeninger.battleboats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends Activity {

    public String accessToken;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);        
        setContentView(R.layout.activity_main);
    	requestAccessToken();
    	
    }

    public void playGame(View view) {   
    	Log.d(Battleboats.DEBUG_TAG, "Clicked!");
        Intent i = new Intent(this, Battleboats.class);
        i.putExtra("accessToken", accessToken);
        startActivity(i);
    }
    
    public void requestAccessToken() {
    	try {
			accessToken = new OauthRequest().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Log.d(Battleboats.DEBUG_TAG, "Access token: "+accessToken);
    }
    
    private class OauthRequest extends AsyncTask<Void, Void, String> {

        String OAUTH_TOKEN_URL = "https://api.att.com/oauth/token";
        String OAUTH_REQUEST = "client_id=715c0b07901f34970719f5d2ad06c3a3&client_secret=102000f5213b75de&grant_type=client_credentials&scope=SPEECH";
        
        @Override
        protected String doInBackground(Void... _) {
        	Log.d(Battleboats.DEBUG_TAG, "starting background...");
            HttpURLConnection oauth = null;
            String token = "?";
            try {
                oauth = (HttpURLConnection) new URL(OAUTH_TOKEN_URL).openConnection();
                oauth.setDoOutput(true);
                oauth.setChunkedStreamingMode(0);

                OutputStream data = new BufferedOutputStream(oauth.getOutputStream());
                data.write(OAUTH_REQUEST.getBytes(Charset.forName("UTF-8")));
                data.close();

                Scanner result = new Scanner(new BufferedInputStream(oauth.getInputStream())).useDelimiter("\\A");
                if (result.hasNext()) {
                	token = new JSONObject(result.next()).getString("access_token");
                }
            } catch (Exception e){
                // TODO
            } finally {
                oauth.disconnect();
            }
            return token;              
        }

    }
}
