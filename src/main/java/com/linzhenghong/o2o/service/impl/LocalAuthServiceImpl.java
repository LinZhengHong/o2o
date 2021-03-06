package com.linzhenghong.o2o.service.impl;

import com.linzhenghong.o2o.dao.LocalAuthDao;
import com.linzhenghong.o2o.dto.LocalAuthExecution;
import com.linzhenghong.o2o.entity.LocalAuth;
import com.linzhenghong.o2o.enums.LocalAuthStateEnum;
import com.linzhenghong.o2o.exception.LocalAuthOperationException;
import com.linzhenghong.o2o.service.LocalAuthService;
import com.linzhenghong.o2o.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author LinZhenHong
 */
@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    @Autowired
    private LocalAuthDao localAuthDao;

    /**
     * 通过账号和密码获取平台账号信息
     *
     * @param userName
     * @param password
     * @return
     */
    @Override
    public LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password) throws LocalAuthOperationException {
        return localAuthDao.queryLocalByUserNameAndPwd(userName, MD5.getMd5(password));
    }

    /**
     * 通过userId获取平台账号信息
     *
     * @param userId
     * @return
     */
    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    /**
     * 绑定微信，生成平台专属的账号
     *
     * @param localAuth
     * @return
     * @throws LocalAuthOperationException
     */
    @Override
    @Transactional
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        //空值判断，传入的localAuth账号密码，用户信息特别是userId不能为空，否则直接返回错误
        if (localAuth==null||localAuth.getPassword()==null||localAuth.getUsername()==null
        ||localAuth.getPersonInfo()==null||localAuth.getPersonInfo().getUserId()==null){
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        //查询此用户是否已绑定过平台账号
        LocalAuth tempAuth=localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
        if (tempAuth!=null){
            //如果绑定过则直接退出，以保证平台账号的唯一性
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
        try{
            //如果之前没有绑定过平台账号，则创建一个平台账号与该用户绑定
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            //对密码进行MD5加密
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            int effectedNum=localAuthDao.insertLocalAuth(localAuth);
            //判断是否创建成功
            if (effectedNum<0){
                throw new LocalAuthOperationException("账号绑定失败");
            }else{
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
            }
        }catch (Exception e){
            throw new LocalAuthOperationException("insertLocalAuth error:"+e.getMessage());
        }
    }

    /**
     * 修改平台账号的登录密码
     *
     * @param userId
     * @param username
     * @param password
     * @return
     * @throws LocalAuthOperationException
     */
    @Override
    @Transactional
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password,String newPassword) throws LocalAuthOperationException {
        //非空判断，判断传入的用户Id,新旧密码是否为空，新旧密码是否相同，若不满足条件则返回错误信息
        if (userId!=null&&username!=null&&password!=null&&newPassword!=null&&!password.equals(newPassword)){
            try{
                //更新密码，并对新密码进行MD5加密
                int effectedNum=localAuthDao.updateLocalAuth(userId,username,MD5.getMd5(password),MD5.getMd5(newPassword),new Date());
                //判断更新是否成功
                if (effectedNum<=0){
                    throw new LocalAuthOperationException("更新密码失败");
                }
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }catch (Exception e){
                throw new LocalAuthOperationException("更新密码失败"+e.toString());
            }
        }else{
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }
}
