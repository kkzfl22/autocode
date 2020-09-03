package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.config.menuTree.MenuNode;
import com.liujun.micro.autocode.config.menuTree.MenuTreeProjectPath;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.entity.config.MethodInfo;
import com.liujun.micro.autocode.entity.config.TypeInfo;
import com.liujun.micro.autocode.entity.config.WhereInfo;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodeDomainKey;
import com.liujun.micro.autocode.generator.builder.constant.MethodTypeEnum;
import com.liujun.micro.autocode.generator.builder.constant.MyBatisKey;
import com.liujun.micro.autocode.generator.builder.constant.MyBatisOperatorFlag;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaBean;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaMybatisMapperXml;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.micro.autocode.generator.builder.utils.MenuTreeProcessUtil;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeSourceEnum;
import com.liujun.micro.autocode.generator.database.entity.DatabaseTypeMsgBO;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.database.service.datatype.DataTypeResource;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;
import com.liujun.micro.autocode.utils.FileUtils;

import java.util.*;
import java.util.Map.Entry;

/**
 * 以javabean的方法生成mybatis的mapper代码
 *
 * <p>修改，去掉默认值的限制
 *
 * @since 2018年4月15日 下午3:29:03
 * @version 0.0.1
 * @author liujun
 */
public class JavaCodeRepositoryMyBatisMapperCreate implements GenerateCodeInf {

  public static final String DOC = "数据库操作";

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> IterTable = map.entrySet().iterator();

    while (IterTable.hasNext()) {
      Entry<String, List<TableColumnDTO>> entry = IterTable.next();
      String tableName = entry.getKey();
      TableInfoDTO tableMsg = tableMap.get(tableName);

      List<TableColumnDTO> columnList = entry.getValue();

      // 获取当前主键列表
      List<TableColumnDTO> primaryKeyList = TableColumnUtils.getPrimaryKey(columnList);
      // 获取po的完整路径
      ImportPackageInfo poPackage =
          param.getPackageMap().get(GenerateCodeDomainKey.PERSIST_PO.getKey()).get(tableName);

      // 获取dao的完整路径
      ImportPackageInfo daoPackage =
          param.getPackageMap().get(GenerateCodeDomainKey.PERSIST_DAO.getKey()).get(tableName);

      // 方法列表
      List<MethodInfo> methodList = param.getGenerateConfig().getGenerate().getCode();

      // 获取数据
      Map<String, TableColumnDTO> parseColumn = param.getColumnMapMap().get(tableName);

      // 进行mapper内容的生成
      StringBuilder sb =
          GenerateJavaMybatisMapperXml.INSTANCE.generateMapperXml(
              tableMsg,
              columnList,
              primaryKeyList,
              poPackage,
              daoPackage,
              methodList,
              parseColumn,
              DOC);

      String javaName = NameProcess.INSTANCE.toFieldName(tableName);

      // 定义mapper文件
      MenuNode mapperNode =
          MenuTreeProjectPath.getRepositoryMybatisMapperNode(param.getProjectMenuTree());
      String outMapperPath = MenuTreeProcessUtil.outPath(mapperNode);

      // 进行文件输出
      outFile(sb, param.getFileBasePath(), javaName, outMapperPath);
    }
  }
  /**
   * 进行文件输出操作
   *
   * @param sb 输出字符
   * @param path 路径
   * @param className 类名
   * @param definePackage 定义的输出目录
   */
  private void outFile(StringBuilder sb, String path, String className, String definePackage) {
    // 获取源代码

    // 获取以路径/进行输出
    String javaPathStr = MenuTreeProcessUtil.outPath(definePackage);

    StringBuilder outPath = new StringBuilder();
    // 输出的基础路径
    outPath.append(path);
    outPath.append(Symbol.PATH);
    outPath.append(javaPathStr);

    // 文件名
    String fileName = className + MyBatisKey.MYBATIS_SUFFIX_NAME;

    // 文件输出操作
    FileUtils.writeFile(outPath.toString(), fileName, sb);
  }
}
