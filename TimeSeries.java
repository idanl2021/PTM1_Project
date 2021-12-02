package test;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class TimeSeries {
	HashMap<String, Vector<Float>> hashMap;
	String[] columns;
	
	public TimeSeries(String csvFileName) {
		try{
			String regex = ",";
			hashMap = new HashMap<>();
			File file = new File(csvFileName);
			Scanner myReader = new Scanner(file);
			String data = myReader.nextLine();
			columns = data.split(regex);
			Vector<Float>[] vectors = new Vector[columns.length];
			while (myReader.hasNextLine()) {
				//data += "/n" + myReader.nextLine();
				String[] splicedData;
				data = myReader.nextLine();
				splicedData = data.split(regex);
				for(int i = 0; i < columns.length; i++){
					vectors[i].add(Float.parseFloat(splicedData[i]));
				}
			}
			for(int i = 0; i < columns.length; i++){
				hashMap.put(columns[i], vectors[i]);
			}
			myReader.close();
		}
		catch (Exception ex){

		}
	}

	public String[] getColumns() {
		return columns;
	}


}
