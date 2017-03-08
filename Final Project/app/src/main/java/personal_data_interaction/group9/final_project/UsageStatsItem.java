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
    private UsageStats stats;
    private CharSequence label;
    private Drawable icon;

    public UsageStatsItem(UsageStats stats, PackageManager packageManager){
        this.stats = stats;

        //Get Application Label
        try {
            ApplicationInfo info = packageManager.getApplicationInfo(stats.getPackageName(), PackageManager.GET_META_DATA);
            label = packageManager.getApplicationLabel(info);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        if(label == null)
            label = stats.getPackageName();

        //Get Icon
        try {
            icon = packageManager.getApplicationIcon(stats.getPackageName());
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        if(icon == null)
            icon = packageManager.getDefaultActivityIcon();

        start = Calendar.getInstance();
        start.setTimeInMillis(stats.getFirstTimeStamp());

        end = Calendar.getInstance();
        end.setTimeInMillis(stats.getLastTimeStamp());

        lastTimeUsed = Calendar.getInstance();
        lastTimeUsed.setTimeInMillis(stats.getLastTimeUsed());
    }



    public long getTotalTimeInForeground() {
        return stats.getTotalTimeInForeground();
    }

    public CharSequence getLabel() {
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
}
