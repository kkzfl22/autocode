package com.liujun.auto.generator.builder.ddd.custom.view.facade;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.MethodTypeEnum;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.ddd.entity.JavaClassEntity;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.utils.JavaCommentUtil;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 参数的校验操作
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeFacadeParamValidCreate implements GenerateCodeInf {

  /** 注释 */
  private static final String CLASS_COMMENT = "的spring校验类";

  /** 跳过的类信息 */
  private static final List<String> JUMP = Arrays.asList(MethodTypeEnum.DETAIL.getType());

  public static final JavaCodeFacadeParamValidCreate INSTANCE =
      new JavaCodeFacadeParamValidCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Map.Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();
      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableNameItem.getKey());

      // 执行参数校验的类定义输出
      paramCheck(param, tableInfo);
    }
  }

  /**
   * 执行参数检查
   *
   * @param param 流程上下文信息
   * @param tableInfo 表信息
   * @return 方法信息
   */
  private Map<String, String> paramCheck(GenerateCodeContext param, TableInfoDTO tableInfo) {
    Map<String, String> dataMap = new HashMap<>();

    // 按方法遍历
    for (MethodInfo method : param.getGenerateConfig().getGenerate().getMethodList()) {
      // 执行输出操作
      if (!JUMP.contains(method.getOperator())) {
        // 类的定义
        this.defineValidPkg(param, method, tableInfo);

        // 代码生成操作
        String codeContext = this.generateCode(param, method, tableInfo);

        // 将代码写入文件中
        this.codeOutFile(codeContext, param, method);
      }
    }

    return dataMap;
  }

  /**
   * 定义校验的包信息
   *
   * @param param 参数信息
   * @param method 方法信息
   * @param tableInfo 表信息
   */
  private void defineValidPkg(
      GenerateCodeContext param, MethodInfo method, TableInfoDTO tableInfo) {
    // 注释
    String docComment =
        JavaCommentUtil.tableCommentProc(tableInfo.getTableComment())
            + method.getComment()
            + CLASS_COMMENT;
    // 获取以java的接口对象
    String javaPackageStr = param.getJavaCodePackage().getInterfaceValidNode().outJavaPackage();
    // 表名
    String className = NameProcess.INSTANCE.toJavaClassName(method.getName());

    // 将领域的存储实现存至流程中
    ImportPackageInfo applicationServicePackage =
        ImportPackageInfo.builder()
            .pkgPath(javaPackageStr)
            .className(className)
            .classComment(docComment)
            .build();
    param.putPkg(tableInfo.getTableName(), method.getName(), applicationServicePackage);
  }

  /**
   * 代码生成操作
   *
   * @param param 参数信息
   * @param method 方法信息
   * @param tableInfo 表信息
   * @return 名称信息
   */
  private String generateCode(
      GenerateCodeContext param, MethodInfo method, TableInfoDTO tableInfo) {
    ImportPackageInfo pkgInfo = param.getPkg(tableInfo.getTableName(), method.getName());

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()

            // 类的关键字
            .classKey(JavaKeyWord.CLASS_KEY)
            // 类名
            .className(pkgInfo.getClassName())
            // 类注释
            .classComment(pkgInfo.getClassComment())
            // 包类路径信息
            .packagePath(pkgInfo.getPackagePath())
            // 作者
            .author(param.getGenerateConfig().getGenerate().getAuthor())
            .build();

    StringBuilder dataOutCode = JavaClassCodeUtils.javaClassDefine(classEntityInfo);

    // 类结束
    JavaClassCodeUtils.classEnd(dataOutCode);

    return dataOutCode.toString();
  }

  private void codeOutFile(String code, GenerateCodeContext param, MethodInfo method) {
    // 获取以java的接口对象
    String javaPackageStr = param.getJavaCodePackage().getInterfaceValidNode().outJavaPackage();
    // 定义项目内的完整目录结构
    String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
    javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

    // 表名
    String className = NameProcess.INSTANCE.toJavaClassName(method.getName());

    // 进行存储层的接口输出
    GenerateOutFileUtils.outJavaFile(
        code, GeneratePathUtils.outServicePath(param), javaPackageStr, className);
  }
}
