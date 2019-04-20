package com.xmonit.solar.arduino.data.sensor;

import com.xmonit.solar.arduino.data.DoubleObject;
import com.xmonit.solar.arduino.data.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Sensor extends DoubleObject {

	public String name;
	public Status status;

	public long receivedTimeMs;

	public Sensor() {
	}

	public Sensor(Integer id, String type, String name, Double value) {
		this(id, type, name, value, null);
	}

	public Sensor(Integer id, String type, String name, Double value, Status status) {
		super(id, type, value);
		this.name = name;
		this.status = status;
	}

	@Override
	public double getValue() {
		if (status == null || status.code == null || status.code == 0) {
			return super.getValue();
		} else {
			return Double.NaN; // value for monitoring system (prometheus) when there is an error
		}
	}

	@Override
	public String getTitle() {
		return name;
	}

	@Override
	public String toString() {
	  	return super.toString();
	}
}
