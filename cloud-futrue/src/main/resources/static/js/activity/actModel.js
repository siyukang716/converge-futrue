/**
 * 用户管理
 */
var pageCurr;
$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var form = layui.form,

            tableIns = table.render({
                elem: '#actModelList'
                , id: 'actModelList'
                , url: '/actmodel/models'
                , method: 'get' //默认：get请求
                , cellMinWidth: 80
                , height: 'full-100'
                , toolbar: '#toolbarDemo'
                , defaultToolbar: ['filter', 'print', 'exports']
                , page: true,
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
                    , {field: 'id', title: 'ID', width: 180, hide: true}
                    , {field: 'key', title: 'KEY'}
                    , {field: 'name', title: '名称'}
                    , {field: 'version', title: 'Version'}
                    , {field: 'createTime', title: '创建时间'}
                    , {field: 'lastUpdateTime', title: '最后更新时间'}
                    , {fixed: 'right', title: '操作', width: 480, align: 'center', toolbar: '#optBar'}
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


        //监听提交
        form.on('submit(modelSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        //监听工具条
        table.on('tool(actModelTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'edit') {
                location.href = "/service/update?modelId=" + data.id;
            } else if (obj.event === 'down') {
                location.href = "/actmodel/export?modelId=" + data.id;
            } else if (obj.event === 'deploy') {
                deploySubmit(data.id);
                //location.href = "/actmodel//models/deploy?id="+data.id;
            } else if (obj.event === 'startProcess') {
                startProcess(data.key);
                //layer.msg(data.key);
            }
        });

        //头工具栏事件
        table.on('toolbar(cartTable)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id); //获取选中行状态
            switch (obj.event) {
                case 'getCheckData':
                    var data = checkStatus.data;  //获取选中行数据
                    layer.alert('待开发');//JSON.stringify(data)
                    break;
            }
            ;
        });

    });
});


function reloadList() {
    loadTable('actModelList', {});
}

//提交表单
function formSubmit(obj) {
    submitForm("/service/create", 'setactModel', 'actModelForm', $("#actModelForm").serialize(),'POST', reloadList);
}

/**
 * 部署流程
 * @param obj
 */
function deploySubmit(id) {
    getData("/actmodel/models/deploy", {'id': id}, callDeploy);
}

function callDeploy(data) {
    if (data.status == "1000") {
        //location.href = data.data
        layer.msg('部署成功');
    } else {
        layer.msg(data.message);
    }
}

function startProcess(key) {
    getData("/actmodel/startProcess/" + key, {}, callDeploy);
}


function load(obj) {
    layui.use(['table'], function () {
        var loadtable = layui.table;
        //重新加载table
        loadtable.reload('uesrList', {
            where: obj.field
            , page: {
                curr: pageCurr //从当前页码开始
            }
        });
    });

}


//打开model框
function addmodel() {
    openForm('setactModel', 'actModelForm', '添加');
}
