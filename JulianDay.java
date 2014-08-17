import java.util.*;
import java.util.Calendar;
import java.lang.Math;


public class JulianDay {


    double ephemerisCentury;
    double jday;
    double century;
    double ephemerisDay;
    double ephemerisMillenium;
    


    public JulianDay(Calendar inputTime){




	// Gregorian Calendar adopted Oct. 15, 1582 (2299161)




	int jGreg= 15 + 31*(10+12*1582);
	double halfSecond = 0.5;
	int UTC = 0;
	int year = inputTime.get(Calendar.YEAR);
	int month = inputTime.get(Calendar.MONTH) + 1;
	int day = inputTime.get(Calendar.DAY_OF_MONTH);
	int hour = inputTime.get(Calendar.HOUR_OF_DAY);
	int minute = inputTime.get(Calendar.MINUTE);
	int second = inputTime.get(Calendar.SECOND);
	int rev_year;
	int rev_month;
	if (month == 1 | month == 2) {
	    rev_year = year - 1;
	    rev_month = month + 12;
	} else {
	    rev_year = year;
	    rev_month = month;
	}
	
	double da = day + hour / 24. + minute / (60. * 24.) + 
	    second /(60 * 60 * 24.);
	
        double inta = Math.floor(rev_year / 100); // Ignoring the case that input date is being before 1582
	double intb = 2 - inta + Math.floor(inta/4);


	jday = Math.floor(365.25*(rev_year + 4716.)) +
	    Math.floor(30.6001 * (rev_month + 1)) + da + intb - 1524.5;
		
	double delta_t = 0; //check this
	ephemerisDay = jday + delta_t / 86400.;
	
	century = (jday - 2451545.) / 36525.;
	
	ephemerisCentury = (ephemerisDay - 2451545.) / 
	    36525;
	
	ephemerisMillenium = ephemerisCentury/10.;
	
    }


}









