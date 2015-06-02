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

public class Test0125 extends Test
{
    public Test0125(String[] properties, String[] options)
    {
        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(10);
        final String aeronDir = "-Daeron.dir=/tmp/" + this.getClass().getSimpleName();
        int port = getPort("local");

        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[0] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[0],
                "Test0125-sub1", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[1] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[1],
                "Test0125-sub2", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[2] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[2],
                "Test0125-sub3", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[3] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[3],
                "Test0125-sub4", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub " + properties[4] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[4],
                "Test0125-sub5", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/pub" + properties[5] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[5],
                "Test0125-pub1", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/pub" + properties[6] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[6],
                "Test0125-pub2", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/pub" + properties[7] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[7],
                "Test0125-pub3", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/pub" + properties[8] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[8],
                "Test0125-pub4", 10);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/pub" + properties[9] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " --driver=embedded -c=udp://localhost:" + port + " " + options[9],
                "Test0125-pub5", 10);
        try
        {
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Test validate()
    {
        final Map result1 = processes.get("Test0110-sub1").getResults();
        final Map result2 = processes.get("Test0110-sub2").getResults();
        final Map result3 = processes.get("Test0110-sub3").getResults();
        final Map result4 = processes.get("Test0110-sub5").getResults();
        final Map result5 = processes.get("Test0110-sub6").getResults();
        final Map result6 = processes.get("Test0000-pub1").getResults();
        final Map result7 = processes.get("Test0000-pub2").getResults();
        final Map result8 = processes.get("Test0000-pub3").getResults();
        final Map result9 = processes.get("Test0000-pub4").getResults();
        final Map result10 = processes.get("Test0000-pub5").getResults();
        return this;
    }
}
