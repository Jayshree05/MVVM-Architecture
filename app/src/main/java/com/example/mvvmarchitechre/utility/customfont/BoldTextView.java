package com.example.mvvmarchitechre.utility.customfont;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class BoldTextView extends TextView {
    Context context;

    public BoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public BoldTextView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    private void init(AttributeSet attr) {
        if (attr != null) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/nunitobold.ttf");
            setTypeface(tf);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
