var pageCurr;
var tableIns;
var control = 'sysoperlog'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/sys/oper/log/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '操作日志记录';

$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        tableIns = table.render({
            elem: '#sysoperlogList'
            , id: 'sysoperlogList'
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
                , {field: 'operId', title: '日志主键', width: 180, hide: true}
                , {field: 'title', title: '模块标题'}
                , {field: 'businessType', title: '业务类型（0其它 1新增 2修改 3删除）'}
                , {field: 'method', title: '方法名称'}
                , {field: 'requestMethod', title: '请求方式'}
                , {field: 'operatorType', title: '操作类别（0其它 1后台用户 2手机端用户）'}
                , {field: 'operName', title: '操作人员'}
                , {field: 'deptName', title: '部门名称'}
                , {field: 'operUrl', title: '请求URL'}
                , {field: 'operIp', title: '主机地址'}
                , {field: 'operLocation', title: '操作地点'}
                , {field: 'operParam', title: '请求参数'}
                , {field: 'jsonResult', title: '返回参数'}
                , {field: 'status', title: '操作状态（0正常 1异常）'}
                , {field: 'errorMsg', title: '错误消息'}
                , {field: 'operTime', title: '操作时间'}
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
    });
});

function reloadsysoperlogList() {
    loadTable('sysoperlogList', {})
}

