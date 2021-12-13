/**
 * 用户管理
 */
var pageCurr;
var tableIns;
var baseUrl = '/supplier/info/';
$(function () {
    layui.use(['table', 'laydate','util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form,
        tableIns = table.render({
            elem: '#supplierinfoList'
            ,id:'supplierinfoList'
            , url: baseUrl+'getList'
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: 'full-100'
            ,toolbar: '#toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
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
                , {field: 'supplierId', title: '供应商ID', hide: true}
                , {field: 'supplierCode', title: '供应商编码'}
                , {field: 'supplierName', title: '供应商名称'}
                , {field: 'supplierType', title: '供应商类型',templet:function(d){return d.supplierType=='1'?"自营":"平台"}}
                , {field: 'linkMan', title: '供应商联系人'}
                , {field: 'phoneNumber', title: '联系电话'}
                , {field: 'bankName', title: '供应商开户银行名称'}
                , {field: 'bankAccount', title: '银行账号'}
                , {field: 'address', title: '供应商地址'}
                , {field: 'supplierStatus', title: '状态',templet:function(d){return d.supplierStatus=='1'?"启用":"禁止"}}
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
        table.on('tool(supplierinfoTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delAddr(data);
            }else {
                editAddr(data);
            }
        });
        //监听提交
        form.on('submit(supplierinfoSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
    });
});


function reloadsupplierinfoList() {
    loadTable('supplierinfoList',{"customerId":$("#reqcustomerId").val()})
}

function addSupplierinfo() {
    openForm('supplierinfoDiv','supplierinfoForm','添加供应商信息');
}

function echoFormCallback(data,formId) {
    openForm('supplierinfoDiv',formId,'修改供应商信息','1000px');
    formAssignment(formId,data.data);
}

function formSubmit(data) {
    submitForm(baseUrl+'aOrU','supplierinfoDiv','supplierinfoForm',data.field,'POST',reloadsupplierinfoList);
}
function delAddr(data) {
    submitForm(baseUrl+'del','supplierinfoDiv','supplierinfoForm', {'supplierId':data.supplierId},'GET',reloadsupplierinfoList)
}
function editAddr(data) {
    echoForm(baseUrl+'getById',{'supplierId':data.supplierId},'supplierinfoForm','GET');
}

