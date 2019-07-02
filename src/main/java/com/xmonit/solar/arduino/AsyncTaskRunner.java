package com.xmonit.solar.arduino;

import com.xmonit.solar.arduino.serial.ArduinoSerialBus;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
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
        } catch (ExecutionException ex) {
            log.error("Failed async task", ex);
            Throwable cause = ex.getCause();
            if ( cause instanceof ArduinoException ) {
                throw (ArduinoException) cause;
            } else {
                throw new ArduinoException("Failed async task", ex);
            }
        } catch (Exception ex) {
            log.error("Failed async task", ex);
            throw new ArduinoException("Failed async task", ex);
        }
        return rtnList;
    }


}
