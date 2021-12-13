/**
 * 用户管理
 */
var pageCurr;
$(function () {
    layui.use(['table', 'laydate','util'], function () {
        var table = layui.table;
        var form = layui.form,

            tableIns = table.render({
                elem: '#leaveBillList'
                ,id:'leaveBillList'
                , url: '/leavebill/getleavebill'
                , method: 'post' //默认：get请求
                , cellMinWidth: 80
                , page: true,
                request: {
                    pageName: 'current' //页码的参数名称，默认：page
                    , limitName: 'size' //每页数据量的参数名，默认：limit
                }, response: {
                    statusName: 'code' //数据状态的字段名称，默认：code
                    , statusCode: 1000 //成功的状态码，默认：0
                    , countName: 'totals' //数据总数的字段名称，默认：count
                    , dataName: 'list' //数据列表的字段名称，默认：data
                    ,msgName: 'message'
                }
                , cols: [[
                    {type: 'numbers'}
                    , {field: 'id', title: 'id', width: 180, hide: true}
                    , {field: 'days', title: '请假天数'}
                    , {field: 'content', title: '请假内容'}
                    , {field: 'remark', title: '备注'}
                    , {field: 'userid', title: '请假人'}
                    , {field: 'state', title: '状态'}
                    , {fixed: 'right', title: '操作', width: 280, align: 'center', toolbar: '#optBar'}
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
                },
                text: { //自定义文本，此处用法--》当返回数据为空时的异常提示
                    none: '暂无请假信息' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                }
            });
        //监听提交
        form.on('submit(leaveBillSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        //监听工具条
        table.on('tool(leaveBillTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'startleaveBillProcess') {//发起请假流程
                startleaveBillProcess(data);
            }else if(obj.event === 'claimTask') {//拾取任务
                claimTask(data.id);
            }else if(obj.event === 'lookProcessPhoto') {//查看当前流程图
                lookProcessPhoto(data.id);
            }
        });
    });
});


/**
 * 填写请假信息
 * @param obj
 */
function formSubmit(obj) {
    $.ajax({
        type: "POST",
        data: $("#actleaveBill").serialize(),
        url: "/leavebill/saveOrUpdate",
        success: function (data) {
            if (data.status == "1000") {
                layer.msg("成功");
                layer.closeAll();
                load();
            } else if (data.status == "1101"){
                location.href = '/';
            }else {
                layer.msg(data.message);
            }
        },
        error: function () {
        }
    });
}
/**
 * 打开请假框
 * @param id
 */
function addleavebill(id) {
    layer.open({
        type: 1,
        title:'填写请假信息',
        fixed: false,
        resize: false,
        shadeClose: true,
        area: ['550px'],
        content: $('#setleaveBill'),
        end: function () {
            cleanUser();
        }
    });
}


/**
 * 打开当前流程活动节点图
 * @param id
 */
function lookProcessPhoto(id) {
    $.ajax({
        type: "POST",
        data: {},
        url: "/acttask/viewCurrentImage/"+id,
        success: function (data) {
            layer.open({
                type: 1,
                title: "流程活动节点图",
                fixed: false,
                resize: false,
                shadeClose: true,
                area: ['800px','400px'],
                content: $('#setleaveBill'),
                end: function () {
                    // cleanUser();
                }
            });
            var consoleDlg = $("#actModelForm");
            $("#nowPhoto").removeAttr('src');
            $("#showStyle").removeAttr('style');
            $("#nowPhoto").attr("src","/acttask/image?deploymentId="+
                data.data.deploymentId+"&imageName="+data.data.imageName);
            $("#showStyle").attr("style","position: absolute;border:5px solid red;top: "+data.data.y+"px;left: "+data.data.x +"px;width: "+data.data.width +"px;height:"+data.data.height +"px;  ");


        },
        error: function () {
        }
    });

}

/**
 * 启动请假流程
 */
function startleaveBillProcess(data) {
    $.ajax({
        type: "POST",
        data: {'id':data.id},
        url: "/leavebill/startleaveBillProcess",
        success: function (data) {
            if (data.status == "1000") {
                layer.msg("成功");
                layer.closeAll();
                load();
            } else {
                layer.msg(data.message);
            }
        },
        error: function () {
        }
    });
}


function load(obj) {
    layui.use([ 'table'], function(){
        var loadtable = layui.table;
        //重新加载table
        loadtable.reload('leaveBillList',{
            where: obj.field
            , page: {
                curr: pageCurr //从当前页码开始
            }
        });
    });
}
function load() {
    layui.use([ 'table'], function(){
        var loadtable = layui.table;
        //重新加载table
        loadtable.reload('leaveBillList',{
             page: {
                curr: pageCurr //从当前页码开始
            }
        });
    });
}

function cleanUser() {

}



