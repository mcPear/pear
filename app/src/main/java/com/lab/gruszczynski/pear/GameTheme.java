package com.lab.gruszczynski.pear;

/**
 * Created by maciej on 30.05.17.
 */
public class GameTheme {
    private int textColor;
    private int backgroundColor;

    public GameTheme(int textColor, int backgroundColor) {
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    public GameTheme() {
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
