var pageCurr;
var tableIns;
var control = 'roledept'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/sys/role/dept/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '角色部门数据权限';

$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        tableIns = table.render({
            elem: '#roledeptList'
            , id: 'roledeptList'
            , url: listUrl
            , method: 'get' //默认：get请求
            , cellMinWidth: 180
            , height: 'full-100'
            , toolbar: '#toolbarDemo'
            , defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where: {"roleId":$("#reqroleId").val()},
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
                , {field: 'rDPurview', title: '主键', width: 180, hide: true}
                , {field: 'deptName', title: '部门'}
                , {field: 'deptId', title: '部门id', hide: true}
                , {field: 'businessType', title: '业务类型'}
                // , {field: 'insertTime', title: '新增时间'}
                // , {field: 'insertUid', title: '新增操作人'}
                // , {field: 'updateUid', title: '修改操作人'}
                // , {field: 'updateTime', title: '修改时间'}
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
        table.on('tool(roledeptTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(roledeptSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(roledeptTable)', function (obj) {
            if (obj.event === 'openDept') {
                openDept();
            }
        });
    });
});

function reloadroledeptList() {
    loadTable('roledeptList', {"roleId":$("#reqroleId").val()})
}

function addroledept() {
    formAssignment(fromId, {"rDPurview": ''})
    //echoForm(,{},fromId,'GET')
    openForm('roledeptDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('roledeptDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloadroledeptList);
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'rDPurview': data.rDPurview}, 'GET', reloadroledeptList)
}

function edit(data) {
    echoForm(getUrl, {'rDPurview': data.rDPurview}, fromId, 'GET');
}

function openDept() {
    openForm('common_dept_radio_TreeDiv', '', '单选部门组件');
}

function dept_radio_Tree_submit_CallBack(node) {
    if (null == node){
        layer.msg("请选择");
        return;
    }
    submitForm(aOrUUrl, 'common_dept_radio_TreeDiv', '', {roleId:$("#reqroleId").val(),"deptId":node.dept_id,businessType:"BT001"}, 'POST', reloadroledeptList);

}