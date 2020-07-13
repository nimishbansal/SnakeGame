package com.example.nim.snakegame;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

class CharacterSprite
{
    private final int length;
    final ArrayList<Slice> slices;
    private Bitmap image;
    private int x, y;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int squareSize;
    public int gapBetweenSquare;
    private int ctr;
    private int count = 1;
    private int speed;
    private int delta_x;
    private int delta_y;
    private String direction;
    private Context context;
    private int end;

    CharacterSprite(Bitmap bmp, Context context)
    {
        image = bmp;
        x = 100;
        y = 100;
        squareSize = 50;
        gapBetweenSquare = 10;
        ctr = 0;
        count = 0;
        speed = 4;
        length = 4;
        slices = new ArrayList<>();
        //Populating snake with 4 slices
        slices.add(new Slice(new Position(6, 17)));
        slices.add(new Slice(new Position(6, 18)));
        slices.add(new Slice(new Position(6, 19)));
        slices.add(new Slice(new Position(6, 20)));
        direction = Direction.FORWARD;
        this.context = context;
        this.end = 0;
    }

    /*this function calculates change in coordinates depending on the direction*/
    private ArrayList get_delta_x_and_delta_y()
    {
        delta_x = 0;
        delta_y = 0;
        if (Objects.equals(this.direction, Direction.FORWARD))
            delta_y = -1;
        else if (Objects.equals(this.direction, Direction.BACKWARD))
            delta_y = +1;
        else if (Objects.equals(this.direction, Direction.LEFT))
            delta_x = -1;
        else if (Objects.equals(this.direction, Direction.RIGHT))
            delta_x = 1;

        ArrayList<Integer> array = new ArrayList<>();
        array.add(delta_x);
        array.add(delta_y);
//        Log.i("MYTAG", "delta_x, delta_y" + array.toString());
        return array;
    }

    /*It moves snake 1 step in the direction*/
    private void move_one_step_forward()
    {
        /*assuming snake is moving forward*/
        ArrayList result = get_delta_x_and_delta_y();
        delta_x = (int) result.get(0);
        delta_y = (int) result.get(1);

        Slice first_slice = this.slices.get(0);
        this.slices.remove(this.slices.size()-1);

        Slice new_slice = new Slice(new Position(first_slice.position.x + delta_x, first_slice.position.y + delta_y));

        boolean flag;
        //Snake collide with own body end game
        flag = check_allowed_to_add(slices, new_slice);
        if (flag)
        {
            this.slices.add(0, new_slice);
        }
        else
        {
            endgame();
        }

    }

    private boolean check_allowed_to_add(ArrayList<Slice> slices, Slice new_slice)
    {
        for (int j=0;j<slices.size();j++)
        {
            if (slices.get(j).position.x==new_slice.position.x && slices.get(j).position.y==new_slice.position.y)
                return false;
        }
        return true;
    }

    /*function to change direction when swiped*/
    void set_direction(String direction)
    {
        if (!Arrays.asList(Direction.BACKWARD, Direction.FORWARD, Direction.LEFT, Direction.RIGHT).contains(direction))
        {
            throw new IllegalArgumentException("direction can only be: FORWARD, BACKWARD, LEFT, RIGHT");
        }

        if ((Objects.equals(this.direction, Direction.FORWARD) && Objects.equals(direction, Direction.BACKWARD)) ||
                (Objects.equals(this.direction, Direction.BACKWARD) && Objects.equals(direction, Direction.FORWARD)) ||
                (Objects.equals(this.direction, Direction.LEFT) && Objects.equals(direction, Direction.RIGHT)) ||
                (Objects.equals(this.direction, Direction.RIGHT) && Objects.equals(direction, Direction.LEFT)))
        {
            throw new IllegalArgumentException("Snake cant reverse");
        }
        this.direction = direction;
    }


    void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        for (int i = 0; i < slices.size(); i++)
        {
            Log.i("TAG", "i = " + i);
            int right, left;
            Slice slice = slices.get(i);
            right = slice.position.y;
            left = slice.position.x;
            if (i == 0){
                // setting mouth to different colour
                Paint paint1 = new Paint();
                paint1.setColor(Color.YELLOW);
                paint1.setStyle(Paint.Style.FILL);
                paint1.setStrokeWidth(3);
//                canvas.drawRect(50 * left + gapBetweenSquare, 50 * right + gapBetweenSquare, 50 * left + 50, 50 * right + 50, paint1);
                canvas.drawCircle(50 * left + 20 + gapBetweenSquare, 50*right + 20 + gapBetweenSquare, 22, paint1);
                continue;
            }
//            canvas.drawRect(50 * left + gapBetweenSquare, 50 * right + gapBetweenSquare, 50 * left + 50, 50 * right + 50, paint);
            canvas.drawCircle(50 * left + 20 + gapBetweenSquare, 50*right + 20 + gapBetweenSquare, 22, paint);
        }
    }

    /* Increases the length of snake by 1*/
    void increase_snake_length_by_1()
    {
        Slice currentLastSlice = slices.get(slices.size()-1);
        Slice lastSlice = new Slice(new Position(currentLastSlice.position.x,currentLastSlice.position.y));
        slices.add(slices.size(), lastSlice);
    }

    void update()
    {
        count += 1;

        if (count % (speed) == 0)
        {
            if (end==0)
            {
                move_one_step_forward();
            }
            ctr+= 1;
        }

        //Boundary Conditions End Game
        if (    (slices.get(0).position.x<0 || slices.get(0).position.x>13) ||
                (slices.get(0).position.y<0 || slices.get(0).position.y>24))
        {
            endgame();

        }

    }

    private void endgame()
    {
        if (end==0)
        {
            Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(1000);
            }
            end = 1;
        }

    }
}

