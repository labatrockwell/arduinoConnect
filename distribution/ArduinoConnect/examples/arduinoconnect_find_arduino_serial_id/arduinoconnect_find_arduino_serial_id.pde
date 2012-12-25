
/**
 *  Arduino Connect Library Example: Print Arduino Serial ID Number
 *
 *  This sketch uses the ArduinoConnect library to print the unique Arduino
 *  Serial ID Number for every Arduino device connected to the console on the
 *  computer that is running this application.
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

void setup() {
  // get list of arduinos connected to this computer
  ArduinoConnect.printArduinoSerialNumber();
}
