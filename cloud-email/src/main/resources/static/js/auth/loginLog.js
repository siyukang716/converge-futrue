/**
 * 用户管理
 */
var pageCurr;
var tableIns;
$(function () {
    layui.use(['table', 'laydate','util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form,
        tableIns = table.render({
            elem: '#loginLogList'
            ,id:'loginLogList'
            , url: '/customer/login/log/getList'
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,toolbar: 'toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            height: 'full-100'
            ,where:{"customerId":$("#reqcustomerId").val()},
            request: {
                pageName: 'current' //页码的参数名称，默认：page
                , limitName: 'size' //每页数据量的参数名，默认：limit
            }, response: {
                statusName: 'code' //数据状态的字段名称，默认：code
                , statusCode: 200 //成功的状态码，默认：0
                , countName: 'totals' //数据总数的字段名称，默认：count
                , dataName: 'list' //数据列表的字段名称，默认：data
                ,msgName: 'message'
            }
            , cols: [[
                {type: 'numbers'}
                , {field: 'loginId', title: '登陆日志ID', width: 180, hide: true}
                , {field: 'customerId', title: '登陆用户ID', hide: true}
                , {field: 'loginTime', title: '用户登陆时间',templet: "<div>{{layui.util.toDateString(d.loginTime, 'yyyy-MM-dd HH:mm:ss')}}</div>"}
                , {field: 'loginIp', title: '登陆IP'}
                , {field: 'loginType', title: '登陆类型：0未成功，1成功',templet:function(d){return d.loginType=='1'?"登录成功":"登录不成功"}}
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

