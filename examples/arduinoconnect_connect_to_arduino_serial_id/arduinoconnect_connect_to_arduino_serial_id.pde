
/**
 *  Arduino Connect Library Example: Print Arduino Serial ID Number
 *
 *  This sketch uses the ArduinoConnect library to connect to an Arduino based on
 *  it's unique Serial ID Number.
 *
 *  Please note that this library currently only works with Arduinos that use the
 *  the Atmega16U2 or Atmega8U2 chip for USB-to-Serial connection. Older 
 *  Arduinos use an FTDI chip, which is not fully supported by the current version
 *  of the library (though adding support for those devices is probably not too 
 *  difficult).
 *
 *  @author    Julio Terra
 *  @date      12/1/2012
 *  @version   1.0.0
 *
 *****************/

import com.rockwellgroup.arduinoconnect.*;
import processing.serial.*;

ArduinoConnect arduino;
Serial port;

void setup() {
  // connect to a specific arduino identified by a unique serial ID number. To get 
  // the serial ID number for an Arduino use the arduinoconnect_print_arduino_serial_id
  // example sketch.

  arduino = new ArduinoConnect(this, true);
  
  // connect to Arduino using the arduino connect library. The first argument is the
  // Ardunion's unique ID number, the second argument is the baud rate for the 
  // serial connection, and the third item is the character that will trigger the
  // serialEvent callback method
  arduino.connect("741323434303513081F0", 57600, '\n');
  
  // you can check if the Arduino connection has been established by calling the a
  // arduinoConObject.isconnected method. 
  if (arduino.isConnected()) {

    // if you prefer to have access to the serial object from a global variable, you
    // can access it directly via the arduinoConObject.getConnection() method or the 
    // arduinoConObject.connection variable. Here's how:
    port = arduino.getConnection();
  
    // you can access the serial connection object for sending messages by either using
    // arduinoConObject.connection or by linking the serial object a local variable as 
    // demonstrated above.
    arduino.connection.write("OK");
    port.write("OK");
  }

}

/**
 * serialEvent Callback method that is called by Processing whenever the trigger char
 * defined in the arduino connection request is received.
 *
 * @param {Serial} myPort the serial port where the message was received / event occured
 */
void serialEvent(Serial myPort) {
  String newStr = myPort.readString();
  println("[serialEvent] received new message: " + newStr);
}

