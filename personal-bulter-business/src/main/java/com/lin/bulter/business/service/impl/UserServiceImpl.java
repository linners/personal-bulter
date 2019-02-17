package com.lin.bulter.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lin.bulter.business.service.UserService;
import com.lin.bulter.common.dto.UserDto;
import com.lin.bulter.common.dto.UserParam;
import com.lin.bulter.common.utils.BeanUtil;
import com.lin.bulter.common.utils.JwtUtils;
import com.lin.bulter.repository.mysql.dao.UserMapper;
import com.lin.bulter.repository.mysql.entity.User;
import com.lin.bulter.repository.redis.RedisUtil;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Value("${encryptSalt}")
    private String encryptSalt;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional
    public Integer insertUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    @Transactional
    public Integer updateUserById(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional
    public Integer deleteUserById(Integer userId) {
        return userMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public User selectUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public List<User> selectAllUsers() {
        return userMapper.selectAll();
    }

    @Override
    public PageInfo<User> selectUserByPage(UserParam userParam) {
        PageInfo<User> users = PageHelper.startPage(userParam.getPage().getPageNum(), userParam.getPage().getPageSize())
                .doSelectPageInfo(() -> userMapper.selectByCondition(userParam));
        return users;
    }

    /**
     * 保存user登录信息，返回token
     * @param username
     */
    public String generateJwtToken(String username) {
        String salt = JwtUtils.generateSalt();
        redisUtil.set("token:" + username, salt, 3600L);
        return JwtUtils.sign(username, salt, 3600); //生成jwt token，设置过期时间为1小时
    }

    /**
     * 获取上次token生成时的salt值和登录用户信息
     * @param username
     * @return
     */
    public UserDto getJwtTokenInfo(String username) {
        String salt = redisUtil.get("token:" + username);
        UserDto user = getUserInfo(username);
        user.setSalt(salt);
        return user;
    }

    /**
     * 清除token信息
     * @param username 登录用户名
     */
    public void deleteLoginInfo(String username) {
        redisUtil.remove("token:"+username);
    }

    /**
     * 获取数据库中保存的用户信息，主要是加密后的密码
     * @param userName
     * @return
     */
    public UserDto getUserInfo(String userName) {
        User user = userMapper.getUserByName(userName);
        UserDto userDto = BeanUtil.copy(user, UserDto::new);
        userDto.setUserId(user.getId());
        userDto.setSalt(encryptSalt);
        //userDto.setEncryptPwd(new Sha256Hash("123456", encryptSalt).toHex());
        return userDto;
    }

    public static void main(String[] args) {
        String encryptSalt = "F12839WhsnnEV$#23b";
        String sha256Hash = new Sha256Hash("123456", encryptSalt).toHex();
        System.out.println(sha256Hash);

    }

    /**
     * 获取用户角色列表，强烈建议从缓存中获取
     * @param userId
     * @return
     */
    public List<String> getUserRoles(Integer userId){
        return Arrays.asList("admin");
    }
}
