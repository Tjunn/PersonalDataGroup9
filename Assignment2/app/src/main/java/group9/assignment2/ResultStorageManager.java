package group9.assignment2;

import android.content.Context;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ResultStorageManager {

    //private static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    //private static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final String FILENAME = "assignment_2_data.csv";
    private static List<ResultItem> items = null;



    public static void addResult(Context context, long result){

        ResultItem item = new ResultItem(result);
        if(items != null)
            items.add(item);

        FileOutputStream fos;
        try {
            fos = context.openFileOutput(FILENAME, Context.MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        OutputStreamWriter osw = new OutputStreamWriter(fos,Charset.forName("UTF-8"));
        BufferedWriter bw = new BufferedWriter(osw);

        try {
            bw.write(item.toString());
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static List<ResultItem> getItems(Context context) {
        if(items == null){
            items = new ArrayList<>();
            FileInputStream fis;
            try {
                fis = context.openFileInput(FILENAME);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
                return new ArrayList<>(0);
            }

            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            try {
                while (true){
                    String line = br.readLine();
                    if(line==null)
                        break;
                    else
                        items.add(new ResultItem(line));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Collections.reverse(items);
        return items;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ResultItem {

        final long time;
        final long result;

        public ResultItem(long result) {
            this.result = result;

            Calendar c = Calendar.getInstance();
            this.time = c.getTimeInMillis();
        }

        ResultItem(String line) {
            String[] fields = line.split(",");
            time = Long.parseLong(fields[0]);
            result =  Long.parseLong(fields[1]);
        }

        @Override
        public String toString() {
            return time + "," + result ;
        }
    }
}
