package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisJunitDaoCreate;
import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisJunitScanConfigCreate;
import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisMapperInfCreate;
import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisMapperXmlCreate;
import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisObjectCreate;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import org.junit.Test;

/**
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryJunitDaoCreateTest {

    @Test
    public void testGenerate() {

        GenerateCodeContext context = CodeBaseUtils.getBase();

        JavaCodeRepositoryMyBatisObjectCreate poInstance = new JavaCodeRepositoryMyBatisObjectCreate();
        JavaCodeRepositoryMyBatisMapperInfCreate daoInstance = new JavaCodeRepositoryMyBatisMapperInfCreate();
        JavaCodeRepositoryMyBatisJunitScanConfigCreate myBatisScanConfigCreate = JavaCodeRepositoryMyBatisJunitScanConfigCreate.INSTANCE;
        JavaCodeRepositoryMyBatisJunitDaoCreate junitDaoInstance = new JavaCodeRepositoryMyBatisJunitDaoCreate();
        JavaCodeRepositoryMyBatisMapperXmlCreate mapperInstance =
                new JavaCodeRepositoryMyBatisMapperXmlCreate();

        // po优先生成
        poInstance.generateCode(context);
        // 再生成dao
        daoInstance.generateCode(context);
        //生成测试的文件
        myBatisScanConfigCreate.generateCode(context);
        // 生成数据库的单元测试
        junitDaoInstance.generateCode(context);
        // 生成mapper文件
        mapperInstance.generateCode(context);
    }
}
