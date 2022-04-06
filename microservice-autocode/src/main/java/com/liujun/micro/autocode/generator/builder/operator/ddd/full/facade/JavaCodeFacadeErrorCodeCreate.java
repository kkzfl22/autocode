package com.liujun.micro.autocode.generator.builder.operator.ddd.full.facade;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.PkgBuildMethod;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaErrorCode;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成接口层的错误码
 *
 * @author liujun
 * @version 1.0.0
 */
public class JavaCodeFacadeErrorCodeCreate implements GenerateCodeInf {

  /** 错误码后缀 */
  private static final String NAME_SUFFIX = "ErrorCodeEnum";

  private static final String NAME_COMMENT = "错误码";

  public static final JavaCodeFacadeErrorCodeCreate INSTANCE = new JavaCodeFacadeErrorCodeCreate();

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

      // 获取以错误码包的路径
      String javaPackageStr =
          param.getJavaCodePackage().getInterfaceErrorCodeNode().outJavaPackage();

      // 将dao信息进行储存至流程中
      ImportPackageInfo errorCodeAssemblerPkg =
          PkgBuildMethod.classInfoComment(javaPackageStr, className, NAME_COMMENT);

      param.putPkg(tableName, GenerateCodePackageKey.INTERFACE_ERROR_CODE, errorCodeAssemblerPkg);

      // 代码的生成操作
      StringBuilder sb =
          GenerateJavaErrorCode.INSTANCE.generateErrorCode(
              errorCodeAssemblerPkg,
              tableNameItem.getValue(),
              param.getGenerateConfig().getGenerate().getMethodList(),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();

      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaPackageStr, className);
    }
  }
}
