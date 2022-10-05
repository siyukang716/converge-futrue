/**
 * 用户管理
 */
var pageCurr;
var tableIns;
$(function () {
    layui.use(['table','upload', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var upload = layui.upload;
        var form = layui.form;
            tableIns = table.render({
                elem: '#uesrList'
                , id: 'uesrListReload'
                , url: '/user/getPageList'
                //, method: 'get' //默认：get请求
                , cellMinWidth: 80
                , toolbar: '#toolbarDemo'
                , defaultToolbar: ['filter', 'print', 'exports']
                , page: true
                , skin: 'line'
                , even: true
                , height: 'full-100'
                , request: {
                    pageName: 'current' //页码的参数名称，默认：page
                    , limitName: 'size' //每页数据量的参数名，默认：limit
                }, response: {
                    statusName: 'code' //数据状态的字段名称，默认：code
                    , statusCode: 200 //成功的状态码，默认：0
                    , countName: 'totals' //数据总数的字段名称，默认：count
                    , dataName: 'list' //数据列表的字段名称，默认：data
                    , msgName: 'message'
                }
                , cols: [[
                    {type: 'numbers'}
                    , {type: 'checkbox'}
                    , {field: 'id', title: 'ID', width: 180, hide: true}
                    , {field: 'username', title: '用户名'}
                    , {field: 'mobile', title: '账号'}
                    , {field: 'email', title: '邮箱'}
                    , {field: 'roleName', title: '角色名称', minWidth: 80}
                    , {
                        field: 'insertTime',
                        title: '添加时间',
                        templet: "<div>{{layui.util.toDateString(d.insertTime, 'yyyy-MM-dd')}}</div>"
                    }
                    , {field: 'isJob', title: '是否在职', width: 95, align: 'center', templet: '#jobTpl'}
                    , {fixed: 'right', title: '操作', width: 500, align: 'center', toolbar: '#optBar'}
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


        var $ = layui.$, active = {
            reload: function(obj){
                //执行重载
                table.reload('uesrListReload', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    ,where: obj
                });
            }
        };


        //监听在职操作
        form.on('switch(isJobTpl)', function (obj) {
            //console.log(this.value + ' ' + this.name + '：'+ obj.elem.checked, obj.othis);
            setJobUser(obj, this.value, this.name, obj.elem.checked);
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(userTable)', function (obj) {
            if (obj.event === 'addUser') {
                //完善用户信息
                addUser();
            }else if (obj.event === 'importUsers') {
                //导入用户信息
                importUsers();
            }else if (obj.event === 'staffTransfer') {
                //职工调岗
                staffTransfer();
            }
        });
        //监听工具条
        table.on('tool(userTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delUser(data, data.id, data.username);
            } else if (obj.event === 'edit') {
                //编辑
                getUser(data, data.id);
            } else if (obj.event === 'recover') {
                //恢复
                recoverUser(data, data.id);
            } else if (obj.event === 'nolockUser') {
                //恢复
                nolockUser(data, data.id);
            } else if (obj.event === 'perfectUserInfo') {
                //完善用户信息
                perfectUserInfo(data, data.id);
            } else if (obj.event === 'role') {
                $("#user_id").val(data.id == null ? '' : data.id);
                $("#set_version").val(data.version == null ? '' : data.version);
                layer.open({
                    type: 1,
                    title: "角色",
                    fixed: false,
                    resize: false,
                    //shadeClose: true,
                    area: ['550px'],
                    content: $('#setRole'),
                    end: function () {
                        cleanUser();
                    }, cancel: function (index, layero) {
                        layer.close(index)
                        $("#setRole").hide();
                        return false;
                    }
                });
                // getRoleTrzz();
            }
        });
        //监听提交
        form.on('submit(userSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        form.on('submit(roleSubmit)', function (data) {
            // TODO 校验
            roleformSubmit(data);
            return false;
        });
        //用户信息表补全
        form.on('submit(userinfoSubmit)', function (data) {
            // TODO 校验
            userinfoSubmit(data);
            return false;
        });
        //搜索框
        var form = layui.form, layer = layui.layer
            , laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#insertTimeStart'
        });
        laydate.render({
            elem: '#insertTimeEnd'
        });
        laydate.render({
            elem: '#entryTime'
            //,type: 'datetime'
        });
        laydate.render({
            elem: '#birthday'
            //,type: 'datetime'
        });
        //TODO 数据校验
        //监听搜索框
        form.on('submit(searchSubmit)', function (data) {
            //重新加载table
            //loadUserList1(data);
            //active['reload'].call(null,data.field);
            loadUserList(data.field);
            return false;
        });






        upload.render({
            elem: '#importUserDataFile'
            , url: "/user/importData" //此处配置你自己的上传接口即可
            , auto: true
            //, data: {"updateSupport": false,"basicId":$("#reqbasicId").val()}
            , accept: 'file'
            , done: function (res) {
                layer.closeAll(); //疯狂模式，关闭所有层
                $("#userImportDiv").hide()
                loadUserList({});
                layer.msg(res.message);

            }

        });

    });



});

