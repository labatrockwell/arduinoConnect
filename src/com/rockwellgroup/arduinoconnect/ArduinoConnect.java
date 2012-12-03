/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      Julio Terra and Quin Kennedy from LAB at Rockwell Group
 * @modified    12/01/20122
 * @version     ##library.prettyVersion## (##library.version##)
 */

package com.rockwellgroup.arduinoconnect;

import processing.serial.*;
import processing.core.*;
import java.lang.ProcessBuilder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

/**
 * ArduinoConnect A class that connects to Arduinos by using unique Arduino serial ids.
 */
public class ArduinoConnect {
  
  public static final String SERIAL_NUMBER = "Serial Number: ";
  public static final String LOCATION = "Location ID: ";
  public static final String REGEX_ID = "^0x0*([1-9a-fA-F][0-9a-fA-F]*[1-9a-fA-F])0*";
  public static final Pattern REGEX_PAT = Pattern.compile(REGEX_ID);
  public static final char CB_FLAG = '\n';
  public static boolean DEBUG = false; 

  public Serial connection;
  private PApplet app;
  private boolean connected;
  private boolean debug;
  
  /**
   * constructor method that initializes key variables
   * @param Link to applet that is linking to the arduino
   */
  public ArduinoConnect(PApplet _app) {  
      app = _app;   
      debug = false;
      connected = false;
  }

  /**
   * constructor method that initializes key variables
   * @param Link to applet that is linking to the Arduino
   * @param Holds value for debug flag
   */
  public ArduinoConnect(PApplet _app, boolean _debug) {  
      app = _app;   
      debug = _debug;
      connected = false;
  }

  /**
   * connect Attempts to connect to an arduino by looking up the arduino_name in a map that
   *   is saved in the file sated in file_path.
   * @param  file_path Holds the path to the file that holds the hashmap
   * @param  device_name Holds the name of the arduino on the map
   * @param  line_div Holds character that divides keys from values on the map
   * @param  baud Holds the baud rate of the arduino serial connection
   * @return  returns true if connection established  
   */
  public boolean connect(String file_path, String arduino_name, char line_div, int baud) {    
      String device_name;
      String serial_list [] = Serial.list();
  
      try {
        device_name = ArduinoConnect.getSerialPortName(arduino_name, file_path, line_div);      
        for(int i = (serial_list.length - 1); i >= 0; i--){     
            if (serial_list[i].indexOf(device_name) == 0) {
                if (this.debug) PApplet.println("[connect] (device_name, baud) connecting to serial device at index: " + i);
                return connect(i, baud);
            }
        }   
      } catch (Exception e) {
        PApplet.println("[connect] (file_path, arduino_name, baud) ERROR file not found or error reading file");
        return false;
      }
      return false;
   }

  /**
   * connect Attempts to connect to the serial device at a specified port or by using a device id
   * @param  device_name Holds the name of the serial port or device id to which we will connect
   * @param  baud Holds the baud rate of the serial connection
   * @return  returns true if connection established  
   */
  public boolean connect(String device_name, int baud) {
    return connect(device_name, baud, CB_FLAG);
  }

  /**
   * connect Attempts to connect to the serial device at a specified port or by using a device id
   * @param  device_name Holds the name of the serial port or device id to which we will connect
   * @param  baud Holds the baud rate of the serial connection
   * @param  cb_flag Holds the char that triggers call of the serialEvent method
   * @return  returns true if connection established  
   */
  public boolean connect(String device_name, int baud, char cb_flag) {
    if (this.debug) PApplet.println("[connect] (device_name, baud)");

    // check if the device name is a device id, and if so, get the appropriate port name
    String regex_str = "([0-9a-fA-F])";
    Pattern regex_pat = Pattern.compile(regex_str);
    Matcher idMatch = regex_pat.matcher(device_name);
    if (idMatch.groupCount() > 0 && idMatch.find()){
      device_name = getSerialPortName(device_name);
    }    

    if (device_name == null) {
      if (this.debug) PApplet.println("[connect] (device_name, baud) ERROR: arduino name not found");
      return false;
    } 

    else {
      if (this.debug) PApplet.println("[connect] (device_name, baud) arduino at port: " + device_name);
      String serial_list [] = Serial.list();
      for(int i = (serial_list.length - 1); i >= 0; i--){     
          if (serial_list[i].indexOf(device_name) >= 0) {
              if (this.debug) PApplet.println("[connect] (device_name, baud) port is at index: " + i);
              return connect(i, baud, cb_flag);
          }
      }   
    }

    return false;
  }

