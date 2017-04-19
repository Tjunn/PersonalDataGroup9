package personal_data_interaction.group9.final_project;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.View;
import group9.assignment2.R;


import java.util.List;

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
    private long[] values;
    private int[] colors;
    private String[] texts;
    private Drawable[] images;

    private float width;
    private float height;

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BarChart, 0, 0);

        try {
            setNumBars(a.getInteger(R.styleable.BarChart_numBars, 5));
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
        textPaint.setTextAlign(Paint.Align.CENTER);

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
            RectF bar = rects[i];

            if(texts != null && i < texts.length){
                canvas.drawText(texts[i],bar.centerX(),bar.top,textPaint);
            }

            canvas.drawRect(bar , shadowPaint);
            if(colors != null && i < colors.length)
                barPaint.setColor(colors[i]);
            else
                barPaint.setColor(Color.BLACK);
            canvas.drawRect(bar , barPaint);

            if(images != null && i < images.length)
                images[i].draw(canvas);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Account for padding
        float xPad = (float)(getPaddingLeft() + getPaddingRight());
        float yPad = (float)(getPaddingTop() + getPaddingBottom());

        width = (float)w - xPad;
        height = (float)h - yPad;
        layoutBars();
    }

    private void layoutBars(){
        gutterWidth = (width*gutterWeight)/numGutters;
        barWidth = (width*(1.0f-gutterWeight))/numBars;

        barHeight = height-(barWidth/2f);

        float maxValue = 0f;
        if( values != null && values.length > 0)
            maxValue = (float)values[0];

        rects=new RectF[numBars];
        for(int i=0;i<numBars;i++)
        {
            float left = getPaddingLeft()+i*(barWidth+gutterWidth);
            float top = getPaddingTop();
            long value = 0;
            if( values != null && values.length > i)
                value = values[i];
            RectF bar = new RectF(
                    left,
                    top+(1-(value/maxValue))*barHeight,
                    left+barWidth,
                    top+barHeight
            );
            rects[i] = bar;
            if(images != null && i < images.length)
                images[i].setBounds((int)bar.left,(int)(bar.bottom-(barWidth/2f)),(int)bar.right,(int)(bar.bottom+(barWidth/2f)));
        }


    }

    public int getNumBars() {
        return numBars;
    }

    public void setNumBars(int numBars) {
        this.numBars = numBars;
        this.numGutters = numBars-1;
        layoutBars();
        invalidate();
        //requestLayout();
    }

    public void setData(List<UsageStatsItem> data) {

        values = new long[numBars];
        colors = new int[numBars];
        images = new Drawable[numBars];
        texts = new String[numBars];

        for(int i = 0; i<numBars;i++){
            UsageStatsItem item = data.get(i);
            Bitmap icon = drawableToBitmap(item.getIcon());
            Palette palette = Palette.from(icon).generate();
            colors[i] = selectColorFromPalette(palette);
            values[i] = item.getTotalTimeInForeground();
            images[i] = item.getIcon();
            texts[i] = DataManager.toHumanShortString(values[i]);
        }

        layoutBars();
        invalidate();
    }

    private int selectColorFromPalette(Palette palette) {
        return palette.getDominantColor(0xFF000000);
    }

    private static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
