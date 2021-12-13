
var pageCurr;
var tableIns;
var control = 'genTable'
    fromId = control+'Form',
    divId = control + "Div",
    baseUrl = '/gen/Table/',
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
            elem: '#genTableList'
            ,id:'genTableList'
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
                ,{type: 'checkbox'}
                , {field: 'tableId', title: '编号', width: 180, hide: true}
                , {field: 'tableName', title: '表名称'}
                , {field: 'tableComment', title: '表描述'}
                //, {field: 'subTableName', title: '关联子表的表名'}
               // , {field: 'subTableFkName', title: '子表关联的外键名'}
                //, {field: 'className', title: '实体类名称'}
                //, {field: 'tplCategory', title: '使用的模板（crud单表操作 tree树表操作 sub主子表操作）'}
                //, {field: 'packageName', title: '生成包路径'}
               // , {field: 'moduleName', title: '生成模块名'}
                , {field: 'businessName', title: '生成业务名'}
                , {field: 'functionName', title: '生成功能名'}
                , {field: 'functionAuthor', title: '生成功能作者'}
                //, {field: 'genType', title: '生成代码方式（0zip压缩包 1自定义路径）'}
                //, {field: 'genPath', title: '生成路径（不填默认项目路径）'}
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





        table.render({
            elem: '#genNotTableList'
            ,id:'genNotTableList'
            , url: baseUrl+"db/list"
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: 'full-100'
            ,toolbar: '#notTablebar'
            //,defaultToolbar: ['filter', 'print', 'exports']
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
                ,{type: 'checkbox'}
                , {field: 'tableName', title: '表名称'}
                , {field: 'tableComment', title: '表描述'}
            ]]
            , done: function (res, curr, count) {
                pageCurr = curr;
            }
        });






        //监听工具条
        table.on('tool(genTableTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delAddr(data);
            }else if (obj.event === 'preview'){
                preview(data);
            }else {
                editAddr(data);
            }
        });
        //监听提交
        form.on('submit(genTableSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });

        /**
         * toolbar监听事件
         */
        table.on('toolbar(genTableTable)', function (obj) {

            if (obj.event === 'importTable') {
                importTable();
            }else if (obj.event === 'batchGenCode'){
                var checkStatus = table.checkStatus(obj.config.id);
                var data = checkStatus.data;
                batchGenCode(data);
            }
        });
        table.on('toolbar(genNotTableTable)', function (obj) {
                   console.info(obj.event +'《----监听');

            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            //batchGenCode(data);
            let arr = [];
            data.forEach(function (d) {
                arr.push(d.tableName);
            });
            getData(baseUrl + 'importTableSave',{'tableNames' : arr.join(",")},callimportTableSave)
        });




    });
});

function callimportTableSave(data) {
    reloadgenTableList();
    layer.closeAll();
    $("#genNotTableDiv").hide();
    layer.msg(data.message, {icon: 6});
}

/**
 * 导入表信息
 */
function importTable() {
    openForm('genNotTableDiv','','导入表结构');
    loadTable("genNotTableList",{});

}

/**
 * 下载生成文件
 * @param data
 */
function batchGenCode(data) {
    let arr = [];
    data.forEach(function (d) {
        arr.push(d.tableId);
    });
    location.href =  "/gen/Table/batchGenCode?tables=" + arr.join(",");
}

/**
 * 预览回调
 */
function callpreview(data) {
    $("#entity").empty();
    $("#controller").empty();
    $("#service").empty();
    $("#mapper").empty();
    $("#xml").empty();
    $("#listjs").empty();
    $("#listhtml").empty();
    for(let key  in data.data){
        if (key == "vm/entity.java.vm"){
            $("#entity").text(data.data[key]);
        }else if (key == "vm/controller.java.vm"){
            $("#controller").text(data.data[key]);
        }else if (key == "vm/mapper.java.vm"){
            $("#service").text(data.data[key]);
        }else if (key == "vm/mapper.xml.vm"){
            $("#mapper").text(data.data[key]);
        }else if (key == "vm/service.java.vm"){
            $("#xml").text(data.data[key]);
        }else if (key == "vm/list.html.vm"){
            $("#listhtml").text(data.data[key]);
        }else if (key == "vm/form.html.vm"){
            $("#formhtml").text(data.data[key]);
        }else if (key == "vm/list.js.vm"){
            $("#listjs").text(data.data[key]);
        }
    }
    openForm('previewCodeDiv','','代码预览','100%');
}

/**
 * 预览
 */
function preview(data) {
    getData(baseUrl+'preview/'+data.tableId,{},callpreview)
}


function reloadgenTableList() {
    loadTable('genTableList',{})
}

function delAddr(data) {
    submitForm(delUrl,divId,fromId, {'tableId':data.tableId},'GET',reloadgenTableList)
}