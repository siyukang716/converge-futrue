
var pageCurr;
var tableIns;
var control = 'specsManager'
    fromId = control+'Form',
    divId = control + "Div",
    baseUrl = '/product/specs/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '商品sku';

$(function () {
    getData("/prod/sku/attr/key/getKeyandVal",{"productId":$("#reqproductId").val()},echoSku);
    layui.use(['table', 'laydate','util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form,
        tableIns = table.render({
            elem: '#specsManagerList'
            ,id:'specsManagerList'
            , url: listUrl
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: 'full-100'
            ,toolbar: '#toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where:{"productId":$("#reqproductId").val()},
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
                , {field: 'specsId', title: 'ID', width: 180, hide: true}
                , {field: 'productId', title: 'ID', width: 180, hide: true}
                , {field: 'skuDepict', title: 'sku 描述'}
                , {field: 'productPrice', title: '商品价格'}
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
        table.on('tool(specsManagerTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delAddr(data);
            }else {
                editAddr(data);
            }
        });
        //监听提交
        form.on('submit(specsManagerSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
    });
});


function reloadspecsManagerList() {
    loadTable('specsManagerList',{"customerId":$("#reqcustomerId").val()})
}

function addSpecsManager() {
    formAssignment(fromId,{"specsId":'',"productId":$("#reqproductId").val()})
    //echoForm(,{},fromId,'GET')
    openForm('specsManagerDiv',fromId,'添加'+menuTitle);
}

function echoFormCallback(data,formId) {
    openForm('specsManagerDiv',formId,'修改'+menuTitle);
    formAssignment(formId,data.data);
}

function formSubmit(data) {
    let obj = {};
    let str = '';
    for(let key  in data.field){
        if (key == 'productId' || key == 'productPrice' || key == 'skuDepict' || key == 'specsId'){
            obj[key] = data.field[key];
        }else {
            str += key+'@&@' +data.field[key] + '@@@@';
        }
    }
    str = str.substring(0,str.length-4);
    obj['sku'] = str;
    submitForm(aOrUUrl,divId,fromId,obj,'POST',reloadspecsManagerList);
}
function delAddr(data) {
    submitForm(delUrl,divId,fromId, {'specsId':data.specsId},'GET',reloadspecsManagerList)
}
function editAddr(data) {
    echoForm(getUrl,{'specsId':data.specsId},fromId,'GET');
}


function echoSku(data){
    $("#skuEach").empty();
    let str = '';
    for(let key  in data.data){
        let obj = data.data[key];
        str +=
        '<div class="layui-form-item">'+
        '<label class="layui-form-label">'+key+'</label>'+
        '<div class="layui-input-inline">';
        if (obj[0].isselect == 0){
            str += ' <input placeholder="" type="text" name="'+key+'" lay-verify="required|number" autoComplete="off"'+
                'class="layui-input">';
        }else {
            str += '<select name="'+key+'" lay-verify="required" lay-search="">';
            obj.forEach(function(element) {
                str += '<option value="'+element.valname+'">'+element.valname+'</option>';
            });
            str += '</select>';
        }
        str += ' </div>'+
        '<div className="layui-form-mid layui-word-aux"></div>'+
        '</div>';
    }
    $("#skuEach").append(str);

}

