package lib.core.utils;

import android.text.TextUtils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 */
public class ExDateUtil {

	private ExDateUtil() {}

	private static class ExDateUtilHolder {
		private static final ExDateUtil edu = new ExDateUtil();
	}

	public static final ExDateUtil getInstance() {
		return ExDateUtilHolder.edu;
	}

	public final String dateFormat(Date date, String strFormat) {
		SimpleDateFormat format = new SimpleDateFormat(strFormat);//yyyy-MM-dd
		return format.format(date);
	}
	
	public final long getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}
	
	public final String getCalendar(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		String str = MessageFormat.format("{0}月{1}日 {2}:{3}", month, day, hour, minute > 9 ? minute : TextUtils.concat("0", String.valueOf(minute)));
		return str;
	}
	
	public final String getWeekday(String date) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdw = new SimpleDateFormat("E");
		Date d = null;
		try {
			d = sd.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdw.format(d);
	}
	
	public final long getFirstDayOfDateInMonth(long date){
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(new Date(date));
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH,1);
		return cal.getTimeInMillis();
	}
	
	public final long getLastDayOfDateInMonth(long date){
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(new Date(date));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
		return cal.getTimeInMillis();
	}
	
	public final long getLastDayOfDateInLastMonth(long date){
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(new Date(date));
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTimeInMillis();
	}
	
	public final long getNextDay(long date){
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(new Date(date));
		cal.add(Calendar.DAY_OF_YEAR, 1);
		return cal.getTimeInMillis();
	}
	
	public final long getSomeDayByNum(long date,int num){
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(new Date(date));
		cal.add(Calendar.DAY_OF_YEAR, num);
		return cal.getTimeInMillis();
	}
	
	public final long getSomeMonthByNum(long date,int num){
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(new Date(date));
		cal.add(Calendar.MONTH, num);
		return cal.getTimeInMillis();
	}
	
	public final int getWeekOfDate(long date) {
        int[] weekDays = {0, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(date));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	
	public final int getDayNum(long date){
		SimpleDateFormat format = new SimpleDateFormat("d"); 
		return Integer.parseInt(format.format(new Date(date)));
	} 
	
	public final String getDateFormat(long time, String sf) {
		Date date = new Date(time);
		SimpleDateFormat df = new SimpleDateFormat(sf);
		String str = df.format(date);
		return str;
	}
	
}
