package n7;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fonctions {
	
	static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); // à changer
	
	static String hash(String mdp) { // TODO hash md5 ou SHA-256
		return mdp;
	}
	
	static Date stringToDate(String dateInString) {

		Date date = null;
		
        try {

            date = formatter.parse(dateInString);
            System.out.println(date);
            System.out.println(formatter.format(date));

        } catch (Exception e) {
            e.printStackTrace();
        }
		return date; 
	}
	
	static String dateToString(Date date) {
		return formatter.format(date);
	}
	
	public static void main(String[] args) {
		String dateStr = "24/12/2017";
		Date date = stringToDate(dateStr);
		System.out.println(date);
	}
	


	public static Date today() {
		return Calendar.getInstance().getTime();
	}


}