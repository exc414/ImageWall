package io.bluephoenix.imagewall.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.util.Util;

/**
 * @author Carlos A. Perez
 */
public class CustomBtnPlus extends CustomBtnBase
{
    //Left Drawing of Sign in with - Google, Facebook, Twitter, etc ...
    private RectF buttonLeftBounds;
    private Paint buttonLeftPaint;
    private Paint buttonLeftContentPaint;

    private int buttonLeftTextSize = (int) Util.getPixelFromDP(16);
    private int buttonLeftColor = Util.getColorFromResource(R.color.colorPrimaryDark);
    private int leftTextPosX;
    private String buttonLeftContent = "";

    public CustomBtnPlus(Context context)
    {
        super(context);
    }

    public CustomBtnPlus(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomBtnPlus, 0, 0);

        //If the width/height is less than the min don't use it. Use the default.
        int widthTemp = ta.getDimensionPixelSize(R.styleable.CustomBtnPlus_width, width);
        int heightTemp = ta.getDimensionPixelSize(R.styleable.CustomBtnPlus_height, height);

        width = (widthTemp > width) ? widthTemp : width;
        height = (heightTemp > height) ? heightTemp : height;

        buttonColor = ta.getColor(R.styleable.CustomBtnPlus_backgroundColor, buttonColor);
        buttonContent = ta.getString(R.styleable.CustomBtnPlus_text);

        buttonTextColor = ta.getColor(R.styleable.CustomBtnPlus_textColor, buttonTextColor);
        buttonTextSize = ta.getDimensionPixelSize(R.styleable.CustomBtnPlus_textSize,
                buttonTextSize);

        buttonPadding = ta.getDimensionPixelSize(R.styleable.CustomBtnPlus_padding,
                buttonPadding);
        buttonPaddingBottom = buttonPadding + Util.getPixelFromDP(10);

        shadowColor = ta.getColor(R.styleable.CustomBtnPlus_shadowColor, shadowColor);

        //Starting blur value and default blur vale at the same time.
        shadowRadiusBlur = ta.getDimensionPixelSize(
                R.styleable.CustomBtnPlus_shadowRadiusBlur, shadowRadiusBlur);
        shadowRadiusStart = shadowRadiusBlur;

        shadowRadiusFinal = ta.getDimensionPixelSize(
                R.styleable.CustomBtnPlus_shadowRadiusBlurSelected, shadowRadiusFinal);
        shadowOffset = ta.getDimensionPixelSize(R.styleable.CustomBtnPlus_shadowOffset,
                shadowOffset);

        buttonLeftColor = ta.getColor(R.styleable.CustomBtnPlus_leftBackground,
                buttonLeftColor);
        buttonLeftContent = ta.getString(R.styleable.CustomBtnPlus_textLeft);
        buttonLeftTextSize = ta.getDimensionPixelSize(R.styleable.CustomBtnPlus_textLeftSize,
                buttonLeftTextSize);

        //TypedArrays are heavyweight objects that should be recycled immediately
        //after all the attributes you need have been extracted.
        ta.recycle();

        initPaint();
        initGeneral();
        //Init specific values for this type of button
        init();
    }

    private void init()
    {
        buttonLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonLeftPaint.setColor(buttonLeftColor);

        buttonLeftContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonLeftContentPaint.setColor(buttonTextColor);
        buttonLeftContentPaint.setTextSize(buttonLeftTextSize);
        buttonLeftContentPaint.setTextAlign(Paint.Align.CENTER);
        buttonLeftContentPaint.setTypeface(latoBlackTypeface);

        //Center the text with respect to the drawing on the left.
        int leftButtonOffset = (int) (width * 0.20F);
        buttonLeftBounds = new RectF(buttonPadding, buttonPadding, leftButtonOffset,
                height - buttonPaddingBottom);

        textPosX = ((width + leftButtonOffset) - buttonPadding) / 2;
        textPosY = height / 2;

        leftTextPosX = (leftButtonOffset + buttonPadding) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        drawButtonWithShadow(canvas);
        drawClip(canvas);
        drawLeftBackground(canvas);
        drawSelected(canvas);
        drawOverlayCircle(canvas);
        drawButtonText(canvas);
        drawLeftLetter(canvas);
    }

    private void drawLeftBackground(Canvas canvas)
    {
        canvas.drawRect(buttonLeftBounds, buttonLeftPaint);
    }

    private void drawLeftLetter(Canvas canvas)
    {
        canvas.drawText(buttonLeftContent, leftTextPosX, textPosY, buttonLeftContentPaint);
    }
}