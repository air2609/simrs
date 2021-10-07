package com.vone.medisafe.printing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PrintServer {

	public static final short PORT = 9999;

	public static void main(String[] args) {
		System.out.println("Print server running...");
		ServerSocket sock;
		Socket clientSock;
		try {
			sock = new ServerSocket(PORT);
			while ((clientSock = sock.accept()) != null) {
				// Process it.
				process(clientSock);
			}
		} catch (IOException e) {
			System.err.println(e);
		}

	}

	static void process(Socket socket) throws IOException {
		System.out.println("Accept from client: " + socket.getInetAddress());
		BufferedReader inputReader = new BufferedReader(
				new InputStreamReader(
				socket.getInputStream()
				));
		
		printOut(socket, inputReader);
		//viewJasper(socket, inputReader);
	}

	private static void printOut(Socket socket, BufferedReader inputReader) throws FileNotFoundException, IOException {
		String baris;
		//FileOutputStream os = new FileOutputStream("lpt1");
//		FileOutputStream os = new FileOutputStream("/home/arifullah/test.txt");
		FileOutputStream os = new FileOutputStream("C:/kwitansi.txt");
		PrintStream ps = new PrintStream(os);

		int i = 1;
		while ((baris = inputReader.readLine()) != null) {
			//System.out.println("" + i + " :" + baris);
			ps.println(baris);
			i++;
		}
		
		inputReader.close();
		ps.print("\f");
		ps.close();
		socket.close();
		
//		Runtime.getRuntime().exec("notepad.exe C:/test.txt");
	}

}
