package com.ifnoif.androidtestdemo.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by syh on 2016/9/3.
 */
public class MyCustomView extends View {
    public String title;
    private Paint paint;

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p>
     * <p>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #View(Context, AttributeSet, int)
     */
    public MyCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init("test");
    }

    public MyCustomView(Context context, String text) {
        super(context);
        init(text);
    }

    private void init(String text){
        this.title = text;
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(getContext().getResources().getDisplayMetrics().density*40);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.RED);
        canvas.drawText(title, 0, paint.getTextSize() * 2, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
