package com.kaazing.staf_aeron;

import java.util.List;

/**
 * Created by mike on 6/2/2015.
 */
public class YAMLTestCases {

    //Flag to runner to start standalone drivers, if any test requires this, all active hosts get a driver regardless
    // of whether there is a non-embedded test being run on that host or not
    private boolean standaloneDriver = false;

    private List<YAMLTestCase> testCases;

    public List<YAMLTestCase> getTestCases(){
        return this.testCases;
    }

    public void setTestCases(List<YAMLTestCase> t){
        this.testCases = t;
    }

    public void dump(){
        for (YAMLTestCase t : testCases){
            System.out.print("Testcase: " + t.getName() + " Hosts: " + t.getHosts().toString() + " Options: " + t.getOptions().toString() + " Properties: " + t.getProperties().toString() + " isEmbedded: " + t.getIsEmbedded());
        }
    }


    public void validateAndPopulateHosts(STAFHosts availableHosts) {
        for (YAMLTestCase t : testCases){
            if(!t.getIsEmbedded()){
                standaloneDriver = true;
            }
            t.loadHosts(availableHosts);
        }
    }

    public boolean isStandaloneDriver(){
        return standaloneDriver;
    }
}
