var pageCurr;
var tableIns;
$(function () {
    layui.use(['table', 'laydate','util','upload'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var upload = layui.upload;
        var form = layui.form,
        tableIns = table.render({
            elem: '#sys_logoinfo_configList'
            ,id:'sys_logoinfo_configList'
            , url: '/sys/logo/info/config/getList'
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: 'full-100'
            ,toolbar: '#toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where:{"customerId":$("#reqcustomerId").val()},
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
                , {field: 'sysFonfigId', title: 'ID', width: 180, hide: true}
                , {field: 'title', title: '系统名称'}
                , {field: 'image', title: '系统图标路径'}
                , {field: 'href', title: 'href'}
                , {fixed: 'right', title: '操作', width: 180, align: 'center', toolbar: '#optBar'}
            ]]
            , done: function (res, curr, count) {
                pageCurr = curr;
            }
        });
        //监听工具条
        table.on('tool(sys_logoinfo_configTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delAddr(data);
            }else {
                editAddr(data);
            }
        });
        //监听提交
        form.on('submit(sys_logoinfo_configSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });

        //拖拽上传
        upload.render({
            elem: '#test10'
            ,url: '/sys/logo/info/config/upload' //此处用的是第三方的 http 请求演示，实际使用时改成您自己的上传接口即可。
            ,data:{}
            ,done: function(res){
                layer.msg('上传成功');
                layui.$('#uploadDemoView').removeClass('layui-hide').find('img').attr('src', res.data.url);
                formAssignment('sys_logoinfo_configForm',{"image":res.data.url})
                console.log(res)
            }
        });


    });
});


function reloadsys_logoinfo_configList() {
    loadTable('sys_logoinfo_configList',{});
}
function echoFormCallback(data,formId) {
    openForm('sys_logoinfo_configDiv',formId,'系统配置');
    formAssignment(formId,data.data);
    layui.$('#uploadDemoView').removeClass('layui-hide').find('img').attr('src', data.data.image);
}

function formSubmit(data) {
    submitForm('/sys/logo/info/config/aOrU','sys_logoinfo_configDiv','sys_logoinfo_configForm',data.field,'POST',reloadsys_logoinfo_configList);
}

function editAddr(data) {
    echoForm('/sys/logo/info/config/getById',{'sysFonfigId':data.sysFonfigId},'sys_logoinfo_configForm','GET');
}

