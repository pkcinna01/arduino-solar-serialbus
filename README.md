# arduino-solar-serialbus

## Synopsis

Library (java jar) supporting communication with Arduino Solar project over USB


## Supported string commands via API SerialBus.execute() call

```
	GET
		List status and configuration of devices. Default: GET
	SET_FAN_MODE,{ON|OFF|AUTO},{PERSIST|TRANSIENT}
		Turn ON or OFF all fans.
		AUTO to let Arduino control them using configured thresholds 
		PERSIST will remember fan mode after arduino reboots.
	SET_FAN_THRESHOLDS,{device filter},{fan filter},{on temp},{off temp},{PERSIST|TRANSIENT}
		Change the temperatures which cause a fan to turn on/off (fahrenheit).
		Use * for filter to match all devices or all fans. Otherwise the filter
		will match if the device or fan name contains the filter string.

		
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
