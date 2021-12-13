package com.cloud.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.SysDeptEntity;
import com.cloud.sys.entity.DictSeqEntity;
import com.cloud.sys.mapper.SysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @ClassName: SysDeptController
 * @Description: 部门
 * @author
 * @date 2021-09-20
 */
@Service
public class SysDeptService extends ServiceImpl<SysDeptMapper, SysDeptEntity> {
    @Autowired
    private SysDeptMapper mapper;
    @Autowired
    private SysUserDeptService userDeptService;
    @Autowired
    private RoleDeptService roleDeptService;
    @Autowired
    private DictSeqService dictSeqService;
    public List<Map> getTree(Long deptId) {
        LambdaQueryWrapper<SysDeptEntity> lambda = new QueryWrapper<SysDeptEntity>().lambda();
        Long companyId = ShiroUtils.getLoginUser().getCompanyId();
        Long deptid = userDeptService.getDeptidByLoginUser();
        lambda.eq(-1 == deptId,SysDeptEntity :: getDeptId,deptid);
        lambda.eq(-1 != deptId,SysDeptEntity :: getParentDeptId,deptId);

        lambda.eq(SysDeptEntity::getCompanyId,companyId);
        List<Map> maps = mapper.treeList(lambda);
        return maps;
    }


    /**
     * 获取当前登录人部门所有子部门id集合
     * @return
     */
    public Set<Long> getOnlineAllChildDeptsIds(){
        return getOnlineAllChildDepts().stream().map(SysDeptEntity::getDeptId).collect(Collectors.toSet());
    }


    /**
     * 获取指定部门所有子部门id集合
     * @return
     */
    public Set<Long> getAllChildDeptsIds(Long deptid){
        return getAllChildDeptsById(deptid).stream().map(SysDeptEntity::getDeptId).collect(Collectors.toSet());
    }


    /**
     * 获取当前登录人部门所有子部门
     * @return
     */
    public List<SysDeptEntity> getOnlineAllChildDepts(){
        List<SysDeptEntity> list = list();
        Long deptid = userDeptService.getDeptidByLoginUser();
        return recursionForDepts(deptid,list,new ArrayList<>());
    }
    /**
     * 获取指定部门所有子部门
     * @param deptid
     * @return
     */
    public List<SysDeptEntity> getAllChildDeptsById(Long deptid){
        List<SysDeptEntity> list = list();
        return recursionForDepts(deptid,list,new ArrayList<>());
    }


    /**
     * 递归所有子部门
     * @param parentId
     * @param deptList
     * @return
     */
    private List<SysDeptEntity> recursionForDepts(Long parentId,List<SysDeptEntity> deptList,List<SysDeptEntity> relist){
        /**
         * Optional.ofNullable(menuList).orElse(new ArrayList<>())  如果menuList是空的则返回一个new ArrayList<>()
         *  .stream() 返回List中的流
         *  .filter(menu -> menu.getParentId().equals(parentId)) 筛选List，返回只有条件成立的元素（当前元素的parentId必须等于父id）
         *  .forEach 遍历这个list
         */
        Optional.ofNullable(deptList).orElse(new ArrayList<>())
                .stream()
                .filter(d ->d.getParentDeptId().equals(parentId))
                .forEach(d -> {
                    recursionForDepts(d.getDeptId(),deptList,relist);
                    relist.add(d);
                });
        return relist;
    }

    /**
     * 部门数据权限
     * @param isSelf
     * @return
     */
    public List<Map> getPurviewTree(Boolean isSelf) {
        LambdaQueryWrapper<SysDeptEntity> lambda = new QueryWrapper<SysDeptEntity>().lambda();
        Long companyId = ShiroUtils.getLoginUser().getCompanyId();
        Long deptid = userDeptService.getDeptidByLoginUser();
        if (isSelf){
            lambda.eq(isSelf,SysDeptEntity :: getDeptId,deptid);
        }else {
            Set deptIds = roleDeptService.getDeptByRoleSet();
            lambda.in(null!=deptIds,SysDeptEntity :: getDeptId,deptIds);
            lambda.eq(null==deptIds,SysDeptEntity :: getDeptId,deptid);
        }
        lambda.eq(SysDeptEntity::getCompanyId,companyId);
        List<Map> maps = mapper.treeList(lambda);
        maps.forEach(d->{
            d.put("parent_dept_id",-1);
        });
        return maps;
    }
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateCustomize(SysDeptEntity entity) {
        if (null == entity.getDeptId()){
            DictSeqEntity dict = dictSeqService.getDictByType("dept_code");
            entity.setCoding("DEPT"+dict.getSerialNumber());
            dictSeqService.update("dept_code",dict.getSerialNumber()+1);
        }
        saveOrUpdate(entity);
        return true;
    }

    /**
     * 获取用户导入模板部门信息列
     * @return
     */
    public List<Map<String, Object>> getDeptComboOurCompany(){
        QueryWrapper<SysDeptEntity> lambda = new QueryWrapper<SysDeptEntity>();
        lambda.select("dept_id deptId","CONCAT(dept_name,'-',coding) combo");
        lambda.eq("company_id",ShiroUtils.getLoginUser().getCompanyId());
        List<Map<String, Object>> maps = listMaps(lambda);
        return maps;
    }

    /**
     * 获取模板选项值
     * @return
     */
    public String[] getExceCombo(){
        List<Map<String, Object>> maps = getDeptComboOurCompany();
        Set<String> combo = maps.stream().map(d -> String.valueOf(d.get("combo"))).collect(Collectors.toSet());
        String[] combos = combo.toArray(new String[]{});
        return combos;
    }

    /**
     * 获取导入数据部门字典值
     * @return
     */
    public Map<String, Long> getExceComboMap(){
        List<Map<String, Object>> maps = getDeptComboOurCompany();
        return maps.stream().collect(Collectors.toMap(d -> String.valueOf(d.get("combo")), a -> Long.valueOf(a.get("deptId").toString()), (k1, k2) -> k1));
    }
}