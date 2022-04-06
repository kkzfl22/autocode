package com.liujun.micro.autocode.config.generate;

import com.liujun.micro.autocode.config.generate.entity.GenerateConfigEntity;
import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author liujun
 * @version 0.0.1
 */
public class GenerateConfigProcessTest {

  @Test
  public void testLoad() {
    GenerateConfigEntity generateInstance = GenerateConfigProcess.INSTANCE.getCfgEntity();
    System.out.println(generateInstance);
    List<MethodInfo> dataList = generateInstance.getGenerate().getMethodList();

    System.out.println();
    for (MethodInfo infoItem : dataList) {
      System.out.println(infoItem);
      System.out.println();
    }

    Assert.assertNotNull(generateInstance.getGenerate().getDatabaseType());
  }
}
