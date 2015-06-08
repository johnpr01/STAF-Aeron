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

public class Test0120 extends Test
{
    public Test0120(String[] properties, String[] options)
    {
        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(4);

        startProcess("local",
                "/usr/local/java/bin/java -Daeron.dir=/tmp/" + this.getClass().getSimpleName() + "/pub " + properties[0] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " --driver=embedded -c=udp://localhost:44444 " + options[0],
                "Test0120-pub", 10);

        try
        {
            Thread.sleep(5000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        startProcess("local",
                "/usr/local/java/bin/java -Daeron.dir=/tmp/" + this.getClass().getSimpleName() + "/sub " + properties[1]  +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:44444 " + options[1],
                "Test0120-sub1", 10);

        startProcess("local",
                "/usr/local/java/bin/java -Daeron.dir=/tmp/" + this.getClass().getSimpleName() + "/sub " + properties[2]  +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:44444 " + options[2],
                "Test0120-sub2", 10);

        startProcess("local",
                "/usr/local/java/bin/java -Daeron.dir=/tmp/" + this.getClass().getSimpleName() + "/sub " + properties[3]  +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:44444 " + options[3],
                "Test0120-sub3", 10);

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
        final Map result1 = processes.get("Test0120-sub1").getResults();
        final Map result2 = processes.get("Test0120-sub2").getResults();
        final Map result3 = processes.get("Test0120-sub3").getResults();
        final Map result4 = processes.get("Test0120-pub").getResults();
        return this;
    }
}
