package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.dao.constraint.ConstraintDao;
import com.xmonit.solar.arduino.dao.device.PowerSwitchDao;
import com.xmonit.solar.arduino.data.LogicLevel;
import com.xmonit.solar.arduino.data.constraint.Constraint;
import com.xmonit.solar.arduino.data.device.Device;
import com.xmonit.solar.arduino.data.device.PowerSwitch;
import org.junit.Before;

import static junit.framework.TestCase.assertEquals;

public class PowerSwitchDaoTest extends DomainDaoTestBase<PowerSwitchDao, PowerSwitch> {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new PowerSwitchDao(serialBus);
    }
    
    @org.junit.Test
    public void doListPowerSwitchsTest() throws ArduinoException {
        PowerSwitch[] powerSwitches = dao.listPowerSwitches();
        for ( PowerSwitch s : powerSwitches ) {
            System.out.println(s);
        }
    }

    public int deviceId = 1;

    @org.junit.Test
    public void setModeTest() throws ArduinoException {
        Device orig = dao.get(deviceId);
        Constraint.Mode newMode = orig.constraint.mode != Constraint.Mode.TEST ? Constraint.Mode.TEST : Constraint.Mode.REMOTE_OR_TEST;
        ConstraintDao.ModeAccessor mode = dao.mode(deviceId);
        mode.set(newMode);
        Constraint c = dao.constraint(deviceId).get();
        System.out.println(c.toString() );
        assertEquals(newMode,c.mode);
        mode.set(orig.constraint.mode);
    }

    @org.junit.Test
    public void setOnTest() throws ArduinoException {
        PowerSwitch orig = dao.get(deviceId);

        Constraint.Mode newMode = null;
        ConstraintDao.ModeAccessor mode = dao.mode(deviceId);
        if ( orig.constraint.mode != Constraint.Mode.REMOTE_OR_TEST ) {
            mode.set(Constraint.Mode.REMOTE_OR_TEST);
        }
        Boolean bNewOn = orig.on ? false : true;
        PowerSwitchDao.OnAccessor onAccessor = dao.on(deviceId);
        onAccessor.set(bNewOn);
        PowerSwitch s1 = dao.get(deviceId);
        System.out.println(s1.toString() );
        assertEquals(bNewOn,s1.on);
        onAccessor.set(orig.on);
        if ( orig.constraint.mode != Constraint.Mode.REMOTE_OR_TEST) {
            mode.set(orig.constraint.mode);
        }
    }

    @org.junit.Test
    public void setRelayOnSignalTest() throws ArduinoException {

        //Get a local copy of a power switch from an arduino
        PowerSwitch orig = dao.get(deviceId);
        LogicLevel relayOnSignalVal = orig.relayOnSignal;

        //Set and then get an individual field of a power switch
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
    }


}
