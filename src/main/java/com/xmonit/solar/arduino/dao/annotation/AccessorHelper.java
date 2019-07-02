package com.xmonit.solar.arduino.dao.annotation;

import com.xmonit.solar.arduino.SerialCmd;
import com.xmonit.solar.arduino.dao.Dao;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AccessorHelper {


    public static Dao.FieldMetaData getFieldMetaData(Method method) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class rtnClass = method.getReturnType();
        if (SerialCmd.FieldAccessor.class.isAssignableFrom(rtnClass)) {
            Class[] accessorTypes = {IntegerAccessor.class, DoubleAccessor.class,StringAccessor.class,BooleanAccessor.class,ChoiceAccessor.class,ConstraintAccessor.class};
            for( Class accessorType : accessorTypes) {
                Object fieldAccessor = method.getAnnotation(accessorType);
                if ( fieldAccessor != null ) {
                    Class fac = fieldAccessor.getClass();
                    Method m = fac.getMethod("validationRegEx");
                    String validationRegEx = (String) m.invoke(fieldAccessor);
                    m = fac.getMethod("value");
                    String value = (String) m.invoke(fieldAccessor);
                    m = fac.getMethod("readOnly");
                    boolean readOnly = (boolean) m.invoke(fieldAccessor);
                    String type = accessorType.getSimpleName().replaceAll("Accessor$","");
                    if (StringUtils.isEmpty(value)) {
                        value = method.getName();
                    }
                    return new Dao.FieldMetaData(value, type, readOnly, validationRegEx);
                }
            }
        }
        return null;
    }
}
