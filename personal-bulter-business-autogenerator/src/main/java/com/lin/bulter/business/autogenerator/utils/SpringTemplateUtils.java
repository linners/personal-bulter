package com.lin.bulter.business.autogenerator.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.lin.bulter.common.dto.datasourceinfo.DatabaseInfo;
import com.lin.bulter.common.dto.datasourceinfo.TableInfo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

public class SpringTemplateUtils {

    private static Map<String, SpringTemplateUtils> instanceMap = new HashMap<>();
    private JdbcTemplate jdbcTemplate;

    public SpringTemplateUtils(String dbIp, Integer dbPort, String tableName, String userName, String password) {
        initSpringTemplateInstance(dbIp, dbPort, tableName, userName, password);
    }

    public static SpringTemplateUtils getInstance(String dbIp, Integer dbPort, String tableName, String userName, String password) {
        if(tableName==null || tableName.equals("")){
            tableName = "information_schema";
        }
        String key = dbIp + ":" + tableName;
        if(instanceMap.get(key)==null){
            instanceMap.put(key, new SpringTemplateUtils(dbIp, dbPort, tableName, userName, password));
        }
        return instanceMap.get(key);
    }

    public void initSpringTemplateInstance(String dbIp, Integer dbPort, String tableName, String userName, String password) {
        // JDBC模板依赖于连接池来获得数据的连接，所以必须先要构造连接池
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://" + dbIp + ":" + dbPort + "/" + tableName);
        dataSource.setUsername(userName);// 用户名
        dataSource.setPassword(password);// 密码
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setInitialSize(2);
        dataSource.setMaxActive(20);
        dataSource.setMinIdle(0);
        dataSource.setMaxWait(60000);
        dataSource.setValidationQuery("SELECT 1");;
        dataSource.setTestOnBorrow(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setPoolPreparedStatements(false);

        // 创建JDBC模板
        jdbcTemplate = new JdbcTemplate();
        // 这里也可以使用构造方法
        jdbcTemplate.setDataSource(dataSource);
    }

    public <T> List<T> queryList(String sql, List<Object> param, Class<T> cls) {
        List<T> result = jdbcTemplate.query(sql, param.toArray(), new BeanPropertyRowMapper(cls));
        return result;
    }
}
