package com.xmonit.solar.arduino.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class DoubleObject extends DomainObject {

    public Double value;

    public DoubleObject() {}

    public DoubleObject(Integer id, String type, Double value) {
        super(id,type);
        this.value = value;
    }

    public double getValue() {
        return value == null ? Double.NaN : value.doubleValue();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
