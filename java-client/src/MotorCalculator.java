import net.java.games.input.*;

public class MotorCalculator {
	
	//TODO: add Field orient
	//TODO: clean up unused code (feedPower, etc.)
	
	static final float DEADZONE = 0.15f;
	static final int COUNTDOWN = 250; // How many loops of execution to wait before checking controllers.

	Controller[] controllers;
	Controller c = null;
	Component[] components;
	Component leftJoystickX, leftJoystickY, rightJoystickX, rightJoystickY, trigger, feedPower,
			intakePower, killSwitch, intakeDirection, feedShot, fieldOrient;

	DirectAndRawInputEnvironmentPlugin env = new DirectAndRawInputEnvironmentPlugin();

	int runs = 0;
	int runs2 = 0;
	int runs3 = 0;
	int runs4 = 0;
	int countdown = 0;
	
	float x;
	float y;
	float rot;
	float angle;

	Packet packet;

	public MotorCalculator(Packet packet) {
		this.packet = packet;

		c = getController();

		if (c == null) { // Couldn't find a gamepad
			System.out.println("Found no gamepad");
			if (!OperatorInterface.DEBUG) {
				System.exit(1);
			}
		} else {
			components = c.getComponents();

			leftJoystickY = components[0];
			leftJoystickX = components[1];
			rightJoystickY = components[2];
			rightJoystickX = components[3];
			// feedPower = components[4]; //Triggers
			trigger = components[4]; // Triggers
			intakeDirection = components[6]; // B Button
			intakePower = components[7]; // X Button
			feedShot = components[10]; // Right Bumper
			fieldOrient = components[9]; // Left Bumper
			killSwitch = components[11]; // Back Button

		}
	}

	public void computeMotors() {

		x = packet.leftXBoxX;
		y = packet.leftXBoxY;
		rot = packet.rightXBoxX;
		
		if (packet.fieldOrient) {
			angle = 90; //TODO: Get Gyro value
		} else {
			angle = 0;
		}

		float[] Di = new float[4];

		/*if (Math.abs(y) > Math.abs(x)) {
			// Initial Values; Order is FL, FR, BR, BL
			// They represent raw motor values
			Di[0] = x - y - rot;
			Di[1] = -x + y - rot;
			Di[2] = -x + y - rot;
			Di[3] = x - y - rot;

			// Maximum - go figure
		} else {
			Di[0] = -x - y - rot;
			Di[1] = x - y - rot;
			Di[2] = -x - y - rot;
			Di[3] = x + y - rot;
		}*/
		
		/*Di[0] = -y - x - rot;
		Di[1] = y + x - rot;
		Di[2] = y - x - rot;
		Di[3] = -y + x - rot;*/
		
		
		//Modification for field orient
		//Di[0] = (float) ((Math.sin(Math.toRadians(angle))-Math.cos(Math.toRadians(angle)))*y + (-Math.sin(Math.toRadians(angle))-Math.cos(Math.toRadians(angle)))*x - rot);
		//Di[1] = (float) ((-Math.sin(Math.toRadians(angle))+Math.cos(Math.toRadians(angle)))*y + (Math.sin(Math.toRadians(angle))+Math.cos(Math.toRadians(angle)))*x - rot);
		//Di[2] = (float) ((+Math.sin(Math.toRadians(angle))+Math.cos(Math.toRadians(angle)))*y + (Math.sin(Math.toRadians(angle))-Math.cos(Math.toRadians(angle)))*x - rot);
		//Di[3] = (float) ((-Math.sin(Math.toRadians(angle))-Math.cos(Math.toRadians(angle)))*y + (-Math.sin(Math.toRadians(angle))+Math.cos(Math.toRadians(angle)))*x - rot);
		
		Di[0] = y + x + rot;
		Di[1] = y - x - rot;
		Di[2] = y + x - rot;
		Di[3] = y - x + rot;
		
		float maxi = 1.0f;

		// Gather the Maximum of all four raw values
		for (int i = 0; i < Di.length; i++) {
			maxi = Math.max(Math.abs(Di[i]), maxi);
		}

		
		for (int i = 0; i < Di.length; i++) {
			
			// Scale all Motors to those values by dividing by maximum power.
			Di[i] = (Di[i] / maxi);
			
			// Invert Motors with Negative Values
			if (Di[i] < 0) {
				Di[i] = -Di[i];
				//packet.MD[i] = -1;
			} else {
				//packet.MD[i] = 1;
			}
			
			// Scale to 25% power (64), then modify it up to  +-25% based on triggers;
			packet.MP[i] = (int) (Di[i] * 64 * (packet.trigger / 2 + 1));
			//packet.MP[i] = (int) (Di[i]*127)+127;
			
			if (packet.kill) {
				packet.MP[i] = 0;
				//packet.MP[i] = 127;
			}
			
			//System.out.println("Motor Values: " + packet.MP[0] + ", " + packet.MP[1] + ", " + packet.MP[2] + ", " + packet.MP[3]);
			//System.out.println("Motor Direction: " + packet.MD[0] + ", " + packet.MD[1] + ", " + packet.MD[2] + ", " + packet.MD[3]);
		}
	}

