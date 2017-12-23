package io.bluephoenix.imagewall.ui.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.util.AnimListener;
import io.bluephoenix.imagewall.util.Util;

/**
 * @author Carlos A. Perez
 */
public class CustomBtn extends View
{
    //Get dimensions of the screen
    private int width = Util.getPixelFromDP(114); //Default value
    private int height = Util.getPixelFromDP(64); //Min value

    //Rounded rectangle radius X and Y
    private float roundedX = Util.getPixelFromDP(4);
    private float roundedY = Util.getPixelFromDP(4);

    //Button
    private Paint buttonWithShadow;
    private int buttonColor = Util.getColorFromResource(R.color.colorPrimary);
    private int shadowColor = Util.getColorFromResource(R.color.colorPrimaryShadow);
    private int buttonPadding = Util.getPixelFromDP(6);
    private int buttonPaddingBottom = Util.getPixelFromDP(16);
    private RectF buttonBounds;

    //Button Content Clip
    Path contentPath = new Path();

    //Button Text
    private String buttonContent = "LOGIN";
    private Paint buttonContentPaint;
    private int buttonTextSize = (int) Util.getPixelFromDP(12);
    private int buttonTextColor = Util.getColorFromResource(R.color.ghostWhite);
    private float textPosX = 0;
    private float textPosY = 0;

    //Animation - Expand circle
    private ValueAnimator circleScaleLongAnimator;

    //Circle Values
    private Paint circlePaint;
    private final int circleColor = Util.getColorFromResource(R.color.white);
    private float circlePosX = 0;
    private float circlePosY = 0;
    private float circleRadius = 0;
    private int baseAlpha = 40;

    //Circle fade
    private ValueAnimator circleFade;

    //Shadow. Final Radius means that the button is selected.
    private int shadowRadiusBlur = Util.getPixelFromDP(1);
    private int shadowRadiusStart = shadowRadiusBlur;
    private int shadowRadiusFinal = Util.getPixelFromDP(3);
    private int shadowOffset = Util.getPixelFromDP(2);

    //Selected values
    ValueAnimator selectedAnimator;
    private Paint selectedPaint;

    //On Long Click circle animation
    private boolean longClickBool = false;
    private boolean circleAnimRan = false;
    private final Handler handler = new Handler();

    public CustomBtn(Context context)
    {
        super(context);
    }

    public CustomBtn(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        //Get all the values set in the xml file.
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomBtn, 0, 0);

        //If the height is not passed the user the default height already set.
        int heightTemp = ta.getDimensionPixelSize(R.styleable.CustomBtn_height, height);
        //Enforce a minimum height
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

        //Turn on software instead of hardware acceleration. If not done the shadow wont draw.
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //TypedArrays are heavyweight objects that should be recycled immediately
        //after all the attributes you need have been extracted.
        ta.recycle();

