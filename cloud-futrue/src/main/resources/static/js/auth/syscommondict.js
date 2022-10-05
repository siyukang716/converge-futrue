var pageCurr;
var tableIns;
var control = 'syscommondict'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/system/dict/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '公共字典';

$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        tableIns = table.render({
            elem: '#syscommondictList'
            , id: 'syscommondictList'
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
                , {field: 'dictId', title: '字典主键', width: 180, hide: true}
                , {field: 'dictName', title: '字典名称'}
                , {field: 'dictType', title: '字典类型'}
                , {field: 'dictMome', title: '备注'}
                , {field: 'insertTime', title: '插入时间'}
                , {field: 'insertUid', title: '操作人'}
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
        table.on('tool(syscommondictTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(syscommondictSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(syscommondictTable)', function (obj) {
            if (obj.event === 'addsyscommondict') {
                //完善用户信息
                addsyscommondict();
            }
        });
    });
});

function reloadsyscommondictList() {
    loadTable('syscommondictList', {})
}

function addsyscommondict() {
    formAssignment(fromId, {"dictId": ''});
    //echoForm(,{},fromId,'GET')
    openForm('syscommondictDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('syscommondictDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloadsyscommondictList);
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'dictId': data.dictId}, 'GET', reloadsyscommondictList)
}

function edit(data) {
    echoForm(getUrl, {'dictId': data.dictId}, fromId, 'GET');
}

