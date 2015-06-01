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

import java.util.ArrayList;


public class STAFHosts {
    ArrayList<STAFHost> hosts = null;

    public STAFHost getHostByName(String name){
        STAFHost rc = null;
        for (STAFHost s : hosts){
            if(s.getHostname().contains(name)){
                rc = s;
                break;
            }
        }
        return rc;
    }

    public void dumpHosts(){
        for (STAFHost s : hosts){
            System.out.println("Hostname: " + s.getHostname() + " ip: " + s.getIPAddress() + " classpath: " + s.getIPAddress() + " os: " + s.getOS());
        }
    }
}
