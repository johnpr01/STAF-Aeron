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

package com.kaazing.staf_aeron;

import com.kaazing.staf_aeron.tests.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AeronSTAFRunner
{
    private ExecutorService threadPool = null;
    private static final String[] EMPTY = { "" };
    private int startPort = 1024;
    private int endPort = 3000;
    private int currentPort = startPort;

    public AeronSTAFRunner()
    {
        threadPool = Executors.newFixedThreadPool(5);
        threadPool.execute(new Test0000(EMPTY, EMPTY));
        threadPool.execute(new Test0005(EMPTY, EMPTY));
        threadPool.execute(new Test0035(EMPTY, EMPTY));
        try
        {
            threadPool.shutdown();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void enqueueTest()
    {

    }

    public static void main(String[] args)
    {
        new AeronSTAFRunner();
    }
}
