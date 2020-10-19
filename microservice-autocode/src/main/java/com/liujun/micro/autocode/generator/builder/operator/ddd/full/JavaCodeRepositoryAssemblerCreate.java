package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.generator.builder.constant.CodeComment;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaMethodName;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaAssembler;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ReturnUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成从领域实体至数据库实体的转换方法
 *
 * @author liujun
 * @version 1.0.0
 */
public class JavaCodeRepositoryAssemblerCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "PersistAssembler";
  private static final String NAME_COMMENT = "领域实体与存储实体的转换";

  public static final JavaCodeRepositoryAssemblerCreate INSTANCE =
      new JavaCodeRepositoryAssemblerCreate();

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
          param.getJavaCodePackage().getRepositoryAssemblerNode().outJavaPackage();

      // 将dao信息进行储存至流程中
      ImportPackageInfo persistAssemblerPkg =
          new ImportPackageInfo(javaPackageStr, className, NAME_COMMENT);
      ImportPackageUtils.putPackageInfo(
          tableName,
          param.getPackageMap(),
          GenerateCodePackageKey.PERSIST_ASSEMBLER.getKey(),
          persistAssemblerPkg,
          tableMap.size());

      // 获取实体信息
      ImportPackageInfo poPackageInfo =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.PERSIST_PO.getKey(), tableName);

      // 获取实体信息
      ImportPackageInfo domainPackageInfo =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.DOMAIN_DO.getKey(), tableName);

      // 进行转换方法的生成
      StringBuilder sb =
          this.generateJavaAssembler(
              tableNameItem.getValue(),
              poPackageInfo,
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
   * @param poPackage 实体1
   * @param domainPackage 实体2
   * @param assemblerPackage 转换类的信息
   * @param methodList 方法
   * @param author 作者
   * @return 生成相关的
   */
  public StringBuilder generateJavaAssembler(
      List<TableColumnDTO> columnList,
      ImportPackageInfo poPackage,
      ImportPackageInfo domainPackage,
      ImportPackageInfo assemblerPackage,
      List<MethodInfo> methodList,
      String author) {

    // 1,方法头的定义
    StringBuilder sb =
        GenerateJavaAssembler.INSTANCE.defineClass(
            poPackage, domainPackage, assemblerPackage, author);

    // 生成领域对象转换为存储层对象
    GenerateJavaAssembler.INSTANCE.assemblerMethod(
        JavaMethodName.ASSEMBLER_ENTITY_PERSIST_NAME,
        CodeComment.ASSEMBLER_ENTITY_PERSIST_COMMENT,
        domainPackage,
        poPackage,
        columnList,
        sb,
        methodList,
        true);

    // 将存储层的对象转换为领域层的对象
    GenerateJavaAssembler.INSTANCE.assemblerMethod(
        JavaMethodName.ASSEMBLER_DOMAIN_ENTITY_NAME,
        CodeComment.ASSEMBLER_PERSIST_ENTITY_COMMENT,
        poPackage,
        domainPackage,
        columnList,
        sb,
        methodList,
        false);

    // 检查当前是否需要有集合的转换
    boolean listFlag = ReturnUtils.checkList(methodList);

    if (listFlag) {
      // 如果存在集合需要转换，则生成集合的转换方法
      // 1，领域转换为存储集合方法
      GenerateJavaAssembler.INSTANCE.assemblerListMethod(
          sb,
          JavaMethodName.ASSEMBLER_ENTITY_PERSIST_LIST_NAME,
          CodeComment.ASSEMBLER_ENTITY_PERSIST_LIST_COMMENT,
          domainPackage,
          poPackage,
          JavaMethodName.ASSEMBLER_ENTITY_PERSIST_NAME);
      // 2，存储转换为领域集合方法
      GenerateJavaAssembler.INSTANCE.assemblerListMethod(
          sb,
          JavaMethodName.ASSEMBLER_PERSIST_ENTITY_LIST_NAME,
          CodeComment.ASSEMBLER_PERSIST_ENTITY_LIST_COMMENT,
          poPackage,
          domainPackage,
          JavaMethodName.ASSEMBLER_DOMAIN_ENTITY_NAME);
    }

    // 结束
    JavaClassCodeUtils.classEnd(sb);

    return sb;
  }
}