        //Init
        initPaint();
        initPosition();
    }

    private void initPaint()
    {
        buttonWithShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonWithShadow.setColor(buttonColor);

        //Radius, DX, DY, Color
        buttonWithShadow.setShadowLayer(shadowRadiusBlur, 0, shadowOffset, shadowColor);

        buttonContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonContentPaint.setColor(buttonTextColor);
        buttonContentPaint.setTextSize(buttonTextSize);
        buttonContentPaint.setTextAlign(Paint.Align.CENTER);
        buttonContentPaint.setTypeface(Typeface.DEFAULT_BOLD);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);
        circlePaint.setAlpha(baseAlpha);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);
        circlePaint.setAlpha(baseAlpha);


        selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setColor(circleColor);
        selectedPaint.setAlpha(0); //Invisible
    }

    private void initPosition()
    {
        //Top, Left, Right, Bottom
        buttonBounds = new RectF(buttonPadding, buttonPadding, width - buttonPadding,
                height - buttonPaddingBottom);

        //Set content path clip
        contentPath.addRoundRect(buttonBounds, roundedX, roundedY, Path.Direction.CW);

        textPosX = width / 2;
        textPosY = (height / 2);

        circlePosX = textPosX;
        circlePosY = textPosY;
    }

    /**
     * Called to determine the size requirements for this view and all of its children.
     * @param widthMeasureSpec  width for the view.
     * @param heightMeasureSpec height for the view.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec)
    {
        return resolveSizeAndState(width, measureSpec, 0);
    }

    private int measureHeight(int measureSpec)
    {
        return resolveSizeAndState(height, measureSpec, 0);
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

    private void drawButtonWithShadow(Canvas canvas)
    {
        buttonWithShadow.setShadowLayer(shadowRadiusBlur, 0, shadowOffset, shadowColor);
        canvas.drawRoundRect(buttonBounds, roundedX, roundedY, buttonWithShadow);
    }

    private void drawClip(Canvas canvas)
    {
        canvas.clipPath(contentPath, Region.Op.INTERSECT);
    }

    private void drawButtonText(Canvas canvas)
    {
        canvas.drawText(buttonContent, textPosX, textPosY, buttonContentPaint);
    }

    private void drawOverlayCircle(Canvas canvas)
    {
        //X, Y, Radius, Paint
        canvas.drawCircle(circlePosX, circlePosY, circleRadius, circlePaint);
    }

    private void drawSelected(Canvas canvas)
    {
        canvas.drawRoundRect(buttonBounds, roundedX, roundedY, selectedPaint);
    }

    /**
     * A quick fade in/out animation for normal clicks.
     * @param startValue an int with the starting alpha value of the paint object.
     * @param finalValue an int with the ending alpha value of the paint object.
     */
    private void selectedAnimation(final int startValue, final int finalValue)
    {
        selectedAnimator = ValueAnimator.ofInt(startValue , finalValue);
        selectedAnimator.setDuration(300);

        selectedAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                int alphaValue = Math.round((int) animation.getAnimatedValue());
                selectedPaint.setAlpha((alphaValue < 0) ? 0 : alphaValue);
                postInvalidate();
            }
        }); selectedAnimator.start();

        selectedAnimator.addListener(new AnimListener()
        {
            @Override
            protected void animationStarted(Animator animation) { }

            @Override
            protected void animationEnded(Animator animation)
            {
                //When start is less than final it means that a fade in was done.
                //Therefore call the method again and do a fade out.
                if(startValue < finalValue) { selectedAnimation(baseAlpha, 0); }
            }
        });
    }

    /**
     * Animate the button's shadow on long press.
     *
     * @param startValue a float with the shadow's starting offset.
     * @param finalValue a float with the shadow's ending offset.
     */
    private void shadowLongAnimation(final float startValue, final float finalValue)
    {
        ValueAnimator shadowBottomExpand = ValueAnimator.ofFloat(startValue, finalValue);
        shadowBottomExpand.setDuration(200);

        shadowBottomExpand.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                shadowRadiusBlur = Math.round((float) animation.getAnimatedValue());
                postInvalidate();
            }
        }); shadowBottomExpand.start();
    }

    /**
     * Expanding circle if the user hold presses the button. Long click.
     */
    private void circleLongAnimation()
    {
        circleScaleLongAnimator = ValueAnimator.ofInt(0 , width);
        circleScaleLongAnimator.setDuration(400);

        circleFade = ValueAnimator.ofInt(baseAlpha, 0);
        circleFade.setDuration(200);

        circleScaleLongAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                circleRadius = Math.round((int) animation.getAnimatedValue());
                postInvalidate();
            }
        });

        //If there is a fade going on cancel it. Since a new circle anim is going to start.
        circleFade.cancel();
        circleScaleLongAnimator.start();

        circleFade.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                //Once the alpha value is below zero, just keep setting zero.
                //If it goes into the negative the alpha will go to 255 again.
                int alphaValue = Math.round((int) animation.getAnimatedValue());
                circlePaint.setAlpha((alphaValue < 0) ? 0 : alphaValue);
                postInvalidate();
            }
        });

        circleFade.addListener(new AnimListener()
        {
            //Start the shadow animation
            @Override
            protected void animationStarted(Animator animation)
            {
                shadowLongAnimation(shadowRadiusFinal, shadowRadiusStart);
            }

            @Override
            protected void animationEnded(Animator animation)
            {
                //Once the fade animation is finished reset the circle's value back to
                //their defaults.
                circleRadius = 0;
                circlePosX = textPosX;
                circlePosY = textPosY;
                circlePaint.setAlpha(baseAlpha);
            }
        });
    }

    /**
     * When the user touches the button (this view) then trigger and animation and an
     * action.
     *
     * @param motionEvent An object which contains information about a user interaction.
     * @return A boolean detonating where this event was consumed or not.
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        //User first presses
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            circlePosX = motionEvent.getX();
            circlePosY = motionEvent.getY();

            //Opacity - first param starting value, second value ending opacity.
            //This does a quick fade in/out animation.
            selectedAnimation(0, baseAlpha);

            //Consider 250 as the start of a long click
            handler.postDelayed(longClick, 250);
            longClickBool = true;
        }

        //Users lets go. Does not trigger if the user keeps their finger pressed.
        if(motionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            //If the circle animator is not null this means that a circle animation might
            //be going on. Cancel and start a fade. Since the user is no longer pressing
            //the button.
            if(longClickBool)
            {
                longClickBool = false;
                handler.removeCallbacks(longClick);

                //Cancel any running, pending animations if circle animation ran.
                if(circleAnimRan)
                {
                    selectedAnimator.cancel();
                    circleScaleLongAnimator.cancel();
                    circleFade.start();
                    circleAnimRan = false;
                }
            }
        }

        performClick();
        return true;
    }


    /**
     * If the user keeps pushing the button (this view) then start a circle expanding animation.
     * This is done to mimic the way the android buttons behave in lollipop+.
     */
    private final Runnable longClick = new Runnable()
    {
        @Override
        public void run()
        {
            if(longClickBool)
            {
                circleAnimRan = true;
                circleLongAnimation();
                //First param - starting shadow position. second - ending shadow position.
                shadowLongAnimation(shadowRadiusStart, shadowRadiusFinal);
            }
        }
    };

    /**
     * If a view that overrides onTouchEvent or uses an OnTouchListener does not also implement
     * performClick and calls it when clicks are detected, the view may not handle accessibility
     * actions properly. Logic handling the click actions should ideally be placed in
     * View#performClick as some accessibility services invoke performClick when
     * a click action should occur.
     * @return a boolean detonating whether a click was performed.
     */
    @Override
    public boolean performClick()
    {
        super.performClick();
        return true;
    }
}