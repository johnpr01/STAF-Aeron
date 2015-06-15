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
id0030: Loss Test
Summary:
    Subscriber disconnects and reconnects when unrecoverable loss occurs.
Tasks:
    While a publisher is actively sending messages to a multiple subscribers, the driver associated with one of the
    subscribers will periodically lose a percentage of messages as documented in the TestPlanning Spreadsheet
    under the ‘LossTest’ config options.
    The subscriber can not keep up or recover (NAKs will NOT be sent for the missed messages).
Expected Results:
    The subscriber which is behind will disconnect and reconnect.  It should start receiving messages from the live stream.  Verify all other subscribers are NOT affected.
NOTE:
    This scenario will be re-executed using the various config options documented in the TestPlanning SpreadSheet in the ‘LossTest’ section.
*/

package com.kaazing.staf_aeron.tests;

import com.kaazing.staf_aeron.YAMLTestCase;

// For this test, all subscribers and the publisher are running on different machines. On the mediadriver for
// subscriber 2, disable NAKS and set the loss rate to .4
public class Test0030 extends Test
{
    public Test0030(YAMLTestCase testCase)
    {
        super(testCase);
    }

    public void run() {
        int[] ports = {
                getPort(hosts[0].getIpAddress()),
                getPort(hosts[1].getIpAddress()),
                getPort(hosts[2].getIpAddress()),
        };
        String[] channels = {
                "udp://" + hosts[0].getIpAddress() + ":" + ports[0],
                "udp://" + hosts[1].getIpAddress() + ":" + ports[1],
                "udp://" + hosts[2].getIpAddress() + ":" + ports[2]
        };
        String[] commands = {SUB, SUB, SUB, PUB};
        String[] types = {"sub1", "sub2", "sub3", "pub"};

        for (int i = 0; i < hosts.length; i++) {
            if (i < hosts.length - 1) {
                startProcess(hosts[i].getHostName(),
                        hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                                hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                                " -cp " + hosts[i].getClasspath() + " " + commands[i] +
                                embedded + " -c=" + channels[i] + " " + hosts[i].getOptions(),
                        testCase.getName() + "-" + types[i], 60);
            } else {
                startProcess(hosts[i].getHostName(),
                        hosts[i].getJavaPath() + hosts[i].getPathSeperator() + "java " + aeronDirs[i] +
                                hosts[i].getPathSeperator() + types[i] + " " + hosts[i].getProperties() +
                                " -cp " + hosts[i].getClasspath() + " " + commands[i] +
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
// Verification: Subscriber 2 connects, but does not receive any messages. Subscriber 2 should disconnect and reconnect
// when it falls behind
    public void validate()
    {

    }
}
