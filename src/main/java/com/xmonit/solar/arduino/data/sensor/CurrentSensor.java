package com.xmonit.solar.arduino.data.sensor;

import com.xmonit.solar.arduino.data.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CurrentSensor extends ArduinoSensor {

	public String channel, gain;
	public Double shuntADC, ratedResistance;
	public Integer millivoltIncrement, ratedAmps, ratedMilliVolts;

	public CurrentSensor() {
	}

	public CurrentSensor(Integer id, String type, String name, Double value, Status status) {
		super(id, type, name, value, status);
	}

	@Override
	public String toString() {
	  	return super.toString();
	}
}
