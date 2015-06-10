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

public class Test0115 extends Test
{
    public Test0115(YAMLTestCase testCase)
    {
        STAFHost host1 = testCase.getStafHosts().get(0);
        STAFHost host2 = testCase.getStafHosts().get(1);
        STAFHost host3 = testCase.getStafHosts().get(2);

        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(3);
        final String aeronDir = "-Daeron.dir=" + host1.getTmpDir() + host1.getPathSeperator() + testCase.getName();
        int port = getPort(host1.getHostName());
        String channel = "-c=udp://localhost:" + port;
        String embedded = testCase.getIsEmbedded() ? " --driver=embedded" :  "--driver=external";

        startProcess(host1.getHostName(),
                host1.getJavaPath() + host1.getPathSeperator() + "java " + aeronDir + host1.getPathSeperator() + "sub" + host1.getProperties() +
                        " -cp " + host1.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " " + embedded + " " + channel + " " + host1.getOptions(),
                "Test0115-sub1", 10);
        startProcess(host2.getHostName(),
                host2.getJavaPath() + host2.getPathSeperator() + "java " + aeronDir + host2.getPathSeperator() + "pub" + host2.getProperties() +
                        " -cp " + host2.getClasspath() +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " " + embedded + " " + channel + " " + host2.getOptions(),
                "Test0115-pub", 10);

        try
        {
            Thread.sleep(5000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        startProcess(host3.getHostName(),
                host3.getJavaPath() + host3.getPathSeperator() + "java " + aeronDir + host3.getPathSeperator() + "sub" + host3.getProperties() +
                        " -cp " + host3.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " " + embedded + " " + channel + " " + host3.getOptions(),
                "Test0115-sub2", 10);

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
        final Map result1 = processes.get("Test0115-sub1").getResults();
        final Map result2 = processes.get("Test0115-sub2").getResults();
        final Map result3 = processes.get("Test0115-pub").getResults();
        return this;
    }
}
