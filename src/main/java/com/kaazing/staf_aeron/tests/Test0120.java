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

public class Test0120 extends Test
{
    public Test0120(YAMLTestCase testCase)
    {
        STAFHost host1 = testCase.getStafHosts().get(0);
        STAFHost host2 = testCase.getStafHosts().get(1);
        STAFHost host3 = testCase.getStafHosts().get(2);
        STAFHost host4 = testCase.getStafHosts().get(3);

        processes = new HashMap<String, AeronSTAFProcess>();
        latch = new CountDownLatch(4);

        startProcess(host1,
                host1.getJavaPath() + host1.getPathSeperator() + "java -Daeron.dir=" + host1.getTmpDir() + host1.getPathSeperator() + testCase.getName() + host1.getPathSeperator() + "pub" + host1.getProperties() +
                        " -cp " + host1.getClasspath() +
                        " uk.co.real_logic.aeron.tools.PublisherTool" +
                        " --driver=embedded -c=udp://localhost:44444 " + host1.getOptions(),
                "Test0120-pub", 10);

        try
        {
            Thread.sleep(5000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        startProcess(host2,
                host2.getJavaPath() + host2.getPathSeperator() + "java -Daeron.dir=" + host2.getTmpDir() + host2.getPathSeperator() + testCase.getName() + host2.getPathSeperator() + "sub" + host2.getProperties()  +
                        " -cp " + host2.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:44444 " + host2.getOptions(),
                "Test0120-sub1", 10);

        startProcess(host3,
                host3.getJavaPath() + host3.getPathSeperator() + "java -Daeron.dir=" + host3.getTmpDir() + host3.getPathSeperator() + testCase.getName() + host3.getPathSeperator() + "sub" + host3.getProperties()  +
                        " -cp " + host3.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:44444 " + host3.getOptions(),
                "Test0120-sub2", 10);

        startProcess(host3,
                host4.getJavaPath() + host4.getPathSeperator() + "java -Daeron.dir=" + host4.getTmpDir() + host4.getPathSeperator() + testCase.getName() + host4.getPathSeperator() + "sub" + host4.getProperties()  +
                        " -cp " + host4.getClasspath() +
                        " uk.co.real_logic.aeron.tools.SubscriberTool" +
                        " --driver=embedded -c=udp://localhost:44444 " + host4.getOptions(),
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

    public void run()
    {

    }

    public void validate()
    {

    }
}
