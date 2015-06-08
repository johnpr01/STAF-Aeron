package com.kaazing.staf_aeron;

import java.util.List;

/**
 * Created by mike on 6/2/2015.
 */
public class YAMLTestCases {
    private List<YAMLTestCase> testCases;

    public List<YAMLTestCase> getTestCases(){
        return this.testCases;
    }

    public void setTestCases(List<YAMLTestCase> t){
        this.testCases = t;
    }

    public void dump(){
        for (YAMLTestCase t : testCases){
            System.out.print("Testcase: " + t.getName() + " Hosts: " + t.getHosts().toString() + " Options: " + t.getOptions().toString() + " Properties: " + t.getProperties().toString());
        }
    }

    public void validateAndPopulateHosts(STAFHosts availableHosts) {
    }
}
