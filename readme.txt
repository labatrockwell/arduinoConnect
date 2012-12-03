ArduinoConnectLibrary

The ArduinoConnect library was created to enable you to link processing
sketches to connect to specific Arduinos, regardless of the port to which  
the Arduino is connected. 

This library was intended for projects that require connecting and managing 
multiple Arduinos from a single computer. This library includes an object 
class that functions as a wrapper to the Processing Serial library, and
enables connecting to Arduinos based on their unique Serial IDs.

A set of static methods provide a functionality such as:
(1) print a list featuring the unique serial ids of all Arduinos that are connected  
	to a computer.
(2) return port name to which an Arduino, which is specified by a unique serial 
	id, is connected. 

Currently only works with Arduino connected via USB using an Atmega16U2 as 
the USB-to-serial converter (older arduinos use FTDI chips instead). That 
said, it could be adapted to support a wider range of devices. 
