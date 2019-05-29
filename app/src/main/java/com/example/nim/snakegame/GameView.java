package com.example.nim.snakegame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private CharacterSprite characterSprite;
    private FoodSprite foodSprite;
    private Context context;


    public GameView(Context context) {
        super(context);
        this.context = context;

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(context)
        {

            @Override
            public void onSwipeRight()
            {
                try{
                    characterSprite.set_direction(Direction.RIGHT);
                }
                catch(IllegalArgumentException ex)
                {
                    Log.i("MYTAG", ex.getMessage());
                }
            }

            @Override
            public void onSwipeLeft()
            {
                try{
                    characterSprite.set_direction(Direction.LEFT);
                }
                catch(IllegalArgumentException ex)
                {
                    Log.i("MYTAG", ex.getMessage());
                }
            }

            @Override
            public void onSwipeTop()
            {
                try
                {
                    characterSprite.set_direction(Direction.FORWARD);
                }
                catch(IllegalArgumentException ex)
                {
                    Log.i("MYTAG", ex.getMessage());
                }

            }

            @Override
            public void onSwipeBottom()
            {
                try{
                    characterSprite.set_direction(Direction.BACKWARD);
                }
                catch(IllegalArgumentException ex)
                {
                    Log.i("MYTAG", ex.getMessage());
                }
            }
        };

        this.setOnTouchListener(onSwipeTouchListener);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.avdgreen), this.context);
        foodSprite = new FoodSprite(characterSprite, this.context);


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
        foodSprite.update();

    }

    @Override
    public void draw(Canvas canvas)
    {

        super.draw(canvas);
        if(canvas!=null) {
            characterSprite.draw(canvas);
            foodSprite.draw(canvas);

        }
    }


}