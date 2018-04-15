package com.xmonit.solar.serialbus;

import java.io.PrintWriter;


public class SerialBusAbout {

    class CommandParamInfo {
        String name;
        String values[][];
        CommandParamInfo(String name, String[][] values) {
            this.name = name;
            this.values = (values == null) ? new String[][]{} : values;
        }
    }

    class CommandInfo {
        String name;
        String description;
        CommandParamInfo params[];

        CommandInfo(String name, String description, CommandParamInfo[] params) {
            this.name = name;
            this.description = description;
            this.params = (params == null) ? new CommandParamInfo[]{} : params;
        }

        void printInfo(PrintWriter pw) {
            pw.print(name);
            for( CommandParamInfo param : params ) {
                pw.print(",{");
                pw.print(param.name);
                pw.print("}");
            }
            pw.println();
            pw.println();
            pw.print("\t");
            pw.println(description);
            pw.println();
            printParams(pw);
        }

        void printParams(PrintWriter pw) {
            for (CommandParamInfo paramInfo : params) {
                pw.print("\t");
                pw.print(paramInfo.name);
                pw.println(":");
                for (String[] valueAndDescription : paramInfo.values) {
                    pw.print("\t\t\t");
                    pw.print(valueAndDescription[0]);
                    pw.print(" - ");
                    pw.println(valueAndDescription[1]);
                }
            }
            if ( params.length > 0 ) {
                pw.println();
            }
        }
    }

    CommandParamInfo persistParam = new CommandParamInfo("persistence", new String[][]{
            {"PERSIST", "Save to EEPROM and remember after Arduino reboot"},
            {"TRANSIENT","Do not save to EEPROM"},
    });

    CommandInfo commands[] = new CommandInfo[]{
            new CommandInfo("GET", "List status and configuration of devices (JSON format).", null),
            new CommandInfo("VERSION", "Show version number and build date", null),
            new CommandInfo("SET_OUTPUT_FORMAT", "Arduino serial bus responses format",
                    new CommandParamInfo[]{
                            new CommandParamInfo("format", new String[][]{
                                    {"JSON_COMPACT", "Minimize white space in JSON"},
                                    {"JSON_PRETTY","Use indentation and white space (DEFAULT)"},
                            }),
                            persistParam
                    }),
            new CommandInfo("SET_FAN_MODE", "Turn on or off all fans (or set to automatic)",
                    new CommandParamInfo[]{
                        new CommandParamInfo("mode", new String[][]{
                                {"ON", "Turn on all fans"},
                                {"OFF","Turn off all fans"},
                                {"AUTO","Automatically control fans using configured thresholds (DEFAULT)"}
                        }),
                        persistParam
            }),
            new CommandInfo("SET_FAN_THRESHOLDS", "Change the temperatures which cause a fan to turn on/off (fahrenheit).",
                    new CommandParamInfo[]{
                            new CommandParamInfo("device filter", new String[][]{
                                    {"*", "Match all devices"},
                                    {"{string}","Device name must start with or equal this"},
                            }),
                            new CommandParamInfo("fan filter", new String[][]{
                                    {"*", "Match all fans for each device matched"},
                                    {"{string}","Fan name must start with or equal this for each device matched"},
                            }),
                            new CommandParamInfo("on temp", new String[][]{
                                    {"{numeric}", "Fan on threshold (fahrenheit)"},
                            }),
                            new CommandParamInfo("off temp", new String[][]{
                                    {"{numeric}", "Fan off threshold (fahrenheit)"},
                            }),
                            persistParam,
                    }),
            new CommandInfo("SET_POWER_METER", "Calibrate voltage drop and resistors on the internal voltage divider.",
                    new CommandParamInfo[]{
                            new CommandParamInfo("component", new String[][]{
                                    {"VCC", "Defaults to 5 volts but power source to Arduino could make it higher or lower"},
                                    {"R1","Resistor 1 of voltage divider (example 'value': 1010000.0)"},
                                    {"R2","Resistor 2 of voltage divider (example 'value': 100500.0)"},
                            }),
                            new CommandParamInfo("power meter filter", new String[][]{
                                    {"*", "Match all power meters"},
                                    {"{string}","Power meter name must start with or equal this"},
                            }),
                            new CommandParamInfo("value", new String[][]{
                                    {"{numeric}", "Value assigned to the component (volts if component=VCC, otherwise OHMS)"},
                            }),
                            persistParam,
                    })
    };



    public void printHelp(PrintWriter pw) {
        pw.println("Command syntax (commands sent to Arduino via USB):");
        pw.println();
        for ( CommandInfo cmd : commands) {
            cmd.printInfo(pw);
        }
        pw.println();
        pw.println("Initialization configuration options");
        pw.println("\tcommPortRegEx: regular expression used to find arduino serial port.  Linux will usually be /dev/tty... ");
        pw.println("\t\tExample: ttyACM.*");
        pw.println("\tbaudRate: USB baud rate (long USB cables or electronic interference may decrease maximum reliable speed)");
        pw.println("\t\tExample: 57600");

        pw.flush();

    }

}

