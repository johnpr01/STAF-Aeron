package com.kaazing.staf_aeron;

import java.util.List;

/**
 * Created by mike on 6/2/2015.
 */
public class YAMLTestCase {
    private String name;
    private List<String> hosts;
    private List<String> options;
    private List<String> properties;

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


}
