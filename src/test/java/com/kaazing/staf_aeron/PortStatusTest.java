package com.kaazing.staf_aeron;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.ibm.staf.STAFHandle;
import com.ibm.staf.STAFResult;
import com.ibm.staf.STAFUtil;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class PortStatusTest {
    @Test
    public void checkPort()
    {
        try {
            String machine = "local";
            int currentPort = 20000;
            String command = "java -cp staf.jar com.kaazing.staf_aeron.util.PortStatus " + currentPort;
            String timeout = "5s";
            final String request = "START SHELL COMMAND " + STAFUtil.wrapData(command) +
                    " WAIT " + timeout + " RETURNSTDOUT STDERRTOSTDOUT";
            STAFHandle tmp = new STAFHandle("port");
            STAFResult result = tmp.submit2(machine, "Process", request);

            System.out.println("RESULT: " + result.rc);
            if (result.rc != 0) {
                assert false;
            } else {
                assert true;
            }

            final Map resultMap = (Map)result.resultObj;
            final String processRC = (String)resultMap.get("rc");

            if (!processRC.equals("0")) {
               assert false;
            } else {
                assert true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkPortFailure()
    {
        try {
            String machine = "local";
            int currentPort = -1;
            String command = "java -cp staf.jar com.kaazing.staf_aeron.util.PortStatus " + currentPort;
            String timeout = "5s";
            final String request = "START SHELL COMMAND " + STAFUtil.wrapData(command) +
                    " WAIT " + timeout + " RETURNSTDOUT STDERRTOSTDOUT";
            STAFHandle tmp = new STAFHandle("port");
            STAFResult result = tmp.submit2(machine, "Process", request);
            if (result.rc != 0) {
                assert false;
            } else {
                assert true;
            }

            final Map resultMap = (Map)result.resultObj;
            final String processRC = (String)resultMap.get("rc");

            if (!processRC.equals("0")) {
                assert false;
            } else {
                assert true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
