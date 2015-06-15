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
import com.kaazing.staf_aeron.tests.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Test0080 extends Test
{
    public Test0080(YAMLTestCase testCase)
    {
        super(testCase);
    }

    public void run()
    {
        int port = getPort(hosts[0]);
        String channel = "udp://" + hosts[0].getIpAddress() + ":" + port;
        String[] commands = { SUB, PUB, PUB };
        String[] types = { "sub", "pub", "pub2" };

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

            killProcess(testCase.getName() + "-" + types[1], true);


            Thread.sleep(3000);
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        validate();
        cleanup();
    }

// the expected result: the subscriber will continue to get messages from publisher 2. Eventually the subscriber reports
// the connection with publisher 1 is INACTIVE
    public void validate()
    {
    }
}
