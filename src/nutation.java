import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
public class nutation {

	public static Double[] deltaMult(ArrayList<ArrayList<Double>> lArray,
			ArrayList<ArrayList<Double>> yTerms, ArrayList<Double> xTerms, double jme) {
		double lLong = 0;
		double lObliq = 0;
		for (int i = 0; i< lArray.size(); i++) {
			ArrayList<Double> lSingle = lArray.get(i);
			ArrayList<Double> yList = yTerms.get(i);
			double tabArgument = 0;
			for (int j = 0; j < yList.size(); j++) {
				tabArgument = yList.get(j) * xTerms.get(j) * Math.PI / 180.;
			}
			lLong = lLong + (lSingle.get(0) + lSingle.get(1) * jme) * Math.sin(tabArgument); 
			lObliq = lObliq + (lSingle.get(2) + lSingle.get(3) * jme) * Math.cos(tabArgument);
		}
		lLong = lLong / 36000000.;
		lObliq = lObliq / 36000000; 
		return new Double[] {lLong, lObliq};
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

	public static double polyVal(ArrayList<Double> p, Double xval) {
		int arrSize = p.size();
		double xReturn = 0;
		for (int i = 0; i < arrSize; i++) {
			double cur = (double) p.get(i);
			xReturn += Math.pow(cur, (double)(arrSize -i - 1));
		}
		return xReturn;
	}
	
	public static double fuckthis(ArrayList<Double> p, Double xval) {
		double result = ((p.get(0) * xval + p.get(1)) * xval + p.get(2)) *xval + p.get(3);
		return result;
	}
	
	double longitude;
	double obliquity;
	double trueObliquity;
	
	public nutation(JulianDay jdate) throws FileNotFoundException {
		double JCE = jdate.ephemerisCentury;
		double JME = jdate.ephemerisMillenium;
		
		// Mean elongation of the moon from the sun
		
		
		ArrayList<Double> meanElong = new ArrayList<Double>() {{
				addAll(Arrays.asList((1./(double)189474), -0.0019142, 445267.11148, 297.85036));
				}};
		final double X0 = fuckthis(meanElong, JCE);
			
		ArrayList<Double> meanAnomalySun = new ArrayList<Double>() {{
			addAll(Arrays.asList(-(1./(double)300000), -0.0001603, 35999.05034, 357.52772));
			}};
		final double X1 = fuckthis(meanAnomalySun, JCE);
		
	
		ArrayList<Double> meanAnomalyMoon = new ArrayList<Double>() {{
			addAll(Arrays.asList((1./(double)56250), 0.0086972, 477198.867398, 134.96298));
			}};
		final double X2 = fuckthis(meanAnomalyMoon, JCE);

		ArrayList<Double> moonArgument = new ArrayList<Double>() {{
			addAll(Arrays.asList((1./(double)327270.), -0.0035825, 483202.016538, 93.27191));
			}};
		final double X3 = fuckthis(moonArgument, JCE);
		
		
		ArrayList<Double> ascendingNode = new ArrayList<Double>() {{
			addAll(Arrays.asList((1./(double)450000), 0.0020708, -1934.136261, 125.04452));
		}};
		final double X4 = fuckthis(ascendingNode, JCE);
		
		
		ArrayList<Double> xTerms = new ArrayList<Double> () {{
			addAll(Arrays.asList(X0, X1, X2, X3, X4));
		}};
		
		ArrayList<ArrayList<Double>> yTerms = new ArrayList<ArrayList<Double>>();
		yTerms = csvToDoubleArray("src/tables/yTerms.csv");
		
		// Calculating delta psi and delta epsilon
		ArrayList<ArrayList<Double>> nutationTerms = new ArrayList<ArrayList<Double>>();
		nutationTerms = csvToDoubleArray("src/tables/nutationTerms.csv");
		
		Double[] results = deltaMult(nutationTerms, yTerms, xTerms, JCE);
		
		longitude = results[0]; 
		obliquity = results[1];
		
		System.out.println(longitude);
		System.out.println(obliquity);
		
		
		
		ArrayList<Double> obliqTerms = new ArrayList<Double>() {{
			addAll(Arrays.asList(2.45, 5.79, 27.87, 7.12, -39.05, -249.67, 
					-51.38, 1999.25, -1.55, -4680.93, 84381.448));
		}};
		
		double u = JME / 10;
		double meanObliquity = 84381.448 + u*(-4680.93 + u*(-1.55 + u*(1999.25 + u*(-51.38 + u*(-249.67 +
				u*(  -39.05 + u*( 7.12 + u*(  27.87 + u*(  5.79 + u*2.45))))))))); //YOLO
		
		trueObliquity = meanObliquity / 3600. + obliquity;
		System.out.println(trueObliquity);
	
		
		
		
	}
		
	
}
