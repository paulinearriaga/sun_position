
public class topocentricSunPosition {
	
	double sunRAParallax;
	double sunRA;
	double sunDec;
	double sunLHA;
	public topocentricSunPosition(earthHeliocentricPosition eHP, location loca, double observerLocalHour, 
			double sunRightAscension, double sunGeocentricDeclination){
		double eqHorizParallax = 8.794 / (3600. * eHP.eHPRadius);
		double uTerm = Math.atan(.99664719 * Math.tan(loca.latitude * Math.PI /180.));
		// Here we say we're at sea level. close enough
		double xTerm = Math.cos(uTerm);
		double yTerm = (.99664719 * Math.sin(uTerm));
		
		
		// Topocentric RA calculation
		double numerator = -xTerm * Math.sin(eqHorizParallax * Math.PI / 180.) * Math.sin(observerLocalHour * Math.PI / 180);
		double denominator = Math.cos(sunGeocentricDeclination * Math.PI / 180) - (xTerm * Math.sin(observerLocalHour * Math.PI / 180.));
		sunRAParallax = Math.atan2(numerator, denominator);
		sunRAParallax = sunRAParallax * 180. / Math.PI;
		sunRA = sunRightAscension + sunRAParallax * 180 / Math.PI;
		
		
		// Topocentric dec calculation
		double decNum = (Math.sin(sunGeocentricDeclination * Math.PI / 180) - 
				(yTerm * Math.sin(eqHorizParallax * Math.PI / 180.))) * Math.cos(sunRAParallax);
		
		double decDenom = Math.cos(sunGeocentricDeclination *Math.PI / 180) -
				(xTerm * Math.sin(eqHorizParallax * Math.PI / 180)) * Math.cos(observerLocalHour * Math.PI / 180) ;
		sunDec = Math.atan2(decNum, decDenom) * 180. / Math.PI;
		
		// Topocentric local hour
		sunLHA = observerLocalHour - sunRAParallax;
	}
	
	
}
