package test;

import java.util.ArrayList;

import test.Commands.Command;
import test.Commands.DefaultIO;

public class CLI {

	ArrayList<Command> commands;
	DefaultIO dio;
	Commands c;
	
	public CLI(DefaultIO dio) {
		int indexOfCommand;
		this.dio=dio;
		c=new Commands(dio); 
		commands=new ArrayList<>();
		// example: commands.add(c.new ExampleCommand());
		// implement
		commands.add(c.new UploadTimeSeriesCommand());
		commands.add(c.new ChangeAlgorithmSettingsCommand());
		commands.add(c.new DetectAnomaliesCommand());
		commands.add(c.new DisplayResultsCommand());
		commands.add(c.new UploadAnomaliesAndAnalyzeResultsCommand());
		 //TODO: add other commands

		commands.add(c.new ExitCommand());
		int exitIndex = commands.size();


		do {
			dio.write("Welcome to the Anomaly Detection Server.\n" +
					"Please choose an option:\n");
			displayCommands();
			String str = dio.readText();
			while (str.equals(""))  str = dio.readText();
			try {
				indexOfCommand =Integer.parseInt(str);  //(int)dio.readVal();
				try {
					commands.get(indexOfCommand-1).execute();
				} catch (Exception e) {
					continue;
				}
			} catch (NumberFormatException e) {
				indexOfCommand = 1;
				continue;
			}
		} while (indexOfCommand != exitIndex);

	}

	protected void displayCommands(){
		for (int i = 0; i < commands.size(); i++) {
			dio.write((i+1) + ". " + commands.get(i).description);
		}
	}
	
	public void start() {
		// implement
	}
}
