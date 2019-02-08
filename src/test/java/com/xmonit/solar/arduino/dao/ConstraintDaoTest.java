package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.dao.constraint.ConstraintDao;
import com.xmonit.solar.arduino.data.constraint.Constraint;
import org.junit.Before;

import static junit.framework.TestCase.assertEquals;

public class ConstraintDaoTest extends DaoTestBase<ConstraintDao, Constraint> {

    int constraintId = 1;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new ConstraintDao(serialBus);
    }
    
    @org.junit.Test
    public void doListTest() throws ArduinoException {
        super.doListTest();
    }

    @org.junit.Test
    public void doVerboseListTest() throws ArduinoException {
        super.doVerboseListTest();
    }

    @org.junit.Test
    public void doFilterTest() throws ArduinoException {
        super.doFilterTest();
    }


    @org.junit.Test
    public void doGetTest() throws ArduinoException {
        super.doGetTest();
    }

    @org.junit.Test
    public void doFindWhereIdInTest() throws ArduinoException {
        super.doFindWhereIdInTest();
    }

    @org.junit.Test
    public void getModeTest() throws ArduinoException {
        Constraint.Mode mode = dao.mode(constraintId).get();
        System.out.println("mode: " + mode.name());
        Constraint c = dao.get(constraintId);
        System.out.println(c.toString() );
        assertEquals(c.mode,mode);
    }

    @org.junit.Test
    public void setModeTest() throws ArduinoException {
        Constraint orig = dao.get(constraintId);
        Constraint.Mode newMode = orig.mode != Constraint.Mode.TEST ? Constraint.Mode.TEST : Constraint.Mode.REMOTE_OR_TEST;
        ConstraintDao.ModeAccessor mode = dao.mode(constraintId);
        mode.set(newMode);
        Constraint c = dao.get(constraintId);
        System.out.println(c.toString() );
        assertEquals(c.mode,newMode);
        mode.set(orig.mode);
    }

    @org.junit.Test
    public void getPassedTest() throws ArduinoException {
        Boolean bPassed = dao.passed(constraintId).get();
        System.out.println("passed: " + bPassed);
        Constraint c = dao.get(constraintId);
        System.out.println(c.toString() );
        assertEquals(c.passed,bPassed);
    }

    @org.junit.Test
    public void setPassedTest() throws ArduinoException {
        try {
            dao.pauseConstraintProcessing(30);
            Constraint orig = dao.get(constraintId);
            boolean bNewPassed = !orig.passed;
            ConstraintDao.PassedAccessor passedAccessor = dao.passed(constraintId);
            passedAccessor.set(bNewPassed);
            Constraint c = dao.get(constraintId);
            System.out.println(c.toString());
            assertEquals((boolean) c.passed, bNewPassed);
            passedAccessor.set(orig.passed);
        } finally {
            dao.resumeConstraintProcessing();
        }
    }

}
