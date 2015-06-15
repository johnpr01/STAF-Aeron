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

// for this scenario, a standalone media driver is running on the same machine.
public class Test0055 extends Test
{
    public Test0055(YAMLTestCase testCase)
    {
        super(testCase);
    }

    public void run()
    {
        int port = getPort(hosts[0]);
        String channel = "udp://" + hosts[0].getIpAddress() + ":" + port;
        String[] commands = { SUB, PUB };
        String[] types = { "sub", "pub" };

        startProcess(hosts[0],
                hosts[0].getJavaPath() + hosts[0].getPathSeperator() + "java " + aeronDirs[0] +
                        hosts[0].getPathSeperator() + " " + "-Daeron.dir.delete.on.exit=true" +
                        " -cp " + hosts[0].getClasspath() + " " + DRIVER,
                testCase.getName() + "-DRIVER", -1);

        for (int i = 0; i < hosts.length; i++) {
            startProcess(hosts[i],
                    hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                            hosts[i].getPathSeperator() + " " + hosts[i].getProperties() +
                            " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                            embedded + " -c=" + channel + " " + hosts[i].getOptions(),
                    testCase.getName() + "-" + types[i], 60);
        }
        try
        {
            Thread.sleep(10000);

            killProcess(testCase.getName() + "-DRIVER", false);

            if (checkForFiles(hosts[0].getIpAddress(), aeronDirs[0])) {
                System.out.println(testCase.getName() + " failed!, The driver files were not cleaned up.");
                for (int i = 0; i < hosts.length; i++) {
                    killProcess(testCase.getName() + "-" + types[i], true);
                }
            } else {
                Thread.sleep(3000);

                startProcess(hosts[0],
                        hosts[0].getJavaPath() + hosts[0].getPathSeperator() + "java " + aeronDirs[0] +
                                hosts[0].getPathSeperator() + " " +
                                " -cp " + hosts[0].getClasspath() + " " + DRIVER,
                        testCase.getName() + "-DRIVER", -1);
            }
            latch.await();

            killProcess(testCase.getName() + "-DRIVER", false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        validate();
        cleanup();
    }

// the expected result for the suspend case: while the media driver is suspended, the publisher will be able to send
// until it is flow controlled. Once the media driver resumes, the subscriber will receive a burst of data
    public void validate()
    {
    }
}
