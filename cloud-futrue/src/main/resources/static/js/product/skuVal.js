
var pageCurr;
var tableIns;
var control = 'skuVal'
    fromId = control+'Form',
    divId = control + "Div",
    baseUrl = '/prod/sku/attr/val/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = 'sku';

$(function () {
    layui.use(['table', 'laydate','util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form,
        tableIns = table.render({
            elem: '#skuValList'
            ,id:'skuValList'
            , url: listUrl
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: 'full-100'
            ,toolbar: '#toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where:{"makId":$("#reqmakId").val()},
            title: menuTitle,
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
                , {field: 'mavId', title: 'ID', width: 180, hide: true}
                , {field: 'makId', title: 'ID', width: 180, hide: true}
                , {field: 'attributeValue', title: '属性选项值'}
                , {field: 'valueSort', title: '排序'}
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
        table.on('tool(skuValTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delAddr(data);
            }else {
                editAddr(data);
            }
        });
        //监听提交
        form.on('submit(skuValSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
    });
});


function reloadskuValList() {
    loadTable('skuValList',{"mavId":'',"makId":$("#reqmakId").val()})
}

function addSkuVal() {
    formAssignment(fromId,{"makId":$("#r eqmakId").val(),"mavId":''})
    //echoForm(,{},fromId,'GET')
    openForm('skuValDiv',fromId,'添加'+menuTitle);
}

function echoFormCallback(data,formId) {
    openForm('skuValDiv',formId,'修改'+menuTitle);
    formAssignment(formId,data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl,divId,fromId,data.field,'POST',reloadskuValList);
}
function delAddr(data) {
    submitForm(delUrl,divId,fromId, {'mavId':data.mavId},'GET',reloadskuValList)
}
function editAddr(data) {
    echoForm(getUrl,{'mavId':data.mavId},fromId,'GET');
}

