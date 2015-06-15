package com.kaazing.staf_aeron.util;

import java.io.File;

/**
 * Created by philip on 6/15/15.
 */
public class Cleanup
{
    public static void deleteDir(File file)
    {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                System.out.println("Deleting: " + f);
                deleteDir(f);
            }
        }
        file.delete();
    }

    public static void main(String[] args)
    {
        File f = new File(args[0]);
        deleteDir(f);
    }
}
