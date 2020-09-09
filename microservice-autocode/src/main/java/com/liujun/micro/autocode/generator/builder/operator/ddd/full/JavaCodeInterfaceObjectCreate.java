package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaBean;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 创建API层的实体
 *
 * @author liujun
 * @version 0.0.1
 * @since 2020/04/08
 */
public class JavaCodeInterfaceObjectCreate implements GenerateCodeInf {

  /** 注释中的描述 */
  private static final String DOC_ANNOTATION = "的API实体信息";

  /** 领域层的后缀名 */
  public static final String DOMAIN_PO = "DTO";

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();

    while (tableNameEntry.hasNext()) {
      Map.Entry<String, List<TableColumnDTO>> tableEntry = tableNameEntry.next();
      // 表名
      String tableName = tableEntry.getKey();
      // 得到类名
      String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
      String className = tableClassName + DOMAIN_PO;
      // 表信息
      TableInfoDTO tableInfo = tableMap.get(tableName);
      // 获取以java定义的api的路径
      String javaPackageStr = param.getJavaCodePackage().getInterfaceObjectNode().outJavaPackage();

      // 注释
      String docComment =
          JavaCommentUtil.tableCommentProc(tableInfo.getTableComment()) + DOC_ANNOTATION;

      // 将当前包信息存入到上下文对象信息中
      // 构建类路径及名称记录下
      ImportPackageInfo packageInfo =
          new ImportPackageInfo(
              javaPackageStr, className, docComment, JavaVarName.INSTANCE_NAME_ENTITY);
      // 将领域对象记录到公共的上下文对象中，领域层的实体对象
      ImportPackageUtils.putPackageInfo(
          tableName,
          param.getPackageMap(),
          GenerateCodePackageKey.INTERFACE_OBJECT.getKey(),
          packageInfo,
          tableMap.size());

      // 进行存储层的bean代码生成
      StringBuilder persistBean =
          GenerateJavaBean.INSTANCE.generateJavaBean(
              packageInfo,
              tableEntry.getValue(),
              param.getGenerateConfig().getGenerate().getCode(),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 进行领域层的实体输出
      GenerateOutFileUtils.outJavaFile(
          persistBean, param.getFileBasePath(), javaPackageStr, className);
    }
  }
}
