/*
 * Copyright 2015 Kaazing Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kaazing.staf_aeron;

import com.ibm.staf.*;
import com.ibm.staf.wrapper.*;
import com.ibm.staf.service.*;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class AeronSTAFProcess
{
    private String machine;
    private String command;
    private String name;
    private STAFHandle handle;
    private CountDownLatch completionLatch = null;
    private int timeout;
    private STAFResult result;
    private int pid = 0;
    protected static final String SERVICE = "Process";

    public AeronSTAFProcess()
    {

    }

    public AeronSTAFProcess(String machine, String command, String name, CountDownLatch completionLatch, int timeout)
    {
        this.machine = machine;
        this.command = command;
        //System.out.println("Command: " + command);
        this.completionLatch = completionLatch;
        this.timeout = timeout;
        this.name = name;

        try {
            handle = new STAFHandle(name);
        } catch (STAFException e) {
            e.printStackTrace();
        }
    }

    public AeronSTAFProcess run()
    {
        try {
            final String request = "START SHELL COMMAND " + command +
                    " WAIT " + timeout + "s RETURNSTDOUT STDERRTOSTDOUT";
            Runnable task = () -> {
                result = handle.submit2(machine, SERVICE, request);
                    if (result.rc != 0) {
                        try {
                            //final Map resultMap = (Map) result.resultObj;
                            System.out.println("ERROR " + name + " " + result.result);
                            System.out.println("Test Failure");
                            PrintWriter output = new PrintWriter(name + ".log");
                            output.println("ERROR: Process RC is not 0.\n");
                            //final List returnedFileList = (List) resultMap.get("fileList");
                            //final Map stdoutMap = (Map) returnedFileList.get(0);
                            //System.out.println((String) stdoutMap.get("data"));
                            output.close();
                            System.exit(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.exit(1);
                    }

                    try {
                        final Map resultMap = (Map) result.resultObj;
                        final String processRC = (String) resultMap.get("rc");

                        if (!processRC.equals("0")) {
                            System.out.println("Test Failure");
                            PrintWriter output = new PrintWriter(name + ".log");
                            output.println("ERROR: Process RC is not 0.\n");
                            final List returnedFileList = (List) resultMap.get("fileList");
                            final Map stdoutMap = (Map) returnedFileList.get(0);
                            System.out.println((String) stdoutMap.get("data"));
                            output.close();
                            System.exit(1);
                        } else {
                            System.out.println("TEST: " + name + " passed!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            };

            final Thread work = new Thread(task);
            work.start();

            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final String request2 = "LIST HANDLES LONG";
            STAFHandle handle2 = new STAFHandle("Test");
            STAFResult result2 = handle2.submit2(machine, SERVICE, request2);
            try {
                final LinkedList resultMap2 = (LinkedList) result2.resultObj;
                for (Object item : resultMap2) {
                    HashMap map = (HashMap)item;

                    if (((String)map.get("command")).trim().equalsIgnoreCase(command.trim())) {
                        pid = Integer.parseInt((String)map.get("pid")) + 1;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            completionLatch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public void kill()
    {
        try {
            final STAFHandle killHandle = new STAFHandle("kill-" + name);
            final String request = "START SHELL COMMAND " + STAFUtil.wrapData("kill " + pid) + " WAIT RETURNSTDOUT STDERRTOSTDOUT";

            final STAFResult result = killHandle.submit2(machine, SERVICE, request);
            if (result.rc != 0) {
                System.out.println("ERROR: STAF " + machine + " " + SERVICE + " " + request +
                        " RC: " + result.rc + ", Result: " + result.result);
                System.exit(1);
            }
            killHandle.unRegister();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause()
    {
        try {
            final STAFHandle pauseHandle = new STAFHandle("pause-" + name);
            final String request = "START SHELL COMMAND `kill -19 " + pid +
                    "` WAIT " + timeout + "s RETURNSTDOUT STDERRTOSTDOUT";

            final STAFResult result = pauseHandle.submit2(machine, SERVICE, request);
            if (result.rc != 0) {
                System.out.println("ERROR: STAF " + machine + " " + SERVICE + " " + request +
                        " RC: " + result.rc + ", Result: " + result.result);
                System.exit(1);
            }
            //System.out.println(result.result);
            pauseHandle.unRegister();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resume()
    {
        try {
            final STAFHandle resumeHandle = new STAFHandle("resume-" + name);
            final String request = "START SHELL COMMAND " + STAFUtil.wrapData("kill -18 " + pid) +
                    " WAIT " + timeout + "s RETURNSTDOUT STDERRTOSTDOUT";
            final STAFResult result = resumeHandle.submit2(machine, SERVICE, request);
            if (result.rc != 0) {
                System.out.println("ERROR: STAF " + machine + " " + SERVICE + " " + request +
                        " RC: " + result.rc + ", Result: " + result.result);
                System.exit(1);
            }
            //System.out.println(result.result);
            resumeHandle.unRegister();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map getResults()
    {
        return (Map)result.resultObj;
    }
}