  /**
   * connect Attempts to connect to the arduino at the specified index location on the serial list
   * @param  device_num Holds the index number in the serial device array of the device to which we will connect
   * @param  baud Holds the baud rate of the serial connection
   * @return  returns true if connection established  
   */
  public boolean connect(int device_num, int baud) {
    return connect(device_num, baud, CB_FLAG);
  }

  /**
   * connect Attempts to connect to the arduino at the specified index location on the serial list
   * @param  device_num Holds the index number in the serial device array of the device to which we will connect
   * @param  baud Holds the baud rate of the serial connection
   * @param  cb_flag Holds the char that triggers call of the serialEvent method
   * @return  returns true if connection established  
   */
  public boolean connect(int device_num, int baud, char cb_flag) {
      String serial_list [] = Serial.list();
      if (this.debug) PApplet.println("[connect] (device_num, baud, cb_flag) ");

      // make sure that device num is valid and then attempt to connect to the arduino.
      if (device_num < serial_list.length) {      
        try{
            connection = new Serial(app, serial_list[device_num], baud);
            app.delay(100);
            connection.bufferUntil(cb_flag);
            connected = true;
            if (this.debug) PApplet.println("[connect] (device_num, baud, cb_flag) connected to serial port: " + serial_list[device_num]);
        } catch(Exception e) {
            // if connection failed print error message
            PApplet.println("[connect] ERROR: unable to connect to serial port");
            connected = false;
        }
      }
      return connected;
  }  
  
  /**
   * getConnection Returns the serial object for direct access. The connection object is public and can
   *   also be accessed directly from the object instance as follows: instance_obj.connection.
   * @return Returns the active serial connection
   */
  public Serial getConnection() {
    if (connected) return connection;
    return null;
  }

  /**
   * isConnected Returns true if the serial port is connect, false if the serial port is not connected.
   * @return Returns the active serial connection
   */
  public boolean isConnected() {
    return connected;
  }

  /********************
   ** STATIC METHODS
   ********************/

  /**
   * getSerialPortName Returns the serial port name by searching a hashmap the arduino_key. The maps it 
   *   searches is stored in a file that is saved at file_path. All map key/value pair are stored on
   *   separate lines.
   * @param arduino_key Holds the key/name of the arduino on the hash map
   * @param file_path Holds the path to the file where the hash map is stored
   * @param line_div Holds the character that divides the maps keys and map values
   * @return Name of port to which the arduino identified on the hash map is connected
   */
  public static String getSerialPortName(String arduino_key, String file_path, char line_div) throws FileNotFoundException, IOException {
    String sID = null;
      BufferedReader reader = new BufferedReader(new FileReader(file_path));
      for(String line = reader.readLine(); line != null; line = reader.readLine()){
        String[] pieces = PApplet.split(line,line_div);
        if (pieces.length == 2 && pieces[0].equals(arduino_key)){
          sID = pieces[1];
          PApplet.println(arduino_key + " [getSerialName]: found arduino key in map " + pieces[0] + " : " + pieces[1]);
          break;
        }
      }
      reader.close();
    //if it isn't in the map, return false (failed)
    if (sID == null){
      return null;
    }
    
    return ArduinoConnect.getSerialPortName(sID);
  }

