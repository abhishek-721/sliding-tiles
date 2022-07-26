package com.creativeideas.slidingtiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class StrokedTextView extends AppCompatTextView {
    private float mStrokeWidth;
    private int mStrokeColor;

    private int startColor;
    private int endColor;

    private boolean isDrawing;
    private Shader shader;

    public StrokedTextView(Context context) {
        super(context);
        mStrokeWidth = 0.0f;
        mStrokeColor = Color.WHITE;

        startColor = Color.BLACK;
        endColor = Color.BLACK;
    }

    public StrokedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokedTextView);

        mStrokeWidth = a.getFloat(R.styleable.StrokedTextView_strokeWidth2, 0.0f);
        mStrokeColor = a.getColor(R.styleable.StrokedTextView_strokeColor, Color.WHITE);

        startColor = a.getColor(R.styleable.StrokedTextView_gradientStartColor, Color.BLACK);
        endColor = a.getColor(R.styleable.StrokedTextView_gradientEndColor, Color.BLACK);

        a.recycle();
    }

    public StrokedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokedTextView);

        mStrokeWidth = a.getFloat(R.styleable.StrokedTextView_strokeWidth2, 0.0f);
        mStrokeColor = a.getColor(R.styleable.StrokedTextView_strokeColor, Color.WHITE);

        startColor = a.getColor(R.styleable.StrokedTextView_gradientStartColor, Color.BLACK);
        endColor = a.getColor(R.styleable.StrokedTextView_gradientEndColor, Color.BLACK);

        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        shader = new LinearGradient(0, 0, 0, getTextSize(), startColor, endColor, Shader.TileMode.CLAMP);
    }

    @Override
    public void invalidate() {
        if (isDrawing) return;
        super.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mStrokeWidth > 0) {
            isDrawing = true;
            int color = getCurrentTextColor();

            Paint paint = getPaint();

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(mStrokeWidth);
            setTextColor(mStrokeColor);
            super.onDraw(canvas);

            paint.setStyle(Paint.Style.FILL);
            paint.setShader(shader);
            setTextColor(color);
            super.onDraw(canvas);

            paint.setShader(null);

            isDrawing = false;
        } else {
            super.onDraw(canvas);
        }
    }
}
