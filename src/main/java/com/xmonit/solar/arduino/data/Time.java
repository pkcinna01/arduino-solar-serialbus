package com.xmonit.solar.arduino.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmonit.solar.arduino.json.ArduinoMapper;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Time {

    public Integer year, month, day, hour, minute, second;
    public Boolean timeSet;

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    public void set(LocalDateTime time) {
        year = time.getYear();
        month = time.getMonthValue();
        day = time.getDayOfMonth();
        hour = time.getHour();
        minute = time.getMinute();
        second = time.getSecond();
    }

    @Override
    public String toString() {
        try {
            return ArduinoMapper.instance.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return super.toString();
        }
    }
}
