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

public class Test0090 extends Test
{
    public Test0090(YAMLTestCase testCase)
    {
        super(testCase);
    }

    public void run()
    {
        int[] ports = {
                getPort(hosts[0].getIpAddress()),
                getPort(hosts[1].getIpAddress())
        };

        String[] channels = {
                "udp://" + hosts[0].getIpAddress() + ":" + ports[0],
                "udp://" + hosts[1].getIpAddress() + ":" + ports[1]
        };
        String[] commands = { SUB, SUB, PUB };
        String[] types = { "sub", "sub2", "pub" };

        for (int i = 0; i < hosts.length; i++) {
            startProcess(hosts[i].getIpAddress(),
                    hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                            hosts[0].getPathSeperator() + " " + "-Daeron.dir.delete.on.exit=false" +
                            " -cp " + hosts[i].getClasspath() + " " + DRIVER,
                    testCase.getName() + "-DRIVER-" + types[i], -1);

            if (i < hosts.length - 1) {
                startProcess(hosts[i].getIpAddress(),
                        hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                                hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                                " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                                embedded + " -c=" + channels[i] + " " + hosts[i].getOptions(),
                        testCase.getName() + "-" + types[i], 60);
            } else {
                startProcess(hosts[i].getIpAddress(),
                        hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                                hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                                " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                                embedded + " -c=" + channels[0] + "," + channels[1] + " " + hosts[i].getOptions(),
                        testCase.getName() + "-" + types[i], 60);
            }
        }

        try
        {
            Thread.sleep(3000);
            killProcess(testCase.getName() + "-" + types[0], true);

            latch.await();

            killProcess(testCase.getName() + "-DRIVER-" + types[0], false);
            killProcess(testCase.getName() + "-DRIVER-" + types[1], false);
            killProcess(testCase.getName() + "-DRIVER-" + types[2], false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        validate();
        cleanup();
    }

// Expected results: Sub 1 and Sub 3 continue to receive messages from the publisher.
// Repeat the scenario suspending one of the subscribers

    public void validate()
    {

    }
}
