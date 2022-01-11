package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Commands {
	
	// Default IO interface
	public interface DefaultIO{
		public String readText();
		public void write(String text);
		public float readVal();
		public void write(float val);

		// you may add default methods here
		//public Boolean hasNextLine();
	}
	
	// the default IO to be used in all commands
	DefaultIO dio;
	public Commands(DefaultIO dio) {
		this.dio=dio;
	}
	
	// you may add other helper classes here

	public class RangeAnomalyReport extends AnomalyReport{
		long endStep;

		public RangeAnomalyReport(String description, long timeStep, long endStep) {
			super(description, timeStep);
			this.endStep = endStep;
		}

		public boolean checkIntersection(OrderedPair pair){
			return pair.start <= endStep && pair.end >= timeStep;
		}
	}
	
	
	// the shared state of all commands
	private class SharedState{
		// implement here whatever you need
		List<AnomalyReport> anomalyReports = new ArrayList<>();
		SimpleAnomalyDetector anomalyDetector = new SimpleAnomalyDetector();
		TimeSeries train_ts, test_ts;
	}
	
	private  SharedState sharedState=new SharedState();

	
	// Command abstract class
	public abstract class Command{
		protected String description;
		
		public Command(String description) {
			this.description=description;
		}
		
		public abstract void execute();
	}
	
	// Command class for example:
	public class ExampleCommand extends Command{

		public ExampleCommand() {
			super("this is an example of command");
		}

		@Override
		public void execute() {
			dio.write(description);
		}		
	}
	
	// implement here all other commands

	public class UploadTimeSeriesCommand extends Command{

		public UploadTimeSeriesCommand() {
			super("upload a time series csv file\n");
		}

		@Override
		public void execute() {
			dio.write("Please upload your local train CSV file.\n");
			sharedState.train_ts = new TimeSeries(dio);
			//sharedState.anomalyDetector.learnNormal(sharedState.test_ts);
			dio.write("Upload complete.\n");
			dio.write("Please upload your local test CSV file.\n");
			sharedState.test_ts = new TimeSeries(dio);
			//sharedState.anomalyReports = anomalyDetector.detect(test_ts);
			dio.write("Upload complete.\n");
		}
	}

	public class ChangeAlgorithmSettingsCommand extends Command{


		public ChangeAlgorithmSettingsCommand() {
			super("algorithm settings\n");
		}

		@Override
		public void execute() {
			dio.write("The current correlation threshold is " + sharedState.anomalyDetector.threshold + "\n");
			dio.write("Type a new threshold\n");
			float ts = dio.readVal();
			while(!(ts < 1 && ts > 0)){
				dio.write("please choose a value between 0 and 1.\n");
				ts = dio.readVal();
			}
			sharedState.anomalyDetector.threshold = ts;
		}
	}

	public class DetectAnomaliesCommand extends Command{

		public DetectAnomaliesCommand() {
			super("detect anomalies\n");
		}

		@Override
		public void execute() {
			sharedState.anomalyDetector.learnNormal(sharedState.train_ts);
			sharedState.anomalyReports = sharedState.anomalyDetector.detect(sharedState.test_ts);
			dio.write("anomaly detection complete.\n");
		}
	}

	public class DisplayResultsCommand extends Command{

		public DisplayResultsCommand() {
			super("display results\n");
		}

		@Override
		public void execute() {
			for(AnomalyReport anomalyReport : sharedState.anomalyReports){
				dio.write(anomalyReport.timeStep + "\t" + anomalyReport.description + "\n");
			}
			dio.write("Done.\n");
		}
	}

	public class UploadAnomaliesAndAnalyzeResultsCommand extends Command{

		public UploadAnomaliesAndAnalyzeResultsCommand() {
			super("upload anomalies and analyze results\n");
		}

		@Override
		public void execute() {
			DecimalFormat df = new DecimalFormat("#.###");
			String str;
			List<OrderedPair> orderedPairs = new ArrayList<>();
			dio.write("Please upload your local anomalies file.\n");
			while (true/*dio.hasNextLine()*/){
				str = dio.readText();
				if(str.equals("done")) break;
				if(str.equals("")) continue;
				orderedPairs.add(new OrderedPair(str));
			}
			dio.write("Upload complete.\n");
			//dio.write("Analyzing...\n");

			ArrayList<RangeAnomalyReport> ranges = new ArrayList<>();
			if (sharedState.anomalyReports.size() > 1)
			{
				String prevDescription = sharedState.anomalyReports.get(0).description;
				long prevTimeStep = sharedState.anomalyReports.get(0).timeStep;
				RangeAnomalyReport currentReport = new RangeAnomalyReport(prevDescription, prevTimeStep, prevTimeStep);
				for (int i = 1; i < sharedState.anomalyReports.size(); i++)
				{
					String currentDescription = sharedState.anomalyReports.get(i).description;
					long currentTimeStep = sharedState.anomalyReports.get(i).timeStep;
					if (prevTimeStep + 1 == currentTimeStep && prevDescription.equals(currentDescription))
					{
						currentReport.endStep = currentReport.endStep + 1;
					}
					else
					{
						ranges.add(currentReport);
						currentReport = new RangeAnomalyReport(currentDescription, currentTimeStep, currentTimeStep);
					}

					prevDescription = currentDescription;
					prevTimeStep = currentTimeStep;
				}

				ranges.add(currentReport);
			}

			double P = orderedPairs.size(), N, TP = 0, FP = 0; // positive, negative, true-positive, false-positive
			int[] TParray = new int[orderedPairs.size()];
			N = sharedState.train_ts.getArray(sharedState.train_ts.getColumns()[0]).length; // num of time steps
			/*
			for (OrderedPair pair :
					orderedPairs) {
					N -= orderedPairs.size();
			}
			*/

			/*
			for (int i = 0; i < orderedPairs.size(); i++) {
				N -= orderedPairs.get(i).getLength();
				OrderedPair pair = orderedPairs.get(i);
				for (AnomalyReport report:
						sharedState.anomalyReports) {
					if(pair.contains((int)report.timeStep)){
						TP++;
						TParray[i] = 1;
						break;
					}
				}
			}
			 */
			for (RangeAnomalyReport report:
					ranges) {
				boolean flag = false;
				for (OrderedPair pair:
						orderedPairs) {
					if(report.checkIntersection(pair)){
						TP++;
						flag = true;
						break;
					}
				}
				if(!flag){
					FP++;
				}
			}

			for (OrderedPair pair:
				 orderedPairs) {
				N -= pair.getLength();
			}

		//dio.write("True Positive Rate: " + df.format(((double)TP)/((double)P)) + "\n");
		//dio.write("False Positive Rate: " + df.format(((double)FP)/((double)N)) + "\n");

		//dio.write("True Positive Rate: " + String.format("%f",((double)TP)/((double)P)) + "\n");
		//dio.write("True Positive Rate: " + String.format("%f",((double)FP)/((double)N)) + "\n");

		dio.write("True Positive Rate: " + toFormat((TP)/(P), 3) + "\n");
		dio.write("False Positive Rate: " + toFormat((FP)/(N), 3) + "\n");
		}
		public String toFormat(Double d, int numOfDigits){
			int num = (int)(d*(Math.pow(10,numOfDigits)));
			d = (double) (num/Math.pow(10,numOfDigits));
			return d.toString();
		}
	}

	public  class ExitCommand extends Command{

		public ExitCommand() {
			super("exit\n");
		}

		@Override
		public void execute() {
			return;
		}
	}





	public class OrderedPair {
		public int start = 0;
		public int end = 0;

		public OrderedPair(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public OrderedPair(DefaultIO dio){
			String str = dio.readText();
			String[] arr = str.split(",");
			start = Integer.parseInt(arr[0]);
			end = Integer.parseInt(arr[1]);
		}

		public OrderedPair(String str){
			String[] arr = str.split(",");
			start = Integer.parseInt(arr[0]);
			end = Integer.parseInt(arr[1]);
		}

		public boolean contains(int num){
			return (num >= start && num <= end);
		}

		public int getLength(){
			return end - start + 1;
		}
	}


}
