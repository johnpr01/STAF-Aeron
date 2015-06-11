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

package com.kaazing.staf_aeron.tests;


import com.ibm.staf.STAFHandle;
import com.ibm.staf.STAFResult;
import com.ibm.staf.STAFUtil;
import com.kaazing.staf_aeron.AeronSTAFProcess;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public abstract class Test extends Thread
{
    protected HashMap<String, AeronSTAFProcess> processes = null;
    protected CountDownLatch latch = null;
    private static final int PORT_MIN = 30000;
    private static final int PORT_MAX = 60000;
    private static int currentPort = PORT_MIN;

    protected void startProcess(final String machine, final String command, final String name, final int timeout)
    {
        processes.put(name, new AeronSTAFProcess(machine, command, name, latch, timeout));
    }

    protected void killProcess(final String name)
    {
        processes.get(name).kill();
    }

    protected void pauseProcess(final String name) { processes.get(name).pause(); }

    protected void resumeProcess(final String name) { processes.get(name).resume(); }

    public abstract Test validate();

    protected int getPort(String machine)
    {
        synchronized (this) {
            boolean found = false;
            if (currentPort == PORT_MAX) {
                currentPort = PORT_MIN;
            }
            try {
                do {
                    String command = "java -cp staf.jar com.kaazing.staf_aeron.util.PortStatus " + currentPort;
                    String timeout = "5s";
                    final String request = "START SHELL COMMAND " + STAFUtil.wrapData(command) +
                            " WAIT " + timeout + " RETURNSTDOUT STDERRTOSTDOUT";
                    STAFHandle tmp = new STAFHandle("port");
                    STAFResult result = tmp.submit2(machine, "Process", request);
                    if (result.rc != 0) {
                        found = false;
                        currentPort++;
                    } else {
                        found = true;
                    }
                    tmp.unRegister();
                } while (!found);

                return currentPort++;

            } catch (Exception e) {
                return 0;
            }
        }
    }
}
