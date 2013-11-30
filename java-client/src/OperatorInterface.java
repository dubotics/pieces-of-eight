//XBee Library
import com.rapplogic.xbee.api.*;

public class OperatorInterface {
	
	public static final String PORT = "COM7"; // Port for xbee communication. COM3 for windows, "/dev/ttyUSB0" for linux
	public static final int BAUDRATE = 38400; // BaudRate for xbee communication. 57600 for windows, 115200 for linux
	public static final int ADDRESS_HIGH = 0x0013A200;
	public static final int ADDRESS_LOW	= 0x4082082B;
	public static final boolean DEBUG = true;
	
	// Hexadecimal Addresses for the XBee are the parameters - High First, then Low
	//TODO: refrence static field
	static XBeeAddress64 robotXbee = new XBeeAddress64(0x00, 0x13, 0xa2, 0x00, 0x40, 0x82, 0x08, 0x2B);

	static RxListener rxListener;
	static TxSender txSender;

	static MotorCalculator2 mc;
	static Gui gui;

	static XBee xbee;

	static Packet packet;

	public static void main(String[] args) {
		setup();
		while (true) {
			loop();
		}
	}
	
	//Setup of gui, xbee, and threads
	static void setup() {
		packet = new Packet();
		mc = new MotorCalculator2(packet);
		gui = new Gui(800, 800, packet);

		  //////////////////////////////
		 // Setup XBee communication //
		//////////////////////////////

		xbee = new XBee();
		
		try {
			xbee.open(PORT, BAUDRATE);
		} catch (XBeeException e) {
			System.out.println("XBeeException from IO Init");
			System.out.println(e);
			if (!DEBUG) {
				System.exit(3);
			}
		}

		rxListener = new RxListener(xbee);
		Thread rxListenerThread = new Thread(rxListener);
		rxListenerThread.start();

		txSender = new TxSender(100, robotXbee, xbee, rxListener, packet);
		Thread txSenderThread = new Thread(txSender);
		txSenderThread.start();
	}
	
	// loops continuously, calculating and sending motor controls, updating the gui,
	// and checking for controller disconnects.
	static void loop() {
		if (!packet.kill) {
			if (!mc.controllerConnected()) {
				packet.kill = true;
			}
			mc.getValues();
			mc.computeMotors();
		} else {
			packet.leftXBoxX = 0;
			packet.leftXBoxY = 0;
			packet.rightXBoxX = 0;
			packet.rightXBoxY = 0;
			System.exit(2);
		}
		
		gui.update();
	}
}