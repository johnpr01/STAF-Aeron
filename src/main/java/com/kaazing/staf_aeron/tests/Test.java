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
import java.util.concurrent.CountDownLatch;

public class Test extends Thread
{
    protected HashMap<String, AeronSTAFProcess> processes = null;
    protected static final String CLASSPATH = "TODO";
    protected CountDownLatch latch = null;

    protected void startProcess(final String machine, final String command, final String name, final int timeout)
    {
        processes.put(name, new AeronSTAFProcess(machine, command, name, latch, timeout));
    }

    protected void killProcess(final String name)
    {
        processes.get(name).kill();
    }
}
