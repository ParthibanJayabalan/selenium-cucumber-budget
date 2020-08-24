package utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Common {

	/**
	 * To check that the variable is null or empty
	 * 
	 * @param string
	 * 
	 */
	public static boolean isNullOrEmpty(Object obj) {
        if(obj != null && !obj.toString().isEmpty())
            return false;
        return true;
    }
	
    public static String generateDate(int days) {
    	/* For JAVA 7
        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
    	
        //Getting current date
    	Calendar cal = Calendar.getInstance();
    	
    	//Number of Days to add
    	cal.add(Calendar.DATE, days);  
    	
        //Date after adding the days to the current date
    	String newDate = sdf.format(cal.getTime());  */
    	
        
        /* For JAVA 8 */
        LocalDateTime today = LocalDateTime.now();
        String newDate = DateTimeFormatter.ofPattern("MM/dd/yyyy").format(today.plusDays(days)); 
        
        return newDate;
    }

    public static String MonthAndDateFormat(String date) throws ParseException {
    	 DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
    	 DateFormat targetFormat = new SimpleDateFormat("E, MMM dd yyyy");
    	 Date input = originalFormat.parse(date);
    	 return targetFormat.format(input);    	 
    }
    
}
