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

/*
id0000: Basic Test
        Summary:
            A publisher sends 1 million messages  to a single subscriber.
        Tasks:
            A subscriber is started and sends a subscription using a particular stream id.
            A publisher is started and receives a subscription for the stream id of interest, the publisher then sends data.
        Expected Results:
            The subscriber receives all messages sent by the publisher.  Verify no memory leaks are reported.
        NOTES:
            Repeat test scenario using the various config options documented in the TestPLanning spreadsheet for the ‘basic test’.
*/

package com.kaazing.staf_aeron.tests;

import com.kaazing.staf_aeron.YAMLTestCase;

public class Test0000 extends Test
{
    public Test0000(YAMLTestCase testCase)
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
                            hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                            " -cp " + hosts[i].getClasspath() + " " + commands[i] + " " +
                            embedded + " -c=" + channel + " " + hosts[i].getOptions(),
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

    public void validate()
    {
        //final Map result1 = processes.get("Test0000-sub").getResults();
        //final Map result2 = processes.get("Test0000-pub").getResults();
    }
}
