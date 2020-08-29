package com.liujun.micro.autocode.generator.builder.operator.ddd;

import com.liujun.micro.autocode.entity.config.GenerateConfigEntity;
import com.liujun.micro.autocode.entity.config.MethodInfo;
import com.liujun.micro.autocode.generator.builder.constant.MethodFixEnum;
import com.liujun.micro.autocode.generator.builder.constant.MethodTypeEnum;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.OperatorMethodInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.*;

/**
 * 进行数据的初始化处理，
 *
 * <p>1，读取配制方法，转换为生成实体的对象
 *
 * @author liujun
 * @version 0.0.1
 * @since 2020/04/08
 */
public class JavaCodeInit implements GenerateCodeInf {

  public static final String TABLE_LAST = "表";

  public static final String TABLE_ADD = "的";

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
      // 表信息
      TableInfoDTO tableInfo = tableMap.get(tableName);

      // 获得当前需要生成方法，并构建成方法对象
      GenerateConfigEntity config = param.getGenerateConfig();
      //  Map<String, MethodInfo> cfgMethodMap = config.getGenerate().getCode();
    }
  }

  /**
   * 根据用户配制方法生成方法信息
   *
   * @param cfgMethodMap
   * @return
   */
  private Map<String, List<OperatorMethodInfo>> generateMethod(
      Map<String, MethodInfo> cfgMethodMap, TableInfoDTO tableInfo) {
    Map<String, MethodInfo> copyMap = this.copyMap(cfgMethodMap);

    // 1,优先按
    List<OperatorMethodInfo> columnMap = new ArrayList<>();

    for (MethodFixEnum method : MethodFixEnum.values()) {
      MethodInfo methodInfo = copyMap.get(method.getKey());

      // 如果当前被用户配制了，则启用
      if (null != methodInfo) {
        OperatorMethodInfo operatorMethod = new OperatorMethodInfo();
        operatorMethod.setMethodName(methodInfo.getName());
        // 方法类型
        operatorMethod.setOperatorType(MethodTypeEnum.getType(methodInfo.getOperator()));
        // 方法的描述
        operatorMethod.setMethodComment(getClassComment(tableInfo, method));
      }
    }
    return null;
  }

  /**
   * 获取类的描述信息
   *
   * @param tableInfo
   * @param method
   * @return
   */
  private String getClassComment(TableInfoDTO tableInfo, MethodFixEnum method) {
    String msg = tableInfo.getTableComment().trim();

    String tableLast = msg.substring(msg.length() - 1);
    if (TABLE_LAST.equals(tableLast)) {
      msg = msg.substring(0, msg.length() - 1);
    }

    return msg + TABLE_ADD + method.getComment();
  }

  private Map<String, MethodInfo> copyMap(Map<String, MethodInfo> cfgMethodMap) {
    Map<String, MethodInfo> result = new HashMap<>(cfgMethodMap.size());

    for (Map.Entry<String, MethodInfo> entryItem : cfgMethodMap.entrySet()) {
      result.put(entryItem.getKey(), entryItem.getValue());
    }

    return result;
  }
}
