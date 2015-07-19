package com.miproducts.miwatch.config;

/**
 * Created by ladam_000 on 7/16/2015.
 */
public interface CustomizedMods  {
    int getSize();
    float getX();
    float getY();
    void changeSize(int newSize);
    void setVisibility(boolean visible);
    int getColor();
    boolean getViewsVisibility();
    int getId();
}
