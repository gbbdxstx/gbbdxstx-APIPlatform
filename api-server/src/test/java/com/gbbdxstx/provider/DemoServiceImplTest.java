package com.gbbdxstx.provider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DemoServiceImplTest {

    @Resource
    private DemoService demoService;


    @Test
    void getInvokeUser() {
        boolean result = demoService.invokeCount(1817194095141089281L, 1L);
        Assertions.assertTrue(result);
    }
}