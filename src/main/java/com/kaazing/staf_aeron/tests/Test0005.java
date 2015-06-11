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
import java.util.concurrent.CountDownLatch;

public class Test0005 extends Test
{
    public Test0005(YAMLTestCase testCase)
    {
        STAFHost host1 = testCase.getStafHosts().get(0);

        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(2);

        int port = getPort(host1.getHostName());
        String channel = "-c=udp://localhost:" + port;
        String embedded = testCase.getIsEmbedded() ? " --driver=embedded" :  "--driver=external";

        startProcess(host1.getHostName(),
                host1.getJavaPath() + host1.getPathSeperator() + "java -Daeron.dir=" + host1.getTmpDir() +
                        host1.getPathSeperator() + testCase.getName() + host1.getPathSeperator() + "pub" +
                        host1.getProperties() +
                        " -cp " + host1.getClasspath() +
                        " uk.co.real_logic.aeron.tools.PublisherTool " +
                        channel + " " + embedded + " -m=1000000 " + host1.getOptions(),
                testCase.getName() + "-pub", 60);

        try
        {
            Thread.sleep(5000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        startProcess(host1.getHostName(),
                host1.getJavaPath() + host1.getPathSeperator() + "java -Daeron.dir=" + host1.getTmpDir() +
                        host1.getPathSeperator() + testCase.getName() + host1.getPathSeperator() + "sub" +
                        host1.getProperties()  +
                        " -cp " + host1.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool " +
                        channel + " " + embedded + " -m=1000000 " + host1.getOptions(),
                testCase.getName() + "-sub", 60);

        try
        {
            latch.await();
            validate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Test validate()
    {
        //final Map result1 = processes.get("Test0005-sub").getResults();
        //final Map result2 = processes.get("Test0005-pub").getResults();
        return this;
    }
}
