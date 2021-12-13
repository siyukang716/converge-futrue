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
            elem: '#customerAddrList'
            ,id:'customerAddrList'
            , url: '/customer/addr/getList'
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: 'full-100'
            ,toolbar: '#toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where:{"customerId":$("#reqcustomerId").val()},
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
                , {field: 'customerAddrId', title: 'ID', width: 180, hide: true}
                , {field: 'customerId', title: 'customerId', hide: true}
                , {field: 'zip', title: '邮编'}
                , {field: 'province', title: '地区表中省份的ID'}
                , {field: 'city', title: '地区表中城市的ID'}
                , {field: 'district', title: '地区表中的区ID'}
                , {field: 'address', title: '具体的地址门牌号'}
                , {field: 'isDefault', title: '是否默认', minWidth: 80,templet:function(d){return d.isDefault=='1'?"是":"否"}}
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
        table.on('tool(customerAddrTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delAddr(data);
            }else {
                editAddr(data);
            }
        });
        //监听提交
        form.on('submit(customerAddrSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
    });
});


function reloadcustomerAddrList() {
    loadTable('customerAddrList',{"customerId":$("#reqcustomerId").val()})
}

function addCustomerAddr() {
    formAssignment('customerAddrForm',{"customerId":$("#reqcustomerId").val()})
    //echoForm(,{},'customerAddrForm','GET')
    openForm('customerAddrDiv','customerAddrForm','完善收货地址','1000px');
}

function echoFormCallback(data,formId) {
    openForm('customerAddrDiv',formId,'修改收货地址','1000px');
    formAssignment(formId,data.data);
}

function formSubmit(data) {
    submitForm('/customer/addr/aOrU','customerAddrDiv','customerAddrForm',data.field,'POST',reloadcustomerAddrList);
}
function delAddr(data) {
    submitForm('/customer/addr/del','customerAddrDiv','customerAddrForm', {'customerAddrId':data.customerAddrId},'GET',reloadcustomerAddrList)
}
function editAddr(data) {
    echoForm('/customer/addr/getById',{'customerAddrId':data.customerAddrId},'customerAddrForm','GET');
}