  /**
   * getSerialPortName Returns the serial port name by searching a hashmap the arduino_key. The maps it 
   *   searches is stored in a file that is saved at file_path.
   * @param arduino_key Holds the key/name of the arduino on the hash map
   * @param file_path Holds the path to the file where the hash map is stored
   * @return Name of port to which the arduino identified on the hash map is connected
   */
  public static String getSerialPortName(String arduino_key, String file_path) throws FileNotFoundException, IOException {
    return getSerialPortName(arduino_key, file_path, ':');
  }

  
  /**
   * getSerialPortName Returns the serial port name where the arduino with the serial id specified by
   *   the arduino_serial_id parameter is connected. If the arduino is not it returns null.
   * @param arduino_serial_id Holds the unique serial id of the arduino that we want to connect to
   * @return Name of port to which the arduino identified on the hash map is connected
   */
  public static String getSerialPortName(String arduino_serial_id){
      //assuming OSX
    String getUsbArgs[] = new String[2];
    getUsbArgs[0]="system_profiler";
    getUsbArgs[1]="SPUSBDataType";
    Matcher idMatch;
    try {
      Process process = new ProcessBuilder(getUsbArgs).start();
      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line;
      String currSerial = null;
        String currLocation = null;
  
          boolean foundArduino = false;
          int serialNumPosition, locationPosition; 
          while ( (line = br.readLine ()) != null) {
            serialNumPosition = line.indexOf(SERIAL_NUMBER);
            if (serialNumPosition >= 0) {
              currSerial = line.substring(serialNumPosition + SERIAL_NUMBER.length());
                if (!currSerial.equals(arduino_serial_id)){
                  currSerial = null;
                }
            } else if (currSerial != null && (line.indexOf("Arduino") >= 0  || line.indexOf("FT232R") >= 0 || line.indexOf("Vendor ID: 0x20a0") >= 0)) { //Vendor ID: 0x20a0 is freetronics
              foundArduino = true;
            } else if (foundArduino && currSerial != null) {
              locationPosition = line.indexOf(LOCATION);
                if (locationPosition >= 0){
                  currLocation = line.substring(locationPosition + LOCATION.length());
                  idMatch = REGEX_PAT.matcher(currLocation);
                  if (idMatch.groupCount() > 0 && idMatch.find()){
                    return "/dev/tty.usbmodem"+idMatch.group(1)+"1";
                  }    
            currSerial = null;
            currLocation = null;
            foundArduino = false;
                }
            }
          }
    }
    catch(IOException e) {
      System.out.println(e.getMessage());
      }
      return null;
    }

  /**
   * printArduinoSerialNumber Prints to the console the unique serial id number for all Arduinos that are currently 
   *   connected to the computer. This code will probably not work with a lot of Arduino clones, though it could 
   *   easily be modified to do so.
   * @return returns a Hashtable with the unique serial ids and port name of each arduino 
   */
  public static Hashtable  <String, String> printArduinoSerialNumber() {
    //assuming OSX
    String getUsbArgs[] = new String[2];
    getUsbArgs[0]="system_profiler";
    getUsbArgs[1]="SPUSBDataType";
    Hashtable <String, String> serialNums = new Hashtable  <String, String> ();
    Matcher idMatch;
    String serialIn;
    try {
      Process process = new ProcessBuilder(getUsbArgs).start();
      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line;
      String currSerial = null;
      String currLocation = null;
      boolean foundArduino = false;
      int serialNumPosition, locationPosition;
      int arduinoCount = 0;
  
      if (DEBUG) PApplet.println("[findSerialNumbers] reading USB device information"); 
      while ( (line = br.readLine ()) != null) {
        if (DEBUG) PApplet.println(line);
        serialNumPosition = line.indexOf(SERIAL_NUMBER);
        if (serialNumPosition >= 0) {
          currSerial = line.substring(serialNumPosition + SERIAL_NUMBER.length());
        } else if (line.indexOf("Arduino") >= 0  || line.indexOf("FT232R") >= 0 || line.indexOf("Vendor ID: 0x20a0") >= 0) { //Vendor ID: 0x20a0 is freetronics
          foundArduino = true;
        } else if (foundArduino && currSerial != null) {
          locationPosition = line.indexOf(LOCATION);
          if (locationPosition >= 0){
            currLocation = line.substring(locationPosition + LOCATION.length());
            idMatch = REGEX_PAT.matcher(currLocation);
            if (idMatch.groupCount() > 0 && idMatch.find()){
              serialIn = "/dev/tty.usbmodem" + idMatch.group(1) + "1";
              serialNums.put(currSerial, serialIn);
              PApplet.println("Arduino Device - " + arduinoCount++);
              PApplet.println("  - Arduino ID: " + currSerial);
              PApplet.println("  - Serial Port: " + serialIn);
              PApplet.println();
            } else {
              PApplet.println("ERROR: ");
              PApplet.println(currSerial + " : " + currLocation);
            }
            currSerial = null;
            currLocation = null;
            foundArduino = false;
          }
        }
      }
    }
    catch(IOException e) {
      PApplet.println(e.getMessage());
      System.out.println(e.getMessage());
    }
    return serialNums;
  }

}
