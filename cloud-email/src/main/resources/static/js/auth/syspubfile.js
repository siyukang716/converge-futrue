var pageCurr;
var tableIns;
var control = 'syspubfile'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/sys/pub/file/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '公共文件';

$(function () {
    layui.use(['table', 'laydate', 'util','upload'], function () {
        var table = layui.table;
        var upload = layui.upload;
        var laydate = layui.laydate;
        var form = layui.form;
        tableIns = table.render({
            elem: '#syspubfileList'
            , id: 'syspubfileList'
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
                , {field: 'upfileId', title: '文件编号', width: 180, hide: true}
                , {field: 'upfileType', title: '文件类型'}
                , {field: 'upfileName', title: '文件名称'}
                , {field: 'upfileAddress', title: '文件地址'}
                , {field: 'businessId', title: '业务编号'}
                , {field: 'businessName', title: '业务名称'}
                , {field: 'tablePrimarykey', title: '业务关联id'}
                , {field: 'businessTablename', title: '业务表名称'}
                , {field: 'businessFileType', title: '业务文件类型'}
                , {field: 'insertTime', title: '插入时间'}
                , {field: 'updateTime', title: '修改时间'}
                , {field: 'isDel', title: '删除标识'}
                //, {fixed: 'right', title: '操作', width: 180, align: 'center', toolbar: '#optBar'}
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
        table.on('tool(syspubfileTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(syspubfileSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(syspubfileTable)', function (obj) {
            if (obj.event === 'addsyspubfile') {
                addsyspubfile();
            }
        });


        upload.render({
            elem: '#pubfile'
            ,url: '/sys/pub/file/aOrU' //此处配置你自己的上传接口即可
            ,auto: true
            ,data : {"tablePrimarykey":"contractor",'businessName':'测试上传','businessTablename':'test_table','businessFileType':'身份证' }
            ,done: function(res){
                layer.msg('上传成功');
                console.log(res)
            }
        });
    });
});

function reloadsyspubfileList() {
    loadTable('syspubfileList', {})
}

function addsyspubfile() {
    formAssignment(fromId, {"upfileId": ''})
    //echoForm(,{},fromId,'GET')
    openForm('syspubfileDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('syspubfileDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloadsyspubfileList);
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'upfileId': data.upfileId}, 'GET', reloadsyspubfileList)
}

function edit(data) {
    echoForm(getUrl, {'upfileId': data.upfileId}, fromId, 'GET');
}

