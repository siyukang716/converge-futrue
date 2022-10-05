var pageCurr;
var tableIns;
var control = 'workrecord'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/roster/work/record/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '排班记录';

$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        tableIns = table.render({
            elem: '#workrecordList'
            , id: 'workrecordList'
            , url: listUrl
            , method: 'get' //默认：get请求
            , cellMinWidth: 180
            , height: 'full-100'
            //, toolbar: '#toolbarDemo'
            //, defaultToolbar: ['filter', 'print', 'exports']
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
                , {field: 'workId', title: '主键', width: 180, hide: true}
                , {field: 'userId', title: '用户id', hide: true}
                , {field: 'arrangeRuleId', title: '排班规则id', hide: true}
                , {field: 'userName', title: '用户名称'}
                , {field: 'workSTime', title: '上班时间'}
                , {field: 'workETime', title: '下班时间'}
                , {field: 'workDate', title: '工作日'}
                , {field: 'workType', title: '工作状态', templet: function (d) {
                        if (d.workType == 1 ) return "正常班";
                        if (d.workType == 2 ) return "请假";
                        if (d.workType == 3 ) return "代班";

                    }}
                , {field: 'makeUpId', title: '补班id workid', hide: true}
                , {field: 'makeUpUser', title: '补班人员', hide: true}
                //, {field: 'insertTime', title: '新增时间'}
                //, {field: 'insertUid', title: '新增操作人'}
                //, {field: 'updateUid', title: '修改操作人'}
                //, {field: 'updateTime', title: '修改时间'}
                //, {field: 'isDel', title: '删除标识'}
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
        table.on('tool(workrecordTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(workrecordSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(workrecordTable)', function (obj) {
            if (obj.event === 'addworkrecord') {
                addworkrecord();
            }
        });
    });
});

function reloadworkrecordList() {
    loadTable('workrecordList', {})
}

function addworkrecord() {
    formAssignment(fromId, {"workId": ''})
    //echoForm(,{},fromId,'GET')
    openForm('workrecordDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('workrecordDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloadworkrecordList);
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'workId': data.workId}, 'GET', reloadworkrecordList)
}

function edit(data) {
    echoForm(getUrl, {'workId': data.workId}, fromId, 'GET');
}

