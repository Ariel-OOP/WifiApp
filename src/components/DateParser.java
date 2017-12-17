package components;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Shir
 * https://stackoverflow.com/questions/13245490/check-how-many-times-string-contains-character-g-in-eligible-string
 */
public class DateParser {
	
	static String correctDateFormat;

    public static void main(String[] args) {
        correctDateFormat = checkDateFormat("7/25/2013 12:1:5");
    	System.out.println(correctDateFormat);
    }
    
    public static String getCorrectDateFormat() {
		return correctDateFormat;
	}

	public static String setCorrectDateFormat(String correctDateFormat) {
		DateParser.correctDateFormat = checkDateFormat(correctDateFormat);
		return DateParser.correctDateFormat;
	}

	/**
	 * The function gets date and convert it to the correct format.
	 * @param date - as String
	 * @return date as String in correct format
	 */
	private static String checkDateFormat(String date) {

    	date = addSeconds(date);
		String time = date.substring(date.indexOf(" ") + 1);
		String newDate = date.substring(0,date.indexOf(" "));

		newDate = newDate.replaceAll("/", "-");

		boolean MM_ddFormat = isValidFormat("MM-dd-yyyy", newDate);
    	boolean M_ddFormat1 = isValidFormat("M-dd-yyyy", newDate);
		boolean MM_dFormat2 = isValidFormat("MM-d-yyyy", newDate);
		//boolean M_dFormat3 = isValidFormat("M-d-yyyy hh:mm:ss", correctFormatDate);
    	if(MM_ddFormat || M_ddFormat1 || MM_dFormat2 ) {
			int firstHyphen = newDate.indexOf("-");
			int secondHyphen = newDate.indexOf("-", firstHyphen + 1);
			String m = newDate.substring(0, firstHyphen);
			String d = newDate.substring(firstHyphen + 1, secondHyphen);
			String remainderDate = newDate.substring(secondHyphen + 1, newDate.length());
			newDate = d + "-" + m + "-" + remainderDate;
		}
    	newDate += " " + time;
    	
		return newDate;
	}

	/**
	 * The function get date and check if need to add a seconds or that has not any time and need to add time
	 * @param string - date as String
	 * @return date with time.
	 */
	private static String addSeconds(String string) {
		
		int count = StringUtils.countMatches(string, ":");
		
		if (count == 1)
			string += ":00";
		if (count == 0)
			string += " 00:00:00";
		
		
		return string ;
	}

	/**
	 * The function gets a format of date and date and check if the date is in the given format.
	 * @param format - a format of date.
	 * @param value - the date will check.
	 * @return true if the date is in a format desired else the function returns false.
	 */
	private static boolean isValidFormat(String format, String value) {
        Date date = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
            
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }
}