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
  private String hostName;
  private String ipAddress;
  private String OS;
  private String classpath;
  private String javaPath;
  private String tmpDir;



  private String pathSeperator;
  private String options;
  private String properties;

  private boolean active;





  public STAFHost()
  {

  }

  //Copy Constructor
  public STAFHost(STAFHost s) {
    this.hostName = s.getHostName();
    this.ipAddress = s.getIpAddress();
    this.OS = s.getOS();
    this.classpath = s.getClasspath();
    this.javaPath = s.getJavaPath();
    this.tmpDir = s.getTmpDir();
    this.pathSeperator = s.getPathSeperator();
    this.options = s.getOptions();
    this.properties = s.getProperties();
    this.active = s.getActive();
  }


  public void setHostName(String hostName)
  {
    this.hostName = hostName;
  }

  public String getHostName()
  {
    return hostName;
  }

  public void setIpAddress(String ipAddress)
  {
    this.ipAddress = ipAddress;
  }

  public String getIpAddress()
  {
    return ipAddress;
  }

  public void setOS(String os)
  {
    this.OS = os;
  }

  public String getOS()
  {
    return OS;
  }

  public void setClasspath(String classpath)
  {
    this.classpath = classpath;
  }

  public String getClasspath()
  {
    return classpath;
  }

  public void setJavaPath(String javaPath){
    this.javaPath = javaPath;
  }

  public String getJavaPath(){
    return javaPath;
  }

  public String getOptions() {
    return options;
  }

  public void setOptions(String options) {
    this.options = options;
  }



  public String getProperties() {
    return properties;
  }

  public void setProperties(String properties) {
    this.properties = properties;
  }

  public boolean getActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getPathSeperator() {
    return pathSeperator;
  }

  public void setPathSeperator(String pathSeperator) {
    this.pathSeperator = pathSeperator;
  }

  public String getTmpDir() {
    return tmpDir;
  }

  public void setTmpDir(String tmpDir) {
    this.tmpDir = tmpDir;
  }

}
