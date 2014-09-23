
public class sunGeocentricPosition {
	double longitude;
	double latitude;
	
	//Sun position calculated relative to the Earth's center. 
	public sunGeocentricPosition(earthHeliocentricPosition ehp){
		longitude = ehp.eHPLongitude + 180.;
		longitude = ehp.setToRange(longitude, 0, 360);
		latitude = -ehp.eHPLatitude;
		latitude = ehp.setToRange(latitude, 0, 180);
		
				
	}
	
}
