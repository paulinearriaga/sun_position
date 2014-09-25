import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Math;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class sunPosition {
	
	public static double getAberrationCorrection(Double radius) {
		double corr = -20.4898 / (3600. * radius);
		return corr;
	}
	
	public static double setToRange(double var, double minInterval, double maxInterval) {
		if (var > 0) {
			var = var - maxInterval * Math.floor(var / maxInterval);
		} else {
			var = var - maxInterval * Math.ceil(var / maxInterval);
		}
		if (var < minInterval) {
			var = var + maxInterval; 
		}	
		var = var - maxInterval * Math.floor(var/maxInterval);	
		return var;	
	}
	
	// Geocentric Right Ascension
	public static double getSunRightAscension(double apparentSunLongitude, double trueObliquity, 
			sunGeocentricPosition sGP) {
		double argNumerator = (Math.sin(apparentSunLongitude * Math.PI / 180.) * Math.cos(trueObliquity * Math.PI / 180.)) - 
				(Math.tan(sGP.latitude * Math.PI / 180) * Math.sin(trueObliquity * Math.PI/180.));
		
		double argDenominator = Math.cos(apparentSunLongitude * Math.PI / 180);
		double sunRightAscension = Math.atan2(argNumerator, argDenominator) * 180. / Math.PI;
		sunRightAscension = setToRange(sunRightAscension, 0,360);
		return sunRightAscension;
	}
	
	// Geocentric declination 
	public static double getSunDeclination(double sunApparentLongitude, double trueObliquity, 
			sunGeocentricPosition sgp) {
		double argument = (Math.sin(sgp.latitude * Math.PI / 180.) * Math.cos(trueObliquity * Math.PI/180.) +
				Math.cos(sgp.latitude * Math.PI / 180.) * Math.sin(trueObliquity * Math.PI / 180.) * Math.sin(sunApparentLongitude * Math.PI / 180.));
		double sunGeocentricDec = Math.asin(argument) * 180. / Math.PI;
		return sunGeocentricDec;
		
	}
	
	public static double getApparentSunLongitude(double sgpLongitude, double nutationLongitude, 
			double aberration) {
		double result = sgpLongitude + nutationLongitude + aberration;
		return result;
	}
	
	public static double getApparentSTimeGrenwich(JulianDay jdate, nutation nut) {
		double jDay = jdate.jday;
		double jCent = jdate.century;
		System.out.println("Julian Century");
		System.out.println(jDay);
		double meanSTime = 280.46061837 + (360.98564736629*(jDay-(double) 2451545)) + 
				Math.pow(jCent, 2) * ( 0.000387933 - (jCent/38710000.));
		meanSTime = setToRange(meanSTime, 0, 360);
		System.out.println("Nu0");
		System.out.println(meanSTime);
		double result = meanSTime + nut.longitude * Math.cos(nut.trueObliquity * Math.PI / 180.);
		return result;		
	}
	
	public static double getObserverLocalHour(double apparentGreenwichTime, location loca, double sunRightAscension) {
		double result = apparentGreenwichTime + loca.longitude - sunRightAscension;
		result = setToRange(result, 0, 360);
		return result;
	}
	
	public static double getSunZenith(location loca, topocentricSunPosition tsp) {
		double argument = (Math.sin(loca.latitude * Math.PI / 180.) * Math.sin(tsp.sunDec * Math.PI / 180)) +
				(Math.cos(loca.latitude * Math.PI / 180) * Math.cos(tsp.sunDec * Math.PI / 180) * Math.cos(tsp.sunLHA * Math.PI / 180));
		double trueElevation = Math.asin(argument)* 180. / Math.PI;
		System.out.println("Elevation");
		System.out.println(trueElevation);
		double argument2 = trueElevation + (10.3 / (trueElevation + 5.11));
		double refractionCorr = 1.02 / (60. * Math.tan(argument2 * Math.PI / 180));
		double apparentElevation;
		if (trueElevation > -5) {
			apparentElevation = trueElevation + refractionCorr;
		} else {
			apparentElevation = trueElevation;
		}
		double sunZenith = 90 - apparentElevation;
		return sunZenith;
	}
	
	public static double getSunAzimuth(location loca, topocentricSunPosition tsp) {
		
		System.out.println("deltprime");
		System.out.println(tsp.sunDec);
		double numerator = Math.sin(tsp.sunLHA * Math.PI / 180.); 
				
		double denominator = Math.cos(tsp.sunLHA* Math.PI/180.)* Math.sin(loca.latitude * Math.PI / 180) -
				Math.tan(tsp.sunDec * Math.PI / 180.) * Math.cos(loca.latitude * Math.PI / 180);
		
		double az = Math.atan2(numerator, denominator) * 180. / Math.PI;
		az = setToRange(az,0,360) + (double) 180.;
		az = setToRange(az, 0, 360);
		return az;
	}
	
	public static double aberrationCorrection;
	public static double apparentSunLongitude;
	public static double sunRightAscension;
	public static double sunDeclination;
	public static double apparentSTimeGreenwich;
	public static double observerLocalHour;
	public static double sunZenith;
	public static double sunAzimuth;
	public static double beta;
	public static double theta;

	
	
	public sunPosition() throws FileNotFoundException {
		location location = new location(34.0722, 118.4441);	// UCLA	(34, 4, 20) (118, 26, 38)
//		location location = new location(39.742476, -105.1786); // Test values from C code
		Calendar rightNow = Calendar.getInstance();
		Calendar tester = new GregorianCalendar(2003,10,17,12,30,30); // Test values from C code
		JulianDay jDate = new JulianDay(tester); // YES
		earthHeliocentricPosition ehp = new earthHeliocentricPosition(jDate); //YES
		nutation nutCorr = new nutation(jDate); // YES
		aberrationCorrection = getAberrationCorrection(ehp.eHPRadius); // epsilon
		sunGeocentricPosition sgp = new sunGeocentricPosition(ehp); //PROBABLY OKAY
		beta = sgp.latitude;
		theta = sgp.longitude;
		apparentSunLongitude = getApparentSunLongitude(sgp.longitude, nutCorr.longitude, aberrationCorrection);
		sunRightAscension = getSunRightAscension(apparentSunLongitude, nutCorr.trueObliquity, sgp);
		sunDeclination = getSunDeclination(apparentSunLongitude, nutCorr.trueObliquity, sgp);
		apparentSTimeGreenwich = getApparentSTimeGrenwich(jDate, nutCorr);
		observerLocalHour = getObserverLocalHour(apparentSTimeGreenwich, location, sunRightAscension); //NOPE
		topocentricSunPosition tsp = new topocentricSunPosition(ehp, location, observerLocalHour, sunRightAscension, sunDeclination);
		sunZenith = getSunZenith(location, tsp); 
		sunAzimuth = getSunAzimuth(location, tsp); 
	}
	public static void main(String args[]) throws FileNotFoundException {
		boolean verbose = true;
		
//		location location = new location(34.0722, -118.4441);	// UCLA	(34, 4, 20) (118, 26, 38) Not sure why negative; defined differently in C code
		location location = new location(39.742476, -105.1786); // Test values from C code
		Calendar rightNow = Calendar.getInstance();
		Calendar tester = new GregorianCalendar(2003,10,17,12,30,30); // Test values from C code
		JulianDay jDate = new JulianDay(tester); // YES
		earthHeliocentricPosition ehp = new earthHeliocentricPosition(jDate); //YES
		nutation nutCorr = new nutation(jDate); // YES
		aberrationCorrection = getAberrationCorrection(ehp.eHPRadius); // epsilon
		sunGeocentricPosition sgp = new sunGeocentricPosition(ehp); //PROBABLY OKAY
		beta = sgp.latitude;
		theta = sgp.longitude;
		apparentSunLongitude = getApparentSunLongitude(sgp.longitude, nutCorr.longitude, aberrationCorrection);
		System.out.println(jDate.century);
		System.out.println(jDate.jday);
		sunRightAscension = getSunRightAscension(apparentSunLongitude, nutCorr.trueObliquity, sgp);
		sunDeclination = getSunDeclination(apparentSunLongitude, nutCorr.trueObliquity, sgp);
		apparentSTimeGreenwich = getApparentSTimeGrenwich(jDate, nutCorr);
		observerLocalHour = getObserverLocalHour(apparentSTimeGreenwich, location, sunRightAscension); 
		topocentricSunPosition tsp = new topocentricSunPosition(ehp, location, observerLocalHour, sunRightAscension, sunDeclination);
		sunZenith = getSunZenith(location, tsp); 
		sunAzimuth = getSunAzimuth(location, tsp); 
		if (verbose) {
			System.out.println("Delta T");
			System.out.println(aberrationCorrection);
			System.out.println("Theta"); //YES
			System.out.println(theta);
			System.out.println("Beta"); //YES
			System.out.println(beta);				
			System.out.println("Lambda");		
			System.out.println(apparentSunLongitude); //CLOSE ENOUGH
			System.out.println("Alpha");
			System.out.println(sunRightAscension); //CLOSE ENOUGH
			System.out.println("Delta");
			System.out.println(sunDeclination); //OH YES
			System.out.println("V");
			System.out.println(apparentSTimeGreenwich);
			System.out.println("H");
			System.out.println(observerLocalHour); //YES YES YES
			System.out.println("Sun Zenith");
			System.out.println(sunZenith);
			System.out.println("Sun Azimuth");
			System.out.println(sunAzimuth);
			
		}
	}
}

