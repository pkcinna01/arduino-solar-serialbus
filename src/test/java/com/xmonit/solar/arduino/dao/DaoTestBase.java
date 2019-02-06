package com.xmonit.solar.arduino.dao;

import com.xmonit.solar.arduino.ArduinoException;
import com.xmonit.solar.arduino.SerialBusTestSetup;
import com.xmonit.solar.arduino.data.DomainObject;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class DaoTestBase<DaoType extends Dao, DataT extends DomainObject> extends SerialBusTestSetup {

    protected DaoType dao;

    public void doListTest() throws ArduinoException {
        boolean bVerbose = false;
        DataT[] items = dao.list(bVerbose);
        for (DataT item : items) {
            System.out.println(item);
        }
    }

    public void doVerboseListTest() throws ArduinoException {
        boolean bVerbose = true;
        DataT[] items = dao.list(bVerbose);
        for (DataT item : items) {
            System.out.println(item);
        }
    }

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

    public void doFindWhereIdInTest() throws ArduinoException {
        int[] ids = { 1, 2 };
        DataT[] items = dao.findWhereIdIn(ids);

        assertEquals(ids.length, items.length);
        for( int i = 0; i < ids.length; i++ ) {
            assertTrue( items[i].id == ids[i]);
            System.out.println(items[i]);
        }
    }

}
