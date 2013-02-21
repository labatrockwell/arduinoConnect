ArduinoConnect Library  
======================  
  
The ArduinoConnect library was created to enable you to link processing sketches to connect to specific Arduinos, regardless of the port to which the Arduino is connected. If you are ready to get started then skip to the Installing the Library section below.
  
This library was intended for projects that require connecting and managing multiple Arduinos from a single computer. This library includes an object class that functions as a wrapper to the Processing Serial library, and enables connecting to Arduinos based on their unique Serial IDs.  
  
The ArduinoConnect library works by using unique serial id numbers to identify specific Arduinos. The unique serial id number is provided as a string parameter in the ArduinoConnect class constructor as demonstrated in the connect_to_arduino_serial_id example sketch.  
  
The serial id numbers for all Arduinos that are connected to a computer can be accessed by calling the ArduinoConnect.printArduinoSerialNumber() method. This method prints to the console the serial id number and serial port name of each Arduino. The find_arduino_serial_id example sketch shows this method in action.  

This is an open source project that leverages code from other open source projects. Please read the license information at the bottom of this read me.
  
@author    	Quin Kennedy & Julio Terra from LAB at Rockwell Group [http://www.rockwellgroup.com]
@credits   	Code for extracting arduino serial id adapted from work by Dave Vondle [http://labs.ideo.com]
@modified  	11/22/2012
@version   	1.0.0


Installing the Library  
======================   
  
Here’s how to install the library in four (or five) easy steps:  
1. Download the zip file from github repo.  
2. Unzip the downloaded file.  
3. Go to "distribution / ArduinoConnect / download" from the base directory.  
4. Unzip the ArduinoConnect.zip file
4. Copy the contents into your Processing libraries folder.  
5. Restart Processing if it was alredy running.  
  

System Requirements  
===================  
Currently only works with Arduino connected via USB using an Atmega16U2 as the USB-to-serial converter (older arduinos use FTDI chips instead). That said, it could be adapted to support a wider range of devices.   
  

License
==========
Copyright © 2012 LAB at Rockwell Group, http://www.rockwellgroup.com/lab  

This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA, 02111-1307 USA
