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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class AeronSTAFProcess
{
    private STAFHost machine;
    private String command;
    private String name;
    private STAFHandle handle;
    private CountDownLatch completionLatch = null;
    private int timeout;
    private STAFResult result;
    private int pid = 0;
    protected static final String SERVICE = "Process";
    private String request = "";

    //public AeronSTAFProcess()
    //{

    //}

    public AeronSTAFProcess(STAFHost machine, String command, String name, CountDownLatch completionLatch, int timeout)
    {
        this.machine = machine;
        this.command = command;
        this.completionLatch = completionLatch;
        this.timeout = timeout;
        this.name = name;

        try {
            handle = new STAFHandle(name);
            run();
        } catch (STAFException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        System.out.println("\n\n" + command);
        try {
            if (timeout > 0) {
                request = "START SHELL COMMAND " + command +
                        " WAIT " + timeout + "s RETURNSTDOUT STDERRTOSTDOUT";
            } else {
                request = "START SHELL COMMAND " + command +
                        " WAIT " + timeout + "s RETURNSTDOUT STDERRTOSTDOUT";
            }

            Runnable task = () -> {
                PrintWriter output = null;

                try {
                    output = new PrintWriter(name + ".log");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                result = handle.submit2(machine.getHostName(), SERVICE, request);
                if (result.rc != 0) {
                    try {
                        System.out.println("Test: " + name + " failed! Timed out");
                        output.println("Test Failure: " + name + " " + "Timed out.\n");
                        final Map resultMap = (Map) result.resultObj;
                        final String processRC = (String) resultMap.get("rc");
                        final List returnedFileList = (List) resultMap.get("fileList");
                        final Map stdoutMap = (Map) returnedFileList.get(0);
                        System.out.println((String) stdoutMap.get("data"));
                        output.println((String) stdoutMap.get("data"));
                        kill();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        final Map resultMap = (Map) result.resultObj;
                        final String processRC = (String) resultMap.get("rc");

                        if (!processRC.equals("0")) {
                            System.out.println("TEST: " + name + " failed! " + result.result);

                            output.println("Test Failure: Process RC is not 0: (" + processRC + ").\n");
                            final List returnedFileList = (List) resultMap.get("fileList");
                            final Map stdoutMap = (Map) returnedFileList.get(0);
                            //System.out.println((String) stdoutMap.get("data"));
                            output.println((String) stdoutMap.get("data"));
                        } else {
                            System.out.println("TEST: " + name + " passed!");
                            final List returnedFileList = (List) resultMap.get("fileList");
                            final Map stdoutMap = (Map) returnedFileList.get(0);
                            output.println((String) stdoutMap.get("data"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                output.close();
                completionLatch.countDown();
            };

            final Thread work = new Thread(task);
            work.start();

            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final String request2 = "LIST RUNNING HANDLES LONG";
            STAFHandle handle2 = new STAFHandle("Test");
            STAFResult result2 = handle2.submit2(machine.getHostName(), SERVICE, request2);
            try {
                final LinkedList resultMap2 = (LinkedList) result2.resultObj;
                for (Object item : resultMap2) {
                    HashMap map = (HashMap)item;
                    //System.out.println("\n\n" + (String)map.get("command") + "\n" + command + "\n\n");
                    if (((String)map.get("command")).trim().equalsIgnoreCase(command.trim())) {
                        pid = Integer.parseInt((String)map.get("pid"));
                        //System.out.println("Process pid is : " + pid);
                    }

                }
                if (pid == 0) {
                    System.out.println("PID IS NOT KNOWN");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            handle.unRegister();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void kill()
    {
        try {
            final STAFHandle killHandle = new STAFHandle("kill-" + name);
            String killCmd;
            if(machine.getOS().equalsIgnoreCase("windows")){
                killCmd = "taskkill /pid ";
            } else {
                killCmd = "kill ";
            }
            final String request = "START SHELL COMMAND " + STAFUtil.wrapData(killCmd + pid) + " WAIT RETURNSTDOUT STDERRTOSTDOUT";

            final STAFResult result = killHandle.submit2(machine.getHostName(), SERVICE, request);
            if (result.rc != 0) {
                System.out.println("Kill Test Failure: " + name + " is still running!");
            } else {
                try {
                    final Map resultMap = (Map) result.resultObj;
                    final String processRC = (String) resultMap.get("rc");

                    if (!processRC.equals("0")) {
                        System.out.println("Kill Test Failure: " + name + " is still running!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            killHandle.unRegister();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause()
    {
        try {
            //For windows pause/resume, PsTools must be installed and on the system path
            // https://technet.microsoft.com/en-us/sysinternals/bb897540.aspx
            final STAFHandle pauseHandle = new STAFHandle("pause-" + name);
            String killCmd;
            if(machine.getOS().equalsIgnoreCase("windows")){
                killCmd = "pssuspend ";
            } else {
                killCmd = "kill -19 ";
            }
            final String request = "START SHELL COMMAND " + STAFUtil.wrapData(killCmd + pid) +
                    " WAIT " + timeout + "s RETURNSTDOUT STDERRTOSTDOUT";

            final STAFResult result = pauseHandle.submit2(machine.getHostName(), SERVICE, request);
            if (result.rc != 0) {
                System.out.println("ERROR: STAF " + machine + " " + SERVICE + " " + request +
                        " RC: " + result.rc + ", Result: " + result.result);
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
            //For windows pause/resume, PsTools must be installed and on the system path
            // https://technet.microsoft.com/en-us/sysinternals/bb897540.aspx
            final STAFHandle resumeHandle = new STAFHandle("resume-" + name);
            String killCmd;
            if(machine.getOS().equalsIgnoreCase("windows")){
                killCmd = "pssuspend -r ";
            } else {
                killCmd = "kill -18 ";
            }
            final String request = "START SHELL COMMAND " + STAFUtil.wrapData(killCmd + pid) +
                    " WAIT " + timeout + "s RETURNSTDOUT STDERRTOSTDOUT";
            final STAFResult result = resumeHandle.submit2(machine.getHostName(), SERVICE, request);
            if (result.rc != 0) {
                System.out.println("ERROR: STAF " + machine + " " + SERVICE + " " + request +
                        " RC: " + result.rc + ", Result: " + result.result);
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
