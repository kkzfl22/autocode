package com.liujun.auto.generator.builder.ddd.code;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.MethodOperatorEnum;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.ImportCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.entity.DataParam;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.ddd.entity.JavaClassEntity;
import com.liujun.auto.generator.builder.ddd.entity.JavaClassFieldEntity;
import com.liujun.auto.generator.builder.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.utils.MethodUtils;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 领域服务的调用
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaApplicationServiceInvoke {

  /** 实例对象 */
  public static final GenerateJavaApplicationServiceInvoke INSTANCE =
      new GenerateJavaApplicationServiceInvoke();

  /** 导入的包信息 */
  private static final List<String> IMPORT_PKG =
      Arrays.asList(
          "lombok.extern.slf4j.Slf4j",
          "org.springframework.beans.factory.annotation.Autowired",
          "org.springframework.stereotype.Service",
          JavaKeyWord.IMPORT_LIST);

  /** 分页相关 导入包 */
  private static final List<String> PAGE_IMPORT_PKG =
      Arrays.asList(ImportCodePackageKey.PAGE_PARAM.getPackageInfo().packageOut());

  /**
   * 生成应用的服务
   *
   * @return 构建的存储层对象
   */
  public StringBuilder generateApplicationService(DataParam generateParam) {

    // 领域服务对象
    ImportPackageInfo domainServicePackage =
        generateParam.getPkg(GenerateCodePackageKey.DOMAIN_SERVICE);

    // 领域对象
    ImportPackageInfo domainPackageInfo = generateParam.getPkg(GenerateCodePackageKey.DOMAIN_DO);

    // 类的声明
    StringBuilder sb =
        GenerateJavaApplicationServiceInvoke.INSTANCE.domainServiceDefine(generateParam);

    // 1,类属性的创建
    sb.append(
        JavaClassCodeUtils.getClassField(
            JavaClassFieldEntity.getPrivateAutowiredField(
                domainServicePackage.getClassName(),
                JavaVarName.DOMAIN_INSTANCE_NAME,
                domainServicePackage.getClassComment())));

    for (MethodInfo methodItem : generateParam.getMethodList()) {
      // 1,针对增删除改的方法进行调用
      if (MethodOperatorEnum.INSERT.getType().equals(methodItem.getOperator())
          || MethodOperatorEnum.UPDATE.getType().equals(methodItem.getOperator())
          || MethodOperatorEnum.DELETE.getType().equals(methodItem.getOperator())) {
        GenerateJavaApplicationServiceInvoke.INSTANCE.updateMethod(
            sb, methodItem, domainPackageInfo, JavaVarName.DOMAIN_INSTANCE_NAME);
      }
      // 如果当前为查询分页操作
      else if (MethodOperatorEnum.QUERY_PAGE.getType().equals(methodItem.getOperator())) {
        GenerateJavaApplicationServiceInvoke.INSTANCE.pageQueryMethod(
            sb, methodItem, domainPackageInfo, JavaVarName.DOMAIN_INSTANCE_NAME);
      }
      // 如果当前为查询则进行查询调用操作
      else if (MethodOperatorEnum.QUERY.getType().equals(methodItem.getOperator())) {
        GenerateJavaApplicationServiceInvoke.INSTANCE.queryMethod(
            sb, methodItem, domainPackageInfo, JavaVarName.DOMAIN_INSTANCE_NAME);
      }
    }

    // 类的结束
    JavaClassCodeUtils.classEnd(sb);
    return sb;
  }

  /**
   * 数据修改相关的调用
   *
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域包信息
   * @param instanceName 实例对象的调用名称
   */
  public void updateMethod(
      StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg, String instanceName) {
    GenerateJavaDomainServiceInvoke.INSTANCE.updateMethod(sb, method, domainPkg, instanceName);
  }

  /**
   * 数据查询相关的调用
   *
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域包信息
   */
  public void queryMethod(
      StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg, String instanceName) {
    GenerateJavaDomainServiceInvoke.INSTANCE.queryMethod(sb, method, domainPkg, instanceName);
  }

  /**
   * 分页相关的方法的调用
   *
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域包信息
   */
  public void pageQueryMethod(
      StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg, String instanceName) {
    GenerateJavaDomainServiceInvoke.INSTANCE.pageQueryMethod(sb, method, domainPkg, instanceName);
  }

  /**
   * 方法的定义
   *
   * @param codeParam 参数信息
   * @return 构建的类头
   */
  public StringBuilder domainServiceDefine(DataParam codeParam) {
    List<String> importList = new ArrayList<>(16);

    // 集合
    importList.addAll(IMPORT_PKG);
    // 检查当前是否存在分页方法,当存在分页时，需要导入相关的包
    if (MethodUtils.checkPageQuery(codeParam.getMethodList())) {
      // 分页数据包导入
      importList.addAll(PAGE_IMPORT_PKG);
    }

    // 导入领域实体
    importList.add(codeParam.getPkg(GenerateCodePackageKey.DOMAIN_DO).packageOut());
    // 领域服务导入
    importList.add(codeParam.getPkg(GenerateCodePackageKey.DOMAIN_SERVICE).packageOut());

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 类的关键字
            .classKey(JavaKeyWord.CLASS_KEY)
            // 类名
            .className(codeParam.getPkg(GenerateCodePackageKey.APPLICATION_SERVICE).getClassName())
            // 类注释
            .classComment(
                codeParam.getPkg(GenerateCodePackageKey.APPLICATION_SERVICE).getClassComment())
            // 包类路径信息
            .packagePath(
                codeParam.getPkg(GenerateCodePackageKey.APPLICATION_SERVICE).getPackagePath())
            // 导入包信息
            .importList(importList)
            // 注解符
            .annotationList(
                Arrays.asList(
                    ImportCodePackageKey.SPRING_SERVICE_ANNOTATION.getPackageInfo().getAnnotation(),
                    ImportCodePackageKey.SLF4J_ANNOTATION.getPackageInfo().getAnnotation()))
            // 作者
            .author(codeParam.getAuthor())
            .build();

    return JavaClassCodeUtils.javaClassDefine(classEntityInfo);
  }
}
