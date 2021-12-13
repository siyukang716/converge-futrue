var pageCurr;
var tableIns;
var control = 'msgbusinessmsgmaster'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/msg/busin/msg/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '消息模块';

$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        tableIns = table.render({
            elem: '#msgbusinessmsgmasterList'
            , id: 'msgbusinessmsgmasterList'
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
                , {field: 'msgId', title: '消息主键', width: 180, hide: true}
                , {field: 'msgTitle', title: '消息标题', width: 100}
                , {field: 'msgContent', title: '消息内容'}
                //, {field: 'businessId', title: ''}
                //, {field: 'businessUrl', title: '业务路径'}
                //, {field: 'businessType', title: '业务类型/来源'}
                //, {field: 'msgStatus', title: '消息状态  1必读  2 非必读'}
                , {field: 'msgPostTime', title: '消息发布时间'}
                //, {fixed: 'right', title: '操作', width: 180, align: 'center', toolbar: '#optBar'}
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
        table.on('tool(msgbusinessmsgmasterTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(msgbusinessmsgmasterSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(msgbusinessmsgmasterTable)', function (obj) {
            if (obj.event === 'addmsgbusinessmsgmaster') {
                addmsgbusinessmsgmaster();
            }
        });
    });
});

function reloadmsgbusinessmsgmasterList() {
    loadTable('msgbusinessmsgmasterList', {})
}

function addmsgbusinessmsgmaster() {
    formAssignment(fromId, {"msgId": ''})
    //echoForm(,{},fromId,'GET')
    openForm('msgbusinessmsgmasterDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('msgbusinessmsgmasterDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloadmsgbusinessmsgmasterList);
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'msgId': data.msgId}, 'GET', reloadmsgbusinessmsgmasterList)
}

function edit(data) {
    echoForm(getUrl, {'msgId': data.msgId}, fromId, 'GET');
}

