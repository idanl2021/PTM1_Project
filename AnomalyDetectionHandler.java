package test;


import test.Commands.DefaultIO;
import test.Server.ClientHandler;

import java.io.*;
import java.util.Scanner;

public class AnomalyDetectionHandler implements ClientHandler{

	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) {
		SocketIO sio = new SocketIO(inFromClient, outToClient);
		CLI cli = new CLI(sio);
		cli.start();
		sio.close();
	}

	public class SocketIO implements DefaultIO{
		Scanner input;
		PrintWriter output;

		public SocketIO(Scanner s, PrintWriter pw){
			input = s;
			output = pw;
		}

		public SocketIO(InputStream is, OutputStream os){
			input = new Scanner(is);
			output = new PrintWriter(os);
		}

		@Override
		public String readText() {
			return input.nextLine();
		}

		@Override
		public void write(String text) {
			output.write(text);
			output.flush();
		}

		@Override
		public float readVal() {
			return input.nextFloat();
		}

		@Override
		public void write(float val) {
			output.print(val);
			output.flush(); //? is necessary
		}

		public void close(){
			input.close();
			output.close();
		}
	}


}
