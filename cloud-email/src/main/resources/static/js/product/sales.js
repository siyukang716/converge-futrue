
var pageCurr;
var tableIns;
var fromId = 'sales',
    baseUrl = '/prod/sku/attr/key/',
    menuTitle = '销售统计';

$(function () {
    layui.use(['table', 'laydate','util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form,
        tableIns = table.render({
            elem: '#salesList'
            ,id:'salesList'
            , url: '/product/info/getsales'
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: 'full-100'
            ,toolbar: '#toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where:{},
            title: menuTitle
            ,totalRow: true
            ,request: {
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
                , {field: 'makId', title: 'ID', width: 180, hide: true}
                , {field: 'product_name', title: '商品'}
                , {field: 'sku_depict', title: '描述'}
                , {field: 'product_price', title: '单价'}
                , {field: 'order_money', title: '应付', totalRow: '{{ d.TOTAL_NUMS }}元' }
                , {field: 'payment_money', title: '实付', totalRow: '{{ d.TOTAL_NUMS }}元'}
                , {field: 'username', title: '销售人'}
                , {field: 'create_time', title: '销售时间',templet: "<div>{{layui.util.toDateString(d.create_time, 'yyyy-MM-dd')}}</div>"}
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
    });
});
