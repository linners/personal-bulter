package com.lin.bulter.web.test.base;

import com.alibaba.fastjson.JSON;
import com.lin.bulter.web.Application;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BaseWebTest {

    @Autowired
    private WebApplicationContext context;

    //mock api 模拟http请求
    private MockMvc mockMvc;

    //初始化工作
    @Before
    public void setUp() {
        //集成Web环境测试（此种方式并不会集成真正的web环境，而是通过相应的Mock API进行模拟测试，无须启动服务器）
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    public String get(String url, String exceptResult){
        try {
            mockMvc.perform(MockMvcRequestBuilders.get(url)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(equalTo(exceptResult)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    public String postJson(String url, Map<String, Object> paramMap, String exceptResult){
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(url);
        ResultActions resultActions = null;
        try {
//            Map<String, Object> param = new HashMap<>();
//            param.put("id", 1);
//            param.put("name", "王成林");
            String parm = JSON.toJSONString(paramMap);
//            mockHttpServletRequestBuilder.param("username", "tom").param("password", "jessica");
            resultActions = mockMvc.perform(mockHttpServletRequestBuilder
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(parm));
            resultActions.andExpect(status().isOk()).andExpect(content().string(equalTo(exceptResult)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
