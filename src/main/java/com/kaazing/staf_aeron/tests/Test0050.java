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
// all subscribers running on same machine as publisher

public class Test0050 extends Test
{
    public Test0050(String[] properties, String[] options)
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
                "Test0050-sub1", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[1] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[1],
                "Test0050-sub2", 10);
        // set rate of consumption of sub3 to be slower than the sending rate of the publisher
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[2] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -r 100kbps -c=udp://localhost:" + port + " " + options[2],
                "Test0050-sub3", 10);
        // set rate of sending of pub to be faster than the consumption rate of sub3
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/pub" + properties[3] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " --driver=embedded -r 400kbps -c=udp://localhost:" + port + " " + options[3],
                "Test0050-pub", 10);

        try
        {
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
// Current results: The rate of consumption of the other subscribers will slow down to match the rate of sub 3
    public Test validate()
    {
        final Map result1 = processes.get("Test0050-sub1").getResults();
        final Map result2 = processes.get("Test0050-sub2").getResults();
        final Map result3 = processes.get("Test0050-sub3").getResults();
        final Map result4 = processes.get("Test0050-pub").getResults();
        return this;
    }
}
