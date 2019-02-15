package com.xmonit.solar.arduino.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConstraintAccessor {
    public String value() default "";
    public boolean readOnly() default true;
    public String validationRegEx() default "";
}