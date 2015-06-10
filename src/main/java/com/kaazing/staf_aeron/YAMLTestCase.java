package com.kaazing.staf_aeron;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 6/2/2015.
 */
public class YAMLTestCase {
    private String name;
    private List<String> hosts;
    private List<String> options;
    private List<String> properties;
    private List<STAFHost> stafHosts;
    private boolean isEmbedded;

    public boolean getIsEmbedded() {
        return isEmbedded;
    }

    public void setIsEmbedded(boolean isEmbedded) {
        this.isEmbedded = isEmbedded;
    }



    public String getName(){
        return name;
    }

    public void setName(String n){
        this.name = n;
    }

    public List<String> getHosts(){
        return hosts;
    }

    public void setHosts(List<String> h){
        this.hosts = h;
    }

    public List<String> getOptions(){
        return options;
    }

    public void setOptions(List<String> o){
        this.options = o;
    }

    public List<String> getProperties(){
        return properties;
    }

    public void setProperties(List<String> p){
        this.properties = p;
    }

    //Method to create only the host objects we care about from the information in staticHosts. Also will set which
    // hosts in the master host list (staticHosts) are active for use when starting stand alone media drivers.
    public void loadHosts(STAFHosts staticHosts){

        if((hosts.size() != properties.size()) && (hosts.size() != options.size())) {
            System.out.println("OH NO we didnt produce same size arrays for hosts options and properties!");
        }

        stafHosts = new ArrayList<STAFHost>();
        int count = 0;
        for (String s : hosts){
            for (STAFHost h : staticHosts.getHosts()){
                if(h.getHostName().equalsIgnoreCase(s)){
                    STAFHost testHost = new STAFHost(h);
                    testHost.setProperties(this.properties.get(count));
                    testHost.setOptions(this.options.get(count));
                    switch(testHost.getOS().toLowerCase()){
                        case "linux":
                            testHost.setPathSeperator("/");
                            break;
                        case "windows":
                            testHost.setPathSeperator("\\");
                            break;
                        default:
                            testHost.setPathSeperator("/");
                            break;
                    }
                    count++;
                    stafHosts.add(testHost);
                    h.setActive(true);
                    break;
                }
            }
        }

        System.out.println(stafHosts.toString());
    }

    public List<STAFHost> getStafHosts(){
        return stafHosts;
    }




}
