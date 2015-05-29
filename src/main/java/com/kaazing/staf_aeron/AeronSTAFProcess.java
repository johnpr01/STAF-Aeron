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
    protected static final String SERVICE = "Process";

    public AeronSTAFProcess()
    {

    }

    public AeronSTAFProcess(String machine, String command, String name, CountDownLatch completionLatch, int timeout)
    {
        this.machine = machine;
        this.command = command;
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
            final String request = "START SHELL COMMAND " + STAFUtil.wrapData(command) +
                    " WAIT " + timeout + "s RETURNSTDOUT STDERRTOSTDOUT";
            Runnable task = () -> {
                    result = handle.submit2(machine, SERVICE, request);
                    if (result.rc != 0) {
                        try {
                            PrintWriter output = new PrintWriter(name + ".log");
                            output.println("ERROR: STAF " + machine + " " + SERVICE + " " + request +
                                    " RC: " + result.rc + ", Result: " + result.result);
                            output.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.exit(1);
                    }
            };
            final Thread work = new Thread(task);
            work.start();
            Thread.sleep(timeout * 2000);

            final Map resultMap = (Map)result.resultObj;
            final String processRC = (String)resultMap.get("rc");

            if (!processRC.equals("0")) {
                PrintWriter output = new PrintWriter(name + ".log");
                output.println("ERROR: Process RC is not 0.\n");
                final List returnedFileList = (List)resultMap.get("fileList");
                final Map stdoutMap = (Map)returnedFileList.get(0);
                output.println((String)stdoutMap.get("data"));
                output.close();
                System.exit(1);
            }

            completionLatch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public void kill()
    {
        final String request = "STOP HANDLE " + handle.getHandle() + "USING SIGKILL";
        final STAFResult result = handle.submit2(machine, SERVICE, request);
        if (result.rc != 0) {
            System.out.println("ERROR: STAF " + machine + " " + SERVICE + " " + request +
                    " RC: " + result.rc + ", Result: " + result.result);
            System.exit(1);
        }
        try {
            completionLatch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map getResults()
    {
        return (Map)result.resultObj;
    }
}
