# arduino-solar-serialbus

## Synopsis

Library (java jar) supporting communication with Arduino Solar project over USB


## SerialBus.execute(String command) 

```
Command syntax (commands sent to Arduino via USB):

GET

	List status and configuration of devices (JSON format).

VERSION

	Show version number and build date

SET_OUTPUT_FORMAT,{format},{persistence}

	Arduino serial bus responses format

	format:
			JSON_COMPACT - Minimize white space in JSON
			JSON_PRETTY - Use indentation and white space (DEFAULT)
	persistence:
			PERSIST - Save to EEPROM and remember after Arduino reboot
			TRANSIENT - Do not save to EEPROM

SET_FAN_MODE,{mode},{persistence}

	Turn on or off all fans (or set to automatic)

	mode:
			ON - Turn on all fans
			OFF - Turn off all fans
			AUTO - Automatically control fans using configured thresholds (DEFAULT)
	persistence:
			PERSIST - Save to EEPROM and remember after Arduino reboot
			TRANSIENT - Do not save to EEPROM

SET_FAN_THRESHOLDS,{device filter},{fan filter},{on temp},{off temp},{persistence}

	Change the temperatures which cause a fan to turn on/off (fahrenheit).

	device filter:
			* - Match all devices
			{string} - Device name must start with or equal this
	fan filter:
			* - Match all fans for each device matched
			{string} - Fan name must start with or equal this for each device matched
	on temp:
			{numeric} - Fan on threshold (fahrenheit)
	off temp:
			{numeric} - Fan off threshold (fahrenheit)
	persistence:
			PERSIST - Save to EEPROM and remember after Arduino reboot
			TRANSIENT - Do not save to EEPROM

SET_POWER_METER,{component},{power meter filter},{value},{persistence}

	Calibrate voltage drop and resistors on the internal voltage divider.

	component:
			VCC - Defaults to 5 volts but power source to Arduino could make it higher or lower
			R1 - Resistor 1 of voltage divider (example 'value': 1010000.0)
			R2 - Resistor 2 of voltage divider (example 'value': 100500.0)
	power meter filter:
			* - Match all power meters
			{string} - Power meter name must start with or equal this
	value:
			{numeric} - Value assigned to the component (volts if component=VCC, otherwise OHMS)
	persistence:
			PERSIST - Save to EEPROM and remember after Arduino reboot
			TRANSIENT - Do not save to EEPROM


Initialization configuration options
	commPortRegEx: regular expression used to find arduino serial port.  Linux will usually be /dev/tty... 
		Example: ttyACM.*
	baudRate: USB baud rate (long USB cables or electronic interference may decrease maximum reliable speed)
		Example: 57600
```

## Motivation

Encapsulate USB and Solar API into single jar that can be used in multiple projects

## Installation

Maven and Java JDK 1.8+ are required.  Example build:

Build jar from project folder:
```
# build jar and make available in local repo for other projects
mvn clean install

```
