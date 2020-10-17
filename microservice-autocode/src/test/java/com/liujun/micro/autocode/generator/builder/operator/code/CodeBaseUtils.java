package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.config.generate.GenerateConfigProcess;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.run.builder.GenerateCodeBuilder;

/**
 * 基础的代码路径
 *
 * @author liujun
 * @version 0.0.1
 */
public class CodeBaseUtils {

    /**
     * 基础的代码
     *
     * @return 构建查询参数信息
     */
    public static GenerateCodeContext getBase() {
        //// 代码基础路径
        //String basePkg = "org.example.manager.";
        //// 表空间
        //String tableSpace = "generate";
        //// 模块名称
        //String modelName = "user";
        //// 文件输出代码路径
        //String filePath = "D:\\java\\workspace\\PARA\\demo\\user";
        //
        //GenerateCodeContext param =
        //    new GenerateCodeContext(
        //        filePath,
        //        basePkg,
        //        modelName,
        //        DatabaseTypeEnum.MYSQL,
        //        tableSpace,
        //        GenerateConfigProcess.INSTANCE.getCfgEntity());


        //生成构建器
        GenerateCodeBuilder builder = new GenerateCodeBuilder(GenerateConfigProcess.INSTANCE.getCfgEntity());

        //数据库的初始化
        builder.databaseLoader();

        return builder.getContext();
    }
}
