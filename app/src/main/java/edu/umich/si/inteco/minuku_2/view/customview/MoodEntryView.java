package edu.umich.si.inteco.minuku_2.view.customview;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import edu.umich.si.inteco.minuku_2.R;

public class MoodEntryView extends View
{
    Float X1 = -50.0f, Y1 = -50.0f;
    Float X2 = -50.0f, Y2 = -50.0f;
    int viewWidth = 0;
    Bitmap background, tapCircleBlue;
    float rectX1 = 0, rectY1 = 0, rectX2 = 0, rectY2 = 0;
    Paint p, p1;
    boolean isBackgroundCroped = false;
    float scale;
    Context mContext = null;

    int colorCode = 0;

    public MoodEntryView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.graph_new);

        tapCircleBlue = BitmapFactory.decodeResource(context.getResources(), getColorId());

        scale = context.getResources().getDisplayMetrics().density;
        Float t = 25 * scale;
        tapCircleBlue = Bitmap.createScaledBitmap(tapCircleBlue, t.intValue(), t.intValue(), true);

        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.BLUE);

        p1 = new Paint();
        p1.setColor(Color.parseColor("#4c6385"));
    }

    private int getColorId()
    {
        Log.v("getColor id ", "************" + colorCode);
        switch (colorCode) {
            case 0:

                return R.drawable.blue_circle;

            case 1:
                Log.v("getColor id ", "************ RedCircle");
                return R.drawable.red_circle;

            case 2:
                return R.drawable.blue_circle;

            case 3:
                return R.drawable.pink_circle;

            case 4:
                return R.drawable.yellow_circle;

            default:
                return R.drawable.blue_circle;
        }

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (!isBackgroundCroped)
        {
            viewWidth = this.getWidth();
            background = Bitmap.createScaledBitmap(background, viewWidth, viewWidth, true);
            isBackgroundCroped = true;
        }
        canvas.drawColor(Color.parseColor("#b5c2d4"));
        canvas.drawBitmap(background, 0, 0, null);
        drawMoods(canvas);
        super.onDraw(canvas);
    }

    private void drawMoods(Canvas canvas)
    {
        canvas.drawBitmap(tapCircleBlue, X1.intValue(), Y1.intValue(), null);
        //canvas.drawBitmap(tapCircleBlue, X2.intValue(), Y2.intValue(), null);
        // canvas.drawCircle(X2.intValue(), Y2.intValue(), 12, p) ;

        canvas.drawRect(rectX1, rectY1, rectX2, rectY2, p1);
    }

    private void updateFirstMoodValues()
    {
        invalidate();
    }

    public void setFirstMood(float f, float g)
    {
        X1 = f;
        Y1 = g;
        updateFirstMoodValues();
    }

    public void setRect(float x1, float x2, float y1, float y2)
    {
        rectX1 = x1;
        rectX2 = x2;
        rectY1 = y1;
        rectY2 = y2;
    }

    public void setPointsColor(int cCode) {
        Log.v("setPointsColor ", "************" + cCode);
        this.colorCode = cCode;
        tapCircleBlue = BitmapFactory.decodeResource(mContext.getResources(), getColorId());
        scale = mContext.getResources().getDisplayMetrics().density;
        Float t = 25 * scale;
        tapCircleBlue = Bitmap.createScaledBitmap(tapCircleBlue, t.intValue(), t.intValue(), true);
    }
}
