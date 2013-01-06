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
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class SpeechConverter {
    
    private String filename;

    public String accessToken;
    public String row = "";
    public String col = "";
    MediaRecorder recorder;
    
    HashMap<String, Integer> wordToIndex;
    HashMap<String, Integer> numberToIndex;
    
    public SpeechConverter(String file, String accessToken) {
        this.filename = file;
        this.accessToken = accessToken;
        setupWordList();
        setupNumberList();
    }
    

    private void setupWordList() {
        wordToIndex = new HashMap<String, Integer>();
        wordToIndex.put("alpha", 7);
        wordToIndex.put("a", 7);
        wordToIndex.put("eight", 7);
        wordToIndex.put("hey", 7);

        wordToIndex.put("beta", 6);
        wordToIndex.put("be", 6);
        wordToIndex.put("bee", 6);
        wordToIndex.put("b", 6);

        wordToIndex.put("charlie", 5);
        wordToIndex.put("she", 7);
        wordToIndex.put("c", 5);

        wordToIndex.put("delta", 4);
        wordToIndex.put("steve", 4);
        wordToIndex.put("d", 4);

        wordToIndex.put("echo", 3);
        wordToIndex.put("e", 3);

        wordToIndex.put("foxtrot", 2);
        wordToIndex.put("f", 2);

        wordToIndex.put("gamma", 1);
        wordToIndex.put("g", 1);
        
        wordToIndex.put("hotel", 0);
        wordToIndex.put("h", 0);
    }

    private void setupNumberList() {
        numberToIndex = new HashMap<String, Integer>();

        numberToIndex.put("eight", 7);
        numberToIndex.put("hate", 7);
        numberToIndex.put("ate", 7);

        numberToIndex.put("seven", 6);

        numberToIndex.put("six", 5);

        numberToIndex.put("five", 4);
        numberToIndex.put("phi", 4);

        numberToIndex.put("four", 3);
        numberToIndex.put("for", 3);
        numberToIndex.put("form", 3);
        numberToIndex.put("more", 3);
        numberToIndex.put("door", 3);

        numberToIndex.put("three", 2);
        numberToIndex.put("free", 2);
        numberToIndex.put("tree", 2);
        numberToIndex.put("ready", 2);

        numberToIndex.put("two", 1);
        numberToIndex.put("too", 1);
        numberToIndex.put("to", 1);

        numberToIndex.put("won", 0);
        numberToIndex.put("one", 0);
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
    	        4000 
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
            new TextRequest().execute(filename).get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        GameScreen.fireShot(getCoordinates());
    }
    
    

    private class TextRequest extends AsyncTask<String, Void, Void> {

        String SPEECH_TO_TEXT_URL = "https://api.att.com/rest/2/SpeechToText";

        @Override
        protected Void doInBackground(String... args) {
            HttpURLConnection speech = null;
            Log.d(Battleboats.DEBUG_TAG, "sending request..");
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
                    JSONObject first = nBest.getJSONObject(0);
                    JSONArray list = first.getJSONArray("Words");
                    row = list.getString(0).toLowerCase();
                    col = list.getString(list.length() - 1).replace(".", "").toLowerCase();
                }
            } catch (Exception e){
                // TODO
            } finally {
                speech.disconnect();
            }     
            return null;
        }
    }
    
    public Coordinate getCoordinates() {
        try {
            Log.d(Battleboats.DEBUG_TAG, "trying..");
            Integer r = wordToIndex.get(row);
            if (r == null) {
                r = wordToIndex.get(row.charAt(0));
                Log.d(Battleboats.DEBUG_TAG, row + " " + row.charAt(0));
            }
            Log.d(Battleboats.DEBUG_TAG, "Number to index: "+numberToIndex.get(col));
            int c = numberToIndex.get(col);
            Log.d(Battleboats.DEBUG_TAG, col + " " + c);
            Log.d(Battleboats.DEBUG_TAG, "d "+r);
            Log.d(Battleboats.DEBUG_TAG, "done.");
            return new Coordinate(c, r);            
        } catch (Exception e){
            Log.d(Battleboats.DEBUG_TAG, e+"");
            return null;
        }
    }

}
