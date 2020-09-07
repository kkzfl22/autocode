package com.liujun.micro.autocode.generator.database.entity;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.database.constant.DatabaseDefValueEnum;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeSourceEnum;
import com.liujun.micro.autocode.generator.database.constant.ParseJavaTypeLength;

/**
 * database type message
 *
 * @author liujun
 * @version 0.0.1
 * @date 2018/09/13
 */
public class DatabaseTypeMsgBO {

  /** key info */
  private String typeKey;

  /** union ,flag is type flat */
  private String type;

  /** jdbc typeinfo */
  private Integer jdbcType;

  /** database type */
  private String dbType;

  /** precision min */
  private Long precisionMin;

  /** precision max */
  private Long precisionMax;

  /** scale min */
  private Integer scaleMin;

  /** scale max */
  private Integer scaleMax;

  /** defalut value */
  private String defValueStr;

  /** data type enum */
  private DatabaseTypeSourceEnum dataTypeEnum;

  private Object defValue;

  public static DatabaseTypeMsgBO parse(String key, String proValue) {

    if (proValue.indexOf(ParseJavaTypeLength.UNION_FLAG) != -1) {
      String[] provalues = proValue.split(Symbol.COMMA);
      if (provalues.length == ParseJavaTypeLength.UNION_TYPE_SIZE
          && ParseJavaTypeLength.UNION_FLAG.equals(provalues[0])) {

        DatabaseTypeMsgBO unionBean = new DatabaseTypeMsgBO();
        unionBean.setTypeKey(key);
        unionBean.setType(ParseJavaTypeLength.UNION_FLAG);
        unionBean.setJdbcType(Integer.parseInt(provalues[1]));
        unionBean.setDbType(provalues[2]);
        unionBean.setPrecisionMin(Long.parseLong(provalues[3]));
        unionBean.setPrecisionMax(Long.parseLong(provalues[4]));
        unionBean.setScaleMin(Integer.parseInt(provalues[5]));
        unionBean.setScaleMax(Integer.parseInt(provalues[6]));
        unionBean.setDefValueStr(provalues[7]);
        unionBean.setDataTypeEnum(DatabaseTypeSourceEnum.getDataBaseType(unionBean.getTypeKey()));
        unionBean.setDefValue(DatabaseDefValueEnum.getDefValue(unionBean.getDefValueStr()));

        return unionBean;
      }

    } else {
      String[] provalues = proValue.split(Symbol.COMMA);
      if (provalues.length == ParseJavaTypeLength.ONE_TYPE_SIZE) {
        DatabaseTypeMsgBO parseBean = new DatabaseTypeMsgBO();
        parseBean.setTypeKey(key);
        parseBean.setJdbcType(Integer.parseInt(provalues[0]));
        parseBean.setDbType(provalues[1]);
        parseBean.setPrecisionMin(Long.parseLong(provalues[2]));
        parseBean.setPrecisionMax(Long.parseLong(provalues[3]));
        parseBean.setDefValueStr(provalues[4]);
        parseBean.setDataTypeEnum(DatabaseTypeSourceEnum.getDataBaseType(parseBean.getTypeKey()));
        parseBean.setDefValue(DatabaseDefValueEnum.getDefValue(parseBean.getDefValueStr()));

        return parseBean;
      }
    }
    return null;
  }

  public String getTypeKey() {
    return typeKey;
  }

  public void setTypeKey(String typeKey) {
    this.typeKey = typeKey;
  }

  public int getJdbcType() {
    return jdbcType;
  }

  public void setJdbcType(int jdbcType) {
    this.jdbcType = jdbcType;
  }

  public String getDbType() {
    return dbType;
  }

  public void setDbType(String dbType) {
    this.dbType = dbType;
  }

  public long getPrecisionMin() {
    return precisionMin;
  }

  public void setPrecisionMin(long precisionMin) {
    this.precisionMin = precisionMin;
  }

  public long getPrecisionMax() {
    return precisionMax;
  }

  public void setPrecisionMax(long precisionMax) {
    this.precisionMax = precisionMax;
  }

  public DatabaseTypeSourceEnum getDataTypeEnum() {
    return dataTypeEnum;
  }

  public void setDataTypeEnum(DatabaseTypeSourceEnum dataTypeEnum) {
    this.dataTypeEnum = dataTypeEnum;
  }

  public String getDefValueStr() {
    return defValueStr;
  }

  public void setDefValueStr(String defValueStr) {
    this.defValueStr = defValueStr;
  }

  public Object getDefValue() {
    return defValue;
  }

  public void setDefValue(Object defValue) {
    this.defValue = defValue;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getScaleMin() {
    return scaleMin;
  }

  public void setScaleMin(int scaleMin) {
    this.scaleMin = scaleMin;
  }

  public int getScaleMax() {
    return scaleMax;
  }

  public void setScaleMax(int scaleMax) {
    this.scaleMax = scaleMax;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("DatabaseTypeMsgBO{");
    sb.append("typeKey='").append(typeKey).append('\'');
    sb.append(", type='").append(type).append('\'');
    sb.append(", jdbcType=").append(jdbcType);
    sb.append(", dbType='").append(dbType).append('\'');
    sb.append(", precisionMin=").append(precisionMin);
    sb.append(", precisionMax=").append(precisionMax);
    sb.append(", scaleMin=").append(scaleMin);
    sb.append(", scaleMax=").append(scaleMax);
    sb.append(", defValueStr='").append(defValueStr).append('\'');
    sb.append(", dataTypeEnum=").append(dataTypeEnum);
    sb.append(", defValue=").append(defValue);
    sb.append('}');
    return sb.toString();
  }
}
