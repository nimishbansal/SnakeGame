package com.example.nim.snakegame;

class Slice
{
    public final Position position;
    /*
            It represents the part of the snake body
     */
    Slice(Position position)
        {
        this.position = position;
        }


    @Override
    public String toString()
    {
        return "("+ String.valueOf(this.position.x)+","+String.valueOf(this.position.y)+")";
    }
}