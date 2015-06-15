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

public class Test0085 extends Test
{
    public Test0085(YAMLTestCase testCase)
    {
        super(testCase);
    }

    public void run()
    {
        int port = getPort(hosts[0]);
        String channel = "udp://" + hosts[0].getIpAddress() + ":" + port;
        String[] commands = { SUB, PUB };
        String[] types = { "sub", "pub" };

        for (int i = 0; i < hosts.length; i++) {
            startProcess(hosts[i],
                    hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                            hosts[0].getPathSeperator() + " " + "-Daeron.dir.delete.on.exit=false" +
                            " -cp " + hosts[i].getClasspath() + " " + DRIVER,
                    testCase.getName() + "-DRIVER-" + types[i], -1);

            startProcess(hosts[i],
                    hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                            hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                            " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                            embedded + " -c=" + channel + " " + hosts[i].getOptions(),
                    testCase.getName() + "-" + types[i], 60);
        }

        try
        {
            Thread.sleep(3000);
            killProcess(testCase.getName() + "-" + types[0], true);

            latch.await();

            killProcess(testCase.getName() + "-DRIVER-" + types[0], false);
            killProcess(testCase.getName() + "-DRIVER-" + types[1], false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        validate();
        cleanup();
    }

// the expected result for the kill subscriber:the publisher will continue to send until flow control kicks in,
// then it will no longer send messages
    public void validate()
    {
    }
}
