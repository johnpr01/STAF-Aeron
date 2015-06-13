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

// For this test, all subscribers and the publisher are running on different machines. On the mediadriver for
// subscriber 2, disable NAKS and set the loss rate to .4
public class Test0030 extends Test
{
    public Test0030(YAMLTestCase testCase)
    {
        super(testCase);
    }

    public void run() {
        int port = getPort(hosts[0].getHostName());
        String channel = "-c=udp://" + hosts[0] + ":" + port;
        String[] commands = {SUB, SUB, SUB, PUB};
        String[] types = {"sub", "sub", "sub", "pub"};
        String[] subnames = {"sub1", "sub2", "sub3", "pub"};
        for (int i = 0; i < hosts.length; i++) {
            startProcess(hosts[i].getHostName(),
                    hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                            hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                            " -cp " + hosts[i].getClasspath() + " " + commands[i] +
                            embedded + " " + channel + " " + hosts[i].getOptions(),
                    "Test0030-" + subnames[i], 60);
        }
        try
        {
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
// Verification: Subscriber 2 connects, but does not receive any messages. Subscriber 2 should disconnect and reconnect
// when it falls behind
    public Test validate()
    {
        final Map result1 = processes.get("Test0030-sub1").getResults();
        final Map result2 = processes.get("Test0030-sub2").getResults();
        final Map result3 = processes.get("Test0030-sub3").getResults();
        final Map result4 = processes.get("Test0030-pub").getResults();
        return this;
    }
}
