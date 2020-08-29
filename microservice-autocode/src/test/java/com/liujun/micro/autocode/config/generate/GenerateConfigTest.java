package com.liujun.micro.autocode.config.generate;

import com.liujun.micro.autocode.entity.config.GenerateConfigEntity;
import com.liujun.micro.autocode.entity.config.MethodInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author liujun
 * @version 0.0.1
 */
public class GenerateConfigTest {

  @Test
  public void testLoad() {
    GenerateConfigEntity generateInstance = GenerateConfig.INSTANCE.getCfgEntity();
    System.out.println(generateInstance);
    List<MethodInfo> dataList = generateInstance.getGenerate().getCode();

    System.out.println();
    for (MethodInfo infoItem : dataList) {
      System.out.println(infoItem);
      System.out.println();
    }

    Assert.assertNotNull(generateInstance.getGenerate().getDatabaseType());
  }
}
