package com.example.springbootmybatis.service;

import com.example.springbootmybatis.dao.UserDao;
import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.pojo.User;
import com.example.springbootmybatis.pojo.query.UserQuery;
import com.example.springbootmybatis.util.DBEncryptUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service // 交由spring容器管理
public class UserServiceImpl implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataPermission dataPermission;

    private static final String RESOURCE = "user";


    @Override
    public List<User> listUser() {
        return userDao.listUser();
    }

    @Override
    public User queryUserByName(String name) {
        return userDao.queryUserByName(name);
    }

    @Override
    public PageInfo<User> listUserByName(UserQuery userQuery) {
        Map dataPermissionMap = dataPermission.getDataPermissionCondition(RESOURCE);
//        List<User> userList = userDao.listUserByName(userQuery);
        if (dataPermissionMap.containsKey("allData")) {
            PageHelper.startPage(userQuery.getPageNum(),userQuery.getPagesize());
            return new PageInfo<User>(userDao.listUserByName(userQuery));
        } else {
            List<User> permittedList = new ArrayList<>();
            if (dataPermissionMap.containsKey("dataPermission")) {
                Set dataPermissionSet = (HashSet) dataPermissionMap.get("dataPermission");
                logger.debug("dataPermission:{}",dataPermissionSet);
//                for (User user: userList) {
//                    logger.debug("user:{},user.getId():{}",user,user.getId());
//                    if (dataPermissionSet.contains(user.getId())) {
//                        permittedList.add(user);
//                    }
//                }
                PageHelper.startPage(userQuery.getPageNum(),userQuery.getPagesize());
                permittedList = userDao.listUserByIdSet(userQuery, dataPermissionSet);
                logger.debug("permittedList:{}",permittedList);
            }
            return new PageInfo<User>(permittedList);
        }
    }

    @Override
    public boolean deleteUserById(Integer id) {
        int i = userDao.deleteUserById(id);
        if (i>0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public JsonRespBo queryUserById(Integer id) {
        JsonRespBo respBo = new JsonRespBo();
        User u = userDao.queryUserById(id);
        u.setPwd("");
        respBo.setData(u);
        return respBo;
    }

    @Override
    public boolean updateUser(User user) {
        String salt = DBEncryptUtil.getRandomSalt();
        String saltPwd = DBEncryptUtil.generate(user.getPwd(),salt);
        user.setSalt(salt);
        user.setPwd(saltPwd);
        user.setLastmodifiedTime();
        user.setLastmodifiedBy();
        int i = userDao.updateUser(user);
        if (i>0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addUser(User user) {
        String salt = DBEncryptUtil.getRandomSalt();
        String saltPwd = DBEncryptUtil.generate(user.getPwd(),salt);
        user.setSalt(salt);
        user.setPwd(saltPwd);
        user.setCreateTime();
        user.setCreateBy();
        System.out.println("随机盐："+salt+"加密密码："+saltPwd);
        int i = userDao.addUser(user);
        if (i>0){
            return true;
        } else {
            return false;
        }
        // return userDao.addUser(user)>0 ? true : false;
    }

}
