package com.gregschoeninger.battleboats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void buttonPressed(View view) {        
    	Log.d(Battleboats.DEBUG_TAG, "TEST");
    	
    }
    
    public void playGame(View view) {        
    	Intent i = new Intent(this, Battleboats.class);
    	startActivity(i);
    }
}