//设置用户是否离职
function setJobUser(obj, id, nameVersion, checked) {
    var name = nameVersion.substring(0, nameVersion.indexOf("_"));
    var version = nameVersion.substring(nameVersion.indexOf("_") + 1);
    var isJob = checked == 1 ? 0 : 1;
    var userIsJob = checked ? "在职" : "离职";
    //是否离职
    layer.confirm('您确定要把用户：' + name + '设置为' + userIsJob + '状态吗？', {
        btn: ['确认', '返回'] //按钮
    }, function () {
        $.post("/user/setJobUser", {"id": id, "isJob": isJob}, function (data) {
            //回调弹框
            layer.alert("操作成功！", function () {
                layer.closeAll();
                //加载load方法
                loadUserList({});
            });
        });
    }, function () {
        layer.closeAll();
        //加载load方法
        loadUserList({});
    });
}

//提交表单
function formSubmit(obj) {
    var currentUser = $("#currentUser").html();
    if ($("#id").val() == currentUser) {
        layer.confirm('更新自己的信息后，需要您重新登录才能生效；您确定要更新么？', {
            btn: ['返回', '确认'] //按钮
        }, function () {
            layer.closeAll();
        }, function () {
            layer.closeAll();//关闭所有弹框
            submitAjax(obj, currentUser);
        });
    } else {
        submitAjax(obj, currentUser);
    }
}

function roleformSubmit(data) {
    var treeObj = $.fn.zTree.getZTreeObj("regionZTree");
    var selectedNodes = treeObj.getSelectedNodes();
    $.ajax({
        type: "POST",
        data: {"userId": $("#user_id").val(), "roleId": selectedNodes[0].id, "version": data.field.version},
        url: "/urcon/setRole",
        success: function (data) {
            if (isLogin(data)) {
                $('#setRole').hide();
                if (data == "ok") {
                    layer.alert("操作成功", function () {
                        layer.closeAll();
                        //加载页面
                        loadUserList({});
                    });
                } else {
                    layer.alert(data, function () {
                        layer.closeAll();
                        //加载load方法
                        loadUserList({});//自定义
                    });
                }
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                loadUserList(obj);//自定义
            });
        }
    });
}

function submitAjax(obj, currentUser) {
    $.ajax({
        type: "POST",
        data: $("#userForm").serialize(),
        url: "/user/setUser",
        success: function (data) {
            layer.alert(data.message, function () {
                if ($("#id").val() == currentUser) {
                    //如果是自己，直接重新登录
                    parent.location.reload();
                } else {
                    layer.closeAll();
                    $('#setUser').hide();
                    cleanUser();
                    //$("#id").val("");
                    //加载页面
                    loadUserList({});
                }
            });
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                loadUserList(obj);//自定义
            });
        }
    });
}


//开通用户
function addUser() {
    openUser(null, "开通用户",'add');
    $("#mobile").prop('readonly', '');
}

function openUser(id, title,flag) {
    if (id == null || id == "") {
        $("#id").val("");
    }
    var zTreeOjb = $.fn.zTree.getZTreeObj("deptZTreeInstance");
    var selectedNodes = zTreeOjb.getSelectedNodes();
//    debugger;
    if(flag != null && flag == 'add'){
        if(selectedNodes == null || selectedNodes.length == 0){
        console.log("未选中左边部门节点");
          layer.msg('未选中左边部门节点', {icon: 6});
        return ;
        }else{
           $("#deptId").val(selectedNodes[0].dept_id);
        }
    }
    layer.open({
        type: 1,
        title: title,
        fixed: false,
        resize: false,
        //shadeClose: true,
        area: ['550px'],
        content: $('#setUser'),
        end: function () {
            cleanUser();
        }
        , cancel: function (index, layero) {
            layer.close(index)
            $("#setUser").hide();
            return false;
        }
    });
}

function getUser(obj, id) {
    //如果已经离职，提醒不可编辑和删除
    if (obj.job) {
        layer.alert("该用户已经离职，不可进行编辑；</br>  如需编辑，请设置为<font style='font-weight:bold;' color='green'>在职</font>状态。");
    } else if (obj.del) {
        layer.alert("该用户已经删除，不可进行编辑；</br>  如需编辑，请先<font style='font-weight:bold;' color='blue'>恢复</font>用户状态。");
    } else {
        //回显数据
        $.get("/user/getUser", {"id": id}, function (data) {
            if (data.status == "1000" && data.data != null) {
                $("#id").val(data.data.id == null ? '' : data.data.id);
                $("#version").val(data.data.version == null ? '' : data.data.version);
                $("#username").val(data.data.username == null ? '' : data.data.username);
                $("#mobile").val(data.data.mobile == null ? '' : data.data.mobile);
                $("#email").val(data.data.email == null ? '' : data.data.email);

                openUser(id, "设置用户",'upd');
                $("#mobile").prop('readonly', 'readonly');
            } else {
                //弹出错误提示
                layer.alert(data.message, function () {
                    layer.closeAll();
                });
            }
        });
    }
}

