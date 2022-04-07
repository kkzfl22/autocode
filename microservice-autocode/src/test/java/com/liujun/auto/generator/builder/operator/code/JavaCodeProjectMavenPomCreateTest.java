package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.full.other.JavaCodeProjectMavenPomCreate;
import org.junit.Test;

/**
 * 测试pom的输出
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeProjectMavenPomCreateTest {


    @Test
    public void testGenerate() {
        JavaCodeProjectMavenPomCreate pomInstance = new JavaCodeProjectMavenPomCreate();


        GenerateCodeContext context = CodeBaseUtils.getBase();

        // pom.xml处理
        pomInstance.generateCode(context);

    }

}
