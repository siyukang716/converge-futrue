var pageCurr;
var tableIns;
var control = 'cuser'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/c/user/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '美容院-顾客';

$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        tableIns = table.render({
            elem: '#cuserList'
            , id: 'cuserList'
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
                , {field: 'userId', title: '用户编号', width: 180, hide: true}
                , {field: 'username', title: '顾客姓名'}
                , {field: 'nickName', title: '别称'}
                , {field: 'mobile', title: '手机号'}
                , {field: 'birthday', title: '生日'}
                , {field: 'email', title: '邮箱'}
                , {field: 'amt', title: '余额'}
                , {fixed: 'right', title: '操作', width: 180, align: 'center', toolbar: '#optBar'}
            ]]
            , done: function (res, curr, count) {
                pageCurr = curr;
            }
        });

        //日期
        laydate.render({
            elem: '#birthday'
        });


        //监听工具条
        table.on('tool(cuserTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(cuserSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(cuserTable)', function (obj) {
            if (obj.event === 'addcuser') {
                addcuser();
            }
        });
    });
});

function reloadcuserList() {
    loadTable('cuserList', {})
}

function addcuser() {
    formAssignment(fromId, {"userId": ''})
    //echoForm(,{},fromId,'GET')
    openForm('cuserDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('cuserDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloadcuserList);
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'userId': data.userId}, 'GET', reloadcuserList)
}

function edit(data) {
    echoForm(getUrl, {'userId': data.userId}, fromId, 'GET');
}

