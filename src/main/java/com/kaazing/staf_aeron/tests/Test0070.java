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

import com.kaazing.staf_aeron.AeronSTAFProcess;
import com.kaazing.staf_aeron.STAFHost;
import com.kaazing.staf_aeron.YAMLTestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

// A standalone media driver and a subscriber is running on machine A.
// A standalone media driver and publisher is running on machine A (media driver 2 is using a different directory).
public class Test0070 extends Test
{
    public Test0070(YAMLTestCase testCase)
    {
        super(testCase);
    }

    public void run()
    {
        int port = getPort(hosts[0].getIpAddress());
        String channel = "udp://" + hosts[0].getIpAddress() + ":" + port;
        String[] commands = { SUB, PUB };
        String[] types = { "sub", "pub" };


        for (int i = 0; i < hosts.length; i++) {
            startProcess(hosts[i].getIpAddress(),
                    hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                            hosts[0].getPathSeperator() + " " + "-Daeron.dir.delete.on.exit=false" +
                            " -cp " + hosts[i].getClasspath() + " " + DRIVER,
                    testCase.getName() + "-DRIVER-" + types[i], -1);

            startProcess(hosts[i].getHostName(),
                    hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                            hosts[i].getPathSeperator() + " " + hosts[i].getProperties() +
                            " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                            embedded + " -c=" + channel + " " + hosts[i].getOptions(),
                    testCase.getName() + "-" + types[i], 60);
        }
        try
        {
            Thread.sleep(10000);

            killProcess(testCase.getName() + "-DRIVER-" + types[0], false);


            Thread.sleep(3000);

            startProcess(hosts[0].getIpAddress(),
                    hosts[0].getJavaPath() + hosts[0].getPathSeperator() + "java " + aeronDirs[0] +
                            hosts[0].getPathSeperator() + " " + "-Daeron.dir.delete.on.exit=false" +
                            " -cp " + hosts[0].getClasspath() + " " + DRIVER,
                    testCase.getName() + "-DRIVER-" + types[0], -1);

            latch.await();

            killProcess(testCase.getName() + "-DRIVER-" + types[0], false);
            killProcess(testCase.getName() + "-DRIVER-" + types[1], false);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        validate();
        cleanup();
    }

// Expected results: The clients should reconnect to the driver and communication should resume
// Repeat the test suspending the media driver
    public void validate()
    {
    }
}
