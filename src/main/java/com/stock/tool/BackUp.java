package com.stock.tool;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/28.
 */
public class BackUp {
    public static void main(String[] args) {

    }

    public void backUpHistoryData(String path) throws IOException {
        FileUtils.deleteDirectory(new File("E:\\stock back up\\stock"));
        String backUpDirectory = "E:\\stock back up\\" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        FileUtils.moveDirectory(new File(path), new File(backUpDirectory));
    }
}
