package com.xmonit.solar.arduino.data.sensor;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompositeSensor extends Sensor {
	public String function; // function used to compute value from the list of sensors
	public List<Sensor> sensors;

	@Override
	public String toString() {
	  	return super.toString();
	}
}
