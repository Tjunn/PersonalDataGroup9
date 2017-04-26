package personal_data_interaction.group9.final_project;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.View;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import group9.assignment2.R;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tjunn on 29/03/2017.
 */
public class BarChart extends View {
    private int numBars;
    private float gutterWeight = 0.2f;
    private int textColor = 0xFFBDBDBD;
    private float textHeight = 24;
    private float textBarPad = 6;
    private float padForText = textHeight+textBarPad;
    private float iconWeight = 0.9f;




    private long[] values;
    private int[] colors;
    private String[] barTexts;
    private Bitmap[] icons;

    // Calculated Values
    private Paint barPaint;
    private Paint shadowPaint;
    private Paint textPaint;
    private float width;
    private float height;
    private int numGutters;
    private RectF[] barRects;
    private Rect[] iconSrc;
    private RectF[] iconDists;


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

    // Initialize Paints used for drawing
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
            RectF bar = barRects[i];

            if(values[i] == 0)
                continue;

            if(barTexts != null && i < barTexts.length){
                canvas.drawText(barTexts[i],bar.centerX(),bar.top - textBarPad,textPaint);
            }

            canvas.drawRect(bar , shadowPaint);
            if(colors != null && i < colors.length)
                barPaint.setColor(colors[i]);
            else
                barPaint.setColor(Color.BLACK);
            canvas.drawRect(bar , barPaint);

            if(icons != null && i < icons.length) {
                Bitmap bitmap = icons[i];
                canvas.drawBitmap(bitmap,iconSrc[i],iconDists[i],null);
            }
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
        invalidate();
    }

    private void layoutBars(){
        float gutterWidth = (width * gutterWeight) / numGutters;
        float barWidth = (width * (1.0f - gutterWeight)) / numBars;
        float halfIconSize = (barWidth * iconWeight)/2f;

        float barHeight = height - halfIconSize - padForText;

        float maxValue = 0f;
        if( values != null && values.length > 0)
            maxValue = (float)values[0];

        barRects = new RectF[numBars];
        iconDists = new RectF[numBars];
        for(int i=0;i<numBars;i++)
        {
            float left = getPaddingLeft()+i*(barWidth + gutterWidth);
            float top = getPaddingTop()+ padForText;
            long value = 0;
            if( values != null && values.length > i)
                value = values[i];
            RectF bar = new RectF(
                    left,
                    top+(1-(value/maxValue))* barHeight,
                    left+ barWidth,
                    top+ barHeight
            );
            barRects[i] = bar;
            iconDists[i] = new RectF(bar.centerX()- halfIconSize,bar.bottom- halfIconSize,bar.centerX()+ halfIconSize,bar.bottom+ halfIconSize);
        }


    }

    public void setData(Stream<UsageStatsItem> data) {

        final HashMap<String,UsageStatsItem> map = new HashMap<>();
        data.forEach(new Consumer<UsageStatsItem>() {
            @Override
            public void accept(UsageStatsItem item) {
                if(!map.containsKey(item.getLabel())){
                    map.put(item.getLabel(),item);
                    return;
                }

                map.get(item.getLabel()).combine(item);
            }
        });

        List<UsageStatsItem> sorted_data = Stream.of(map.values())
                .sorted(new Comparator<UsageStatsItem>() {
                    @Override
                    public int compare(UsageStatsItem o1, UsageStatsItem o2) {
                        return Long.compare(o2.getTotalTimeInForeground(),o1.getTotalTimeInForeground());
                    }
                }).toList();

        values = new long[numBars];
        colors = new int[numBars];
        barTexts = new String[numBars];
        icons = new Bitmap[numBars];
        iconSrc = new Rect[numBars];

        for(int i = 0; i<numBars;i++){

            if(i >= sorted_data.size()) {
                colors[i] = textColor;
                values[i] = 0;
                barTexts[i] = "";
                icons[i] = null;
                iconSrc[i] = null;
                continue;
            }

            UsageStatsItem item = sorted_data.get(i);
            Bitmap icon = drawableToBitmap(item.getIcon());
            Palette palette = Palette.from(icon).generate();
            colors[i] = selectColorFromPalette(palette);
            values[i] = item.getTotalTimeInForeground();
            barTexts[i] = DataManager.toHumanShortString(values[i]);
            icons[i] = icon;
            iconSrc[i] = new Rect(0,0,icon.getWidth(),icon.getHeight());
        }

        layoutBars();
        invalidate();
    }

    public int getNumBars() {
        return numBars;
    }

    public void setNumBars(int numBars) {
        this.numBars = numBars;
        this.numGutters = numBars-1;
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
