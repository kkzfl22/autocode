package com.liujun.micro.autocode.generator.database.constant;

import com.liujun.micro.autocode.constant.Symbol;

/**
 * 数据库类型信息
 *
 * @author liujun
 * @version 1.0.0
 */
public enum DatabaseTypeEnum {
    /**
     * 数据库类型为mysql
     */
    MYSQL("mysql"),

    /**
     * 数据库类型为oracle
     */
    ORACLE("oracle"),
    ;

    /**
     * 数据库类型
     */
    private final String databaseType;

    DatabaseTypeEnum(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    /**
     * get proerties the type
     *
     * @param propertiesName properties name
     * @return type info
     */
    public static DatabaseTypeEnum getPropertiesDbType(String propertiesName) {
        if (propertiesName != null) {
            String name =
                    propertiesName.substring(
                            propertiesName.indexOf(Symbol.UNDER_LINE) + 1,
                            propertiesName.lastIndexOf(Symbol.POINT));

            for (DatabaseTypeEnum types : values()) {
                if (types.databaseType.equalsIgnoreCase(name)) {
                    return types;
                }
            }
        }
        return null;
    }

    /**
     * get proerties the type
     *
     * @param databaseConfigType database type info
     * @return type info
     */
    public static DatabaseTypeEnum getDbType(String databaseConfigType) {

        for (DatabaseTypeEnum types : values()) {
            if (types.databaseType.equals(databaseConfigType)) {
                return types;
            }
        }


        throw new IllegalArgumentException("generate.yml config error ,databaseType : " + databaseConfigType);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DatabaseTypeEnum{");
        sb.append("databaseType='").append(databaseType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
