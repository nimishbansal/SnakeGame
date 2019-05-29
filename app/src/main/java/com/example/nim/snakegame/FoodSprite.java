package com.example.nim.snakegame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class FoodSprite
{
    private final CharacterSprite characterSprite;
    private final Context context;
    private int food_position_x;
    private int food_position_y;
    private Canvas canvas;
    

    ArrayList<Integer> generate_random_coordinates_other_than_slices(ArrayList<Slice> slices)
    {
        int random_x;
        int random_y;
        random_x = new Random().nextInt(13);
        random_y = new Random().nextInt(24);
        boolean generate_again = true;
        int j;
        while (generate_again)
        {
            for (j = 0; j < slices.size(); j++)
            {
                if (slices.get(j).position.x == random_x && slices.get(j).position.y == random_y)
                {
                    generate_again = true;
                    break;
                }
            }
            if (j == slices.size())
            {
                generate_again = false;
            }
        }

        ArrayList<Integer> randomArray = new ArrayList<>();
        randomArray.add(random_x);
        randomArray.add(random_y);
        return randomArray;

    }

    FoodSprite(CharacterSprite characterSprite, Context context)
    {
        this.characterSprite = characterSprite;
        this.context = context;
        ArrayList<Integer> food_positions;
        food_positions = generate_random_coordinates_other_than_slices(this.characterSprite.slices);
        this.food_position_x = food_positions.get(0);
        this.food_position_y = food_positions.get(1);
    }


    void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(3);
//        canvas.drawRect(0,0,700,1280, paint);
        Log.i("MYTAG", String.valueOf(this.food_position_x)+","+String.valueOf(this.food_position_y));
        canvas.drawRect(50 * this.food_position_x +this.characterSprite.gapBetweenSquare, 50 * this.food_position_y+ this.characterSprite.gapBetweenSquare, 50 * this.food_position_x + 50, 50 * this.food_position_y + 50, paint);
    }
    
    void update()
    {
        if (
                (characterSprite.slices.get(0).position.x==this.food_position_x && characterSprite.slices.get(0).position.y==this.food_position_y) ||
                (characterSprite.slices.get(1).position.x==this.food_position_x && characterSprite.slices.get(1).position.y==this.food_position_y))
        {
            Log.i("MYTAG", "collision");
            ArrayList<Integer> food_positions = generate_random_coordinates_other_than_slices(this.characterSprite.slices);
            this.food_position_x = food_positions.get(0);
            this.food_position_y = food_positions.get(1);
            characterSprite.increase_snake_length_by_1();

        }
    }
    
}
