package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.SerialBusTestSetup;
import com.xmonit.solar.arduino.data.DomainObject;

import static junit.framework.TestCase.assertEquals;

public class DaoTestBase<DaoType extends Dao, DataT extends DomainObject> extends SerialBusTestSetup {

    DaoType dao;

    @org.junit.Test
    public void doListTest() throws ArduinoException {
        DataT[] items = dao.list();
        for (DataT item : items) {
            System.out.println(item);
        }
    }

    @org.junit.Test
    public void doVerboseListTest() throws ArduinoException {
        boolean bOldVerbose = dao.getVerbose();
        dao.setVerbose(true);
        DataT[] items = dao.list();
        for (DataT item : items) {
            System.out.println(item);
        }
        dao.setVerbose(bOldVerbose);
    }

    @org.junit.Test
    public void doFilterTest() throws ArduinoException {
        DataT[] foundList = dao.findByTitleLike("*Switch*");
        DataT[] othersList = dao.findByTitleNotLike("*Switch*");
        System.out.println("====================Titles with Switch==========================");
        for( DataT i : foundList ) {
            System.out.println(i);
        }
        System.out.println("====================Titles without Switch==========================");
        for( DataT i : othersList ) {
            System.out.println(i);
        }
        DataT[] all = dao.list();
        assertEquals( all.length, foundList.length + othersList.length);
    }


    @org.junit.Test
    public void doGetTest() throws ArduinoException {
        DataT item = dao.get(1);
        DataT[] list = dao.list();
        System.out.println(item);
        DataT listItem = null;
        for ( DataT i : list ) {
            if ( i.id == item.id ) {
                System.out.println(i);
                listItem = i;
                break;
            }
        }
        assertEquals( listItem.getTitle(), item.getTitle());
    }


}
