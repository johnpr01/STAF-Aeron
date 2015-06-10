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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class   AeronSTAFRunner
{
    private ExecutorService threadPool = null;
    private int startPort = 1024;
    private int endPort = 3000;
    private int currentPort = startPort;
    static final String TEST_CLASS_PREFIX = "com.kaazing.staf_aeron.tests.";

    public AeronSTAFRunner()
    {
        STAFHosts availableHosts = YAMLParser.parseHosts("config/hosts.yaml");
        YAMLTestCases tests = YAMLParser.parseTests("config/tests.yaml");
        //This step is needed before using the test cases. It correlates hostNames to host objects and populates each
        // test case
        tests.validateAndPopulateHosts(availableHosts);
        if(tests.isStandaloneDriver()){
            //Method to loop through available hosts, and start drivers on the active hosts
            startStandAloneDrivers(availableHosts);
        }

        YAMLTestCases cases = tests;
        threadPool = Executors.newFixedThreadPool(5);
        Class test = null;
        // Loop through each test case and start the corresponding class using the test's name as specified via YAML
        for(YAMLTestCase t : cases.getTestCases()){
            try {
                test = Class.forName(TEST_CLASS_PREFIX + t.getName());

                Class[] types = {YAMLTestCase.class};
                Constructor constructor = test.getConstructor(types);

                Object[] params = {t};
                // Here is where we would add the test permutations
                Object newTestInstance = constructor.newInstance(params);
                threadPool.execute((Test)newTestInstance);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

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

    public void startStandAloneDrivers(STAFHosts availableHosts){

    }

    public static void main(String[] args)
    {
        new AeronSTAFRunner();
    }
}
