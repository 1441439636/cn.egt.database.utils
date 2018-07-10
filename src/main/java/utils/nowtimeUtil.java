package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class nowtimeUtil {
	public static String getTime(){
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}public static String getYMDTime(){
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(date);
	} 
}
