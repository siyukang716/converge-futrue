var pageCurr;
var tableIns;
var control = 'syscommondictkv'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/system/dict/kv/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '字典管理';

$(function () {
    getData(baseUrl+"/getDictMapById",{'dictTypes':'sex'})
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        tableIns = table.render({
            elem: '#syscommondictkvList'
            , id: 'syscommondictkvList'
            , url: listUrl
            , method: 'get' //默认：get请求
            , cellMinWidth: 180
            , height: 'full-100'
            , toolbar: '#toolbarDemo'
            , defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where: {'dictType':$("#reqdictType").val()},
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
                , {field: 'dictChildId', title: '主键', width: 180, hide: true}
                , {field: 'dictId', title: '字典编码'}
                , {field: 'dictCode', title: '字典编码'}
                , {field: 'dictTag', title: '字典名称'}
                , {field: 'dictSort', title: '排序'}
                , {field: 'dictMemo', title: '备注'}
                , {field: 'dictStatus', title: '状态',templet:"#dictStatusTpl"}
                , {field: 'insertTime', title: '插入时间'}
                , {field: 'updateTime', title: '修改时间'}
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
        table.on('tool(syscommondictkvTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(syscommondictkvSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(syscommondictkvTable)', function (obj) {
            if (obj.event === 'addsyscommondictkv') {
                //完善用户信息
                addsyscommondictkv();
            }
        });

        //监听在职操作
        form.on('switch(isdictStatusTpl)', function (obj) {
            //console.log(this.value + ' ' + this.name + '：'+ obj.elem.checked, obj.othis);
            ajaxPostData(baseUrl+'updateStatus',{'dictStatus':this.name,'dictChildId':this.value},changeDictCall)
            debugger;
        });
    });
});

function reloadsyscommondictkvList() {
    loadTable('syscommondictkvList', {'dictType':$("#reqdictType").val()})
}

function addsyscommondictkv() {
    formAssignment(fromId, {"dictChildId": '','dictId':$("#reqdictType").val()})
    openForm('syscommondictkvDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('syscommondictkvDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloadsyscommondictkvList);
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'dictChildId': data.dictChildId}, 'GET', reloadsyscommondictkvList)
}

function edit(data) {
    if (data.dictStatus == 1){
        layer.msg('发布后不可修改', {icon: 6});
        return;
    }
    if (data.isDel == 0){
        layer.msg('已删除不可修改', {icon: 6});
        return;
    }
    echoForm(getUrl, {'dictChildId': data.dictChildId}, fromId, 'GET');
}
function changeDictCall(data) {
    if (data.status == '1000'){
        reloadsyscommondictkvList();
      layer.msg(data.message=='' || data.message == null || data.message==undefined?"操作成功":data.message, {icon: 6});

    }else {
        layer.msg(data.message, {icon: 5});
    }
}


