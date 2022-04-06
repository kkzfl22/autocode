package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.other.JavaCodeProjectCfgCopyCreate;
import org.junit.Test;

/**
 * 相关配制文件的配制
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeProjectCfgCopyCreateTest {


    @Test
    public void testGenerate() {
        JavaCodeProjectCfgCopyCreate configInstance = new JavaCodeProjectCfgCopyCreate();


        GenerateCodeContext context = CodeBaseUtils.getBase();

        // 资源文件的处理
        configInstance.generateCode(context);

    }

}
