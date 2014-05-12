package uos.codingsroom.ddmgroup.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormat {

	SimpleDateFormat dateFormat;
	Date pDate;
	Date today;
	long difference;

	public String timeDelay(String postDate) {
		String delay_time;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			pDate = dateFormat.parse(postDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		today = new Date();

		long difference = today.getTime() - pDate.getTime();

		if (difference > 604800000) { // 일주일 이상 차이날 때,
			if (pDate.getYear() > today.getYear()) {
				delay_time = pDate.getYear() + "년 " + pDate.getMonth() + "월 ";
			} else {
				delay_time = pDate.getMonth() + 1 + "월 " + pDate.getDate() + "일";
			}

		} else if (difference > 86400000) {
			delay_time = difference / 86400000 + "일 전";
		} else if (difference > 3600000) {
			delay_time = difference / 3600000 + "시간 전";
		} else if (difference > 60000) {
			delay_time = difference / 60000 + "분 전";
		} else {
			delay_time = "방금 전";
		}

		return delay_time;
	}

}
