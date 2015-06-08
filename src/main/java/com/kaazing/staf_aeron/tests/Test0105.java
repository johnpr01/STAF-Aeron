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

// While a Publisher is actively sending to a subscriber, a new publisher is started and sends to the same subscriber
// New (late) publisher will use a different stream id
public class Test0105 extends Test
{
    public Test0105(String[] properties, String[] options)
    {
        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(3);
        final String aeronDir = "-Daeron.dir=/tmp/" + this.getClass().getSimpleName();
        int port = getPort("local");

        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[0] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " -c=udp://localhost:" + port + " " + options[0],
                "Test0105-sub", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/pub" + properties[1] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " -c=udp://localhost:" + port + " " + options[1],
                "Test00105-pub1", 10);
        //Allow publisher 1 to send to the subscriber a few seconds before starting publisher 2 using a different
        // stream id
        try
        {
            Thread.sleep(10000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/pub" + properties[2] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " -c=udp://localhost:" + port + " " + options[2],
                "Test0105-pub2", 10);

        try
        {
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
// Verification: The subscriber will only receive messages from publisher 1.

    public Test validate()
    {
        final Map result1 = processes.get("Test0105-sub").getResults();
        final Map result2 = processes.get("Test0105-pub1").getResults();
        final Map result3 = processes.get("Test0105-pub2").getResults();
        return this;
    }
}
