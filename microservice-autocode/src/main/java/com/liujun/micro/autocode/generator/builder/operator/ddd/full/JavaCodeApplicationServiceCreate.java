package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.constant.MethodTypeEnum;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaClassFieldEntity;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.ddd.code.GenerateJavaDomainServiceInvoke;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 聚合领域层服务的实现，主要是调用领域服务
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeApplicationServiceCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "ApplicationService";
  private static final String CLASS_COMMENT = "的应用服务";

  @Override
  public void generateCode(GenerateCodeContext param) {
    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Map.Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();
      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableNameItem.getKey());

      // 表名
      String tableName = tableNameItem.getKey();

      // 得到类名
      String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
      String className = tableClassName + NAME_SUFFIX;

      // 注释
      String docComment =
          JavaCommentUtil.tableCommentProc(tableInfo.getTableComment()) + CLASS_COMMENT;

      // 获取以java定义的package路径
      String javaPackageStr =
          param.getJavaCodePackage().getApplicationServiceNode().outJavaPackage();

      // 将领域的存储实现存至流程中
      ImportPackageInfo applicationServicePackage =
          new ImportPackageInfo(javaPackageStr, className, docComment, JavaVarName.SPRING_INSTANCE_NAME);
      ImportPackageUtils.putPackageInfo(
          tableName,
          param.getPackageMap(),
          GenerateCodePackageKey.APPLICATION_SERVICE.getKey(),
          applicationServicePackage,
          tableMap.size());
      Map<String, ImportPackageInfo> packageMap = param.getPackageMap().get(tableName);

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 调用存储接口
      StringBuilder sb =
          this.generateApplicationService(
              packageMap,
              param.getGenerateConfig().getGenerate().getCode(),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(sb, param.getFileBasePath(), javaPackageStr, className);
    }
  }

  /**
   * 生成应用的服务
   *
   * @param packageMap 导入的包信息
   * @param methodList 方法
   * @param author 作者
   * @return 构建的存储层对象
   */
  private StringBuilder generateApplicationService(
      Map<String, ImportPackageInfo> packageMap, List<MethodInfo> methodList, String author) {

    // 领域实体实体
    ImportPackageInfo domainPackageInfo = packageMap.get(GenerateCodePackageKey.DOMAIN_DO.getKey());

    // 领域的服务接口
    ImportPackageInfo domainServicePackage =
        packageMap.get(GenerateCodePackageKey.DOMAIN_SERVICE.getKey());

    // 应用的服务接口
    ImportPackageInfo applicationService =
        packageMap.get(GenerateCodePackageKey.APPLICATION_SERVICE.getKey());

    // 类的声明
    StringBuilder sb =
        GenerateJavaDomainServiceInvoke.INSTANCE.domainServiceDefine(
            domainServicePackage, domainPackageInfo, applicationService, methodList, author);

    // 1,类属性的创建
    sb.append(
        JavaClassCodeUtils.getClassField(
            JavaClassFieldEntity.getPrivateAutowiredField(
                domainServicePackage.getClassName(),
                JavaVarName.DOMAIN_INSTANCE_NAME,
                domainServicePackage.getClassComment())));

    for (MethodInfo methodItem : methodList) {
      // 1,针对增删除改的方法进行调用
      if (MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.DELETE.getType().equals(methodItem.getOperator())) {
        GenerateJavaDomainServiceInvoke.INSTANCE.updateMethod(
            sb, methodItem, domainPackageInfo, JavaVarName.DOMAIN_INSTANCE_NAME);
      }
      // 如果当前为查询分页操作
      else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())
          && methodItem.getPageQueryFlag() != null
          && methodItem.getPageQueryFlag()) {
        GenerateJavaDomainServiceInvoke.INSTANCE.pageQueryMethod(
            sb, methodItem, domainPackageInfo, JavaVarName.DOMAIN_INSTANCE_NAME);
      }
      // 如果当前为查询则进行查询调用操作
      else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
        GenerateJavaDomainServiceInvoke.INSTANCE.queryMethod(
            sb, methodItem, domainPackageInfo, JavaVarName.DOMAIN_INSTANCE_NAME);
      }
    }

    // 类的结束
    JavaClassCodeUtils.classEnd(sb);
    return sb;
  }
}
