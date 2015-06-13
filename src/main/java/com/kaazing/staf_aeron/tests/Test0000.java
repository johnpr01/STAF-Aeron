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
import java.util.concurrent.CountDownLatch;

public class Test0000 extends Test
{
    public Test0000(YAMLTestCase testCase)
    {
        super(testCase);
    }

    public void run()
    {
        int port = getPort(hosts[0].getHostName());
        String channel = "-c=udp://" + hosts[0].getIpAddress() + ":" + port;
        String[] commands = { SUB, PUB };
        String[] types = { "sub", "pub" };

        for (int i = 0; i < hosts.length; i++) {
            startProcess(hosts[i].getHostName(),
                    hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                            hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                            " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                            embedded + " " + channel + " " + hosts[i].getOptions(),
                    testCase.getName() + "-" + types[i], 60);
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

    public Test validate()
    {
        //final Map result1 = processes.get("Test0000-sub").getResults();
        //final Map result2 = processes.get("Test0000-pub").getResults();
        return this;
    }
}
