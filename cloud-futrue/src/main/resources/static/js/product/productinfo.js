
var pageCurr;
var tableIns;
var control = 'productinfo'
    fromId = control+'Form',
    divId = control + "Div",
    baseUrl = '/product/info/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '商品信息';

$(function () {
    //初始化品牌
    getData('/brand/info/linkage',{},selectEcho,'brandId','brand_id','brand_name');
    //初始化商品分类
    getData('/product/category/linkage',{'categoryId':0},selectEcho,'oneCategoryId','categoryId','categoryName');
    getData('/supplier/info/linkage',{},selectEcho,'supplierId','supplierId','supplierName');
    layui.use(['table', 'laydate','util','colorpicker'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
         var   colorpicker = layui.colorpicker,
        tableIns = table.render({
            elem: '#productinfoList'
            ,id:'productinfoList'
            , url: listUrl
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,toolbar: '#toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where:{},
            height: 'full-100',
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
                , {field: 'productId', title: '商品ID', width: 180, hide: true}
                , {field: 'productCore', title: '商品编码', minWidth: 180}
                , {field: 'productName', title: '商品名称', minWidth: 180}
                , {field: 'barCode', title: '国条码', minWidth: 180}
                , {field: 'brandId', title: '品牌', minWidth: 180,hide: true}
                , {field: 'brandName', title: '品牌', minWidth: 180}
                , {field: 'oneCategoryId', title: '一级分类ID', minWidth: 180,hide: true}
                , {field: 'oneCategoryName', title: '一级分类', minWidth: 180}
                , {field: 'twoCategoryId', title: '二级分类ID', minWidth: 180,hide: true}
                , {field: 'twoCategoryName', title: '二级分类', minWidth: 180}
                , {field: 'threeCategoryId', title: '三级分类ID', minWidth: 180,hide: true}
                , {field: 'threeCategoryName', title: '三级分类', minWidth: 180}
                , {field: 'supplierId', title: '商品的供应商ID', minWidth: 180,hide: true}
                , {field: 'supplierName', title: '商品的供应商', minWidth: 180}
                , {field: 'price', title: '商品销售价格', minWidth: 180}
                , {field: 'averageCost', title: '商品加权平均成本', minWidth: 180}
                , {field: 'publishStatus', title: '上下架状态', minWidth: 180,templet:function(d){return d.publishStatus=='1'?"是":"否"}}
                , {field: 'auditStatus', title: '审核状态', minWidth: 180,templet:function(d){return d.auditStatus=='1'?"已审核":"未审核"}}
                , {field: 'weight', title: '商品重量', minWidth: 180}
                , {field: 'length', title: '商品长度', minWidth: 180}
                , {field: 'height', title: '商品高度', minWidth: 180}
                , {field: 'width', title: '商品宽度', minWidth: 180}
                , {field: 'colorType', title: '商品颜色', minWidth: 180}
                , {field: 'productionDate', title: '生产日期', minWidth: 180
                    ,templet: "<div>{{layui.util.toDateString(d.productionDate, 'yyyy-MM-dd')}}</div>"}
                , {field: 'shelfLife', title: '商品有效期', minWidth: 180
                    ,templet: "<div>{{layui.util.toDateString(d.productionDate, 'yyyy-MM-dd')}}</div>"}
                , {field: 'descript', title: '商品描述', minWidth: 180}
                , {field: 'indate', title: '商品录入时间', minWidth: 180
                    ,templet: "<div>{{layui.util.toDateString(d.productionDate, 'yyyy-MM-dd')}}</div>"}
                , {fixed: 'right', title: '操作', width: 380, align: 'center', toolbar: '#optBar'}
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
                res.list.forEach(function (item,index) {
                    $(".layui-table-body tbody tr[data-index='"+index+"'] td[data-field='colorType']").css({'background-color': item.colorType });

                });
            }
        });
        //监听工具条
        table.on('tool(productinfoTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delAddr(data);
            }else if (obj.event === 'definetemplate') {

                definetemplate(data);
            }else if (obj.event === 'looktemplate') {

                looktemplate(data);
            }else {
                editAddr(data);
            }
        });
        //监听提交
        form.on('submit(productinfoSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        form.on('submit(skutemplateSubmit)', function (data) {
            skutemplateSubmit(data);
            return false;
        });
        laydate.render({
            elem: '#productionDate'
        });
        laydate.render({
            elem: '#shelfLife'
        });
        laydate.render({
            elem: '#indate'
        });
        //表单赋值
        colorpicker.render({
            elem: '#color-form'
            ,color: '#0b4876'
            ,done: function(color){
                $('#color-form-input').val(color);
            }
        });
        form.on('select(changeCategory)', function (data) {
            // TODO 校验
            changeCategory(data);
            return false;
        });

    });
});


function reloadproductinfoList() {
    loadTable('productinfoList',{})
}

function addProductinfo() {
    formAssignment(fromId,{'productId':''});
    openForm('productinfoDiv',fromId,'添加'+menuTitle);
}

function echoFormCallback(data,formId) {
    openForm('productinfoDiv',formId,'修改'+menuTitle);
    formAssignment(formId,data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl,divId,fromId,data.field,'POST',reloadproductinfoList);
}
function delAddr(data) {
    submitForm(delUrl,divId,fromId, {'productId':data.productId},'GET',reloadproductinfoList);
}
function editAddr(data) {
    echoForm(getUrl,{'productId':data.productId},fromId,'GET');
}

function changeCategory(data) {
    getData('/product/category/linkage',{'categoryId':data.value},selectEcho,$(data.elem).attr('child'),'categoryId','categoryName');
}


function definetemplate(data) {
    getData('/prod/sku/attr/key/getTemplate',{},callTemplate,data.productId);
}
function callTemplate(data,productId){
    $('#skuCheckbox').empty();
    var str = '';
    data.data.forEach(function(element) {
        str += '<input type="checkbox" name="makId'+element.makId+'" title="'+element.attributeName+'" value="'+element.makId+'" lay-skin="primary">';
    });
    $('#skuCheckbox').append(str);
    openForm('skutemplateDiv','skutemplateForm','修改');
    formAssignment('skutemplateForm',{'productId':productId});
    getData('/prod/sku/attr/key/getProByPid',{"productId":productId},echoTemplate,data.productId);

}
function echoTemplate(data) {
    formAssignment('skutemplateForm',{"makId1":"1"});
    layui.form.render();
}


function skutemplateSubmit(data){
    var makid = '';
    for(let key  in data.field){
        if (key.indexOf('makId') >= 0){
            makid += data.field[key]+',';
        }
    }
    makid = makid.substring(0,makid.length-1);
    submitForm('/product/Link/Sku/aOrU',divId,fromId, {'productId':data.field.productId,"keys":makid},'GET')
}
function looktemplate(data) {
    getData('/prod/sku/attr/key/getProByPid',{"productId":data.productId},callLookTemplate,data.productId);
}
function callLookTemplate(data) {
    var str = '';
    data.data.forEach(function(element) {
        str += element.attributeName+';';
    });
    layer.msg(str, {icon: 6});
}
function buySopping() {
    top.location = '/product/info/list';
}