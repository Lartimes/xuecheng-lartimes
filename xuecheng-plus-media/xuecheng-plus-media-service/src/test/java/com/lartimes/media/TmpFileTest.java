package com.lartimes.media;

import org.junit.jupiter.api.Test;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/4/23 11:39
 */
public class TmpFileTest {
    @Test
    public void testTmpFile()throws Exception{
//        File tempFile = File.createTempFile("chunk", "");
//        System.out.println(tempFile.getAbsolutePath());
        String s = "qwe/chunk/";
        System.out.println(s.substring(0, s.indexOf("/chunk/")));
        s += ".mp4";
        System.out.println(s.substring(s.indexOf(".")));
    }
}
