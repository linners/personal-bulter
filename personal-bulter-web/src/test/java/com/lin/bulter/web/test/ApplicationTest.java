package com.lin.bulter.web.test;

import com.lin.bulter.web.test.base.BaseWebTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ApplicationTest extends BaseWebTest {

    private static Logger logger = LoggerFactory.getLogger(ApplicationTest.class);

    @Test
    public void test() throws Exception {
        String result = get("/bulter/user/getAllUsers", "success");
        logger.info(">>>>> result = " + result);
    }

    @Test
    public void test2() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("id", 1);
        param.put("name", "王成林");
        String result = postJson("/test2", param, "success");
        logger.info(">>>>> result = " + result);
    }
}
