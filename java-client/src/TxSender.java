

import com.rapplogic.xbee.api.*;
import com.rapplogic.xbee.api.wpan.*;

public class TxSender implements Runnable {
	// TODO: Clean up comments
	// TODO: remove redudant while

	int interval;
	XBeeAddress64 destination;
	RxListener rxListener;
	Packet packet;
	XBee xbee; //

	public TxSender(int msInterval, XBeeAddress64 address, XBee xbee, RxListener rx, Packet packet) {
		this.interval = msInterval;
		this.destination = address;
		this.rxListener = rx;
		this.packet = packet;
		this.xbee = xbee;
	}

	public void run() {
		while (true) {
			// packet is a member variable of the PApplet instance that is the parent object of TxSender
			int[] payload = packet.getSendPacket();

			spawnTxThread(destination, payload, rxListener);

//			try {
//				Thread.sleep(interval);
//			} catch (InterruptedException e) {
//				System.out.println("TxSender woken up");
//			}
		}
	}

	private void spawnTxThread(XBeeAddress64 dest, int[] payload, RxListener rx) {

		TxRequest64 tx = new TxRequest64(dest, payload);

		try {
			xbee.sendSynchronous(tx, 50);
		} catch (XBeeException e) {
			rx.error();
		}

		rx.register(tx.getFrameId());
	}
}
