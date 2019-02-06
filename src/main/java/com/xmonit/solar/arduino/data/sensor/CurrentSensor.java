package com.xmonit.solar.arduino.data.sensor;

import com.xmonit.solar.arduino.data.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CurrentSensor extends ArduinoSensor {

	public enum Channel { CHANNEL_A0, CHANNEL_A1, CHANNEL_A2, CHANNEL_A3, DIFFERENTIAL_0_1, DIFFERENTIAL_2_3 };
	public enum Gain { GAIN_ONE, GAIN_TWO, GAIN_FOUR, GAIN_EIGHT, GAIN_SIXTEEN, GAIN_TWOTHIRDS };

	public Gain gain;
	public Channel channel;
	public Double shuntADC, ratedOhms;
	public Integer millivoltIncrement, ratedAmps, ratedMillivolts;

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
