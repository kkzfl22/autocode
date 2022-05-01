/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.database.service.table;

import java.sql.SQLException;

/**
 * @author liujun
 * @since 2022/5/1
 */
@FunctionalInterface
public interface TableParseFunction<T, R> {

  /**
   * Applies this function to the given argument.
   *
   * @param t the function argument
   * @return the function result
   */
  R apply(T t) throws SQLException;
}
