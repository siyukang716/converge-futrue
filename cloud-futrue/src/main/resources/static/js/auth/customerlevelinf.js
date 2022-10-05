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
            elem: '#levelinfList'
            ,id:'levelinfList'
            , url: '/customer/level/inf/getList'
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: 'full-100'
            ,toolbar: '#toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            title:'用户级别信息',
            where:{},
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
                , {field: 'customerLevel', title: '会员级别ID', width: 180, hide: true}
                , {field: 'levelName', title: '会员级别名称'}
                , {field: 'minPoint', title: '该级别最低积分'}
                , {field: 'maxPoint', title: '该级别最高积分'}
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
        table.on('tool(levelinfTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delAddr(data);
            }else {
                editAddr(data);
            }
        });
        //监听提交
        form.on('submit(levelinfSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
    });
});


function reloadLevelinfList() {
    loadTable('levelinfList',{})
}

function addLevelinf() {
    openForm('levelinfDiv','levelinfForm','完善收货地址');
}

function echoFormCallback(data,formId) {
    openForm('levelinfDiv',formId,'修改收货地址');
    formAssignment(formId,data.data);
}

function formSubmit(data) {
    submitForm('/customer/level/inf/aOrU','levelinfForm','levelinfDiv',data.field,'POST',reloadLevelinfList);
}
function delAddr(data) {
    submitForm('/customer/level/inf/del','levelinfDiv','levelinfForm', {'customerLevel':data.customerLevel},'GET',reloadLevelinfList)
}
function editAddr(data) {
    echoForm('/customer/level/inf/getById',{'customerLevel':data.customerLevel},'levelinfForm','GET');
}

