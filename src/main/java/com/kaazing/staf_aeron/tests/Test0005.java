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
id0005: Basic Test
Summary:
    A subscriber is started after the publisher.
Tasks:
    Start a single publisher.
    Wait a few seconds
    Start a single subscriber using the stream ID of interest.
Expected Results:
    Before the subscriber is started, the senders offer will fail.  After the subscriber is known, the sender will be
    allowed to send and the subscriber should receive all messages
NOTES:
    Repeat test scenario using the various config options documented in the TestPLanning spreadsheet for the ‘BasicTest’.

 */

package com.kaazing.staf_aeron.tests;

import com.kaazing.staf_aeron.YAMLTestCase;

public class Test0005 extends Test
{
    public Test0005(YAMLTestCase testCase)
    {
        super(testCase);
    }

    public void run()
    {
        int port = getPort(hosts[0]);
        String channel = "udp://" + hosts[1].getIpAddress() + ":" + port;
        String[] commands = { PUB, SUB };
        String[] types = { "pub", "sub" };

        for (int i = 0; i < hosts.length; i++) {
            if (i == 1) {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
            validate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        cleanup();
    }

    public void validate()
    {
        //final Map result1 = processes.get("Test0005-sub").getResults();
        //final Map result2 = processes.get("Test0005-pub").getResults();
    }
}
