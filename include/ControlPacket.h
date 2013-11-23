/** @file ControlPacket.h
 *
 * Defines the packet structure used for communication with Pieces of Eight.
 */
#ifndef ControlPacket_h
#define ControlPacket_h 1

#ifdef __cplusplus
extern "C" {
#endif

/** Control-data packet type.  This structure is used to pass control data to
 * the robot.
 *
 * DO NOT DELETE the "packed" attribute.  It is magic.
 */
typedef struct __attribute__ (( packed ))
{
  struct {
    int8_t
      front_left,		/**< Front-left motor speed/direction */
      front_right,		/**< Front-right motor speed/direction */
      back_left,		/**< Back-left motor speed/direction */
      back_right;		/**< Back-right motor speed/direction */
  } motor;			/**< Motor control data. */

  int8_t
    intake,			/**< Intake belt speed/direction. */
    feed,			/**< Feed belt power. */
    shooter;			/**< Shooter wheels motor power. */

  uint8_t kill;			/**< Kill flag. */
} ControlPacket;


#ifdef __cplusplus
}
#endif

#endif	/* ControlPacket_h */
