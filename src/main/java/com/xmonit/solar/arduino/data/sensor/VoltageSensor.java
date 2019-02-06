package com.xmonit.solar.arduino.data.sensor;

import com.xmonit.solar.arduino.data.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VoltageSensor extends ArduinoSensor {

	public Double vcc, r1, r2, maxVccAgeMs;


	public VoltageSensor() {
	}

	public VoltageSensor(Integer id, String type, String name, Double value, Status status) {
		super(id, type, name, value, status);
	}

	@Override
	public String toString() {
	  	return super.toString();
	}
}
