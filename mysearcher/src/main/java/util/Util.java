package util;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Util {

    public static final String DATE_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String formatSize(long size){
        String[] formats = {"B", "KB", "MB", "GB"};
        for(int i=0; i<formats.length; i++){
            if(size < 1024)
                return String.format("%.2f"+formats[i], new BigDecimal(size));
            size /= 1024;
        }
        return String.format("%.2f"+formats[3], new BigDecimal(size));
    }

    public static String formatDate(long lastModified){
        DateFormat df = new SimpleDateFormat(DATE_STRING_FORMAT);
        return df.format(new Date(lastModified));
    }
}
