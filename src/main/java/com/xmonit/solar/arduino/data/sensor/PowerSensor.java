package com.xmonit.solar.arduino.data.sensor;

import com.xmonit.solar.arduino.data.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PowerSensor extends ArduinoSensor {

	public CurrentSensor current;
	public VoltageSensor voltage;

	public PowerSensor() {
	}

	public PowerSensor(Integer id, String type, String name, Double value, Status status) {
		super(id, type, name, value, status);
	}

	@Override
	public String toString() {
	  	return super.toString();
	}
}
