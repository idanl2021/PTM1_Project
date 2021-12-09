package test;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class TimeSeries {
	private HashMap<String, float[]> hashMap;
	private String[] columns;
	
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
				String[] splittedData;
				data = myReader.nextLine();
				splittedData = data.split(regex);
				for(int i = 0; i < columns.length; i++){
					vectors[i].add(Float.parseFloat(splittedData[i]));
				}
			}
			for(int i = 0; i < columns.length; i++){
				hashMap.put(columns[i], this.vectorToArray(vectors[i]));
			}
			myReader.close();
		}
		catch (Exception ex){

		}
	}

	public String[] getColumns() {
		return columns;
	}

	public float[] getVector(String column){
		return hashMap.get(column);
	}

	private float[] vectorToArray(Vector<Float> vector){
		float[] floats = new float[vector.size()];
		for(int i = 0; i < vector.size(); i++){
			floats[i] = vector.get(i);
		}
		return floats;
	}
}
