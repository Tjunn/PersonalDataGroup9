package personal_data_interaction.group9.final_project;

import android.app.usage.UsageStats;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by Tjunn on 29/03/2017.
 */
public class GranularUsageStatsSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "granular";

    private static final String OBSERVATION_TABLE_NAME = "observation";
    private static final String OBSERVATION_ID = "id";
    private static final String START = "start";
    private static final String END = "end";
    private static final String TIME = "time";
    private static final String PACKAGE = "package";
    private static final String LAST_USED = "last";

    /*private static final String ENTRY_TABLE_NAME = "entries";
    private static final String ENTRY_ID = "id";
    private static final String OBSERVATION = "observation";*/


    private static final String OBSERVATION_TABLE_CREATE =
            "CREATE TABLE " + OBSERVATION_TABLE_NAME + " (" +
                    OBSERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    START + " INTEGER NOT NULL, " +
                    END + " INTEGER NOT NULL, " +
                    LAST_USED + " INTEGER NOT NULL, " +
                    TIME + " INTEGER NOT NULL, " +
                    PACKAGE + " TEXT NOT NULL);";

    /*private static final String ENTRY_TABLE_CREATE =
            "CREATE TABLE " + ENTRY_TABLE_NAME + " (" +
                    ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    OBSERVATION + " INTEGER NOT NULL REFERENCES " + OBSERVATION_TABLE_NAME + "(" + OBSERVATION_ID + ") ON DELETE CASCADE, " +
                    TIME + " INTEGER NOT NULL, " +
                    PACKAGE + " TEXT NOT NULL);";*/

     //"DROP TABLE IF EXISTS " + ENTRY_TABLE_NAME + ";" +
    
    private static final String DELETE_DB = "DROP TABLE IF EXISTS " + OBSERVATION_TABLE_NAME + ";";


    GranularUsageStatsSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    void storeStats(List<UsageStats> stats){

        if(stats.size() < 1)
            return;

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            for(UsageStats stat : stats)
            {
                ContentValues values = new ContentValues();
                values.put(START,stat.getFirstTimeStamp());
                values.put(END,stat.getLastTimeStamp());
                values.put(LAST_USED,stat.getLastTimeUsed());
                values.put(PACKAGE,stat.getPackageName());
                values.put(TIME,stat.getTotalTimeInForeground());
                db.insert(OBSERVATION_TABLE_NAME,null,values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(OBSERVATION_TABLE_CREATE);
        //db.execSQL(ENTRY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_DB);
        onCreate(db);
    }
}
