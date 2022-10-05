var pageCurr;
var tableIns;
var control = 'sysdept'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/system/dept/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '部门';
var renderTable ;
$(function () {
    layui.use(['table', 'laydate', 'util', 'treetable'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var treetable = layui.treetable;
        var form = layui.form;
        renderTable = function () {
            treetable.render({
                treeColIndex: 2,
                treeSpid: '-1',
                treeIdName: 'deptId',
                treePidName: 'parentDeptId',
                elem: '#sysdeptList'
                , id: 'sysdeptList'
                , url: listUrl
                , method: 'get' //默认：get请求
                , cellMinWidth: 180
                , height: 'full-100'
                , toolbar: '#toolbarDemo'
                , defaultToolbar: ['filter', 'print', 'exports']
                , page: false,
                where: {},
                cols: [[
                    {type: 'numbers'}
                    , {field: 'deptId', title: '部门id', width: 180, hide: true}
                    , {field: 'deptName', title: '部门名称'}
                    , {field: 'parentDeptId', title: '上级部门', hide: true}
                    , {field: 'coding', title: '部门编码'}
                    , {field: 'tel', title: '电话'}
                    , {field: 'delflag', title: '删除标识', hide: true}
                    , {field: 'insertTime', title: '新增时间'}
                    , {field: 'updateTime', title: '修改时间'}
                    , {fixed: 'right', title: '操作', width: 380, align: 'center', toolbar: '#optBar'}
                ]]
                , done: function (res, curr, count) {

                }
            });
        }
        renderTable();
        //监听工具条
        table.on('tool(sysdeptTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            } else if (obj.event === 'addsysdept') {
                addsysdept(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(sysdeptSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(sysdeptTable)', function (obj) {
            if (obj.event === 'addsysdept') {
                //完善用户信息
                addsysdept();
            }
        });
    });

});


function reloadsysdeptList() {
    //loadTable('sysdeptList', {});
    renderTable();
    //parent.$(".layui-tab-item.layui-show").find("iframe")[0].contentWindow.location.reload();
}

function addsysdept(data) {
    formAssignment(fromId, {"deptId": '', 'parentDeptId': data.deptId, 'deptName': '', 'tel': ''})
    //echoForm(,{},fromId,'GET')
    openForm('sysdeptDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('sysdeptDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloadsysdeptList);
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'deptId': data.deptId}, 'GET', reloadsysdeptList)
}

function edit(data) {
    echoForm(getUrl, {'deptId': data.deptId}, fromId, 'GET');
}

