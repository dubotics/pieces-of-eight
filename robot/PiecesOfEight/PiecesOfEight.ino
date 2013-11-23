/** @file PiecesOfEight.ino
 *
 * Robot-side code for the Pieces of Eight ball-shooting robot.
 *
 * Serial connections:
 *
 *   - Sabertooth motor controllers on serial 0 (Serial)
 *   - XBee on serial 1 (Serial 1)
 *
 * Relay pins: see constants below.
 *
 */
#include <XBee.h>
#include <Sabertooth.h>
#include <limits.h>
#include "ControlPacket.h"

/* Pins used for relay-based belt/shooter control. */
const int INTAKEPOWER = 23;
const int INTAKEDIR = 25;
const int FEEDPOWER = 52;
const int SHOOTERPOWER = 6;
const int SHPS2 =7;
const int FEEDDIR = 35;


/** Connection error counter. */
unsigned int safetyCounter = 0;

/** Maximum number of connection errors before we kill the motors. */
const int safetyLimit = 100;

/* Motor-controller, communication interface objects. */
XBee xbee;
Sabertooth mcFront(128, Serial);
Sabertooth mcBack(129, Serial);


void setup() {
  /* Set up relay pin directions. */
  pinMode(SHOOTERPOWER,OUTPUT);
  pinMode(INTAKEPOWER,OUTPUT);
  pinMode(FEEDPOWER, OUTPUT);
  pinMode(FEEDDIR, OUTPUT);
  pinMode(SHPS2, OUTPUT);
  
  /* The Sabertooth 2x5s _require_ a baud rate of 9600 (there's a bug in the
     firmware). */
  Serial.begin(9600);
  mcFront.autobaud();
  mcBack.autobaud();

  /* Set up the XBee to use second serial port at appropriate baud rate. */
  Serial1.begin(38400);
  xbee.setSerial(Serial1);
}

/** Continuously reads packets, looking for RX64 response packets. */
void loop() {
  xbee.readPacket();

  ControlPacket packet;
  Rx64Response rx64;

  if (xbee.getResponse().isAvailable() && xbee.getResponse().getApiId() == RX_64_RESPONSE)
    {
      safetyCounter = 0;	/* reset safety counter (we've got connectivity) */

      xbee.getResponse().getRx64Response(rx64);
      memcpy(&packet, rx64.getData(), sizeof(ControlPacket));

      digitalWrite(13, HIGH);
    }
  else if (xbee.getResponse().isError())
    { 
      if ( safetyCounter < UINT_MAX ) /* prevent overflow */
	safetyCounter++;

      digitalWrite(13, LOW);
      //TODO: add error code handling
    }

  
  /* Kill all outputs if we've reached the safety limit or if the "kill" input
     is pressed.  */
  if ( safetyCounter > safetyLimit || packet.kill )
    {
      memset(&packet, 0, sizeof(packet));
      packet.kill = 1;
    }
  
  run(packet);
}

/** Set the outputs to those specified in the given packet.
 */
void
run(const ControlPacket& p)
{
  mcFront.motor(2, p.motor.front_left);
  mcFront.motor(1, p.motor.front_right);
  mcBack.motor(2, p.motor.back_right);
  mcBack.motor(1, p.motor.back_left);
  
  digitalWrite(INTAKEPOWER, p.intake);
  digitalWrite(INTAKEDIR, p.intake < 0 ? 0 : 1);
  digitalWrite(FEEDPOWER, p.feed);
  digitalWrite(FEEDDIR, HIGH);
  digitalWrite(50, p.feed);
  digitalWrite(53, p.feed);
}
