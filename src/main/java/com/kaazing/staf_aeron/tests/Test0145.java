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

// Stress test: Media Driver fails while multiple publishers (50) are sending to multiple subscribers (50)
// For this put startProcess in a loop?
public class Test0145 extends Test
{
    public Test0145(String[] properties, String[] options)
    {
        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(2);
        final String aeronDir = "-Daeron.dir=/tmp/" + this.getClass().getSimpleName();
        int port = getPort("local");

        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[0] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[0],
                "Test0145-sub", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/pub" + properties[1] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[1],
                "Test0145-pub", 10);
//Allow the publishers to send to the subscribers for a few seconds, kill or suspend the mediadriver
//Restart or resume mediadriver after a few seconds
        try
        {
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
// Verification: expectation pubs and subs would reconnect to the driver and communication would resume

    public Test validate()
    {
        final Map result1 = processes.get("Test0145-sub").getResults();
        final Map result2 = processes.get("Test0145-pub").getResults();
                return this;
    }
}
