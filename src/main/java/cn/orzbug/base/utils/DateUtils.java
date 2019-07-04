package cn.orzbug.base.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: SQJ
 * @data: 2018/4/9 13:38
 * @version:
 */
public class DateUtils {

    public static String getTheDate(){
        //以图片类别+日期规划图片文件夹层级
        Calendar date = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(date.getTime());
        return time;
    }



    public static Date getDateFromTimestamp(String timestamp){
        long time  = Long.parseLong(timestamp);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return date;
    }



    public static void main(String[] args) {
        System.out.println(getTheDate());
    }
}
