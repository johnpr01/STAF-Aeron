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

public class Test0000 extends Test
{
    public Test0000(String[] properties, String[] options)
    {
        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(2);
        final String aeronDir = "-Daeron.dir=/tmp/" + this.getClass().getSimpleName();
        int port = getPort("local");

        System.out.println("GOT PORT: " + port);
        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/sub" + properties[0] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -m=1000000 -c=udp://localhost:" + port + " " + options[0],
                "Test0000-sub", 60);

        startProcess("local",
                "/usr/local/java/bin/java " + aeronDir + "/pub" + properties[0] +
                        " -cp " + CLASSPATH +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " --driver=embedded -m=1000000 -c=udp://localhost:" + port + " " + options[0],
                "Test0000-pub", 60);

        try
        {
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        validate();
    }

    public Test validate()
    {
        //System.out.println("Done");
        //final Map result1 = processes.get("Test0000-sub").getResults();
        //final Map result2 = processes.get("Test0000-pub").getResults();
        return this;
    }
}
