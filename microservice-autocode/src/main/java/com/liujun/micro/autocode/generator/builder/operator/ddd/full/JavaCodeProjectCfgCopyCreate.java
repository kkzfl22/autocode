package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.config.generate.GenerateConfigLoader;
import com.liujun.micro.autocode.config.generate.entity.Generate;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.utils.StreamUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 工程的配制文件拷贝
 *
 * @author liujun
 * @version 1.0.0
 */
@Slf4j
public class JavaCodeProjectCfgCopyCreate implements GenerateCodeInf {


    /**
     * 配制文件
     */
    private static final String CONFIG_YML = "application.yml";

    /**
     * 配制的日志
     */
    private static final String CONFIG_LOG_BACK = "logback.xml";

    /**
     * 配制的项目级project的文件
     */
    private static final String CFG_SRC_RESOURCE_PATH = "config/project/service/src/main/resources";


    /**
     * 配制的测试的的资源文件
     */
    private static final String CFG_TEST_RESOURCE_PATH = "config/project/service/src/test/resources";


    public static final JavaCodeProjectCfgCopyCreate INSTANCE = new JavaCodeProjectCfgCopyCreate();

    @Override
    public void generateCode(GenerateCodeContext param) {

        //对资源文件进行清理操作
        GenerateOutFileUtils.cleanFile(GeneratePathUtils.outServicePath(param),
                param.getProjectPath().getResourceNode().outPath(), CONFIG_YML);
        GenerateOutFileUtils.cleanFile(GeneratePathUtils.outServicePath(param),
                param.getProjectPath().getResourceNode().outPath(), CONFIG_LOG_BACK);
        //测试目录下的文件也执行清理
        GenerateOutFileUtils.cleanFile(GeneratePathUtils.outServicePath(param),
                param.getProjectPath().getTestResourceNode().outPath(), CONFIG_YML);

        //再执行文件的拷贝动作
        fileProcess(param, CFG_SRC_RESOURCE_PATH, param.getProjectPath().getResourceNode().outPath(), CONFIG_YML);
        fileProcess(param, CFG_SRC_RESOURCE_PATH, param.getProjectPath().getResourceNode().outPath(), CONFIG_LOG_BACK);
        fileProcess(param, CFG_TEST_RESOURCE_PATH, param.getProjectPath().getTestResourceNode().outPath(), CONFIG_YML);

    }


    /**
     * 项目级的pom文件处理
     */
    private void fileProcess(GenerateCodeContext param, String srcPath, String targetPath, String fileName) {
        //1,读取pom.xml文件模板
        String readData = readFile(srcPath + Symbol.PATH + fileName);


        //文件的输出操作
        GenerateOutFileUtils.outFile(
                readData, GeneratePathUtils.outServicePath(param), targetPath, fileName, false);
    }


    /**
     * 读取文件内容
     *
     * @param path
     * @return
     */
    private String readFile(String path) {
        StringBuilder outValue = new StringBuilder();

        InputStream input = null;
        try {
            input = this.getFileInputStream(path);
            byte[] readByte = new byte[1024];
            int readLength = -1;
            while ((readLength = input.read(readByte)) != -1) {
                outValue.append(new String(readByte, 0, readLength));
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("read file {} ", e);
        } finally {
            StreamUtils.close(input);
        }
        return outValue.toString();
    }

    /**
     * 文件获取,优先加载本地配制文件，未找则则从内部找文件
     *
     * @param absPath
     * @return
     */
    private InputStream getFileInputStream(String absPath) {
        // 优先加载外部配制文件
        InputStream input = getOutFileStream(absPath);
        if (null == input) {
            // 当外部文件不存时，则使用内部配制文件
            input = GenerateConfigLoader.class.getClassLoader().getResourceAsStream(absPath);
        }
        if (null == input) {
            throw new IllegalArgumentException(absPath + " not found");
        }

        return input;
    }

    /**
     * 获取外部文件的流
     *
     * @return 文件流
     */
    private InputStream getOutFileStream(String path) {
        InputStream outFileStream = null;

        try {
            outFileStream = new FileInputStream(path);
        }
        // 当外部文件不存在时，会报出文件不存在异常，此异常需忽略，后续加载内置文件即可
        catch (FileNotFoundException e) {
            log.info("out file not exists :" + path);
        }

        return outFileStream;
    }


}
