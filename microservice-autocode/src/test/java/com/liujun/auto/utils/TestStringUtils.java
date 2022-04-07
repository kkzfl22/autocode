/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * 测试数据替换空字符
 *
 * @author liujun
 * @since 2021/9/28
 */
public class TestStringUtils {

  @Test
  public void testContainTrim() {
    String outContext = "主板信息(PC：序列号，移动端：设备ID）, CPU信息（型号，核心数）, 内存信息（型号，容量)，物理硬盘及序列\r\nJSON格式";
    String outData = StringUtils.containerTrim(outContext);
    Assert.assertEquals(outContext.length(), outData.length() + 2);
  }
}
