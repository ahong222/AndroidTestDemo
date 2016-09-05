package com.ifnoif.androidtestdemo.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by syh on 2016/9/3.
 */
public class MyCustomView extends View {
    public String title;
    private Paint paint;

    public MyCustomView(Context context, String text) {
        super(context);
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
}
