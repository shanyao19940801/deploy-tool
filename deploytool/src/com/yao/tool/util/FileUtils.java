package com.yao.tool.util;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

public class FileUtils {
    public static String enCoding(String path) throws java.io.UnsupportedEncodingException {
        if (path.indexOf("?") != -1)
            return new String(path.getBytes("iso-8859-1"), "utf-8");
        return path;
    }

    //备份文件
    public static void backupFile(String sourceDir, String targetDir, String logPath) throws IOException {
        File[] files = new File(sourceDir).listFiles();
        String codePath = logPath + "/code.txt";
        File codeFile = new File(codePath);
        if (codeFile.isFile() && codeFile.exists()) {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(codeFile));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String lineTxt = null;
            while ((lineTxt = reader.readLine()) != null) {
                String sourcePath = sourceDir + lineTxt;
                String targetPath = targetDir + lineTxt;

                copyFilesAndDir(sourcePath,targetPath);
            }
            reader.close();
        }
    }

    public static void copyFilesAndDir(String sourcePath, String targetPath) {
        Log.INFO(" copy " + sourcePath + " to " + targetPath);
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists()) {
            String fileName = sourceFile.getName();
            int lastIndex = targetPath.lastIndexOf("\\");
            String dir = targetPath.substring(0,lastIndex);
            File temp = new File(dir);
            temp.mkdirs();
            File targetFile = new File(new File(dir).getAbsolutePath() + File.separator + fileName);
            try {
                targetFile.setLastModified(sourceFile.lastModified());

                copyFile(sourceFile,targetFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean writePathToFile(List<String> list, String path) {
        Log.INFO("----生成code.txt------");
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            File fileCode = new File(path,"code.txt");
            if (!fileCode.exists()) {
                fileCode.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileCode));
            for (String filePath : list) {
                writer.write(filePath);
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.ERROR("ERROR: 生成code.txt出错" + e.getMessage());
            return false;
        }
        Log.INFO("----生成code.txt结束------");
        return true;
    }

    public static void copyFile(File sourceFile, File targetFile) throws IOException
    {
        if (targetFile.exists()) {
            targetFile.delete();
        }
        Files.copy(sourceFile.toPath(),targetFile.toPath());
    }


    //获取path目录下所有文件的路径
    private static List<String> traverseFolderWithTreeSet(String path, TreeSet<String> lists, String orginalPath) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        lists.addAll(traverseFolderWithTreeSet(file2.getAbsolutePath(),lists,orginalPath));
                    } else {
                        lists.add(file2.getAbsolutePath().substring(orginalPath.length()));
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        return new ArrayList<String>(lists);
    }
    public static List<String> traverseFolder(String path) {
        return traverseFolderWithTreeSet(path,new TreeSet<String>(),path);
    }

    public static String deployFile(String outPutPath, String tomcatFilePathTxtText, String logPath) {
        try {
            backupFile(outPutPath, tomcatFilePathTxtText, logPath);
        } catch (IOException e) {
            e.printStackTrace();
            return "false";
        }
        return "";
    }


    public static void writeContextToFole(String filePath,String context) throws IOException {

        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
        bw.write(context);
        bw.newLine();
        bw.close();
    }
}
