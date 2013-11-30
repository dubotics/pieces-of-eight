

import java.util.Set;
import java.util.HashSet;

import com.rapplogic.xbee.api.*;
import com.rapplogic.xbee.api.wpan.*;

public class RxListener implements Runnable {

	// This is a count of packets that were successfully sent,
	// ie, the XBee got an ACK
	int successfulPackets;

	// A count of packets that were not successfully sent
	int failedPackets;

	// A set of packets for which no response, ACK or error, has been received
	Set<Integer> unreturnedPackets;

	XBee xbee;
	XBeeResponse rx;

	// XBee must be open()
	public RxListener(XBee xbee) {
		successfulPackets = 0;
		failedPackets = 0;

		this.unreturnedPackets = new HashSet<Integer>();
		this.xbee = xbee;
	}

	public final void run() {
		while (true) {
			try {
				// Blocking call (synchronous) - waits 100ms, then throws an
				// exception.
				rx = xbee.getResponse(100);

				if (rx.getApiId() == ApiId.TX_STATUS_RESPONSE) {
					TxStatusResponse txrs = (TxStatusResponse) rx;
					
					//Remove recived packet from list of unreturned packets
					if (unreturnedPackets.contains(txrs.getFrameId())) {
						unreturnedPackets.remove(txrs.getFrameId()); 
					}
					
					//Whether response was successful
					if (txrs.getStatus() == TxStatusResponse.Status.SUCCESS) {
						successfulPackets++;
					} else {
						System.out.println(txrs.getStatus());
						failedPackets++;
					}
				} else if (rx.getApiId() == ApiId.RX_16_RESPONSE) {
					for (Integer i : rx.getProcessedPacketBytes()) {
						System.out.print(i + " "); // Might need converting from hex
					}
					System.out.println();
				}

			} catch (XBeeTimeoutException e) {
				System.out.println("XBeeTimeoutException");
			} catch (XBeeException e) {
				System.out.println("RxListener XBeeException");
			} catch (NullPointerException e) {
				System.out.println("Null pointer in RXListener");
			}
			
			//Sleep for 100ms
			try {
				System.out.println("success: " + getSuccessfulPackets() + " failure: " + getFailedPackets());
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.print("RxListener sleep interrupted");
			}
		}
	}
	
	//Registers a packet as sent, so we can check if it is returned
	public void register(int frameId) {
		unreturnedPackets.add(frameId);
	}

	public void error() {
		failedPackets++;
	}

	public int getSuccessfulPackets() {
		return successfulPackets;
	}

	public int getFailedPackets() {
		return failedPackets;
	}

	public int getUnreturnedPackets() {
		return unreturnedPackets.size();
	}
}