function delUser(obj, id, name) {
    if (null != id) {
        layer.confirm('您确定要删除' + name + '用户吗？', {
            btn: ['确认', '返回'] //按钮
        }, function () {
            $.post("/user/deleteUser", {"id": id}, function (data) {
                if (isLogin(data)) {
                    if (data == "ok") {
                        //回调弹框
                        layer.alert("删除成功！", function () {
                            layer.closeAll();
                            //加载load方法
                            loadUserList(obj);//自定义
                        });
                    } else {
                        layer.alert(data, function () {
                            layer.closeAll();
                            //加载load方法
                            loadUserList(null);//自定义
                        });
                    }
                }
            });
        }, function () {
            layer.closeAll();
        });
    }
}

function recoverUser(obj, id) {
    //console.log("需要恢复的用户id="+id);

    if (null != id) {
        layer.confirm('您确定要恢复' + name + '用户吗？', {
            btn: ['确认', '返回'] //按钮
        }, function () {
            $.post("/user/recoverUser", {"id": id}, function (data) {
                if (isLogin(data)) {
                    if (data == "ok") {
                        //回调弹框
                        layer.alert("恢复成功！", function () {
                            layer.closeAll();
                            //加载load方法
                            loadUserList(obj);//自定义
                        });
                    } else {
                        layer.alert(data, function () {
                            layer.closeAll();
                            //加载load方法
                            loadUserList(null);//自定义
                        });
                    }
                }
            });
        }, function () {
            layer.closeAll();
        });
    }
}

//解锁用户
function nolockUser(data,id) {
    getData('/user/unlockAccount',{'username':data.mobile},nolockUserCall)
}
function nolockUserCall(data) {
    if (data.status == '1000'){
        layer.msg(data.message=='' || data.message == null || data.message==undefined?"操作成功":data.message, {icon: 6});
    }else {
        layer.msg(data.message=='' || data.message == null || data.message==undefined?"操作失败":data.message, {icon: 5});
    }
}

function cleanUser() {
    //$("#id").val("");
    $("#username").val("");
    $("#mobile").val("");
    $("#email").val("");
    $("#password").val("");
}


//------------------用户信息---------------------
//完善用户信息
function perfectUserInfo(data, id) {
    formAssignment('userinfoForm', {'customerId': id, "userMoney": 0, "userPoint": 0});
    echoForm("/customer/inf/getById", {'customerId': id}, "userinfoForm", "GET");

}

function echoFormCallback(data, formId) {
    openForm("userinfoDiv", formId, "完善用户信息");
    formAssignment(formId, data.data);
}


function userinfoSubmit() {
    $.ajax({
        type: "POST",
        data: $("#userinfoForm").serialize(),
        url: "/customer/inf/aOrU",
        success: function (data) {
            layer.alert(data.message, function () {
                layer.closeAll();
                $("#userinfoDiv").hide();
            });
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                loadUserList({});//自定义
            });
        }
    });
}

function loadUserList(obj) {
    // layui.use([], function () {
    //     var loadtable = layui.table;
    //     //重新加载table
    //     loadtable.reload('uesrListReload', {
    //         where: obj
    //         , page: {
    //             curr: pageCurr //从当前页码开始
    //         }
    //     });
    // });

    tableIns.reload({
        where: obj
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });

}
function deptZTreeInstanceOnClickCall(event, treeId, treeNode, clickFlag) {
    loadUserList({'deptid': treeNode.dept_id});
    //active['reload'].call(null,{'deptid': treeNode.dept_id})
}

/**
 * 导入职工信息
 */
function importUsers(){
    openForm('userImportDiv', '', '导入职工信息');
}

/**
 * 职工调岗
 */
function staffTransfer(){
    var checkStatus = layui.table.checkStatus('uesrListReload');
    var data = checkStatus.data;
    if (data.length < 1){
        layer.msg("请选择需要调岗人员", {icon: 5});
    }else {
        openForm('common_dept_radio_TreeDiv', '', '单选部门组件');
    }
};
function staffTransferCall(data){
    if (data.status == '1000'){
        layer.closeAll();
        $("#common_dept_radio_TreeDiv").hide();
        loadUserList();
        layer.msg(data.message=='' || data.message == null || data.message==undefined?"操作成功":data.message, {icon: 6});
    }else {
        layer.msg(data.message, {icon: 5});
    }
}

function dept_radio_Tree_submit_CallBack(node) {
    var checkStatus = layui.table.checkStatus('uesrListReload');
    var data = checkStatus.data;
    var ids = data.map((obj) => {
        return obj.id;
    }).join(",");
    var deptid = node.dept_id;
    ajaxPostData("/system/user/staffTransfer",{'userIds':ids,'deptid':deptid},staffTransferCall)
}