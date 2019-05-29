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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class CharacterSprite
{
    private final int length;
    public final ArrayList<Slice> slices;
    private Bitmap image;
    private int x, y;
    private int xVelocity = 10;
    private int yVelocity = 5;
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
        speed = 15;
        length = 4;
        slices = new ArrayList<>();
        slices.add(new Slice(new Position(6, 17)));
        slices.add(new Slice(new Position(6, 18)));
        slices.add(new Slice(new Position(6, 19)));
        slices.add(new Slice(new Position(6, 20)));
        direction = Direction.FORWARD;
        this.context = context;
        this.end = 0;
    }

    ArrayList get_delta_x_and_delta_y()
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

    void move_one_step_forward()
    {
        /*assuming snake is moving forward*/
        ArrayList result = get_delta_x_and_delta_y();
        delta_x = (int) result.get(0);
        delta_y = (int) result.get(1);

        Slice first_slice = this.slices.get(0);
        this.slices.remove(this.slices.size()-1);

        Slice new_slice = new Slice(new Position(first_slice.position.x + delta_x, first_slice.position.y + delta_y));

        boolean flag;
        flag = check_allowed_to_add(slices, new_slice);
        if (flag)
        {
            this.slices.add(0, new_slice);
        }
        else
        {
            endgame();
        }

//        Log.i("MYTAG", this.slices.toString());

    }

    private boolean check_allowed_to_add(ArrayList<Slice> slices, Slice new_slice)
    {
        for (int j=0;j<slices.size();j++)
        {
//            Log.i("MYTAG", "a1, " + String.valueOf(slices.get(j).position.x)+", "+ String.valueOf(new_slice.position.x));
//            Log.i("MYTAG", "a2, " +String.valueOf(slices.get(j).position.y)+", "+ String.valueOf(new_slice.position.y));
            if (slices.get(j).position.x==new_slice.position.x && slices.get(j).position.y==new_slice.position.y)
                return false;
        }
        return true;
    }


    void set_direction(String direction)
    {
//        Log.i("MYTAG", "trying setting direction as " + direction);
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

//        Log.i("MYTAG", "setted direction as " + direction);
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
            int right, left;
            Slice slice = slices.get(i);
            right = slice.position.y;
            left = slice.position.x;
            canvas.drawRect(50 * left + gapBetweenSquare, 50 * right + gapBetweenSquare, 50 * left + 50, 50 * right + 50, paint);
        }
    }

    void increase_snake_length_by_1()
    {
        Slice currentLastSlice = slices.get(slices.size()-1);
        Slice lastSlice = new Slice(new Position(currentLastSlice.position.x,currentLastSlice.position.y));
        slices.add(slices.size(), lastSlice);
    }

    void update()
    {
        count += 1;

        if (count % (20 - speed) == 0)
        {
            if (end==0)
            {
                move_one_step_forward();
            }
            ctr+= 1;
        }

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
            end=1;
        }

    }
}

