package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
		public Boolean hasNextLine();
	}
	
	// the default IO to be used in all commands
	DefaultIO dio;
	public Commands(DefaultIO dio) {
		this.dio=dio;
	}
	
	// you may add other helper classes here
	
	
	
	// the shared state of all commands
	private class SharedState{
		// implement here whatever you need
		List<AnomalyReport> anomalyReports = new ArrayList<>();
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
			SimpleAnomalyDetector anomalyDetector = new SimpleAnomalyDetector();
			dio.write("Please upload your local train CSV file.\n");
			TimeSeries train_ts = new TimeSeries(dio);
			anomalyDetector.learnNormal(train_ts);
			dio.write("Upload complete");
			dio.write("Please upload your local test CSV file.\n");
			TimeSeries test_ts = new TimeSeries(dio);
			sharedState.anomalyReports = anomalyDetector.detect(test_ts);
			dio.write("Upload complete");
		}
	}
	
}
