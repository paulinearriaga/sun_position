import java.util.*;
import java.util.Calendar;
import java.lang.Math;



public class SolarDrive {







    public static void main(String[] args) { 

	HashMap<String,Double> location = new HashMap<String,Double>();
	location.put("Latitude", new Double(34.0722));
	location.put("Longitude", new Double(118.4441)); 
	Calendar rightNow = Calendar.getInstance();
	JulianDay jDate = new JulianDay(rightNow);
     	System.out.println(jDate.jday);       


    }



    










}




