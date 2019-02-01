# arduino-solar-serialbus

## Synopsis

Library (java jar) supporting communication with Arduino Solar project over USB


## SerialBus.execute(String command) 

```
Command syntax (commands sent to Arduino via USB):

#NOTICE This project is being reworked... get/read functions have been added but set/write not added yet.#

## Motivation

Encapsulate USB and Solar API into single jar that can be used in multiple projects

## Installation

Maven and Java JDK 1.8+ are required.  Example build:

Build jar from project folder:
```
# build jar and make available in local repo for other projects
mvn clean install

```
