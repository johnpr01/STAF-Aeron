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
id0025: Loss Test
Summary:
    Subscriber disconnects and reconnects  when unrecoverable loss occurs.
Tasks:
    While a publisher is actively sending messages to subscriber, the driver associated with the subscriber
    will periodically lose a percentage of messages as documented in the TestPlanning Spreadsheet  under the ‘LossTest’ config options.
    The subscriber can not keep up or recover (NAKs will NOT be sent for the missed messages).
Expected Results:
    The subscriber which is behind will disconnect and reconnect.  It should start receiving messages from the live stream.
NOTE:
    This scenario will be re-executed using the various config options documented in the TestPlanning SpreadSheet in the ‘LossTest’ section.
 */

package com.kaazing.staf_aeron.tests;

import com.kaazing.staf_aeron.YAMLTestCase;

// For this test, set the loss rate to .02 on the mediadriver and disabled NAKS
public class Test0025 extends Test
{
    public Test0025(YAMLTestCase testCase)
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
                            " -cp " + hosts[i].getClasspath() + " " + commands[i] +
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

// Verification: The subscriber will only a few messages (or it may not receive any messages at all). Flow control
// does not allow the publisher to overrun the subscriber.  With NAKS disabled, it will just block the publisher from
// going any further.  The subscriber will consider the publisher to still be alive

    public void validate()
    {

    }
}
