package com.xmonit.solar.arduino.data;

import org.junit.Test;

public class EnvironmentTest extends Environment {

	@Test
	public void dateTimeTest() {
		Environment env = new Environment();
		env.time = "1970-1-1 2:35:28";
		System.out.println(env.getDateTime());
	}

}
