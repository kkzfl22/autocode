package com.liujun.auto.generator.run.constant;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * 测试获取枚举值
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateScopeEnumTest {


    @Test
    public void testGetList() {
        String scope = "api,application,domain,repository";

        List<GenerateScopeEnum> dataList = GenerateScopeEnum.generateScope(scope);

        Assert.assertEquals(dataList.size(), 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetListException() {
        String scope = "api,application,domain,repositoryss";

        GenerateScopeEnum.generateScope(scope);


    }

}
