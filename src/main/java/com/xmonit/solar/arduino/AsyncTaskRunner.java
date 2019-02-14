package com.xmonit.solar.arduino;

import com.xmonit.solar.arduino.serial.ArduinoSerialBus;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncTaskRunner {

    public interface AsyncTask<ReturnT> {
        public abstract TaskResult<ReturnT> exec(ArduinoSerialBus bus) throws ArduinoException;
    }

    public static class TaskResult<ReturnT> {
        public ReturnT data;
        public Integer id;
        public String name;

        public TaskResult(String name, Integer id, ReturnT data) {
            this.name = name;
            this.id = id;
            this.data = data;
        }
    }

    public <ReturnT> List<TaskResult<ReturnT>> process(Collection<ArduinoSerialBus> buses, AsyncTask<ReturnT> task) throws ArduinoException {
        List<TaskResult<ReturnT>> rtnList = new LinkedList<>();
        ExecutorService EXEC = Executors.newCachedThreadPool();
        List<Callable<TaskResult<ReturnT>>> tasks = new ArrayList<Callable<TaskResult<ReturnT>>>();
        for (ArduinoSerialBus bus : buses) {
            Callable<TaskResult<ReturnT>> c = new Callable<TaskResult<ReturnT>>() {
                @Override
                public TaskResult<ReturnT> call() throws ArduinoException {
                    return task.exec(bus);
                }
            };
            tasks.add(c);
        }
        try {
            List<Future<TaskResult<ReturnT>>> results = EXEC.invokeAll(tasks);
            for (Future<TaskResult<ReturnT>> future : results) {
                rtnList.add(future.get());
            }
        } catch (Exception ex) {
            throw new ArduinoException("Failed async task", ex);
        }
        return rtnList;
    }


}
