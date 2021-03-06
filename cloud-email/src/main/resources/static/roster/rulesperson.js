var pageCurr;
var tableIns;
var control = 'rulesperson'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/roster/rules/person/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '排版人员规则关联';

$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        tableIns = table.render({
            elem: '#rulespersonList'
            , id: 'rulespersonList'
            , url: listUrl
            , method: 'get' //默认：get请求
            , cellMinWidth: 180
            , height: 'full-100'
            , toolbar: '#toolbarDemo'
            , defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where: {'arrangeRuleId':$("#reqarrangeRuleId").val()},
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
                , {field: 'rulesPersonId', title: '主键', width: 180, hide: true}
                , {field: 'arrangeRuleId', title: '排班规则id', hide: true}
                , {field: 'userId', title: '人员id', hide: true}
                , {field: 'userName', title: '人员'}
                , {field: 'insertTime', title: '新增时间'}
                , {field: 'insertUid', title: '新增操作人'}
                , {field: 'updateUid', title: '修改操作人'}
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
        table.on('tool(rulespersonTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(rulespersonSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(rulespersonTable)', function (obj) {
            if (obj.event === 'addrulesperson') {
                addrulesperson();
            }
        });
    });
});

function reloadrulespersonList() {
    loadTable('rulespersonList', {'arrangeRuleId':$("#reqarrangeRuleId").val()})
}

function addrulesperson() {
    formAssignment(fromId, {"rulesPersonId": ''})
    //echoForm(,{},fromId,'GET')
    openForm('rulespersonDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('rulespersonDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloadrulespersonList);
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'rulesPersonId': data.rulesPersonId}, 'GET', reloadrulespersonList)
}

function edit(data) {
    echoForm(getUrl, {'rulesPersonId': data.rulesPersonId}, fromId, 'GET');
}

