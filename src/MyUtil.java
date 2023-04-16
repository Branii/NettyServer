

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;

public class MyUtil {

    public static final String DATE_FORMAT_YMD = "yyyyMMdd";
    public static final String TIME_FORMAT_YMD = "HH:mm:ss";
    public static final String TIME_FORMAT_NOW = "HH:mm";

    public static String getTodaysDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_YMD);
        Date today = new Date();
        return formatter.format(today);
    }

    public static String getTodaysTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT_NOW);
        Date time = new Date();
        return formatter.format(time);
    }

    public static String getRandom(int num) {
        Random random = new Random();
        int number = random.nextInt(num);
        return String.format("%05d", number);
    }

    public static String getCount(int count) {
        int number = count;
        return String.format("%04d", number);
    }

    public static String stringArray2Implode(String[] arr, String need) {
        if (arr.length == 1) return arr[0];
        StringBuilder res = new StringBuilder();
        for (String s : arr) {
            res.append(s).append(need);
        }
        return res.substring(0, res.length() - need.length());
    }

    public static String set2Implode(Set<String> set, String need)
    {
        StringBuilder res = new StringBuilder();
        for(String s : set){
            res.append(s).append(need);
        }
        return res.substring(0, res.length() - need.length());
    }
    
}
