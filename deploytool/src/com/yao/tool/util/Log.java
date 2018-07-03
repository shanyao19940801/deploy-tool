package com.yao.tool.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    public static void INFO(String logContext) {
        printLog(logContext,1);
    }

    public static void printLog(String logContext, int type) {

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringBuffer context = new StringBuffer();

        if (type == 1) {
            context.append("INFO:");
        }
        if (type == 2) {
            context.append("ERROR: ");
        }
        context.append("[" + date + "] :");
        context.append(logContext);

        String path = Constants.logPath + File.separator + "deploy.log";
        try {
            FileUtils.writeContextToFole(path,context.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.ERROR("" + e.getMessage());
        }

    }

    public static void ERROR(String logContext) {
        printLog(logContext,2);
    }

}