	// Extracts values from the XBox Controller and stores them in Packet values
	void getValues() {
		c.poll(); // Updates all the values for the controller

		// Gets individual values
		packet.leftXBoxX = leftJoystickX.getPollData();
		packet.leftXBoxY = -leftJoystickY.getPollData();
		packet.rightXBoxX = rightJoystickX.getPollData();
		packet.rightXBoxY = -rightJoystickY.getPollData();
		packet.trigger = -trigger.getPollData(); // Right trigger is negative,
													// so invert

		// Calibrate for deadzone
		if (Math.abs(packet.leftXBoxX) < DEADZONE) {
			packet.leftXBoxX = 0;
		}

		if (Math.abs(packet.leftXBoxY) < DEADZONE) {
			packet.leftXBoxY = 0;
		}

		if (Math.abs(packet.rightXBoxX) < DEADZONE) {
			packet.rightXBoxX = 0;
		}

		if (Math.abs(packet.rightXBoxY) < DEADZONE) {
			packet.rightXBoxY = 0;
		}

		if (Math.abs(packet.trigger) < DEADZONE) {
			packet.trigger = 0;
		}

		/*
		 * FEED POWER TEST: This test prevents the trigger from reverting by
		 * triggering more than once per loop of the code, as it cycles much
		 * more than the human trigger reflex
		 */
		/*
		 * if ((feedPower.getPollData() < -DEADZONE) && (runs == 0)) {
		 * packet.feed = !packet.feed; runs = 100; } else if (runs > 0) {
		 * runs--; }
		 */

		// This button is held, so the above test is not applicable
		if (feedShot.getPollData() == 1.0f && !packet.feed) {
			packet.feed = true;
		} else if (!(feedShot.getPollData() == 1.0f) && packet.feed) {
			packet.feed = false;
		}

		// See the feed power test.
		if ((intakePower.getPollData() == 1.0f) && (runs2 == 0)) {
			packet.intake = !packet.intake;
			runs2 = 100;
		} else if (runs2 > 0) {
			runs2--;
		}

		// See the feed power test.
		if ((intakeDirection.getPollData() == 1.0f) && (runs3 == 0)) {
			packet.intakeDirection = !packet.intakeDirection;
			runs3 = 100;
		} else if (runs3 > 0) {
			runs3--;
		}
		
		if ((fieldOrient.getPollData() == 1.0f) && (runs4 == 0)) {
			packet.fieldOrient = !packet.fieldOrient;
			runs4 = 100;
		} else if (runs4 > 0){
			runs4--;
		}
				
		// Kill Switch Engagement
		if (killSwitch.getPollData() == 1.0f) {
			packet.kill = true;
		}
	}

	// Finds the first gamepad out of all plugged in controllers and returns it.
	// Otherwise returns null.
	Controller getController() {
		controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		for (int i = 0; i < controllers.length && c == null; i++) {
			if (controllers[i].getType() == Controller.Type.GAMEPAD) {
				return controllers[i]; // Found a gamepad
			}
		}
		return null;
	}

	// Updates the list of controllers and checks if a gamepad is connected.
	// Returns true if it is, false otherwise.
	public boolean controllerConnected() {
		if (countdown == 0) {
			env = new DirectAndRawInputEnvironmentPlugin();
			countdown = COUNTDOWN;
		} else {
			countdown--;
		}
		controllers = env.getControllers();
		for (int i = 0; i < controllers.length; i++) {
			if (controllers[i].getType() == Controller.Type.GAMEPAD) {
				return true;
			}
		}
		return false;

	}
}