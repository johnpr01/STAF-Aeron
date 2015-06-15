package com.kaazing.staf_aeron.util;

import java.io.File;

/**
 * Created by philip on 6/15/15.
 */
public class CheckForFiles {

    public static void main(String[] args)
    {
        File f = new File(args[0]);
        if (f.exists()) {
            System.exit(0);
        }
        System.exit(1);
    }
}
