package com.cloud.sys.service;


import cn.hutool.core.util.PhoneUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.config.RuoYiConfig;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.UserEntity;
import com.cloud.sys.entity.CustomerInfEntity;
import com.cloud.sys.entity.SysUserDeptEntity;
import com.cloud.sys.mapper.SysUserDeptMapper;
import com.cloud.sys.mapper.UserMapper;
import com.cloud.sys.vo.ExcelUserVo;
import com.cloud.util.IStatusMessage;
import com.cloud.util.Result;
import com.cloud.util.StringUtils;
import com.cloud.util.file.FileUtils;
import com.cloud.util.poi.ExcelUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity>{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SysUserDeptMapper sysUserDeptMapper;
    @Autowired
    @Lazy
    private  SysDeptService deptService;
    @Transactional(rollbackFor = Exception.class)
    public Result setUser( UserEntity user){
        Result result = Result.getInstance();
        user.setCompanyId(ShiroUtils.getLoginUser().getCompanyId());
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        if (user.getId() == null){
            if (user.getDeptId() == null){
                result.setStatus(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
                result.setMessage("部门ID不能为空");
                return result;
            }
            LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserEntity::getMobile,user.getMobile());
            UserEntity userEntity = user.selectOne(wrapper);
            if (userEntity != null){
                result.setMessage("用户名重复o(╥﹏╥)o");
                return result;
            }else {
                //修改成返回主键ID
                user.insert();
                result.setMessage("操作成功");
                UserEntity userInfo = user.selectOne(wrapper);
                SysUserDeptEntity userDeptEntity = new SysUserDeptEntity();
                userDeptEntity.setUserId(userInfo.getId());
                userDeptEntity.setDeptId(user.getDeptId());
                sysUserDeptMapper.batchUserDeptAorU(Arrays.asList(userDeptEntity));
                result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
                return result;
            }

        }else {
            userMapper.updateById(user);
            result.setMessage("修改成功");
        }
        // 如果是自己，修改完成之后，直接退出；重新登录
        UserEntity adminUser = ShiroUtils.getLoginUser();
        if (adminUser != null && adminUser.getId() == user.getId()){
            //logger.debug("更新自己的信息，退出重新登录！adminUser=" + adminUser);
            SecurityUtils.getSubject().logout();
        }
        return result;

//        // 方案一【不推荐】：通过SessionDAO拿到所有在线的用户，Collection<Session> sessions =
//        // sessionDAO.getActiveSessions();
//        // 遍历找到匹配的，更新他的信息【不推荐，分布式或用户数量太大的时候，会有问题。】；
//        // 方案二【推荐】：用户信息价格flag（或version）标记，写个拦截器，每次请求判断flag（或version）是否改动，如有改动，请重新登录或自动更新用户信息（推荐）；
//
//        // 清除ehcache中所有用户权限缓存，必须触发鉴权方法才能执行授权方法doGetAuthorizationInfo
//        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils
//                .getSecurityManager();
//        ShiroRealm authRealm = (ShiroRealm) rsm.getRealms().iterator()
//                .next();
//        authRealm.clearCachedAuth();
//        logger.debug("清除所有用户权限缓存！！！");
    }

    /**
     * 自定义查询对象
     * @return
     */
    public Page<UserEntity> listUsers(Page<UserEntity> p, QueryWrapper<UserEntity> wrapper) {

        return userMapper.listUsers(p,wrapper);
    }

    /**
     * 恢复用户
     * 自定义方法恢复 ，因为mybatis-plus 设置逻辑删除 需自定义
     * @param id
     */
    public void recoverUser(Long id) {
        userMapper.recoverUser(id);
    }


    /**
     * 更加用户id集合获取用户信息 返回map格式数据
     * @param coll
     * @return
     */
    public Map<Long, UserEntity> getUsersByIdsIn(Collection<?> coll){
        LambdaQueryWrapper<UserEntity> wrapper = new QueryWrapper<UserEntity>().lambda();
        wrapper.in(UserEntity::getId,coll);
        List<UserEntity> list = list(wrapper);
        Map<Long, UserEntity> mapUser = list.stream().collect(Collectors.toMap(UserEntity::getId, a -> a, (k1, k2) -> k1));
        return mapUser;
    }

    /**
     * 下载导入模板
     * @param response
     * @throws Exception
     */
    public void importTemplate(HttpServletResponse response) throws Exception {
        String[] exceCombo = deptService.getExceCombo();
        Map<String,String[]> comboMap = new HashMap<String,String[]>();
        comboMap.put("deptCombo",exceCombo);
        ExcelUtil<ExcelUserVo> util = new ExcelUtil<ExcelUserVo>(ExcelUserVo.class,comboMap );
        Result result = util.importTemplateExcel("用户信息");
        String fileName = (String) result.getData();
        if (!FileUtils.checkAllowDownload(fileName)) {
            throw new RuntimeException(com.cloud.util.StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
        }
        String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
        String filePath = RuoYiConfig.getDownloadPath() + fileName;
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        FileUtils.setAttachmentResponseHeader(response, realFileName);
        FileUtils.writeBytes(filePath, response.getOutputStream());
        FileUtils.deleteFile(filePath);
    }

    /**
     * 导入用户数据
     * @param file
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public String importUser(MultipartFile file) throws Exception {
        ExcelUtil<ExcelUserVo> util = new ExcelUtil<ExcelUserVo>(ExcelUserVo.class);
        List<ExcelUserVo> entities = util.importExcel(file.getInputStream());
        if (StringUtils.isNull(entities) || entities.size() == 0) {
            throw new RuntimeException("导入用户数据不能为空！");
        }
        int num = 0;
        StringBuilder msg = new StringBuilder();
        Map<String, Long> exceComboMap = deptService.getExceComboMap();
        for (ExcelUserVo vo : entities) {
            num++;
            try {
                if (checkEntity(vo,msg,num)){
                    LambdaQueryWrapper<UserEntity> lambda = new QueryWrapper<UserEntity>().lambda();
                    lambda.eq(UserEntity::getMobile,vo.getTel());
                    List<UserEntity> list = super.list(lambda);
                    if (null == list || list.size() == 0){//账号未注册的情况，导入信息
                        Long deptId = exceComboMap.get(vo.getDept());
                        UserEntity user = new UserEntity();
                        user.setUsername(vo.getName());
                        user.setMobile(vo.getTel());
                        user.setPassword("202cb962ac59075b964b07152d234b70");
                        user.insert();
                        SysUserDeptEntity ud = new SysUserDeptEntity();
                        ud.setUserId(user.getId());
                        ud.setDeptId(deptId);
                        ud.insert();
                        CustomerInfEntity inf = new CustomerInfEntity();
                        inf.setCustomerId(user.getId());
                        inf.setCustomerName(vo.getName());
                        inf.setMobilePhone(vo.getTel());
                        inf.setGender(vo.getSex());
                        inf.setAge(vo.getAge());
                        inf.setEntryTime(vo.getEntryTime());
                        inf.setPosition(vo.getPosition());
                        inf.insert();
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return msg.toString();
    }

    /**
     * 导入字段校验
     * @param o
     * @param failureMsg
     * @param failureNum
     * @return
     */
    private boolean checkEntity(ExcelUserVo o, StringBuilder failureMsg, int failureNum) {
        boolean status = true;
        if (StringUtils.isEmpty(o.getName())){
            failureMsg.append("第" + (failureNum+1) + "行、姓名为空");
            status = false;
        }
        if (Objects.isNull(o.getAge()) || !StringUtils.isNumeric(o.getAge()+"")){
            failureMsg.append("第" + (failureNum+1) + "行、年龄有误");
            status = false;
        }
        if (Objects.isNull(o.getTel()) || !PhoneUtil.isMobile(o.getTel())){
            failureMsg.append("第" + (failureNum+1) + "行、手机号有误");
            status = false;
        }
        if (!status)
            failureMsg.append("<br/>");
        return status;
    }
}
