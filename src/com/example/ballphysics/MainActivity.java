package com.example.ballphysics;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	TextView myView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Display display = getWindowManager().getDefaultDisplay(); 
	    final int version = android.os.Build.VERSION.SDK_INT;
	    final int width, height;
	    if (version >= 13)
	    {
	        Point size = new Point();
	        display.getSize(size);
	        width = size.x;
	        height = size.y;
	    }
	    else
	    {
	        display = getWindowManager().getDefaultDisplay(); 
	        width = display.getWidth();
	        height = display.getHeight();	        		
	    }
	    
	    setContentView(R.layout.activity_main);

	    final BallSurfaceView myBallView =new BallSurfaceView(getApplicationContext(), width, height);
	    LinearLayout surface = (LinearLayout)findViewById(R.id.surfaceView1);
	    surface.addView(myBallView);
	
	}
}


