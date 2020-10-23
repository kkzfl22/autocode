package com.liujun.micro.autocode.generator.builder.operator.ddd.increment.field;

import com.liujun.micro.autocode.algorithm.booyermoore.CharMatcherBmBadChars;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.utils.FileReaderUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 当数据库增加字段后，通过对比进行程序内字段的添加操作
 *
 * <p>why:当数据库添加字段后，手动需要在实体中添加字段，较为麻烦，而且也容易出错。这类功能可通过程序来实现，节省开发资源
 *
 * <p>what:将数据库中字段影射成实体字段，存储至map容器对，然后按当前实体中的字段进行删除，最后留下的即为需要添加的字段。
 *
 * <p>how:读取数据库中字段，与现有实体中的字段进行对比。找出当前实体中缺少的字段，进行添加操作
 *
 * @author liujun
 * @version 0.0.1
 * @since 2020/04/08
 */
public class JavaCodeRepositoryObjectFieldCompareAdd implements GenerateCodeInf {

  /** 实例对象 */
  public static final JavaCodeRepositoryObjectFieldCompareAdd INSTANCE =
      new JavaCodeRepositoryObjectFieldCompareAdd();

  /** { 位置匹配 */
  private static final CharMatcherBmBadChars BRACE_LEFT_MATCHER =
      CharMatcherBmBadChars.getBadInstance(Symbol.BRACE_LEFT);

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();

    while (tableNameEntry.hasNext()) {
      Map.Entry<String, List<TableColumnDTO>> tableEntry = tableNameEntry.next();
      // 表名
      String tableName = tableEntry.getKey();

      // 1,读取当前文件
      String repositoryObejct = readerRepositoryObject(param, tableName);

      // 1,进行当前文件的解析操作
      RepositoryEntity repositoryEntity = new RepositoryEntity("", "");

      //// 进行存储层的bean代码生成
      // StringBuilder persistBean =
      //    GenerateJavaBean.INSTANCE.generateJavaBean(
      //        packageInfo,
      //        tableEntry.getValue(),
      //        param.getGenerateConfig().getGenerate().getCode(),
      //        param.getGenerateConfig().getGenerate().getAuthor(),
      //        param.getTypeEnum());
      //
      //// 定义项目内的完整目录结构
      // String outPackagePath =
      //    param.getProjectPath().getSrcJavaNode().outPath()
      //        + Symbol.PATH
      //        + packageInfo.getPackagePath();
      //
      //// 进行存储层的实体输出
      // GenerateOutFileUtils.outJavaFile(
      //    persistBean,
      //    GeneratePathUtils.outServicePath(param),
      //    outPackagePath,
      //    packageInfo.getClassName());
    }
  }

  /**
   * 读取存储实体的内容信息
   *
   * @param param 上下文参数信息
   * @param tableName 表名
   * @return 读取的文件内容
   */
  private String readerRepositoryObject(GenerateCodeContext param, String tableName) {
    String repositoryPath = param.getProjectPath().getSrcJavaNode().outPath();
    String readerPath = GeneratePathUtils.outServicePath(param) + Symbol.PATH + repositoryPath;
    String mapperFileName =
        readerPath + Symbol.PATH + JavaCodeRepositoryObjectCreate.INSTANCE.getClassName(tableName);

    return FileReaderUtils.readFile(mapperFileName);
  }

  /**
   * 读取当前的内容，转化为实体对象信息
   *
   * @param data
   * @return
   */
  private Map<String, RepositoryEntity> toRepositoryMap(String data) {

    // 1,找到类开始标签

    return Collections.emptyMap();
  }

  /** 内部类，存储层实体信息 */
  @Getter
  @Setter
  @ToString
  private class RepositoryEntity {

    /** 实体类型 */
    private String fieldType;

    /** 实体名称 */
    private String fieldName;

    public RepositoryEntity(String fieldType, String fieldName) {
      this.fieldType = fieldType;
      this.fieldName = fieldName;
    }
  }
}
