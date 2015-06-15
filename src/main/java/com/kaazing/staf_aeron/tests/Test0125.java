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


import com.kaazing.staf_aeron.YAMLTestCase;

public class Test0125 extends Test
{
    public Test0125(YAMLTestCase testCase) {
        super(testCase);
    }

    public void run()
    {
        int[] ports = {
                getPort(hosts[0].getIpAddress()),
                getPort(hosts[1].getIpAddress()),
                getPort(hosts[2].getIpAddress()),
                getPort(hosts[3].getIpAddress()),
                getPort(hosts[4].getIpAddress())
        };
        String[] channels = {
                "udp://" + hosts[0].getIpAddress() + ":" + ports[0],
                "udp://" + hosts[1].getIpAddress() + ":" + ports[1],
                "udp://" + hosts[2].getIpAddress() + ":" + ports[2],
                "udp://" + hosts[3].getIpAddress() + ":" + ports[3],
                "udp://" + hosts[4].getIpAddress() + ":" + ports[4]
        };

        String[] commands = { SUB, SUB, SUB, SUB, SUB, PUB, PUB, PUB, PUB, PUB };
        String[] types = { "sub1", "sub2", "sub3", "sub4", "sub5", "pub1", "pub2", "pub3", "pub4", "pub5" };

        for (int i = 0; i < hosts.length; i++) {
            startProcess(hosts[i].getHostName(),
                    hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                            hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                            " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                            embedded + " -c" + channels[i % 5] + " " + hosts[i].getOptions(),
                    testCase.getName() + "-" + types[i], 120);
        }
        try
        {
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        cleanup();
    }

    public void validate()
    {

    }
}
