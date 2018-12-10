package com.vily.audiodemo2;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {
    /**
     * 获取年月日
     *
     * @param time
     * @return 14/12/25
     */
    public static String getYearTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        int year = calendar.get(Calendar.YEAR);
        return year % 100 + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"
                + calendar.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 获取年月日
     *
     * @param time
     * @return 2014年12月25日
     */
    public static String getSpYearTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        int year = calendar.get(Calendar.YEAR);
        return year + "年" + (calendar.get(Calendar.MONTH) + 1) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH) + "日";
    }

    public static String getMonthTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        return format.format(new Date(time));
    }




    // 获取当前月： 12月
    public static String getMonth() {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = format.format(curDate);
        return str;
    }
    // 获取当前年： 12月
    public static String getYear() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = format.format(curDate);
        return str;
    }
    // 获取当前日： 12月
    public static String getDay() {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = format.format(curDate);
        return str;
    }



    // 获取当前月： 12月
    public static int getIntMonth() {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = format.format(curDate);
        return Integer.parseInt(str);
    }
    // 获取当前年： 12月
    public static int getIntYear() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = format.format(curDate);
        return Integer.parseInt(str);
    }
    // 获取当前日： 12月
    public static int getIntDay() {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = format.format(curDate);
        return Integer.parseInt(str);
    }

    // 获取上个月的月份 11月
    public static String getLastMonthOnly(){
        SimpleDateFormat formatter =new SimpleDateFormat ("MM");
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)-1);
        Date date=curr.getTime();
        String str = formatter.format(date);
        return str;
    }
    // 获取上上个月的月份 10月
    public static String getLastMonthTwo(){
        SimpleDateFormat formatter =new SimpleDateFormat ("MM");
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)-2);
        Date date=curr.getTime();
        String str = formatter.format(date);
        return str;
    }
    /**
     * 获取小时和分钟
     *
     * @param time
     * @return
     */
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }


    // 获取秒
    public static String getSecond(long time) {
        SimpleDateFormat format = new SimpleDateFormat("ss");
        return format.format(new Date(time));
    }



    // 获取秒
    public static String getMinAndSecond(long time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(new Date(time));
    }

    /**
     * 获取小时和分钟
     *
     * @param time
     * @return
     */
    public static String getHourAndMinAndSen(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date(time));
    }
    public static String getLongToData(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm:ss");
        return format.format(new Date(time));
    }



    /**
     * 获取首页的时间展示
     *
     * @param time
     * @return
     */
    public static String getChatTime(long time) {
        String result = "";
        Date year = new Date(System.currentTimeMillis());
        Date otherYear = new Date(time);
        @SuppressWarnings("deprecation")
        int yearTemp = year.getYear() - otherYear.getYear();
        if (yearTemp > 0) {
            // 其他年
            result = getYearTime(time);
        } else {
            // 今年
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            Date today = new Date(System.currentTimeMillis());
            Date otherDay = new Date(time);
            int temp = Integer.parseInt(dayFormat.format(today))
                    - Integer.parseInt(dayFormat.format(otherDay));
            switch (temp) {
                case 0:
                    // 今天
                    result = getHourAndMin(time);
                    break;
                case 1:
                    // 昨天
                    result = "昨天";
                    break;
                default:
                    result = getMonthTime(time);
                    break;
            }
        }
        return result;
    }

    /**
     * 获取通用的时间展示 今天显示:HH:mm 昨天显示:昨天 HH:mm 其他展示MM月dd日 其他年展示：XX年XX月XX日
     *
     * @param string
     * @return
     */
    public static String getCommonTime(String string) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        long time = -1;
        try {
            Date date = df.parse(string);
            time = date.getTime();
        } catch (ParseException ex) {
            return "";
        }

        String result = "";
        Date year = new Date(System.currentTimeMillis());
        Date otherYear = new Date(time);
        @SuppressWarnings("deprecation")
        int yearTemp = year.getYear() - otherYear.getYear();
        if (yearTemp > 0) {
            // 其他年
            result = getSpYearTime(time);
        } else {
            // 今年
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            Date today = new Date(System.currentTimeMillis());
            Date otherDay = new Date(time);
            int temp = Integer.parseInt(dayFormat.format(today))
                    - Integer.parseInt(dayFormat.format(otherDay));
            switch (temp) {
                case 0:
                    // 今天
                    result = getHourAndMin(time);
                    break;
                case 1:
                    // 昨天
                    result = "昨天 " + getHourAndMin(time);
                    ;
                    break;
                default:
                    result = getMonthTime(time) + getHourAndMin(time);
                    ;
                    break;
            }
        }
        return result;
    }

    public static int[] getHourMinutesSeconds(long time) {
        int[] times = new int[3];

        int hour = (int) (time / 3600);
        times[0] = hour;
        int minutes = (int) ((time - hour * 3600) / 60);
        times[1] = minutes;
        int seconds = (int) (time - hour * 3600 - minutes * 60);
        times[2] = seconds;
        return times;
    }

    /**
     * 2015-11-26 16:27:14 转为：11-26 16:27
     * 如果是去年则会显示年份
     *
     * @param serverTime 2015-11-26 16:27:14
     * @return 11-26 16:27  或者2014-11-26 16:27
     */
    public static String formatServerTime(String serverTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        StringBuilder sb = new StringBuilder();
        try {
            long time = df.parse(serverTime).getTime();
            Calendar ca = Calendar.getInstance();
            int curYear = ca.get(Calendar.YEAR);
            ca.setTimeInMillis(time);
            int itemPubYear = ca.get(Calendar.YEAR);
            if (curYear != itemPubYear) {  //不是今年
//                sb.append(DateUtil.formatTime(time, "yyyy-MM-dd")).append(" ").append(DateUtil.formatTime(time, "HH:mm"));
                return sb.toString();
            } else {   //是今年
//                sb.append(DateUtil.formatTime(time, "MM-dd")).append(" ").append(DateUtil.formatTime(time, "HH:mm"));
                return sb.toString();
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @return  获取当前手机的时间
     */
    public static String getCurrentTime(){
        SimpleDateFormat formatter =new SimpleDateFormat ("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());

        //获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     *
     * @return 获取一周前的日期
     */
    public static String getLastWeek(){
        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DAY_OF_MONTH,curr.get(Calendar.DAY_OF_MONTH)-7);
        Date date=curr.getTime();
        String str = formatter.format(date);
        return str;
    }

    /**
     *
     * @return 获取一个月前的日期
     */
    public static String getLastMonth(){
        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Calendar curr = Calendar.getInstance();
//        curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)-1);
        curr.set(Calendar.DAY_OF_MONTH,curr.get(Calendar.DAY_OF_MONTH)-30);
        Date date=curr.getTime();
        String str = formatter.format(date);
        return str;
    }

    /**
     *
     * @return   获取最近3天
     */
    public static String getLastThree() {

        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Calendar curr = Calendar.getInstance();
//        curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)-1);
        curr.set(Calendar.DAY_OF_MONTH,curr.get(Calendar.DAY_OF_MONTH)-3);
        Date date=curr.getTime();
        String str = formatter.format(date);
        return str;
    }
    /**
     *
     * @return 获取当前日期
     */
    public static String getCurrentData(){
        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = formatter.format(curDate);

        return str;
    }
    /**
     *
     * @return 获取当前日期
     */
    public static String getCurrentPData(){
        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = formatter.format(curDate);

        return str;
    }


    /**
     *
     * @return  当天的0点
     */
    public static String getCurrZero(){
        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date=cal.getTime();
        String str = formatter.format(date);
        return str;
    }

    /**
     *
     * @return  当月的第一天
     */
    public static String getMonZero(){
        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date=cal.getTime();
        String str = formatter.format(date);
        return str;
    }
    /**
     *
     * @return  上个月的第一天
     */
    public static String getLastMonthFirst(){
        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date=cal.getTime();
        String str = formatter.format(date);
        return str;
    }

    /**
     *
     * @return  上个月最后一天
     */
    public static String getLastMonthLast(){
        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-1);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date=cal.getTime();
        String str = formatter.format(date);
        return str;
    }

    /**
     *
     * @return  上上个月第一天
     */
    public static String getForMonthFirst(){
        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-2);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date=cal.getTime();
        String str = formatter.format(date);
        return str;
    }
    /**
     *
     * @return  上上个月最后一天
     */
    public static String getForMonthLast(){
        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-1);
        cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)-cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date=cal.getTime();
        String str = formatter.format(date);
        return str;
    }
    /**
     *
     * @return  半年前的一天
     */
    public static String getHalfYearData(){
        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-6);
        Date date=cal.getTime();
        String str = formatter.format(date);
        return str;
    }
    /**
     *
     * @return  一年前的一天
     */
    public static String getLastYearData(){
        SimpleDateFormat formatter =new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1);
        Date date=cal.getTime();
        String str = formatter.format(date);
        return str;
    }
    /**
     * 将 2018-09-22 11:33:44  分隔出来
     * @param time   2018-09-22 11:33:44 格式的时间传进来
     * @param index  选取 年月日
     * @param index1 选取 时分秒
     * @return
     */
    public static String getTime(String time,int index,int index1){

        if(!TextUtils.isEmpty(time) && time.contains("-") && time.contains(" ") ){
            String[] split = time.split(" ");



            switch (index){
                case 0 :
                    String calendar = split[0];
                    if(!TextUtils.isEmpty(calendar) && calendar.contains("-")){
                        String[] split1 = calendar.split("-");
                        return split1[index1];
                    }
                    break;
                case 1 :
                    String time2 = split[1];
                    if(!TextUtils.isEmpty(time2) && time2.contains(":")){
                        String[] split2 = time2.split(":");
                        return split2[index1];
                    }
                    break;
                case 2 :
                    String time3 = split[1];
                    if(!TextUtils.isEmpty(time3) ){
                        return time3;
                    }
                    break;


            }
        }
        return null;
    }

    // 2018-20-33
    public static int getYear(String time){

        if(time.contains("-")){
            String[] split = time.split("-");
            return Integer.parseInt(split[0]);

        }

        return 0;
    }
    public static int getMonth(String time){

        if(time.contains("-")){
            String[] split = time.split("-");
            return Integer.parseInt(split[1]);

        }

        return 0;
    }
    public static int getDay(String time){

        if(time.contains("-")){
            String[] split = time.split("-");
            return Integer.parseInt(split[2]);

        }

        return 0;
    }
    public static String getYearMonthDay(String time){

        if(time.contains(" ")){
            String[] split = time.split(" ");

            return split[0];

        }

        return null;
    }

}
