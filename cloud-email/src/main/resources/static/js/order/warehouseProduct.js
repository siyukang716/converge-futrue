
var pageCurr;
var tableIns;
var control = 'warehouseProduct'
    fromId = control+'Form',
    divId = control + "Div",
    baseUrl = '/warehouse/product/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '库存信息';

$(function () {
    getData('/warehouse/info/linkage',{},selectEcho,'wid','warehouseId','warehoustName');
    layui.use(['table', 'laydate','util','slider'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var slider = layui.slider;
        var form = layui.form,
        tableIns = table.render({
            elem: '#warehouseProductList'
            ,id:'warehouseProductList'
            , url: listUrl
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: 'full-100'
            ,toolbar: '#toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where:{'productId':$("#reqproductId").val()},
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
                , {field: 'wpId', title: '主键ID', width: 180, hide: true}
                , {field: 'productId', title: '商品ID', width: 180, hide: true}
                , {field: 'wid', title: '仓库ID', hide: true}
                , {field: 'currentCnt', title: '当前商品数量'}
                , {field: 'lockCnt', title: '当前占用数据'}
                , {field: 'inTransitCnt', title: '在途数据'}
                , {field: 'averageCost', title: '移动加权成本'}
                , {field: 'modifiedTime', title: '最后修改时间'}
                , {fixed: 'right', title: '操作', width: 280, align: 'center', toolbar: '#optBar'}
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
        table.on('tool(warehouseProductTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delAddr(data);
            }else if (obj.event === 'edit') {
                editAddr(data);
            }else if (obj.event === 'addInventory') {
                addInventory(data);
            }else if (obj.event === 'reduceInventory') {
                reduceInventory(data);
            }
        });
        //监听提交
        form.on('submit(warehouseProductSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        form.on('submit(inventoryRecordSubmit)', function (data) {
            // TODO 校验
            inventory(data);
            return false;
        });
    });
});


function reloadwarehouseProductList() {
    loadTable('warehouseProductList',{});
}

function addWarehouseProduct() {
    formAssignment(fromId,{"wpId":'','productId':$("#reqproductId").val()});
    openForm('warehouseProductDiv',fromId,'添加'+menuTitle);
    $("#currentCnt").prop("readonly",false);
}

function echoFormCallback(data,formId) {
    openForm('warehouseProductDiv',formId,'修改'+menuTitle);
    formAssignment(formId,data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl,divId,fromId,data.field,'POST',reloadwarehouseProductList);
}
function editAddr(data) {
    echoForm(getUrl,{'wpId':data.wpId},fromId,'GET');
    $("#currentCnt").prop("readonly",true);
}

function delAddr(data) {
    submitForm(delUrl,divId,fromId, {'wpId':data.wpId},'GET',reloadwarehouseProductList);
}


function addInventory(data){
    openForm('inventoryRecordDiv','inventoryRecordForm','加库存');
    formAssignment('inventoryRecordForm',{
        "wpId":data.wpId,
        'productId':$("#reqproductId").val(),
        'businessType': 2,
        'inventoryOperations' :1,
        'amount':1
    });
    sliderRender();
}

function reduceInventory(data){
    openForm('inventoryRecordDiv','inventoryRecordForm','减库存');
    formAssignment('inventoryRecordForm',{
        "wpId":data.wpId,
        'productId':$("#reqproductId").val(),
        'businessType': 2,
        'inventoryOperations' :2,
        'amount':1
    });
    sliderRender();

}

function inventory(data){
    submitForm('/warehouse/product/aOrRInventory','inventoryRecordDiv',fromId, data.field,'GET',reloadwarehouseProductList)
}

function sliderRender(){
    //开启输入框
    layui.slider.render({
        elem: '#slideTest8'
        , input: true //输入框
        , min: 1
        ,max: 10000
        , value: 1
        , change: function (value) {
            $("#amount").val(value);
        }
    });
}
