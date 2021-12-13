
var pageCurr;
var tableIns;
var control = 'gentablecolumn'
fromId = control+'Form',
    divId = control + "Div",
    baseUrl = '/gen/Table/Column/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '物流公司信息';

$(function () {
    layui.use(['table', 'laydate','util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form,
            tableIns = table.render({
                elem: '#gentablecolumnList'
                ,id:'gentablecolumnList'
                , url: listUrl
                , method: 'get' //默认：get请求
                , cellMinWidth: 180
                ,height: 'full-100'
                ,toolbar: '#toolbarDemo'
                ,defaultToolbar: ['filter', 'print', 'exports']
                , page: true,
                where:{"tableId":$("#reqtableId").val()},
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
                    , {field: 'columnId', title: '主键ID', width: 180, hide: true}
                    , {field: 'tableId', title: '归属表编号'}
                    , {field: 'columnName', title: '列名称'}
                    , {field: 'columnComment', title: '列描述'}
                    , {field: 'columnType', title: '列类型'}
                    , {field: 'javaType', title: 'JAVA类型'}
                    , {field: 'javaField', title: 'JAVA字段名'}
                    , {field: 'isPk', title: '是否主键'}
                    , {field: 'isIncrement', title: '是否自增'}
                    , {field: 'isRequired', title: '是否必填'}
                    , {field: 'isInsert', title: '是否为插入字段'}
                    , {field: 'isEdit', title: '是否编辑字段'}
                    , {field: 'isList', title: '是否列表字段'}
                    , {field: 'isQuery', title: '是否查询字段'}
                    , {field: 'queryType', title: '查询方式'}
                    , {field: 'htmlType', title: '显示类型'}
                    , {field: 'dictType', title: '字典类型'}
                    , {field: 'sort', title: '排序'}
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
        table.on('tool(gentablecolumnTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delAddr(data);
            }else {
                editAddr(data);
            }
        });
        //监听提交
        form.on('submit(gentablecolumnSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        //监听提交
        form.on('submit(genTableSubmit)', function (data) {
            // TODO 校验
            //formSubmit(data);
            submitForm("/gen/Table/aOrU",'','',data.field,'POST');
            return false;
        });
        //回显表单
        getData("/gen/Table/getById",{"tableId":$("#reqtableId").val()},genTableCall)

    });
});
function genTableCall(data) {
    formAssignment('genTableForm',data.data);
}







function reloadgentablecolumnList() {
    loadTable('gentablecolumnList',{});
}

function echoFormCallback(data,formId) {
    openForm('gentablecolumnDiv',formId,'修改'+menuTitle);
    formAssignment(formId,data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl,divId,fromId,data.field,'POST',reloadgentablecolumnList);
}
function editAddr(data) {
    echoForm(getUrl,{'shipId':data.shipId},fromId,'GET');
}

