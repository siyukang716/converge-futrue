var pageCurr;
var tableIns;
var control = 'uploadfileconfig'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/system/config/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '文件上传路径配置';

$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form,
            tableIns = table.render({
                elem: '#uploadfileconfigList'
                , id: 'uploadfileconfigList'
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
                    , {field: 'configId', title: '', width: 180, hide: true}
                    , {field: 'businessType', title: '上传业务key'}
                    , {field: 'businessName', title: '业务描述'}
                    , {field: 'ip', title: 'ip', hide: true}
                    , {field: 'port', title: '端口', hide: true}
                    , {field: 'windowsCall', title: '访问路径'}
                    , {field: 'windowsPath', title: 'window上传路径'}
                    , {field: 'linuxCall', title: '访问路径'}
                    , {field: 'linuxPath', title: 'linux上传路径'}
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
        table.on('tool(uploadfileconfigTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(uploadfileconfigSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(uploadfileconfigTable)', function (obj) {
            if (obj.event === 'adduploadfileconfig') {
                //完善用户信息
                adduploadfileconfig();
            }
        });
    });
});

function reloaduploadfileconfigList() {
    loadTable('uploadfileconfigList', {})
}

function adduploadfileconfig() {
    formAssignment(fromId, {"configId": ''})
    //echoForm(,{},fromId,'GET')
    openForm('uploadfileconfigDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('uploadfileconfigDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    var field = data.field;
    var windowsCall = field.windowsCall;
    if(new RegExp("^/upfile/").test(windowsCall)) {
        submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloaduploadfileconfigList);
    }else {
        layer.msg('文件路径必须是” /upfile/ “ 开头!!!!', {icon: 5});
    }
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'configId': data.configId}, 'GET', reloaduploadfileconfigList)
}

function edit(data) {
    echoForm(getUrl, {'configId': data.configId}, fromId, 'GET');
}

