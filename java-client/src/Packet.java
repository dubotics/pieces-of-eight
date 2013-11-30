

public class Packet {

	// Order is FL, FR, BR, BL - MOTORS ARE 2,3,4,5 Correspond to Pin Values on
	// Arduino Chip;
	// MP[5] is the Shooting Mechanism, hence no Corresponding Direction
	int[] MP;

	boolean intake;
	boolean intakeDirection;
	boolean feed;
	boolean kill;
	boolean fieldOrient;

	float leftXBoxX;
	float leftXBoxY;
	float rightXBoxX;
	float rightXBoxY;
	float trigger;

	// CONSTRUCTOR
	public Packet() {
		MP = new int[4];
	}

	public int[] getSendPacket() {
		int[] sendPacket = new int[14];
		sendPacket[0] = 11;

		sendPacket[1] = (int) MP[0];
		sendPacket[2] = (int) MP[1];
		sendPacket[3] = (int) MP[2];
		sendPacket[4] = (int) MP[3];

		// The remainder of these pins are non-Pulse-Width-Modulated;
		// therefore, they pass 1 or 0 instead of a 0-255 value.
		sendPacket[9] = toInt(intake);
		sendPacket[10] = toInt(intakeDirection);
		sendPacket[11] = toInt(feed);
		sendPacket[12] = toInt(feed) * 255;

		// Kill switch
		sendPacket[13] = toInt(kill);

		// Make sure the motors stop when killed
		if (kill) {
			sendPacket[1] = 0;
			sendPacket[2] = 0;
			sendPacket[3] = 0;
			sendPacket[4] = 0;
		}

		return sendPacket;
	}

	// It is necessary to ensure that a 'true' resolves to 1, and not any other
	// positive number.
	public int toInt(boolean b) {
		if (b) {
			return 1;
		} else {
			return 0;
		}
	}

}
