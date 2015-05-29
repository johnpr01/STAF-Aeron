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

        try
        {
            handle = new STAFHandle(name);
        }
        catch (STAFException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        try
        {
            final String request = "START SHELL COMMAND " + STAFUtil.wrapData(command) +
                    " WAIT " + timeout + "s RETURNSTDOUT STDERRTOSTDOUT";
            final Runnable task = new Runnable()
            {
                public void run()
                {
                    result = handle.submit2(machine, SERVICE, request);
                    if (result.rc != 0)
                    {
                        System.out.println("ERROR: STAF " + machine + " " + SERVICE + " " + request +
                                " RC: " + result.rc + ", Result: " + result.result);
                        System.exit(1);
                    }
                }
            };
            final Thread work = new Thread(task);
            work.start();
            work.join(timeout);

            final Map resultMap = (Map)result.resultObj;
            final String processRC = (String)resultMap.get("rc");

            if (!processRC.equals("0"))
            {
                System.out.println("ERROR: Process RC is not 0.\n");
                System.exit(1);
            }

            final List returnedFileList = (List)resultMap.get("fileList");
            final Map stdoutMap = (Map)returnedFileList.get(0);
            final String stdoutData = (String)stdoutMap.get("data");
            System.out.println("Process Stdout:\n" + stdoutData);
            completionLatch.countDown();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void kill()
    {
        final String request = "STOP HANDLE " + handle.getHandle() + "USING SIGKILL";
        final STAFResult result = handle.submit2(machine, SERVICE, request);
        if (result.rc != 0)
        {
            System.out.println("ERROR: STAF " + machine + " " + SERVICE + " " + request +
                    " RC: " + result.rc + ", Result: " + result.result);
            System.exit(1);
        }
    }

    public Map getResults()
    {
        return (Map)result.resultObj;
    }
}
