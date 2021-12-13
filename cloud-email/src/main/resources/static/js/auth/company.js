var pageCurr;
var tableIns;
var control = 'company'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/sys/company/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '公司维护';
var nosetting, yessetting;
//定义当前选中公司对象，做授权使用
var companyObj = {};
$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        tableIns = table.render({
            elem: '#companyList'
            , id: 'companyList'
            , url: listUrl
            , method: 'get' //默认：get请求
            , cellMinWidth: 180
            , height: 'full-100'
            , toolbar: '#toolbarDemo'
            , defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where: {},
            title: menuTitle,
            request: {
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
                , {field: 'companyId', title: '主键', width: 180, hide: true}
                , {field: 'companyName', title: '公司名称'}
                , {field: 'companyInfo', title: '公司简介'}
                , {field: 'insertTime', title: '新增时间'}
                , {field: 'updateUid', title: '操作人'}
                , {field: 'insertUid', title: '修改人'}
                , {field: 'updateTime', title: '修改时间'}
                , {fixed: 'right', title: '操作', width: 180, align: 'center', toolbar: '#optBar'}
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
        table.on('tool(companyTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            }else if (obj.event === 'authority') {
                authority(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(companySubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        form.on('submit(companyUpdateSubmit)', function (data) {
            // TODO 校验
            companyUpdateForm(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(companyTable)', function (obj) {
            if (obj.event === 'addcompany') {
                addcompany();
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
            url: '/ztree/getAllPermTree',
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
            url: '/perm/getTermTreeBycompanyId',
            autoParam: ['id'],
            otherParam: ['companyId', function () {
                return $("#treeCompanyId").val();
            }]
        }, callback: {}
    };





});

function reloadcompanyList() {
    loadTable('companyList', {})
}

function addcompany() {
    formAssignment(fromId, {"companyId": ''})
    //echoForm(,{},fromId,'GET')
    openForm('companyDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('companyUpdateDiv', 'companyUpdateForm', '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloadcompanyList);
}
function companyUpdateForm(data) {
    submitForm(aOrUUrl, 'companyUpdateDiv', 'companyUpdateForm', data.field, 'POST', reloadcompanyList);
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'companyId': data.companyId}, 'GET', reloadcompanyList)
}

function edit(data) {
    echoForm(getUrl, {'companyId': data.companyId}, 'companyUpdateForm', 'GET');
}

/**
 * 打开授权
 * @param data
 */
function authority(data){
    openForm('setCompanyPermDiv', "setCompanyPermForm", '授权');
    reloadTree(true,true);
    $("#treeCompanyId").val(data.companyId);
}

/**
 * 初始化树
 */
function reloadTree(x1,x2) {
    getData("/ztree/getPermTree",{},reloadTreeCall,x1,x2)
}
function reloadTreeCall(data,x1,x2){
    if (x1)
        $.fn.zTree.init($("#noPermZtree"), nosetting, data);
    if (x2)
        $.fn.zTree.init($("#yesPermZtree"), yessetting, data);
}


/**
 * 新增
 */
function companyPermSave() {
    var zTreeOjb = $.fn.zTree.getZTreeObj("noPermZtree");
    var checkedNodes = zTreeOjb.getCheckedNodes(true);
    var length = checkedNodes.length;
    if (length == 0) {
        layer.msg('请选择需要撤销的角色功能');
        return;
    }
    var perms = checkedNodes.map((obj) => {
        return obj.id;
    }).join(",");
    ajaxPostData("/sys/comp/perm/aOrUS",{perids:perms,companyId:$("#treeCompanyId").val()},OrUSCallBack);

}
function OrUSCallBack(data){
    layer.msg('操作成功', {icon: 6});
    reloadTree(false,true);

}
/**
 * 删除
 */
function companyPermDel() {
    var zTreeOjb = $.fn.zTree.getZTreeObj("yesPermZtree");
    var checkedNodes = zTreeOjb.getCheckedNodes(true);
    var length = checkedNodes.length;
    if (length == 0) {
        layer.msg('请选择需要撤销的角色功能');
        return;
    }
    var perms = checkedNodes.map((obj) => {
        return obj.id;
    }).join(",");
    getData("/sys/comp/perm/dels",{perids:perms,companyId:$("#treeCompanyId").val()},OrUSCall);
}