package personal_data_interaction.group9.final_project;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import group9.assignment2.R;

/**
 * Created by Tjunn on 29/03/2017.
 */
public class BarChart extends View {
    private int numBars,numGutters;
    private float gutterWeight = 0.1f;
    private Paint textPaint;
    private int textColor = 0xFFBDBDBD;
    private float textHeight;
    private Paint barPaint;
    private Paint shadowPaint;
    private float gutterWidth,barWidth;
    private float barHeight;

    private RectF[] rects;
    private float[] values = new float[]{1.0f,0.9f,0.8f,0.7f,0.6f,0.5f,0.4f};

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BarChart, 0, 0);

        try {
            numBars = a.getInteger(R.styleable.BarChart_numBars, 5);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init(){
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        if (textHeight == 0) {
            textHeight = textPaint.getTextSize();
        } else {
            textPaint.setTextSize(textHeight);
        }

        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setTextSize(textHeight);

        shadowPaint = new Paint(0);
        shadowPaint.setColor(0xFF101010);
        shadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i=0;i<numBars;i++)
        {
            canvas.drawRect(rects[i], shadowPaint);
            barPaint.setColor(0xFFFF0000);
            canvas.drawRect(rects[i], barPaint);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Account for padding
        float xPad = (float)(getPaddingLeft() + getPaddingRight());
        float yPad = (float)(getPaddingTop() + getPaddingBottom());

        float ww = (float)w - xPad;
        float hh = (float)h - yPad;

        gutterWidth = (ww*gutterWeight)/numGutters;
        barWidth = (ww*(1.0f-gutterWeight))/numBars;

        barHeight = hh;

        rects=new RectF[numBars];
        for(int i=0;i<numBars;i++)
        {
            float left = getPaddingLeft()+i*(barWidth+gutterWidth);
            float top = getPaddingTop();
            rects[i] = new RectF(
                    left,
                    top+(1-values[i])*barHeight,
                    left+barWidth,
                    top+barHeight
            );
        }


    }

    public int getNumBars() {
        return numBars;
    }

    public void setNumBars(int numBars) {
        this.numBars = numBars;
        this.numGutters = numBars-1;
        invalidate();
        //requestLayout();
    }
}
