package com.liujun.auto.generator.builder.ddd.full.facade;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.MethodTypeEnum;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.code.GenerateJavaAssembler;
import com.liujun.auto.generator.builder.ddd.constant.CodeComment;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.ImportCodeActionDefaultPackageKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaMethodName;
import com.liujun.auto.generator.builder.ddd.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.ddd.entity.DataParam;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.utils.ReturnUtils;
import com.liujun.auto.generator.builder.utils.RunMethodUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

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
public class JavaCodeFacadeAssemblerCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "TransferAssembler";
  private static final String NAME_COMMENT = "传输实体与领域实体的转换";

  public static final JavaCodeFacadeAssemblerCreate INSTANCE = new JavaCodeFacadeAssemblerCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();
      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableNameItem.getKey());

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
          PkgBuildMethod.classInfoComment(javaPackageStr, className, NAME_COMMENT);

      param.putPkg(tableName, GenerateCodePackageKey.INTERFACE_ASSEMBLER, persistAssemblerPkg);

      // 构建参数信息
      DataParam generateParam =
          DataParam.builder()
              .putPkg(
                  GenerateCodePackageKey.INTERFACE_OBJECT,
                  param.getPkg(tableName, GenerateCodePackageKey.INTERFACE_OBJECT))
              .putPkg(
                  GenerateCodePackageKey.DOMAIN_DO,
                  param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_DO))
              .putPkg(
                  GenerateCodePackageKey.INTERFACE_ASSEMBLER,
                  param.getPkg(tableName, GenerateCodePackageKey.INTERFACE_ASSEMBLER))
              .putPkg(
                  GenerateCodePackageKey.DOMAIN_PAGE_ENTITY,
                  ImportCodeActionDefaultPackageKey.DOMAIN_PAGE.getPackageInfo())
              .author(param.getGenerateConfig().getGenerate().getAuthor())
              .methodList(param.getGenerateConfig().getGenerate().getMethodList())
              .tableInfo(tableInfo)
              .databaseType(param.getTypeEnum())
              .columnList(tableNameItem.getValue())
              .build();

      // 进行转换方法的生成
      StringBuilder sb = this.generateJavaAssembler(generateParam);

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
   * @param generateParam 参数信息
   * @return 生成相关的
   */
  private StringBuilder generateJavaAssembler(DataParam generateParam) {

    List<TableColumnDTO> columnList = generateParam.getColumnList();
    ImportPackageInfo dtoPackage = generateParam.getPkg(GenerateCodePackageKey.INTERFACE_OBJECT);
    ImportPackageInfo domainPackage = generateParam.getPkg(GenerateCodePackageKey.DOMAIN_DO);
    ImportPackageInfo assemblerPackage =
        generateParam.getPkg(GenerateCodePackageKey.INTERFACE_ASSEMBLER);
    List<MethodInfo> methodList = generateParam.getMethodList();
    String author = generateParam.getAuthor();

    // 1,方法头的定义
    StringBuilder sb =
        GenerateJavaAssembler.INSTANCE.defineClass(
            dtoPackage, domainPackage, assemblerPackage, author);

    // 私有构造方法
    GenerateJavaAssembler.INSTANCE.assemblerConstruct(assemblerPackage, sb);

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

    // 检查是否存在分页查询方法
    if (RunMethodUtils.checkMethodExists(
        generateParam.getMethodList(), MethodTypeEnum.QUERY_PAGE)) {
      // 构建分页方的对象信息
      sb.append(GenerateJavaAssembler.INSTANCE.generatePageAssembler(generateParam));
    }

    // 结束
    JavaClassCodeUtils.classEnd(sb);

    return sb;
  }
}
