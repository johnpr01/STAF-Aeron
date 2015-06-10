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
import com.kaazing.staf_aeron.tests.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Test0080 extends Test
{
    public Test0080(YAMLTestCase testCase)
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
                "Test0080-sub", 10);
        startProcess(host2.getHostName(),
                host2.getJavaPath() + host2.getPathSeperator() + "java " + aeronDir + "/pub" + host2.getProperties() +
                        " -cp " + host2.getClasspath() +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " " + embedded + " " + channel + " " + host2.getOptions(),
                "Test0080-pub1", 10);
        startProcess(host3.getHostName(),
                host3.getJavaPath() + host3.getPathSeperator() + "java " + aeronDir + "/pub" + host3.getProperties() +
                        " -cp " + host3.getClasspath() +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " " + embedded + " " + channel + " " + host3.getOptions(),
                "Test0080-pub2", 10);
        // allow the publishers to send for a few seconds before killing the publishers
        try
        {
            Thread.sleep(10000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // Kill the publisher . Repeat test using different kill method
        // (i.e suspend the publisher)
        killProcess("Test0080-pub1");
       
        try
        {
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
// the expected result: the subscriber will continue to get messages from publisher 2. Eventually the subscriber reports
// the connection with publisher 1 is INACTIVE
    public Test validate()
    {
        final Map result1 = processes.get("Test0080-sub").getResults();
        final Map result2 = processes.get("Test0080-pub1").getResults();
        final Map result3 = processes.get("Test0080-pub2").getResults();
        return this;
    }
}
