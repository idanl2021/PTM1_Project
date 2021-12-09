package test;
import java.util.List;
import java.util.Vector;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	CorrelatedFeatures[] correlatedFeatures;

	@Override
	public void learnNormal(TimeSeries ts) {
		String[] columns = ts.getColumns();
		Vector<CorrelatedFeatures> features = new Vector<>();
		final double threshold = 0.05;

		for(int i = 0; i < columns.length; i++){
			for(int j = i + 1; j < columns.length; j++){

			}
		}
	}


	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		return null;
	}
	
	public List<CorrelatedFeatures> getNormalModel(){
		return null;
	}
}
