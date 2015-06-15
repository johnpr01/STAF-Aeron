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
// all subscribers running on same machine as publisher

public class Test0050 extends Test
{
    public Test0050(YAMLTestCase testCase)
    {
        super(testCase);
    }

    public void run()
    {
        int[] ports = {
                getPort(hosts[0].getIpAddress()),
                getPort(hosts[1].getIpAddress()),
                getPort(hosts[2].getIpAddress())
        };
        String[] channels = {
                "udp://" + hosts[0].getIpAddress() + ":" + ports[0],
                "udp://" + hosts[1].getIpAddress() + ":" + ports[1],
                "udp://" + hosts[2].getIpAddress() + ":" + ports[2]
        };
        String[] commands = { SUB, SUB, SUB, PUB };
        String[] types = { "sub1", "sub2", "sub3", "pub" };

        for (int i = 0; i < hosts.length; i++) {
            if (i < hosts.length - 1) {
                startProcess(hosts[i].getHostName(),
                        hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                                hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                                " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                                embedded + " -c=" + channels[i] + " " + hosts[i].getOptions(),
                        testCase.getName() + "-" + types[i], 60);
            } else {
                startProcess(hosts[i].getHostName(),
                        hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                                hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                                " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                                embedded + " -c=" + channels[0] + "," + channels[1] + "," + channels[2] + " " +
                                hosts[i].getOptions(),
                        testCase.getName() + "-" + types[i], 60);
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
        validate();
        cleanup();
    }

// Current results: The rate of consumption of the other subscribers will slow down to match the rate of sub 3
    public void validate()
    {
    }
}
