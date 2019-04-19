# arduino-solar-serialbus

## Synopsis

Library (java jar) supporting communication with Arduino Solar project over USB


## Example call to a power switch 

```
//Get a local copy of a power switch from an arduino
PowerSwitch orig = dao.get(deviceId);
LogicLevel relayOnSignalVal = orig.relayOnSignal;

//Toggle an individual field of a power switch
PowerSwitchDao.RelayOnSignalAccessor relayOnSignalAccessor = dao.relayOnSignal(deviceId);
LogicLevel newRelayOnSignalVal = relayOnSignalVal == LogicLevel.HIGH ? LogicLevel.LOW : LogicLevel.HIGH;
relayOnSignalAccessor.set(newRelayOnSignalVal);

//Confirm value changed using field accessor
LogicLevel newValueFromFieldAccessor = relayOnSignalAccessor.get();
assertEquals(newRelayOnSignalVal,newValueFromFieldAccessor);

//Get another local copy and confirm value changed
PowerSwitch s1 = dao.get(deviceId);
assertEquals(newRelayOnSignalVal,s1.relayOnSignal);
        
//Set power switch back to original value
relayOnSignalAccessor.set(orig.relayOnSignal);
```

# build jar and make available in local repo for other projects
```
mvn clean install

```
