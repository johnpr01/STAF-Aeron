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

public class Test0125 extends Test
{
    public Test0125(YAMLTestCase testCase) {
        super(testCase);
    }

    public void run()
    {
        int port1 = getPort(hosts[0]);
        int port2 = getPort(hosts[1]);
        int port3 = getPort(hosts[2]);
        int port4 = getPort(hosts[3]);
        int port5 = getPort(hosts[4]);
        String channel1 = "-c=udp://" + hosts[0].getIpAddress() + ":" + port1;
        String channel2 = "-c=udp://" + hosts[1].getIpAddress() + ":" + port2;
        String channel3 = "-c=udp://" + hosts[2].getIpAddress() + ":" + port3;
        String channel4 = "-c=udp://" + hosts[3].getIpAddress() + ":" + port4;
        String channel5 = "-c=udp://" + hosts[4].getIpAddress() + ":" + port5;
        String[] commands = { SUB, SUB, SUB, SUB, SUB, PUB, PUB, PUB, PUB, PUB };
        String[] types = { "sub", "sub", "sub", "sub", "sub", "pub", "pub", "pub", "pub", "pub" };
        String[] subNames = { "sub1", "sub2", "sub3", "sub4", "sub5", "pub1", "pub2", "pub3", "pub4", "pub5" };

        for (int i = 0; i < hosts.length; i++) {
            if (i == 0 || i == 5) {

                startProcess(hosts[i],
                        hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] + i +
                                hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                                " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                                embedded + " " + channel1 + " " + hosts[i].getOptions(),
                        testCase.getName() + "-" + subNames[i], 120);
            }
            if (i == 1 || i == 6) {
                startProcess(hosts[i],
                        hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] + i +
                                hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                                " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                                embedded + " " + channel2 + " " + hosts[i].getOptions(),
                        testCase.getName() + "-" + subNames[i], 120);
            }
            if (i == 2 || i == 7) {
                startProcess(hosts[i],
                        hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] + i +
                                hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                                " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                                embedded + " " + channel3 + " " + hosts[i].getOptions(),
                        testCase.getName() + "-" + subNames[i], 120);
            }
            if (i == 3 || i == 8) {
                startProcess(hosts[i],
                        hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] + i +
                                hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                                " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                                embedded + " " + channel4 + " " + hosts[i].getOptions(),
                        testCase.getName() + "-" + subNames[i], 120);
            }
            if (i == 4 || i == 9) {
                startProcess(hosts[i],
                        hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] + i +
                                hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                                " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                                embedded + " " + channel5 + " " + hosts[i].getOptions(),
                        testCase.getName() + "-" + subNames[i], 120);
            }
        }
        try
        {
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        cleanup(true);
    }

    public Test validate()
    {
        final Map result1 = processes.get("Test0110-sub1").getResults();
        final Map result2 = processes.get("Test0110-sub2").getResults();
        final Map result3 = processes.get("Test0110-sub3").getResults();
        final Map result4 = processes.get("Test0110-sub5").getResults();
        final Map result5 = processes.get("Test0110-sub6").getResults();
        final Map result6 = processes.get("Test0000-pub1").getResults();
        final Map result7 = processes.get("Test0000-pub2").getResults();
        final Map result8 = processes.get("Test0000-pub3").getResults();
        final Map result9 = processes.get("Test0000-pub4").getResults();
        final Map result10 = processes.get("Test0000-pub5").getResults();
        return this;
    }
}
