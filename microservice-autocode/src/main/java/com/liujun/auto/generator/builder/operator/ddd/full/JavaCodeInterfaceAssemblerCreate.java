package com.liujun.auto.generator.builder.operator.ddd.full;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.constant.CodeComment;
import com.liujun.auto.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.operator.GenerateCodeInf;
import com.liujun.auto.generator.builder.operator.code.GenerateJavaAssembler;
import com.liujun.auto.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.operator.utils.ReturnUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;
import com.liujun.auto.generator.builder.constant.JavaMethodName;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成从传输与领域对象的相互转换方法
 *
 * @author liujun
 * @version 1.0.0
 */
public class JavaCodeInterfaceAssemblerCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "TransferAssembler";
  private static final String NAME_COMMENT = "传输实体与领域实体的转换";

  public static final JavaCodeInterfaceAssemblerCreate INSTANCE =
      new JavaCodeInterfaceAssemblerCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();

      // 表名
      String tableName = tableNameItem.getKey();

      // 得到类名
      String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
      String className = tableClassName + NAME_SUFFIX;

      // 获取以java定义的package路径
      String javaPackageStr =
          param.getJavaCodePackage().getInterfaceAssemblerNode().outJavaPackage();

      // 将dao信息进行储存至流程中
      ImportPackageInfo persistAssemblerPkg =
          new ImportPackageInfo(javaPackageStr, className, NAME_COMMENT);
      ImportPackageUtils.putPackageInfo(
          tableName,
          param.getPackageMap(),
          GenerateCodePackageKey.INTERFACE_ASSEMBLER.getKey(),
          persistAssemblerPkg,
          tableMap.size());

      // 获取实体信息
      ImportPackageInfo transferPackageInfo =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.INTERFACE_OBJECT.getKey(), tableName);

      // 获取实体信息
      ImportPackageInfo domainPackageInfo =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.DOMAIN_DO.getKey(), tableName);

      // 进行转换方法的生成
      StringBuilder sb =
          this.generateJavaAssembler(
              tableNameItem.getValue(),
              transferPackageInfo,
              domainPackageInfo,
              persistAssemblerPkg,
              param.getGenerateConfig().getGenerate().getCode(),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();

      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaPackageStr, className);
    }
  }

  /**
   * 生成java实体转换的实体类
   *
   * @param columnList 列信息
   * @param dtoPackage 实体1
   * @param domainPackage 实体2
   * @param assemblerPackage 转换类的信息
   * @param methodList 方法
   * @param author 作者
   * @return 生成相关的
   */
  private StringBuilder generateJavaAssembler(
      List<TableColumnDTO> columnList,
      ImportPackageInfo dtoPackage,
      ImportPackageInfo domainPackage,
      ImportPackageInfo assemblerPackage,
      List<MethodInfo> methodList,
      String author) {

    // 1,方法头的定义
    StringBuilder sb =
        GenerateJavaAssembler.INSTANCE.defineClass(
            dtoPackage, domainPackage, assemblerPackage, author);

    // 将传输对象转换为领域对象
    GenerateJavaAssembler.INSTANCE.assemblerMethod(
        JavaMethodName.ASSEMBLER_TRANSFER_DOMAIN_NAME,
        CodeComment.ASSEMBLER_TRANSFER_DOMAIN_COMMENT,
        dtoPackage,
        domainPackage,
        columnList,
        sb,
        methodList,
        true);

    // 将领域对象转换为传输对象
    GenerateJavaAssembler.INSTANCE.assemblerMethod(
        JavaMethodName.ASSEMBLER_DOMAIN_TRANSFER_NAME,
        CodeComment.ASSEMBLER_DOMAIN_TRANSFER_COMMENT,
        domainPackage,
        dtoPackage,
        columnList,
        sb,
        methodList,
        false);

    // 检查当前是否需要有集合的转换
    boolean listFlag = ReturnUtils.checkList(methodList);

    if (listFlag) {
      // 如果存在集合需要转换，则生成集合的转换方法
      // 1，将传输集合转换为领域集合
      GenerateJavaAssembler.INSTANCE.assemblerListMethod(
          sb,
          JavaMethodName.ASSEMBLER_TRANSFER_DOMAIN_LIST_NAME,
          CodeComment.ASSEMBLER_TRANSFER_DOMAIN_LIST_COMMENT,
          dtoPackage,
          domainPackage,
          JavaMethodName.ASSEMBLER_TRANSFER_DOMAIN_NAME);
      // 2，将领域集合转换为传输集合
      GenerateJavaAssembler.INSTANCE.assemblerListMethod(
          sb,
          JavaMethodName.ASSEMBLER_DOMAIN_TRANSFER_LIST_NAME,
          CodeComment.ASSEMBLER_DOMAIN_TRANSFER_LIST_COMMENT,
          domainPackage,
          dtoPackage,
          JavaMethodName.ASSEMBLER_DOMAIN_TRANSFER_NAME);
    }

    // 结束
    JavaClassCodeUtils.classEnd(sb);

    return sb;
  }
}
