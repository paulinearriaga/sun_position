import java.util.Calendar;
import java.lang.Math;

// Differs from python .2, need to change into my time zone!!!
public class JulianDay {


    double ephemerisCentury;
    double jday;
    double century;
    double ephemerisDay;
    double ephemerisMillenium;
    

    
    public JulianDay(Calendar inputTime){
	// Gregorian Calendar adopted Oct. 15, 1582 (2299161)

    //Extraction of variables
	int year = inputTime.get(Calendar.YEAR);
	int month = inputTime.get(Calendar.MONTH);
	int day = inputTime.get(Calendar.DAY_OF_MONTH);
	int hour = inputTime.get(Calendar.HOUR_OF_DAY);
	int minute = inputTime.get(Calendar.MINUTE);
	int second = inputTime.get(Calendar.SECOND);
	int rev_year;
	int rev_month;
		
	
	// Beginning of year, roll back to previous year
	if (month == 1 | month == 2) {
	    rev_year = year - 1;
	    rev_month = month + 12;
	} else {
	    rev_year = year;
	    rev_month = month;
	}
	
	double  UTCCorr = (double) 7. / 24; // Assuming GMT-8
	
	// Calculating fraction of the day
	double da = day + hour / 24. + minute / (60. * 24.) + 
	    second /(60 * 60 * 24.);
	
	// Parameters used below
    double inta = Math.floor(rev_year / 100); // Ignoring the case that input date is being before 1582
	double intb = 2 - inta + Math.floor(inta/4);

	// Calculation of the julian date
	jday= Math.floor(365.25*(rev_year + 4716.)) +
			Math.floor(30.6001 * (rev_month + 1)) + da + intb - 1524.5 + UTCCorr;
//	jday = 2452930.312847;
	
	double delta_t = 0;  // Difference between earth rotation time and terrestrial time, fuck it
	ephemerisDay = jday + (delta_t / (double)86400.);
	
	century = (jday - (double)2451545.) / 36525.;
	
	ephemerisCentury = (ephemerisDay - (double)2451545.) / (double)36525.;
	
	ephemerisMillenium = ephemerisCentury/10.;
	
    }


}









