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

public class Test0090 extends Test
{
    public Test0090(String[] properties, String[] options)
    {
        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(4);
        final String aeronDir = "-Daeron.dir=/tmp/" + this.getClass().getSimpleName();
        int port = getPort("local");

        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[0] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[0],
                "Test0090-sub1", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[1] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[1],
                "Test0090-sub2", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[2] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -r 100kbps -c=udp://localhost:" + port + " " + options[2],
                "Test0090-sub3", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/pub" + properties[3] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[3],
                "Test0090-pub", 10);
        // allow the publisher to send for a few seconds before killing one of the subscribers
        try
        {
            Thread.sleep(10000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // Kill one of the subscribers
        killProcess("Test0090-sub2");
        try
        {
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
// Expected results: Sub 1 and Sub 3 continue to receive messages from the publisher.
// Repeat the scenario suspending one of the subscribers

    public Test validate()
    {
        final Map result1 = processes.get("Test0090-sub1").getResults();
        final Map result2 = processes.get("Test0090-sub2").getResults();
        final Map result3 = processes.get("Test0090-sub3").getResults();
        final Map result4 = processes.get("Test0090-pub").getResults();
        return this;
    }
}
