package com.gregschoeninger.battleboats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	public static String SPEECH_FILE_NAME = "/speech.amr";

    public String accessToken;
    public String text;
    public boolean isRecording;
    MediaRecorder recorder;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestAccessToken();
        Log.d(Battleboats.DEBUG_TAG, "Path oncreate "+getFilesDir()+SPEECH_FILE_NAME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void playGame(View view) {   
    	Log.d(Battleboats.DEBUG_TAG, "Clicked!");
        if (isRecording) {
            stopRecording();
            convertSpeechToText();
        } else {
            setupRecorder();
            startRecording();
        }
    }

    private void setupRecorder() {
        isRecording = false;
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getFilesDir()+SPEECH_FILE_NAME);
        recorder.setAudioChannels(1);
    }

    private void startRecording() {
        isRecording = true;
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        recorder.start();
    }

    private void stopRecording() {
        isRecording = false;
        recorder.stop();
        recorder.reset();
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
    	Log.d("Token", accessToken);
    }

    private void convertSpeechToText() {
    	try {
			text = new TextRequest().execute(getFilesDir()+SPEECH_FILE_NAME).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Log.d(Battleboats.DEBUG_TAG, text);
    }
    
    private class OauthRequest extends AsyncTask<Void, Void, String> {

        String OAUTH_TOKEN_URL = "https://api.att.com/oauth/token";
        String OAUTH_REQUEST = "client_id=715c0b07901f34970719f5d2ad06c3a3&client_secret=102000f5213b75de&grant_type=client_credentials&scope=SPEECH";
        
        @Override
        protected String doInBackground(Void... _) {
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

    private class TextRequest extends AsyncTask<String, Void, String> {

        String SPEECH_TO_TEXT_URL = "https://api.att.com/rest/2/SpeechToText";

        @Override
        protected String doInBackground(String... args) {
            HttpURLConnection speech = null;
            String text = "?";
            Log.d(Battleboats.DEBUG_TAG, "starting");
            try {
                speech = (HttpURLConnection) new URL(SPEECH_TO_TEXT_URL).openConnection();
                speech.setDoOutput(true);
                speech.setChunkedStreamingMode(0);
                speech.setRequestMethod("POST");
                speech.setRequestProperty("Authorization", "Bearer " + accessToken);
                speech.setRequestProperty("Content-Type", "audio/amr");
                OutputStream data = new BufferedOutputStream(speech.getOutputStream());
               
                InputStream audio = new BufferedInputStream(new FileInputStream(args[0]));
                byte[] buffer = new byte[4096];
                int count;
                while((count = audio.read(buffer)) != -1) {
                    data.write(buffer, 0, count);
                }
                data.close();
                Scanner result = new Scanner(new BufferedInputStream(speech.getInputStream())).useDelimiter("\\A");
                if (result.hasNext()) {
                    JSONObject recognition = new JSONObject(result.next()).getJSONObject("Recognition");
                    Log.d(Battleboats.DEBUG_TAG, "Surprise Mothafucker! "+recognition);
                    JSONArray nBest = recognition.getJSONArray("NBest");
                    JSONObject words = nBest.getJSONObject(0);
                    text = words.getString("ResultText");
                }
            } catch (Exception e){
                // TODO
            } finally {
                speech.disconnect();
            }
            return text;              
        }
    }
}
