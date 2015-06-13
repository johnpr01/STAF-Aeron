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
import com.kaazing.staf_aeron.STAFHost;
import com.kaazing.staf_aeron.YAMLTestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Test0035 extends Test
{
    public Test0035(YAMLTestCase testCase)
    {
        STAFHost host1 = testCase.getStafHosts().get(0);

        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(2);
        final String aeronDir = "-Daeron.dir=" + host1.getTmpDir() + host1.getPathSeperator() + testCase.getName();
        int port = getPort(host1.getHostName());

        startProcess(host1.getHostName(),
                host1.getJavaPath() + host1.getPathSeperator() + "java " + aeronDir + host1.getPathSeperator() + "sub" + host1.getProperties() +
                        " -cp " + host1.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -m=1000000 -r=5Mbps -c=udp://localhost:" + port + " " + host1.getOptions(),
                "Test0035-sub", 60);
        startProcess(host1.getHostName(),
                host1.getJavaPath() + host1.getPathSeperator() + "java " + aeronDir + "/pub" + host1.getProperties() +
                        " -cp " + host1.getClasspath() +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " --driver=embedded -r=10Mbps -m=1000000 -c=udp://localhost:" + port + " " + host1.getOptions(),
                "Test0035-pub", 60);

        try
        {
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {

    }

    public Test validate()
    {
        final Map result1 = processes.get("Test0035-sub").getResults();
        final Map result2 = processes.get("Test0035-pub").getResults();
        return this;
    }
}
