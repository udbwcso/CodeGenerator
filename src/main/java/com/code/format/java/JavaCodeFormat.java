package com.code.format.java;


import de.hunsicker.io.FileFormat;
import de.hunsicker.jalopy.Jalopy;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Administrator on 2016/8/19.
 */
public class JavaCodeFormat {
    public static void main(String[] args) throws FileNotFoundException {
        Jalopy jalopy = new Jalopy();
        jalopy.setFileFormat(FileFormat.AUTO);
        jalopy.setEncoding("UTF-8");
        jalopy.setInspect(true);
        jalopy.setBackup(true);
        jalopy.setBackupDirectory("E:\\workspace\\code\\backup");
        jalopy.setBackupLevel(0);
//        jalopy.setHistoryPolicy();
        jalopy.setForce(false);
        jalopy.setInput(new File("E:\\workspace\\code\\FineOrderPicture.java"));
        jalopy.setOutput(new File("E:\\workspace\\code\\FineOrderPicture.java"));
        jalopy.format();
    }
}
