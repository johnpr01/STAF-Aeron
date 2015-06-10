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

public class Test0125 extends Test
{
    public Test0125(YAMLTestCase testCase)
    {
        STAFHost host1 = testCase.getStafHosts().get(0);
        STAFHost host2 = testCase.getStafHosts().get(1);
        STAFHost host3 = testCase.getStafHosts().get(2);
        STAFHost host4 = testCase.getStafHosts().get(3);
        STAFHost host5 = testCase.getStafHosts().get(4);
        STAFHost host6 = testCase.getStafHosts().get(5);
        STAFHost host7 = testCase.getStafHosts().get(6);
        STAFHost host8 = testCase.getStafHosts().get(7);
        STAFHost host9 = testCase.getStafHosts().get(8);
        STAFHost host10 = testCase.getStafHosts().get(9);

        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(10);
        final String aeronDir = "-Daeron.dir=" + host1.getTmpDir() + host1.getPathSeperator() + testCase.getName();
        int port = getPort(host1.getHostName());
        String channel = "-c=udp://localhost:" + port;
        String embedded = testCase.getIsEmbedded() ? " --driver=embedded" :  "--driver=external";

        startProcess(host1.getHostName(),
                host1.getJavaPath() + host1.getPathSeperator() + "java " + aeronDir + host1.getPathSeperator() + "sub1 " + host1.getProperties() +
                        " -cp " + host1.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " " + embedded + " " + channel + " " + host1.getOptions(),
                "Test0125-sub1", 10);
        startProcess(host2.getHostName(),
                host2.getJavaPath() + host2.getPathSeperator() + "java " + aeronDir + host2.getPathSeperator() + "sub2 " + host2.getProperties() +
                        " -cp " + host2.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " " + embedded + " " + channel + " " + host2.getOptions(),
                "Test0125-sub2", 10);
        startProcess(host3.getHostName(),
                host3.getJavaPath() + host3.getPathSeperator() + "java " + aeronDir + host3.getPathSeperator() + "sub3 " + host3.getProperties() +
                        " -cp " + host3.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " " + embedded + " " + channel + " " + host3.getOptions(),
                "Test0125-sub3", 10);
        startProcess(host4.getHostName(),
                host4.getJavaPath() + host4.getPathSeperator() + "java " + aeronDir + host4.getPathSeperator() + "sub4 " + host4.getProperties() +
                        " -cp " + host4.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " " + embedded + " " + channel + " " + host4.getOptions(),
                "Test0125-sub4", 10);
        startProcess(host5.getHostName(),
                host5.getJavaPath() + host5.getPathSeperator() + "java " + aeronDir + host5.getPathSeperator() + "sub5 " + host5.getProperties() +
                        " -cp " + host5.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " " + embedded + " " + channel + " " + host5.getOptions(),
                "Test0125-sub5", 10);
        startProcess(host6.getHostName(),
                host6.getJavaPath() + host6.getPathSeperator() + "java " + aeronDir + host6.getPathSeperator() + "pub1 " + host6.getProperties() +
                        " -cp " + host6.getClasspath() +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " " + embedded + " " + channel + " " + host6.getOptions(),
                "Test0125-pub1", 10);
        startProcess(host7.getHostName(),
                host7.getJavaPath() + host7.getPathSeperator() + "java " + aeronDir + host7.getPathSeperator() + "pub2 " + host7.getProperties() +
                        " -cp " + host7.getClasspath() +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " " + embedded + " " + channel + " " + host7.getOptions(),
                "Test0125-pub2", 10);
        startProcess(host8.getHostName(),
                host8.getJavaPath() + host8.getPathSeperator() + "java " + aeronDir + host8.getPathSeperator() + "pub3 " + host8.getProperties() +
                        " -cp " + host8.getClasspath() +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " " + embedded + " " + channel + " " + host8.getOptions(),
                "Test0125-pub3", 10);
        startProcess(host9.getHostName(),
                host9.getJavaPath() + host9.getPathSeperator() + "java " + aeronDir + host9.getPathSeperator() + "pub4 " + host9.getProperties() +
                        " -cp " + host9.getClasspath() +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " " + embedded + " " + channel + " " + host9.getOptions(),
                "Test0125-pub4", 10);
        startProcess(host10.getHostName(),
                host10.getJavaPath() + host10.getPathSeperator() + "java " + aeronDir + host10.getPathSeperator() + "pub5 " + host10.getProperties() +
                        " -cp " + host10.getClasspath() +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " " + embedded + " " + channel + " " + host10.getOptions(),
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
