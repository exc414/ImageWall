package io.bluephoenix.imagewall.ui.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
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
public abstract class CustomBtnBase extends View
{
    //Get dimensions of the screen
    int width = Util.getPixelFromDP(114); //Default value
    int height = Util.getPixelFromDP(64); //Min value

    //Rounded rectangle radius X and Y
    float roundedX = Util.getPixelFromDP(4);
    float roundedY = Util.getPixelFromDP(4);

    //Button
    private Paint buttonWithShadow;
    int buttonColor = Util.getColorFromResource(R.color.colorPrimary);
    int shadowColor = Util.getColorFromResource(R.color.colorPrimaryShadow);
    int buttonPadding = Util.getPixelFromDP(6);
    int buttonPaddingBottom = Util.getPixelFromDP(16);
    private RectF buttonBounds;

    //Button Content Clip
    Path contentPath = new Path();

    //Button Text
    String buttonContent = "";
    Paint buttonContentPaint;
    int buttonTextSize = (int) Util.getPixelFromDP(12);
    int buttonTextColor = Util.getColorFromResource(R.color.ghostWhite);
    float textPosX = 0; //Could change in other buttons
    float textPosY = 0;

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
    int shadowRadiusBlur = Util.getPixelFromDP(1);
    int shadowRadiusStart = shadowRadiusBlur;
    int shadowRadiusFinal = Util.getPixelFromDP(3);
    int shadowOffset = Util.getPixelFromDP(2);

    //Selected values
    private ValueAnimator selectedAnimator;
    private Paint selectedPaint;

    //Typeface
    Typeface latoBoldTypeface;
    Typeface latoBlackTypeface;

    //On Long Click circle animation
    private boolean longClickBool = false;
    private boolean circleAnimRan = false;
    private final Handler handler = new Handler();

    public CustomBtnBase(Context context)
    {
        super(context);
        initTypeFace(context);
    }

    public CustomBtnBase(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initTypeFace(context);
    }

    void initTypeFace(Context context)
    {
        //Set custom fonts
        String PATH_TO_LATO_BOLD_FONT = "fonts/Lato-Bold.ttf";
        latoBoldTypeface = Typeface.createFromAsset(context.getAssets(),
                PATH_TO_LATO_BOLD_FONT);

        String PATH_TO_LATO_BLACK_FONT = "fonts/Lato-Black.ttf";
        latoBlackTypeface = Typeface.createFromAsset(context.getAssets(),
                PATH_TO_LATO_BLACK_FONT);
    }

    void initPaint()
    {
        //Turn on software instead of hardware acceleration. If not done the shadow wont draw.
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        buttonWithShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonWithShadow.setColor(buttonColor);

        //Radius, DX, DY, Color
        buttonWithShadow.setShadowLayer(shadowRadiusBlur, 0, shadowOffset, shadowColor);

        buttonContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonContentPaint.setColor(buttonTextColor);
        buttonContentPaint.setTextSize(buttonTextSize);
        buttonContentPaint.setTextAlign(Paint.Align.CENTER);
        buttonContentPaint.setTypeface(latoBoldTypeface);

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

    void initGeneral()
    {
        //Top, Left, Right, Bottom
        buttonBounds = new RectF(buttonPadding, buttonPadding, width - buttonPadding,
                height - buttonPaddingBottom);

        //Set content path clip
        contentPath.addRoundRect(buttonBounds, roundedX, roundedY, Path.Direction.CW);

        //Init circle position
        circlePosX = width / 2;
        circlePosY = height / 2;
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

    int measureWidth(int measureSpec)
    {
        return resolveSizeAndState(width, measureSpec, 0);
    }

    int measureHeight(int measureSpec)
    {
        return resolveSizeAndState(height, measureSpec, 0);
    }

    void drawButtonWithShadow(Canvas canvas)
    {
        buttonWithShadow.setShadowLayer(shadowRadiusBlur, 0, shadowOffset, shadowColor);
        canvas.drawRoundRect(buttonBounds, roundedX, roundedY, buttonWithShadow);
    }

    void drawClip(Canvas canvas)
    {
        canvas.clipPath(contentPath, Region.Op.INTERSECT);
    }

    void drawButtonText(Canvas canvas)
    {
        canvas.drawText(buttonContent, textPosX, textPosY, buttonContentPaint);
    }

    void drawOverlayCircle(Canvas canvas)
    {
        //X, Y, Radius, Paint
        canvas.drawCircle(circlePosX, circlePosY, circleRadius, circlePaint);
    }

    void drawSelected(Canvas canvas)
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
     * If the user keeps pushing the button (this view) then start a circle expanding
     * animation.
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
     * performClick and calls it when clicks are detected, the view may not handle
     * accessibility actions properly. Logic handling the click actions should ideally
     * be placed in View#performClick as some accessibility services invoke performClick
     * when a click action should occur.
     * @return a boolean detonating whether a click was performed.
     */
    @Override
    public boolean performClick()
    {
        super.performClick();
        return true;
    }
}
