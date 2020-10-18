package com.liujun.micro.autocode.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 文件操作公共方法
 *
 * @author liujun
 * @version 0.0.1
 */
@Slf4j
public class FileUtils {

    /**
     * 进行公共的文件写入的操作
     *
     * @param filePath 输出的文件路径
     * @param fileName 输出文件名
     * @param data     数据内容
     */
    public static void writeFile(String filePath, String fileName, String data) {
        writeFile(filePath, fileName, data, false);
    }

    /**
     * 进行公共的文件写入的操作
     *
     * @param filePath 输出的文件路径
     * @param fileName 输出文件名
     * @param data     数据内容
     */
    public static void writeFile(
            String filePath, String fileName, String data, boolean append) {

        OutputStream fw = null;
        OutputStreamWriter outStreamWriter = null;
        BufferedWriter buffOut = null;
        try {
            File outPathCheck = new File(filePath);
            // 当文件夹不存在，则执行创建操作
            if (!outPathCheck.exists()) {
                boolean mkRsp = outPathCheck.mkdirs();
                if (!mkRsp) {
                    throw new IllegalArgumentException("path error:" + outPathCheck);
                }
            }

            File outFile = new File(filePath, fileName);
            fw = new FileOutputStream(outFile, append);
            // 输出指定编码的文件
            outStreamWriter = new OutputStreamWriter(fw, StandardCharsets.UTF_8);
            buffOut = new BufferedWriter(outStreamWriter);
            buffOut.write(data);
        } catch (IOException e) {
            log.error("FileUtils IOException", e);
        } finally {
            StreamUtils.close(buffOut);
            StreamUtils.close(outStreamWriter);
            StreamUtils.close(fw);
        }
    }


    /**
     * 执行文件的清
     *
     * @param filePath 文件路径
     * @param fileName 文件名称
     */
    public static void cleanFile(String filePath, String fileName) {
        File outfile = new File(filePath, fileName);
        // 当文件夹不存在，则返回
        if (!outfile.exists()) {
            return;
        }

        //存在，则执行删除操作
        boolean deleteRps = outfile.delete();
        log.info("delete rsp {}", deleteRps);
    }
}
