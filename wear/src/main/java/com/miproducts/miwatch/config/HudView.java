package com.miproducts.miwatch.config;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.utilities.Consts;

/**
 * Created by larry on 7/17/15.
 */
public class HudView extends Mods {
    private static final String TAG = "HudView";

    private WatchFaceSurfaceViewConfig svView;
    private int xPos, yPos;
    private Rect rectHud;
    private Paint pRect;

    public HudView(Context context, WatchFaceSurfaceViewConfig svView) {
        super(context, svView);
        this.svView = svView;
        xPos = 0;
        yPos = Consts.yHudPosition; // same as event i think will be fine, prob should make its own constant.

        initRect();
        paintRect();
    }

    private void initRect() {
        rectHud = new Rect(xPos,yPos,(int)svView.getCanvasWidth(),(int) svView.getCanvasHeight());
    }

    private void paintRect(){
        pRect = new Paint();
        pRect.setColor(getResources().getColor(R.color.digital_time_blue));
        pRect.setStyle(Paint.Style.FILL);
    }

    @Override
    public boolean touchInside(MotionEvent event) {
        return super.touchInside(event);
    }

    @Override
    public void unSelectPaint() {
        super.unSelectPaint();
    }

    @Override
    public int getId() {
        return -1;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(rectHud, pRect);


    }







    public HudView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HudView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public HudView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
