package io.bluephoenix.imagewall.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.util.Util;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class CustomBtn extends CustomBtnBase
{
    public CustomBtn(Context context) { super(context); }

    public CustomBtn(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        //Get all the values set in the xml file.
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomBtn, 0, 0);

        int widthTemp = ta.getDimensionPixelSize(R.styleable.CustomBtn_width, width);
        //If the height is not passed the user the default height already set.
        int heightTemp = ta.getDimensionPixelSize(R.styleable.CustomBtn_height, height);
        //Enforce a minimum height
        width = (widthTemp > width) ? widthTemp : width;
        height = (heightTemp > height) ? heightTemp : height;

        buttonColor = ta.getColor(R.styleable.CustomBtn_backgroundColor, buttonColor);
        buttonContent = ta.getString(R.styleable.CustomBtn_text);

        buttonTextColor = ta.getColor(R.styleable.CustomBtn_textColor, buttonTextColor);
        buttonTextSize = ta.getDimensionPixelSize(R.styleable.CustomBtn_textSize,
                buttonTextSize);

        buttonPadding = ta.getDimensionPixelSize(R.styleable.CustomBtn_padding,
                buttonPadding);
        buttonPaddingBottom = buttonPadding + Util.getPixelFromDP(10);

        shadowColor = ta.getColor(R.styleable.CustomBtn_shadowColor, shadowColor);

        //Starting blur value and default blur vale at the same time.
        shadowRadiusBlur = ta.getDimensionPixelSize(
                R.styleable.CustomBtn_shadowRadiusBlur, shadowRadiusBlur);
        shadowRadiusStart = shadowRadiusBlur;

        shadowRadiusFinal = ta.getDimensionPixelSize(
                R.styleable.CustomBtn_shadowRadiusBlurSelected, shadowRadiusFinal);
        shadowOffset = ta.getDimensionPixelSize(R.styleable.CustomBtn_shadowOffset,
                shadowOffset);

        //TypedArrays are heavyweight objects that should be recycled immediately
        //after all the attributes you need have been extracted.
        ta.recycle();

        initPaint();
        initGeneral();
        //Specific view init
        initPosition();
    }

    private void initPosition()
    {
        textPosX = width / 2;
        textPosY = height / 2;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        drawButtonWithShadow(canvas);
        drawClip(canvas);
        drawSelected(canvas);
        drawOverlayCircle(canvas);
        drawButtonText(canvas);
    }
}