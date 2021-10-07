package com.vone.medisafe.report;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

public class PrintServerJasper {

	/**
	 * @param args
	 */
	public static final short PORT = 9998;

	public static void main(String[] args) {
		//mancing
		JFrame viewer = new JFrame();
		viewer.dispose();
		//end of mancing
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

		ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(
				socket.getInputStream()));
		Object obj;
		try {
			obj = is.readObject();
			if (obj instanceof JFrame){
				JFrame viewer = (JFrame)obj;
				viewer.pack(); 
				viewer.setSize(800,600);
				viewer.setLocation(100, 80);
				viewer.setResizable(true);
				viewer.setVisible(true);
				System.out.println("JFrame lewat socket");
			}else if (obj instanceof JasperPrint){
				JasperPrint jasperPrint = (JasperPrint) obj;
				viewJasper(jasperPrint);
			}else if(obj instanceof BufferedReader){
				BufferedReader str = (BufferedReader) obj;
				printOut(str);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void viewJasper(JasperPrint jrPrint) {
		JRViewer jrViewer = new JRViewer(jrPrint);

		JFrame viewer = new JFrame();
		viewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		viewer.setTitle("PREVIEW LAPORAN"); 
		viewer.getContentPane().add(jrViewer); 
		viewer.pack(); 
		viewer.setSize(800,600);
		viewer.setLocation(100, 80);
		viewer.setResizable(true);
		viewer.setVisible(true);
	}

	public static void printOut(BufferedReader inputReader)
			throws FileNotFoundException, IOException {
		String baris;
		FileOutputStream os = new FileOutputStream("lpt1");
		// FileOutputStream os = new FileOutputStream("d:\\test.txt");
		PrintStream ps = new PrintStream(os);

		int i = 1;
		while ((baris = inputReader.readLine()) != null) {
			System.out.println("" + i + " :" + baris);
			ps.println(baris);
			i++;
		}

		inputReader.close();
		// ps.print("\f");
		ps.close();
	}

}
