package test;
import java.util.*;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	ArrayList<CorrelatedFeatures> correlatedFeatures;

	@Override
	public void learnNormal(TimeSeries ts) {
		String[] columns = ts.getColumns();
		ArrayList<CorrelatedFeatures> features = new ArrayList<>();
		final float threshold = 0.1f;//
		float correlation;

		for(int i = 0; i < columns.length; i++){
			for(int j = i + 1; j < columns.length; j++){
				float[] x = ts.getArray(columns[i]);
				float[] y = ts.getArray(columns[j]);
				correlation = StatLib.pearson(x, y);
				Line l = StatLib.linear_reg(x,y);
				if(correlation > threshold){
					features.add(new CorrelatedFeatures(columns[i], columns[j], correlation, l, findThresholdOfLine(l,x,y)));
				}
			}
		}
		correlatedFeatures = features;

	}


	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		HashMap<String, float[]> hashMap = ts.getHashMap();
		String[] columns = ts.getColumns();
		ArrayList<AnomalyReport> anomalyReports = new ArrayList<>();
		for(int i = 0; i < ts.getArray(columns[0]).length; i++){
			for(CorrelatedFeatures cf : this.correlatedFeatures){
				if(StatLib.dev(
						new Point(ts.getArray(cf.feature1)[i], ts.getArray(cf.feature2)[i]) // p(x,y) = (feature1, feature2)
						,cf.lin_reg)
						> cf.threshold){
					anomalyReports.add(new AnomalyReport(cf.feature1+"-"+cf.feature2, i+1));
				}
			}
		}
		return anomalyReports;
	}

	public List<CorrelatedFeatures> getNormalModel(){
		return correlatedFeatures;
	}

	public float findThresholdOfLine(Line line, float[] x, float[] y){
		float max = 0;
		float difference = 0;
		for(int i = 0; i < x.length; i++){
			difference = StatLib.dev(new Point(x[i],y[i]),line);
			if(difference > max){
				max = difference;
			}
		}
		return max*1.1f;
	}
}
