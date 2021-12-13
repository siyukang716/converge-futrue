/**
 * 用户管理
 */
var pageCurr;
var nosetting, yessetting;
$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var form = layui.form,

            tableIns = table.render({
                elem: '#roleList'
                , url: '/role/getRoles'
                , method: 'get' //默认：get请求
                , cellMinWidth: 80
                ,height: 'full-100'
                , page: true,
                request: {
                    pageName: 'current' //页码的参数名称，默认：page
                    , limitName: 'size' //每页数据量的参数名，默认：limit
                }, response: {
                    statusName: 'code' //数据状态的字段名称，默认：code
                    , statusCode: 200 //成功的状态码，默认：0
                    , countName: 'totals' //数据总数的字段名称，默认：count
                    , dataName: 'list' //数据列表的字段名称，默认：data
                }
                , cols: [[
                    {type: 'numbers'}
                    , {field: 'id', title: 'ID', width: 180, hide: true}
                    , {field: 'roleName', title: '角色名称'}
                    , {field: 'descpt', title: '角色描述'}
                    , {field: 'code', title: '角色编号'}
                    , {
                        field: 'insertTime',
                        title: '添加时间',
                        templet: "<div>{{layui.util.toDateString(d.insertTime, 'yyyy-MM-dd')}}</div>"
                    }
                    , {fixed: 'right', title: '操作', width: 280, align: 'center', toolbar: '#optBar'}
                ]]
                , done: function (res, curr, count) {
                    //如果是异步请求数据方式，res即为你接口返回的信息。
                    //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                    //console.log(res);
                    //得到当前页码
                    //console.log(curr);
                    //得到数据总量
                    //console.log(count);
                    //$("[data-field='id']").css('display', 'none');
                    pageCurr = curr;
                }
            });


        //监听工具条
        table.on('tool(roleTable)', function (obj) {
            var data = obj.data;
            $("#roleId").val(data.id);
            reloadTree();
            if (obj.event === 'rolePerm') {
                //delUser(data, data.id, data.username);
                layer.open({
                    type: 1,
                    title: "授权管理",
                    fixed: false,
                    resize: false,
                    //shadeClose: true,
                    area: ['550px'],
                    content: $('#setRolePerm'),
                    end: function () {
                        //cleanUser();
                    },cancel: function(index, layero){
                        layer.close(index)
                        $("#setRolePerm").hide();
                        return false;
                    }
                });
            }
        });
    });


    nosetting = {
        data: {
            simpleData: {
                enable: true,
                idKey: 'id',
                pIdKey: 'pId',
                rootPId: 0
            }
        }, view: {
            showLine: false,
            showIcon: true,
        }, check: {
            enable: true
        }, async: {
            enable: true,
            type: "post",
            dataType: 'json',
            //url: '/ztree/getAllPermTree',
            url: '/perm/getTermTreeByLoginCompanyId',
            autoParam: ['id']
            // otherParam: ['flag', 0, 'roleId', function () {
            //     return $("#roleId").val()
            // }]
        }, callback: {}
    };
    yessetting = {
        data: {
            simpleData: {
                enable: true,
                idKey: 'id',
                pIdKey: 'pId',
                rootPId: 0
            }
        }, view: {
            showLine: false,
            showIcon: true,
        }, check: {
            enable: true,
            chkboxType: { "Y": "", "N": "" }
        }, async: {
            enable: true,
            type: "post",
            dataType: 'json',
            url: '/ztree/getRoleTermTree',
            autoParam: ['id'],
            otherParam: ['roleId', function () {
                return $("#roleId").val()
            }]
        }, callback: {}
    };
    reloadTree();
});
function reloadTree() {
    $(function () {
        $.ajax({
            url: '/ztree/getPermTree',
            data: {},
            cache: false,
            dataType: 'json',
            success: function (data) {
                $.fn.zTree.init($("#noZtree"), nosetting, data);
                $.fn.zTree.init($("#yesZtree"), yessetting, data);
            },
            error: function (data) {

            }
        })
    });
}
/**
 * 新增
 */
function rolePermSave() {
    var zTreeOjb = $.fn.zTree.getZTreeObj("noZtree");
    var checkedNodes = zTreeOjb.getCheckedNodes(true);
    var length = checkedNodes.length;
    var perms = '';
    for (var i = 0; i < length; i++) {
        perms += checkedNodes[i].id + ',';
    }
    perms = perms.substring(0, perms.length - 1);
    //alert(funcids);
    if (perms == '') {
        layer.msg('请选择需要授予给角色的功能');
        return;
    }
    $.ajax({
        url: '/rolePerm/addObjs',
        data: {'prems': perms, "roleid": $("#roleId").val()},
        cache: false,
        dataType: 'json',
        success: function (data) {
            reloadTree();
            layer.msg('操作成功', {icon: 6});
        },
        error: function (data) {

        }
    })
}

/**
 * 删除
 */
function rolePermDel() {
    var zTreeOjb = $.fn.zTree.getZTreeObj("yesZtree");
    var checkedNodes = zTreeOjb.getCheckedNodes(true);
    var length = checkedNodes.length;
    var perms = '';
    for (var i = 0; i < length; i++) {
        perms += checkedNodes[i].id + ',';
    }
    perms = perms.substring(0, perms.length - 1);
    if (perms == '') {
        layer.msg('请选择需要撤销的角色功能');
        return;
    }
    $.ajax({
        url: '/rolePerm/delObjs',
        data: {'prems': perms, "roleid": $("#roleId").val()},
        cache: false,
        dataType: 'json',
        success: function (data) {
            reloadTree();
            layer.msg('操作成功', {icon: 6});
        },
        error: function (data) {

        }
    })
}



