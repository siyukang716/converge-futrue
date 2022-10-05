var pageCurr;
var tableIns;
var control = 'schedulingrules'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/roster/scheduling/rules/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '排班规则';

$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        tableIns = table.render({
            elem: '#schedulingrulesList'
            , id: 'schedulingrulesList'
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
                , {field: 'arrangeRuleId', title: '排班规则id', width: 180, hide: true}
                , {field: 'arrangeRuleName', title: '规则名称'}
                , {field: 'arrangeRuleType', title: '排班类型'}
                , {field: 'isRoster', title: '是否排班', templet: function (d) {
                        return d.isRoster == '1' ? "不排班" : "排班"
                    }}
                , {field: 'workStartTime', title: '上班时间'}
                , {field: 'workingHours', title: '工时(小时)'}
                //, {field: 'workEndTime', title: '下班时间'}
                , {field: 'postStatus', title: '发布状态', templet: function (d) {
                        return d.postStatus != '1' ? "发布" : "未发布"
                    }}
                , {field: 'insertTime', title: '新增时间'}
                //, {field: 'insertUid', title: '新增操作人'}
                //, {field: 'updateUid', title: '修改操作人'}
                , {field: 'updateTime', title: '修改时间'}
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
        //监听工具条
        table.on('tool(schedulingrulesTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'publish') {//发布
                publish(data);
            }else if (obj.event === 'editRoster') {//修改排班状态
                editRoster(data);
            }else if (obj.event === 'selectManyUser') {//选择本规则人员
                selectManyUser(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(schedulingrulesSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(schedulingrulesTable)', function (obj) {
            if (obj.event === 'addschedulingrules') {
                addschedulingrules();
            }
        });

        //时间选择器
        laydate.render({
            elem: '#workStartTime'
            ,type: 'time'
        });

    });
});

function reloadschedulingrulesList() {
    loadTable('schedulingrulesList', {})
}

function addschedulingrules() {
    formAssignment(fromId, {"arrangeRuleId": ''})
    //echoForm(,{},fromId,'GET')
    openForm('schedulingrulesDiv', fromId, '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('schedulingrulesDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloadschedulingrulesList);
}

/**
 * 发布
 * @param data
 */
function publish(data) {
    submitForm(baseUrl+'publish', '', '', {'arrangeRuleId': data.arrangeRuleId}, 'GET', reloadschedulingrulesList)
}

/**
 * 修改排班状态
 * @param data
 */
function editRoster(data) {
    submitForm(baseUrl+'editRoster', '', '', {'arrangeRuleId': data.arrangeRuleId,'isRoster':data.isRoster==1?2:1}, 'GET', reloadschedulingrulesList)
}

function edit(data) {
    echoForm(getUrl, {'arrangeRuleId': data.arrangeRuleId}, fromId, 'GET');
}

//规则id
var arrangeRuleId = '';
/**
 * 选择人员
 */
function selectManyUser(data) {
    openForm('commonDeptTreeLinkUserListDiv', '', '添加', '100%');
    loadTable('uesrCommonListReload', {});
    arrangeRuleId = data.arrangeRuleId;
}


function getCommonCheckUserDataCallBack(data) {
    if (data.length == 0)return
    var names = [],ids = [];
    for (let key in data) {
        names.push(data[key]['username']);
        ids.push(data[key]['id']);
    }
    ajaxPostData('/roster/rules/person/aOrUs',{'arrangeRuleId':arrangeRuleId,"ids":ids.join(',')},aOrUsCall);

}
function aOrUsCall(data) {
    if (data.status == '1000'){
        layer.closeAll();//必须有
        $("#commonDeptTreeLinkUserListDiv").hide();//必须有
        layer.msg(data.message=='' || data.message == null || data.message==undefined?"操作成功":data.message, {icon: 6});
    }else {
        layer.msg(data.message, {icon: 5});
    }
}