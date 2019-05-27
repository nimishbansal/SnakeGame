package com.example.nim.snakegame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private CharacterSprite characterSprite;

    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(context)
        {

            @Override
            public void onSwipeRight()
            {
                characterSprite.set_direction(Direction.RIGHT);
            }

            @Override
            public void onSwipeLeft()
            {
                characterSprite.set_direction(Direction.LEFT);
            }

            @Override
            public void onSwipeTop()
            {
                characterSprite.set_direction(Direction.FORWARD);
            }

            @Override
            public void onSwipeBottom()
            {
                characterSprite.set_direction(Direction.BACKWARD);
            }
        };
        this.setOnTouchListener(onSwipeTouchListener);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.avdgreen));


        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

            } catch(InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        characterSprite.update();

    }

    @Override
    public void draw(Canvas canvas)
    {

        super.draw(canvas);
        if(canvas!=null) {
            characterSprite.draw(canvas);

        }
    }


}