
var pageCurr;
var tableIns;
var control = 'brandinfo'
    fromId = control+'Form',
    divId = control + "Div",
    baseUrl = '/brand/info/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '品牌信息';

$(function () {
    layui.use(['table', 'laydate','util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form,
        tableIns = table.render({
            elem: '#brandinfoList'
            ,id:'brandinfoList'
            , url: listUrl
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: 'full-100'
            ,toolbar: '#toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where:{},
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
                , {field: 'brandId', title: 'ID', width: 180, hide: true}
                , {field: 'brandName', title: '品牌名称'}
                , {field: 'telephone', title: '联系电话'}
                , {field: 'brandWeb', title: '品牌网络'}
                , {field: 'brandLogo', title: '品牌logo URL'}
                , {field: 'brandDesc', title: '品牌描述'}
                , {field: 'brandOrder', title: '排序'}
                , {field: 'brandStatus', title: '品牌状态', minWidth: 80, templet: '#brandStatusTpl'}
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
        form.on('switch(isbrandStatusTpl)', function (obj) {
            submitForm(baseUrl+'statusSwitch',divId,fromId, {'brandId':this.name,'brandStatus':this.value},'GET',reloadbrandinfoList);
        });
        //监听工具条
        table.on('tool(brandinfoTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delAddr(data);
            }else {
                editAddr(data);
            }
        });
        //监听提交
        form.on('submit(brandinfoSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
    });

});


function reloadbrandinfoList() {
    loadTable('brandinfoList',{})
}

function addBrandinfo() {
    formAssignment(fromId,{'brandId':''});
    openForm('brandinfoDiv',fromId,'添加'+menuTitle);
}

function echoFormCallback(data,formId) {
    openForm('brandinfoDiv',formId,'修改'+menuTitle);
    formAssignment(formId,data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl,divId,fromId,data.field,'POST',reloadbrandinfoList);
}
function delAddr(data) {
    submitForm(delUrl,divId,fromId, {'brandId':data.brandId},'GET',reloadbrandinfoList)
}
function editAddr(data) {
    echoForm(getUrl,{'brandId':data.brandId},fromId,'GET');
}
