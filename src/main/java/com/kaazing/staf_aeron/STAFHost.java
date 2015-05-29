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
