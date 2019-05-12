package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Server extends Application {

	@FXML
	Button btnStart, btnStop, btnRequest;
	
	@FXML
	Label lblStatus;
	
	@FXML
	TextField txtPort;
	
	@FXML
	TextArea txtResponse;
	
	ServerSocket ss;
	Socket s;
	DataInputStream din;
	DataOutputStream dout;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage s) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Server.fxml"));
		s.setScene(new Scene(root, 244, 348));
		s.setResizable(false);
		s.setTitle("Server");
		s.show();
	}
	
	public void startServer() {
		lblStatus.setText("Status: Running");
		int port = Integer.parseInt(txtPort.getText());		
		
		try {
			ss = new ServerSocket(port);
			s = ss.accept();
			din = new DataInputStream(s.getInputStream());
			dout = new DataOutputStream(s.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopServer() {
		lblStatus.setText("Status: Stopped");
		
		try {
			ss.close();
			s.close();
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void requestKeys() {
		String keys;

		try {
			String req = "Get_Keys";
			dout.writeUTF(req);
			
			Thread.sleep(1000);
			
			keys = din.readUTF();
			if(keys.equals(null))
				keys = "null";
			
			txtResponse.setText(keys);
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
}
