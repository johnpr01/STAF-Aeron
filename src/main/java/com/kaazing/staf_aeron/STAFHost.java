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

import java.net.InetAddress;

public class STAFHost
{
  private String hostname;
  private InetAddress ipAddress;
  private String os;
  private String classpath;

  public STAFHost()
  {

  }

  public void setHostname(String hostname)
  {
    this.hostname = hostname;
  }

  public String getHostname()
  {
    return hostname;
  }

  public void setIPAddress(InetAddress ipAddress)
  {
    this.ipAddress = ipAddress;
  }

  public InetAddress getIPAddress()
  {
    return ipAddress;
  }

  public void setOS(String os)
  {
    this.os = os;
  }

  public String getOS()
  {
    return os;
  }

  public void setClasspath(String classpath)
  {
    this.classpath = classpath;
  }

  public String getClasspath()
  {
    return classpath;
  }
}
