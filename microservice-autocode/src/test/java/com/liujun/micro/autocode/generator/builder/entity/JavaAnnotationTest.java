package com.liujun.micro.autocode.generator.builder.entity;

import com.liujun.micro.autocode.generator.builder.constant.CodeAnnotation;
import org.junit.Test;

/**
 * 进行注解用例的生成
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaAnnotationTest {

  @Test
  public void testAnnotation() {
    JavaAnnotation annotation =
        JavaAnnotation.builder()
            .annotation(CodeAnnotation.SWAGGER_API)
            .annotationValue(CodeAnnotation.SWAGGER_API_TAGS, "tag")
            .annotationValue(CodeAnnotation.SWAGGER_API_VALUE, "value")
            .build();
    System.out.println(annotation);
  }
}
