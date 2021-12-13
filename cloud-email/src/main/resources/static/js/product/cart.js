
var pageCurr;
var tableIns;
var control = 'cart'
    fromId = control+'Form',
    divId = control + "Div",
    baseUrl = '/buy/cart/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getCart',
    menuTitle = '购物车';

$(function () {
    layui.use(['table', 'laydate','util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form,
        tableIns = table.render({
            elem: '#cartList'
            ,id:'cartList'
            , url: listUrl
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: 'full-100'
            ,toolbar: '#toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where:{}
            ,title: menuTitle,
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
                {type: 'numbers' }
                ,{type:'checkbox'}
                , {field: 'specsId', title: 'ID', width: 180, hide: true,templet:function (d) {
                        return d.sku.specsId;
                    }}
                , {field: 'productName', title: '商品名称',templet: "<span>{{d.sku.productName}}</span>"}
                , {field: 'skuDepict', title: '选中商品',templet: "<span>{{d.sku.skuDepict}}</span>"}
                , {field: 'productPrice', title: '商品单价',templet: "<span>{{d.sku.productPrice}}</span>"}
                , {field: 'amount', title: '购买数'}
                , {field: 'payMoney', title: '应付',templet:function (d) {
                        return floatMul(d.amount,d.sku.productPrice);
                    } }//, totalRow: '{{ d.TOTAL_NUMS }}元'
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
        table.on('tool(cartTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'placeAnOrder') {
                placeAnOrder(data);
            }else {
                delCart(data);
            }
        });
        //监听提交
        form.on('submit(cartSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });

        //头工具栏事件
        table.on('toolbar(cartTable)', function(obj){
            var checkStatus = table.checkStatus(obj.config.id); //获取选中行状态
            switch(obj.event){
                case 'getCheckData':
                    var data = checkStatus.data;  //获取选中行数据
                    layer.alert('待开发');//JSON.stringify(data)
                    break;
            };
        });



    });

});
function placeAnOrder(data) {
    var obj = {};
    obj.specsId = data.sku.specsId;
    obj.productName = data.sku.productName;
    obj.productCnt = data.amount;
    obj.productPrice = data.sku.productPrice;
    obj.paymentMoney = floatMul(data.amount,data.sku.productPrice);
    obj.orderMoney = floatMul(data.amount,data.sku.productPrice);
    getData("/order/master/placeAnOrderDCart",obj,callBack);
}

function callBack(data) {
    layer.msg(data.message, {icon: 6});
    reloadcartList();
}
function reloadcartList() {
    loadTable('cartList',{})
}
function delCart(data) {
    submitForm('/buy/cart/del',divId,fromId, {'specsId':data.sku.specsId},'GET',reloadcartList)
}

