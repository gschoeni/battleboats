package com.gregschoeninger.battleboats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

public class SpeechConverter {
	
	private String filename;

    public String accessToken;
    public String text;
    MediaRecorder recorder;
    
    public SpeechConverter(String file, String accessToken) {
    	this.filename = file;
    	this.accessToken = accessToken;
    }
    

    public void buttonPressed() {   
    	Log.d(Battleboats.DEBUG_TAG, "Clicked! Start Recording.");
    	setupRecorder();
        startRecording();
    	
    	new java.util.Timer().schedule( 
    	        new java.util.TimerTask() {
    	            @Override
    	            public void run() {
    	            	Log.d(Battleboats.DEBUG_TAG, "STOP Recording.");
    	            	stopRecording();
    	                convertSpeechToText();
    	            }
    	        }, 
    	        3000 
    	);

    }

    private void setupRecorder() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(filename);
        recorder.setAudioChannels(1);
    }

    private void startRecording() {
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
        recorder.stop();
        recorder.reset();
    }

    private void convertSpeechToText() {
    	try {
			text = new TextRequest().execute(filename).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Coordinate c = getCoordinates();
    	Log.d(Battleboats.DEBUG_TAG, "Text: "+text+" Coordinates: "+c);
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
    
    private Coordinate getCoordinates() {
        
        int row = 7 - (text.codePointAt(0) - 65);

        String[] words = { "one", "two", "three", "four", "five", "six", "seven", "eight" };
        List<String> values = Arrays.asList(words);
        int col = values.indexOf(text.substring(text.lastIndexOf(" ") + 1, text.length()));
        
        return new Coordinate(row, col);
    }

}
