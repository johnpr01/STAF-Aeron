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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

// For this test, all subscribers and the publisher are running on different machines. On the mediadriver for
// subscriber 2, disable NAKS and set the loss rate to .4
public class Test0030 extends Test
{
    public Test0030(String[] properties, String[] options)
    {
        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(4);
        final String aeronDir = "-Daeron.dir=/tmp/" + this.getClass().getSimpleName();
        int port = getPort("local");

        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[0] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " -c=udp://localhost:" + port + " " + options[0],
                "Test0030-sub1", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[1] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " -c=udp://localhost:" + port + " " + options[1],
                "Test0030-sub2", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[2] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " -c=udp://localhost:" + port + " " + options[2],
                "Test0030-sub3", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/pub" + properties[3] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " -c=udp://localhost:" + port + " " + options[3],
                "Test0030-pub", 10);

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
