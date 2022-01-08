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
		commands.add(c.new ChangeAlgorithmSettings());
		 //TODO: add other commands

		int exitIndex = commands.size();

		dio.write("Welcome to the Anomaly Detection Server.\n" +
				"Please choose an option:\n");
		do {
			displayCommands();
			indexOfCommand = (int)dio.readVal();
			commands.get(indexOfCommand-1).execute();
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
