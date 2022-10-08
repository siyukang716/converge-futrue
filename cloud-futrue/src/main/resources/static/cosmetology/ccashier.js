var pageCurr;
var tableIns;
var control = 'ccashier'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/c/cashier/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    getProdUrl = '/c/product/getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '美容院-收银管理';

$(function () {
    getData('/c/product/getProdList',{},selectEcho,'prodId','prodId','prodName');
    getData('/c/user/getUserList',{},selectEcho,'userId','userId','username');
    getData('/user/getUserList',{},selectEcho,'staffId','id','username');

    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        tableIns = table.render({
            elem: '#ccashierList'
            , id: 'ccashierList'
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
                , {field: 'cashierId', title: '收银编号', width: 180, hide: true}
                , {field: 'username', title: '顾客姓名'}
                , {field: 'userId', title: '顾客ID',hide: true}
                , {field: 'prodId', title: '商品ID',hide: true}
                , {field: 'prodName', title: '商品名称'}
                , {field: 'staffId', title: '收益ID',hide: true}
                , {field: 'staffName', title: '提成人'}
                , {field: 'royalty', title: '提成金'}
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
        table.on('tool(ccashierTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(ccashierSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(ccashierTable)', function (obj) {
            if (obj.event === 'addccashier') {
                addccashier();
            }
        });
        var prodId = '';
        form.on('select(trainProdChange)', function (data) {
            if (prodId == data.value){
                return ;
            }
            prodId = data.value;
            getData(getProdUrl,{'prodId':prodId},attrProdInfo);
        });

    });
});

function attrProdInfo(data) {
    if (data.status != "1000") {
        layer.msg(data.message);
    }
    $('#prodAmt').val(data.data.prodAmt);
}
function reloadccashierList() {
    loadTable('ccashierList', {})
}

function addccashier() {
    formAssignment(fromId, {"cashierId": ''})
    //echoForm(,{},fromId,'GET')
    openForm('ccashierDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('ccashierDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloadccashierList);
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'cashierId': data.cashierId}, 'GET', reloadccashierList)
}

function edit(data) {
    echoForm(getUrl, {'cashierId': data.cashierId}, fromId, 'GET');
}

