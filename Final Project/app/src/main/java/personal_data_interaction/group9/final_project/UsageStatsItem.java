package personal_data_interaction.group9.final_project;

import android.app.usage.UsageStats;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.Calendar;

/**
 * Created by Tjunn on 08/03/2017.
 */
public class UsageStatsItem {

    private Calendar end, start, lastTimeUsed;
    private String label;
    private Drawable icon;
    private long totalTimeInForeground;
    UsageStats stats;


    public UsageStatsItem(UsageStats stats, PackageManager packageManager){
        //Get Application Label
        try {
            ApplicationInfo info = packageManager.getApplicationInfo(stats.getPackageName(), PackageManager.GET_META_DATA);
            label = packageManager.getApplicationLabel(info).toString();
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        if(label == null)
            label = stats.getPackageName();

        //Get Icon
        try {
            icon = packageManager.getApplicationIcon(stats.getPackageName());
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        start = Calendar.getInstance();
        start.setTimeInMillis(stats.getFirstTimeStamp());

        end = Calendar.getInstance();
        end.setTimeInMillis(stats.getLastTimeStamp());

        lastTimeUsed = Calendar.getInstance();
        lastTimeUsed.setTimeInMillis(stats.getLastTimeUsed());

        totalTimeInForeground = stats.getTotalTimeInForeground();

        this.stats = stats;
    }



    public long getTotalTimeInForeground() {
        return totalTimeInForeground;
    }

    public String getLabel() {
        return label;
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getEnd() {
        return end;
    }

    public Calendar getLastTimeUsed(){
        return lastTimeUsed;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void combine(UsageStatsItem item) {
        if(!label.equals(item.label))
            throw new IllegalArgumentException("UsageStatsItem do not have the same Labels and cant be combined");

        if(item.start.before(start))
            start = item.start;

        if(item.end.after(end))
            end = item.end;

        if(item.lastTimeUsed.after(lastTimeUsed))
            lastTimeUsed = item.lastTimeUsed;

        totalTimeInForeground += item.totalTimeInForeground;
    }
}
