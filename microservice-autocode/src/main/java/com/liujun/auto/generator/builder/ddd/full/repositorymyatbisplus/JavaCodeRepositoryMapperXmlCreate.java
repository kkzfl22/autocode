package com.liujun.auto.generator.builder.ddd.full.repositorymyatbisplus;

import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.code.GenerateJavaMybatisMapperXml;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 以javabean的方法生成mybatisPlus的mapper代码
 *
 * <p>修改，去掉默认值的限制
 *
 * @author liujun
 * @version 0.0.1
 * @since 2018年4月15日 下午3:29:03
 */
public class JavaCodeRepositoryMapperXmlCreate implements GenerateCodeInf {

  public static final String DOC = "数据库操作";

  public static final JavaCodeRepositoryMapperXmlCreate INSTANCE =
      new JavaCodeRepositoryMapperXmlCreate();

  /** mapper文件后缀名称 */
  private static final String MYBATIS_SUFFIX_NAME = "Mapper.xml";

  @Override
  public void generateCode(GenerateCodeContext param) {
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> iterTable = map.entrySet().iterator();

    while (iterTable.hasNext()) {
      Entry<String, List<TableColumnDTO>> entry = iterTable.next();
      String tableName = entry.getKey();

      // 进行mapper内容的生成
      StringBuilder sb =
          GenerateJavaMybatisMapperXml.INSTANCE.generateMapperXml(tableName, param, DOC);

      // 定义项目内的完整目录结构
      String outMapperPath = param.getProjectPath().getRepositoryMybatisMapperNode().outPath();

      // 文件名
      String fileName = getMapperName(tableName);
      // 将mybatis文件的mapper输出到文件中
      GenerateOutFileUtils.outFile(
          sb, GeneratePathUtils.outServicePath(param), outMapperPath, fileName);
    }
  }

  /**
   * 获取mapper对外输出的文件名
   *
   * @param tableName 表名
   * @return
   */
  public String getMapperName(String tableName) {

    // 文件名,以java的表名转换的属性名前缀，再加后缀
    return NameProcess.INSTANCE.toFieldName(tableName) + MYBATIS_SUFFIX_NAME;
  }
}
