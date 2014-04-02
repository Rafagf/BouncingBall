package com.example.ballphysics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BallSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	private Bitmap backgroundBitmap;
	private Bitmap ballBitmap;
	private Ball ball;
	Context context;
	ViewThread thread;
	private int height, weight;
    private static final int INITIAL_Y_VELOCITY = 8;
    private static final int INITIAL_X_VELOCITY = 4;

    private static final double ACCELERATION = 1.1;
    // What proportion of the velocity is retained on a bounce?  if 1.0, no energy
    // is lost, and the ball will bounce infinitely.
    private static final double COEFFICIENT_OF_RESTITUTION = 0.8;
    // While the ball is rolling along the bottom of the screen, its x velocity
    // is multiplied by this amount each frame.
    private static final double COEFFICIENT_OF_FRICTION = 0.9;
	
	public BallSurfaceView(Context context, int w, int h){
		
		super(context);
		this.context = context;
		thread = new ViewThread(getHolder(), this);
		getHolder().addCallback(this);
		setFocusable(true);
		
		weight = w;
		height = h;
		
        ball = new Ball(200, 0, INITIAL_X_VELOCITY, INITIAL_Y_VELOCITY, 20, 20);
		ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.balliga);
	    backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.estadio3);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
	
		super.onDraw(canvas);
		
		double v2 = (ACCELERATION * 1) + ball.getyVelocity();

        ball.setyVelocity(v2);
        ball.update();

        int maxY = getHeight() - (ball.getHeight() / 2);
        int maxX = getWidth() - (ball.getWidth() / 2);
        int minX = 0 + ball.getWidth() / 2;

        // Ball is out of bounds in y dimension
        if (ball.getY() > maxY) {
            ball.setY(maxY);
            ball.setyVelocity(-COEFFICIENT_OF_RESTITUTION * ball.getyVelocity());
        }
        else if (ball.getY() < 0) {
            ball.setY(0);
            ball.setyVelocity(-COEFFICIENT_OF_RESTITUTION * ball.getyVelocity());
        }


        // Ball is out of bounds in x dimension
        if (ball.getX() > maxX) {
            ball.setX(maxX);
            ball.setxVelocity(-COEFFICIENT_OF_RESTITUTION * ball.getxVelocity());
        }
        else if (ball.getX() < minX) {
            ball.setX(minX);
            ball.setxVelocity(-COEFFICIENT_OF_RESTITUTION * ball.getxVelocity());
        }

        // ball is rolling along the bottom
        if (ball.getY() == maxY) {
            ball.setxVelocity(COEFFICIENT_OF_FRICTION * ball.getxVelocity());
        }

        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
	    canvas.drawBitmap(ballBitmap, ball.getX(), ball.getY(), null);
	}
	
	
	   
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
	       
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		thread.startrun(true);
		thread.start();
	}
	    
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		thread.startrun(false);
	    thread.stop();
	}  
}

class ViewThread extends Thread {

	private SurfaceHolder msurfaceHolder;
	private BallSurfaceView mySurfaceView;
	private boolean mrun =false;
	
	public ViewThread(SurfaceHolder holder, BallSurfaceView view) {
	
	    msurfaceHolder = holder;
	    mySurfaceView = view;
	}
	
	public void startrun(boolean run) {
	
	    mrun=run;
	}
	
	@Override
	public void run() {
	
		super.run();
	    Canvas canvas;
	    while (mrun) {
	    	canvas=null;
	        try {
	        	canvas = msurfaceHolder.lockCanvas(null);
	            synchronized (msurfaceHolder) {
	            	mySurfaceView.onDraw(canvas);
	            }
	        } finally {
	        	if (canvas != null) {
	        		msurfaceHolder.unlockCanvasAndPost(canvas);
	            }
	        }
	   }	
	}	
}