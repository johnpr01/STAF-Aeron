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

public class Test0075 extends Test
{
    public Test0075(YAMLTestCase testCase)
    {
        STAFHost host1 = testCase.getStafHosts().get(0);
        STAFHost host2 = testCase.getStafHosts().get(1);

        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(2);
        final String aeronDir = "-Daeron.dir=" + host1.getTmpDir() + host1.getPathSeperator() + testCase.getName();
        int port = getPort(host1.getHostName());
        String channel = "-c=udp://localhost:" + port;
        String embedded = testCase.getIsEmbedded() ? " --driver=embedded" :  "--driver=external";

        startProcess(host1.getHostName(),
                host1.getJavaPath() + host1.getPathSeperator() + "java " + aeronDir + host1.getPathSeperator() + "sub" + host1.getProperties() +
                        " -cp " + host1.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " " + embedded + " " + channel + " " + host1.getOptions(),
                "Test0075-sub", 10);
        startProcess(host2.getHostName(),
                host2.getJavaPath() + host2.getPathSeperator() + "java " + aeronDir + "/pub" + host2.getProperties() +
                        " -cp " + host2.getClasspath() +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " " + embedded + " " + channel + " " + host2.getOptions(),
                "Test0075-pub", 10);
        // allow the publisher to send for a few seconds before it is suspended
        try
        {
            Thread.sleep(10000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // suspend the publisher. After ~5sec the publisher is resumed. Repeat test using different kill method
        // (i.e kill publisher via Ctrl-C and restart)
        pauseProcess("Test0075-pub");
        try
        {
            Thread.sleep(5000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        resumeProcess("Test0075-pub");
        try
        {
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
// the expected result for the suspend case: after the publisher is resumed, the subscriber should
// continue to receive messages
    public Test validate()
    {
        final Map result1 = processes.get("Test0075-sub").getResults();
        final Map result2 = processes.get("Test0075-pub").getResults();
        return this;
    }
}