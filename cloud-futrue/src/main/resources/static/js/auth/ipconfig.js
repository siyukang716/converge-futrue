var pageCurr;
var tableIns;
var control = 'ipconfig'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/sys/ip/config/',
    aOrUUrl = baseUrl + 'aOrU',
    ipOne = baseUrl + 'getIpOne',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '系统ip配置';

$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;


        //监听提交
        form.on('submit(ipconfigSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });

    });
    getData(ipOne, {},getIpOneCall);
});
function getIpOneCall(data, formId) {
    formAssignment("ipconfigForm", data.data);
}

/**
 * formAssignment('uploadfileForm', {"uploadKey": data.basicId})
* */

function formSubmit(data) {
    var data = data.field;
    if (data == null || data == undefined){
        layer.msg("请求参数不能为空", {icon: 2});
    }
    if (data.ipAddress == null || data.ipAddress == undefined || data.ipAddress ==''){
        layer.msg("IP不能为空", {icon: 2});
    }
    // if (isValidIP(data.ipAddress)){
        // data.ipAddress = 'http://'+data.ipAddress
        ajaxPostData(aOrUUrl, data,formSubmitCall);
    // }else {
    //     layer.msg("请检查IP是否正确", {icon: 2});
    // }
}
function isValidIP(ip) {
    // var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\:([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-5]{2}[0-3][0-5])$/;
    return reg.test(ip);
}

function formSubmitCall(data){
    layer.msg(data.message=='' || data.message == null || data.message==undefined?"操作成功":data.message, {icon: 6});
}