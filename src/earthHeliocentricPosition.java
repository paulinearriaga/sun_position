import java.awt.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class earthHeliocentricPosition {
	double eHPLongitude;
	double eHPLatitude;
	double eHPRadius;
	
	// Set to range wraps around var until it fits between minInterval and maxInterval
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
	
	
	// Helper function that does the array operation numpy.sum(l[0] * numpy.cos(l[1] + l[2] * jme))
	public static double eHelioLong(ArrayList<ArrayList<Double>> lArray, double jme) {
		double lDouble = 0.;
		for (int i = 0; i < lArray.size(); i++) {
			ArrayList<Double> lSingle = lArray.get(i);
			lDouble = lDouble + lSingle.get(0) * Math.cos(lSingle.get(1) + lSingle.get(2) * jme); 
		}
		return lDouble;
	}
	
	//read CSV to doubleArray
	public static ArrayList<ArrayList<Double>> csvToDoubleArray(String fname) throws FileNotFoundException {
		BufferedReader CSVFile = new BufferedReader(new FileReader(fname));
		String dataRow;
		ArrayList<ArrayList<Double>> dataList = new ArrayList<ArrayList<Double>>();
		try {
			dataRow = CSVFile.readLine();
			while (dataRow != null ){
				String[] dataArray = dataRow.split(",");
				ArrayList<Double> doubleDataRow = new ArrayList<Double>();
				for (int i = 0; i<dataArray.length; i++) {
					doubleDataRow.add(Double.parseDouble((dataArray[i])));
				}
				dataList.add(doubleDataRow);
				dataRow = CSVFile.readLine();
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			CSVFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataList;
	}
		
		
	
	
	//Function confirmed using NREL
	public earthHeliocentricPosition(JulianDay jDate) throws FileNotFoundException{
		double jme = jDate.ephemerisMillenium;
		ArrayList<ArrayList<Double>> longitudeL0 = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> longitudeL1 = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> longitudeL2 = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> longitudeL3 = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> longitudeL4 = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> longitudeL5 = new ArrayList<ArrayList<Double>>();
		longitudeL0 = csvToDoubleArray("src/tables/longitudeL0.csv");
		longitudeL1 = csvToDoubleArray("src/tables/longitudeL1.csv");
		longitudeL2 = csvToDoubleArray("src/tables/longitudeL2.csv");
		longitudeL3 = csvToDoubleArray("src/tables/longitudeL3.csv");
		longitudeL4 = csvToDoubleArray("src/tables/longitudeL4.csv");
		longitudeL5 = csvToDoubleArray("src/tables/longitudeL5.csv");

		double l0 = eHelioLong(longitudeL0, jme);
		double l1 = eHelioLong(longitudeL1, jme);
		double l2 = eHelioLong(longitudeL2, jme);
		double l3 = eHelioLong(longitudeL3, jme);
		double l4 = eHelioLong(longitudeL4, jme);
		double l5 = eHelioLong(longitudeL5, jme);
		
				

		eHPLongitude = (l0 + (l1 * jme) + (l2 * Math.pow(jme,2)) + (l3 * Math.pow(jme,3))
				+ (l4 * Math.pow(jme, 4)) + (l5 * Math.pow(jme, 5))) / Math.pow(10, 8);
		eHPLongitude = eHPLongitude * 180. / Math.PI;
		eHPLongitude = setToRange(eHPLongitude, 0., 360.);
				
		
		ArrayList<ArrayList<Double>> latitudeB0 = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> latitudeB1 = new ArrayList<ArrayList<Double>>();

		//Confirmed
		latitudeB0 = csvToDoubleArray("src/tables/helio_latitude_B0.csv");
		latitudeB1 = csvToDoubleArray("src/tables/helio_latitude_B1.csv");
		double latL0 = eHelioLong(latitudeB0, jme);
		double latL1 = eHelioLong(latitudeB1, jme);
		eHPLatitude = (latL0 + (latL1 * jme)) / (Math.pow(10, 8));
		eHPLatitude = eHPLatitude * 180. / Math.PI;
		eHPLatitude = setToRange(eHPLatitude,0,360);
		ArrayList<ArrayList<Double>> arrayR0 = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> arrayR1 = new ArrayList<ArrayList<Double>>();		
		ArrayList<ArrayList<Double>> arrayR2 = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> arrayR3 = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> arrayR4 = new ArrayList<ArrayList<Double>>();
		
		arrayR0 = csvToDoubleArray("src/tables/rVectorR0.csv");
		arrayR1 = csvToDoubleArray("src/tables/rVectorR1.csv");
		arrayR2 = csvToDoubleArray("src/tables/rVectorR2.csv");
		arrayR3 = csvToDoubleArray("src/tables/rVectorR3.csv");
		arrayR4 = csvToDoubleArray("src/tables/rVectorR4.csv");

		double radiusL0 = eHelioLong(arrayR0, jme);
		double radiusL1 = eHelioLong(arrayR1, jme);
		double radiusL2 = eHelioLong(arrayR2, jme);
		double radiusL3 = eHelioLong(arrayR3, jme);
		double radiusL4 = eHelioLong(arrayR4, jme);
		
		eHPRadius = (radiusL0 + (radiusL1 * jme) + (radiusL2 * Math.pow(jme,2)) + 
				(radiusL3 * Math.pow(jme,3)) + (radiusL4 * Math.pow(jme, 4))) / Math.pow(10, 8);
	}
	
	
}

