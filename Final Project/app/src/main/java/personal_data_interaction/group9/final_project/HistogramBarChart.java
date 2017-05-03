package personal_data_interaction.group9.final_project;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.annimon.stream.Stream;
import com.annimon.stream.function.BiFunction;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.IndexedConsumer;
import com.annimon.stream.function.Predicate;
import group9.assignment2.R;

import java.util.*;

/**
 * Created by Tjunn on 29/03/2017.
 */
public class HistogramBarChart extends View {
    private int numBars;
    private float gutterWeight = 0.2f;
    private float lineWith = 3f;
    private float textHeight = 24;
    private float textBarPad = 6;
    private float yellowFactor = 0.8f;
    //private float padForText = textHeight+textBarPad;

    private int textColor = 0xFFBDBDBD;
    private int lineColor = 0xFF8A8A8A;
    private int redColor = 0xFFFD7257;
    private int yellowColor = 0xFFFFBA66;
    private int greenColor = 0xFF8DD06B;




    private long[] values;
    private int[] colors;
    private String[] barLabels;
    private long lineValue;

    // Calculated Values
    private Paint barPaint;
    private Paint shadowPaint;
    private Paint textPaint;
    private Paint linePaint;
    private float width;
    private float height;
    private float lineHeight;
    private int numGutters;
    private RectF[] barRects;



    public HistogramBarChart(Context context, AttributeSet attrs) {
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

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWith);

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

            /*if(barTexts != null && i < barTexts.length){
                canvas.drawText(barTexts[i],bar.centerX(),bar.top - textBarPad,textPaint);
            }*/

            canvas.drawRect(bar , shadowPaint);
            if(colors != null && i < colors.length)
                barPaint.setColor(colors[i]);
            else
                barPaint.setColor(Color.BLACK);
            canvas.drawRect(bar , barPaint);

            if(barLabels != null && i < barLabels.length){
                canvas.drawText(barLabels[i],bar.centerX(),bar.bottom+textHeight,textPaint);
            }
        }

        canvas.drawLine(getPaddingLeft(),lineHeight,getPaddingLeft()+width,lineHeight,linePaint);

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
        float labelSpace = textHeight;

        float barHeight = height - labelSpace;


        float maxValue = lineValue;
        if( values != null)
            for (long value : values) maxValue = Math.max(maxValue, value);

        lineHeight = getPaddingTop() + (1-(lineValue/maxValue))* barHeight;

        barRects = new RectF[numBars];
        for(int i=0;i<numBars;i++)
        {
            float left = getPaddingLeft()+gutterWidth+i*(barWidth + gutterWidth);
            float top = getPaddingTop();
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
        }

    }

    public void setData(Stream<UsageStatsItem> data, long goal) {

        lineValue = goal;
        values = new long[numBars];
        barLabels = new String[numBars];
        colors = new int[numBars];

        data.groupBy(new Function<UsageStatsItem, Integer>() {
            @Override
            public Integer apply(UsageStatsItem usageStatsItem) {
                return usageStatsItem.getStart().get(Calendar.DAY_OF_YEAR);
            }
        }).sorted(new Comparator<Map.Entry<Integer, List<UsageStatsItem>>>() {
            @Override
            public int compare(Map.Entry<Integer, List<UsageStatsItem>> o1, Map.Entry<Integer, List<UsageStatsItem>> o2) {
                return Integer.compare(o2.getKey(),o1.getKey());
            }
        }).forEachIndexed(new IndexedConsumer<Map.Entry<Integer, List<UsageStatsItem>>>() {
            @Override
            public void accept(int index, Map.Entry<Integer, List<UsageStatsItem>> integerListEntry) {
                int i = numBars-1-index;
                values[i] = Stream.of(integerListEntry.getValue()).reduce(0L, new BiFunction<Long, UsageStatsItem, Long>() {
                    @Override
                    public Long apply(Long value1, UsageStatsItem value2) {
                        return value1 + value2.getTotalTimeInForeground();
                    }
                });
                switch (integerListEntry.getValue().get(0).getStart().get(Calendar.DAY_OF_WEEK)){
                    case Calendar.MONDAY:
                        barLabels[i] = "M";
                        break;
                    case Calendar.TUESDAY:
                        barLabels[i] = "T";
                        break;
                    case Calendar.WEDNESDAY:
                        barLabels[i] = "W";
                        break;
                    case Calendar.THURSDAY:
                        barLabels[i] = "T";
                        break;
                    case Calendar.FRIDAY:
                        barLabels[i] = "F";
                        break;
                    case Calendar.SATURDAY:
                        barLabels[i] = "S";
                        break;
                    case Calendar.SUNDAY:
                        barLabels[i] = "S";
                        break;
                }
                long yellowLimit = (long)(lineValue*yellowFactor);
                if(values[i] > lineValue){
                    colors[i] = redColor;
                } else if(values[i] > yellowLimit){
                    colors[i] = yellowColor;
                }else{
                    colors[i] = greenColor;
                }


            }
        });

        layoutBars();
        invalidate();
    }

    public void setNumBars(int numBars) {
        this.numBars = numBars;
        this.numGutters = numBars+1;
        layoutBars();
        invalidate();
    }




}
