package com.yao.tool.util;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class FileUtils {

    public static final String[] _regular_src = {"/src", "/main/java", "/resources", "/main/resources"};
    public static final String[] _regular_web = {"/web", "/main/webapp"};


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

                copyFilesAndDir(sourcePath, targetPath);
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
            String dir = targetPath.substring(0, lastIndex);
            File temp = new File(dir);
            temp.mkdirs();
            File targetFile = new File(new File(dir).getAbsolutePath() + File.separator + fileName);
            try {
                targetFile.setLastModified(sourceFile.lastModified());

                copyFile(sourceFile, targetFile);
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
            File fileCode = new File(path, "code.txt");
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

    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        if (targetFile.exists()) {
            targetFile.delete();
        }
        Files.copy(sourceFile.toPath(), targetFile.toPath());
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
                        lists.addAll(traverseFolderWithTreeSet(file2.getAbsolutePath(), lists, orginalPath));
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
        return traverseFolderWithTreeSet(path, new TreeSet<String>(), path);
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


    public static void writeContextToFole(String filePath, String context) throws IOException {

        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
        bw.write(context);
        bw.newLine();
        bw.close();
    }

    public static String deployFileDeploy(File codeFile, String outBuildPath, String outFilePath, boolean copySourceFile) throws IOException {
        Set<String> fileInfo = handleFileInfo(codeFile, outBuildPath, copySourceFile);
        String[] allowSuffixes = ConfigUtil.getValue("allowsuffix").split(",");
        String[] denySuffix = ConfigUtil.getValue("denysuffix").split(",");
        String parentPath = null;
        String command = null;
        StringBuffer buffer = new StringBuffer("");
        String temp = "";
        Boolean include = Boolean.valueOf(false);
        Boolean exclude = Boolean.valueOf(true);
        for (String f : fileInfo)
            if ((f != null) && (!"".equals(f)) && (f.indexOf(".") != -1)) {

                parentPath = getParentPath(f);
                boolean mkdirok = mkdir(outFilePath + "/" + parentPath);
                if (!mkdirok) {
                    buffer.append("创建目录" + outFilePath + "/" + parentPath + "失败\n");
                }
                if (copySourceFile) {
                    command = getCommand(outBuildPath + "/" + f, outFilePath + "/" + parentPath);
                    exec(command);
                } else {
                    parentPath = getParentPath(f);

                    temp = outBuildPath + "/" + f.replaceAll(".java", ".class");
                    File file = new File(temp);
                    if (!file.exists()) {
                        buffer.append(file.getAbsolutePath() + "不存在\n");
                    }


                    String suffix = temp.substring(temp.lastIndexOf(".") + 1);
                    for (int i = 0; i < allowSuffixes.length; i++) {
                        if ((suffix.equals(allowSuffixes[i])) || ("all".equals(allowSuffixes[i]))) {
                            include = Boolean.valueOf(true);
                            break;
                        }
                    }

                    for (int i = 0; i < denySuffix.length; i++) {
                        if (suffix.equals(denySuffix[i])) {
                            exclude = Boolean.valueOf(false);
                            break;
                        }
                    }
                    if ((include.booleanValue()) && (exclude.booleanValue())) {
                        command = getCommand(temp, outFilePath + "/" + parentPath);
                        if (!exec(command)) {
                            buffer.append("执行" + command + "失败\n");
                        }
                    }
                    include = Boolean.valueOf(false);
                    exclude = Boolean.valueOf(true);
                }
            }
        return buffer.toString();
    }

    public static Set<String> handleFileInfo(File codeFilePath, String outBuildPath, boolean copySourceFile) throws IOException {
        Set<String> lineFiles = new java.util.HashSet();
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(codeFilePath));
        String line = null;
        while ((line = reader.readLine()) != null) {
            lineFiles.add(copySourceFile ? line : handleLine(line));
        }
        Set<String> matchFile = new java.util.HashSet();

        Set<String> file = null;
        for (String codeFile : lineFiles) {
            if (codeFile.endsWith(".java")) {
                file = matchFile(codeFile, outBuildPath);
                if (file != null) {
                    for (String str : file)
                        matchFile.add(str);
                }
            }
        }
        for (String f : matchFile) {
            lineFiles.add(f);
        }
        return lineFiles;
    }

    public static String getParentPath(String codePath) {
        return codePath.substring(0, codePath.lastIndexOf("/"));
    }

    public static String handleLine(String line) throws IOException {
        line = line.replaceAll("\\\\", "/");
        if ((line != null) && (!"".equals(line))) {
            if (line.indexOf(".java") != -1)
                return "WEB-INF/classes" + parseFile(line, _regular_src);
            if (line.indexOf(".tld") != -1)
                return parseFile(line, new String[]{"WEB-INF"});
            if (line.indexOf("/lib") != -1)
                return "WEB-INF/lib" + parseFile(line, new String[]{"/"});
            if ((line.substring(line.length() - 3, line.length()).equals(".js")) && (exists(line, _regular_web))) {
                return parseFile(line, _regular_web);
            }


            if ((exists(line, _regular_src)) && (exists(line, _regular_web)) && (!line.endsWith(".java"))) {
                if (line.contains("web.xml")) {
                    return parseFile(line, _regular_web);
                }

                return parseFile(line, _regular_web);
            }


            if ((exists(line, _regular_src)) && (!exists(line, _regular_web)))
                return "WEB-INF/classes" + parseFile(line, _regular_src);
            if ((!exists(line, _regular_src)) && (exists(line, _regular_web))) {
                return parseFile(line, _regular_web);
            }


            return line;
        }

        return "";
    }

    private static String parseFile(String line, String[] keywords) {
        for (String keyword : keywords) {
            line = parseFile(line, keyword);
        }

        return line;
    }

    private static String parseFile(String line, String keyword) {
        int index = line.lastIndexOf(keyword) + keyword.length();
        if (line.contains(keyword)) {
            String str = line.substring(index);
            char[] chars = str.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (((c == '/') || (c == '\\')) && (i + 1 < str.length())) {
                    return str.substring(i);
                }
            }
        }

        return line;
    }

    public static boolean mkdir(String path) {
        try {
            File file = new File(path);
            if ((!file.isDirectory()) && (!file.exists())) {
                return file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getCommand(String filePath, String outPath) {
        String os = System.getProperties().getProperty("os.name");
        if (os != null) {
            os = os.toUpperCase();
        } else {
            os = "";
        }

        if (os.contains("MAC")) {
            return "cp -p " + filePath + " " + outPath;
        }

        return "cmd /c copy " + filePath.replaceAll("/", "\\\\") + " " + outPath.replaceAll("/", "\\\\");
    }

    public static boolean exec(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Set<String> matchFile(String codeFile, String outBuildPath)
            throws java.io.UnsupportedEncodingException {
        File file = new File(enCoding(outBuildPath) + "/" + codeFile.replaceAll(".java", ".class"));
        if ((file == null) || (!file.exists()))
            return null;
        File f = new File(file.getParent());
        String name = file.getName().substring(0, file.getName().indexOf("."));
        File[] files = f.listFiles();
        Set<String> set = new java.util.HashSet();
        for (File fs : files) {
            if ((fs.getName().indexOf("$") != -1) && (fs.getName().startsWith(name))) {
                set.add(fs.getAbsolutePath().substring(outBuildPath.length()).replaceAll("\\\\", "/"));
            }
        }
        return set;
    }

    private static boolean exists(String line, String[] keywords) {
        boolean result = false;
        for (String keyword : keywords) {
            result |= line.contains(keyword);
        }
        return result;
    }
    public static void copyFilesAndDirForDeploy(String sourceDir, String targetDir) throws IOException {
        new File(targetDir).mkdirs();
        File[] file = new File(sourceDir).listFiles();
        if (file != null) {
            for (int i = 0; i < file.length; i++) {
                if (!file[i].getPath().equals(targetDir))
                {


                    if (file[i].isFile()) {
                        File sourceFile = file[i];
                        File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                        if (!"code.txt".equals(targetFile.getName())) {
                            try {
                                copyFile(sourceFile, targetFile);
                            } catch (IOException e) {
                                throw new IOException("将日志文件目录中的文件复制到部署目录时出错。");
                            }
                        }
                    }

                    if ((file[i].isDirectory()) && (!".svn".equals(file[i].getName()))) {
                        String dir1 = sourceDir + "/" + file[i].getName();
                        String dir2 = targetDir + "/" + file[i].getName();
                        copyFilesAndDir(dir1, dir2);
                    }
                }
            }
        }
    }



}
