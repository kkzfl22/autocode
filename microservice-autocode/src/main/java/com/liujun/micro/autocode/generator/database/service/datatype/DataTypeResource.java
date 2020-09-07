package com.liujun.micro.autocode.generator.database.service.datatype;

import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeSourceEnum;
import com.liujun.micro.autocode.generator.database.entity.DatabaseTypeMsgBO;
import com.liujun.micro.autocode.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author liujun
 * @version 0.0.1
 * @date 2018/09/13
 */
@Slf4j
public class DataTypeResource {

  /** instance Object */
  public static final DataTypeResource INSTANCE = new DataTypeResource();

  /** resource path */
  private static final String RESOURCE_PATH_NAME = "config/dbtype/";

  /** 数据库的类型文件信息 */
  private static final String[] DATABASE_FILE_NAME =
      new String[] {"sqltype_mysql.properties", "sqltype_oracle.properties"};

  /** db TYPE INFO by properties key */
  private static final Map<DatabaseTypeEnum, Map<DatabaseTypeSourceEnum, DatabaseTypeMsgBO>>
      TARGET_DBTYPE_CACHE_MAP = new HashMap<>(10);

  /** src type info */
  private static final Map<DatabaseTypeEnum, Map<Integer, DatabaseTypeMsgBO>>
      SRCDB_TO_TARGERKEY_MAP = new HashMap<>(10);

  static {
    // load database Column type
    INSTANCE.loadResourceDbColumnType();
  }

  /** load database Column type */
  private void loadResourceDbColumnType() {

    for (String dbTypeFile : DATABASE_FILE_NAME) {
      // 进行文件加载
      loadFile(RESOURCE_PATH_NAME + dbTypeFile);
    }
  }

  private void loadFile(String fileName) {

    // load resource
    Properties properties = new Properties();
    InputStream input = null;

    try {

      DatabaseTypeEnum typeNum = DatabaseTypeEnum.getPropertiesDbType(fileName);

      Map<DatabaseTypeSourceEnum, DatabaseTypeMsgBO> dbTypeKeyMap =
          TARGET_DBTYPE_CACHE_MAP.get(typeNum);

      if (null == dbTypeKeyMap) {
        dbTypeKeyMap = new HashMap<>(45);
      }

      Map<Integer, DatabaseTypeMsgBO> srcTargerMap = SRCDB_TO_TARGERKEY_MAP.get(typeNum);

      if (null == srcTargerMap) {
        srcTargerMap = new HashMap<>(45);
      }

      // 优先执行外部文件加载
      this.getInputStream(fileName);

      // 当外部文件未加载成功，再加载内部文件
      if (input == null) {
        input = this.getClass().getClassLoader().getResourceAsStream(fileName);
      }

      properties.load(input);
      Iterator<Map.Entry<Object, Object>> iter = properties.entrySet().iterator();

      Map.Entry<Object, Object> iten = null;
      DatabaseTypeSourceEnum dbTypeItem = null;
      DatabaseTypeMsgBO typeBo = null;

      while (iter.hasNext()) {
        iten = iter.next();
        String dbTypeKeys = String.valueOf(iten.getKey());
        String dbTypeValue = String.valueOf(iten.getValue());

        dbTypeItem = DatabaseTypeSourceEnum.getDataBaseType(dbTypeKeys);

        if (null != dbTypeItem) {
          typeBo = DatabaseTypeMsgBO.parse(dbTypeKeys, dbTypeValue);
          dbTypeKeyMap.put(dbTypeItem, typeBo);

          srcTargerMap.put(typeBo.getJdbcType(), typeBo);
        } else {
          throw new RuntimeException("curr dbtype config exception ,key:" + dbTypeKeys);
        }
      }

      TARGET_DBTYPE_CACHE_MAP.put(typeNum, dbTypeKeyMap);
      SRCDB_TO_TARGERKEY_MAP.put(typeNum, srcTargerMap);

      properties.clear();
    } catch (IOException e) {
      e.printStackTrace();
      log.error("IOException", e);
    } finally {
      StreamUtils.close(input);
    }
  }

  /**
   * 获取外部文件
   *
   * @return 文件流
   */
  private InputStream getInputStream(String outFileName) {
    try {
      // 优先使用外部文件加载
      return new FileInputStream(outFileName);
    }
    // 此处只捕获异常不处理，外部文件可能不存在，并非错误，使用内部文件即可
    catch (FileNotFoundException e) {
    }

    return null;
  }

  /**
   * get default value
   *
   * @param srcDbType type
   * @param collumnType java.sql.Types
   * @return defaultValue
   */
  public Object getDefaultValue(
      DatabaseTypeEnum srcDbType, int collumnType, DatabaseTypeEnum targetDbType) {

    DatabaseTypeMsgBO srcTypeMsg = this.getSrcTypeinfo(srcDbType, collumnType);

    DatabaseTypeMsgBO targetMsg =
        this.getTargetTypeinfo(targetDbType, srcTypeMsg.getDataTypeEnum());

    return targetMsg.getDefValue();
  }

  /**
   * get default value
   *
   * @param srcColumnType java.sql.Types
   * @return defaultValue
   */
  public DatabaseTypeMsgBO getSrcTypeinfo(DatabaseTypeEnum srcDbType, int srcColumnType) {

    Map<Integer, DatabaseTypeMsgBO> dbcolumn = SRCDB_TO_TARGERKEY_MAP.get(srcDbType);

    // src type info
    return dbcolumn.get(srcColumnType);
  }

  /**
   * get default value
   *
   * @param targetDbType java.sql.Types
   * @return defaultValue
   */
  public DatabaseTypeMsgBO getTargetTypeinfo(
      DatabaseTypeEnum targetDbType, DatabaseTypeSourceEnum targetColumnEnum) {

    Map<DatabaseTypeSourceEnum, DatabaseTypeMsgBO> dbcolumn =
        TARGET_DBTYPE_CACHE_MAP.get(targetDbType);

    // src type info
    return dbcolumn.get(targetColumnEnum);
  }

  /**
   * get target typeinfo
   *
   * @param srcDbType
   * @param collumnType
   * @param targetDbType
   * @return
   */
  public DatabaseTypeMsgBO getTargetType(
      DatabaseTypeEnum srcDbType, int collumnType, DatabaseTypeEnum targetDbType) {

    DatabaseTypeMsgBO srcTypeMsg = this.getSrcTypeinfo(srcDbType, collumnType);

    DatabaseTypeMsgBO targetMsg =
        this.getTargetTypeinfo(targetDbType, srcTypeMsg.getDataTypeEnum());

    return targetMsg;
  }
}
