/**
 * 用户管理
 */
var pageCurr;
var tableIns;
$(function () {
    layui.use(['table', 'laydate','util'], function () {
        var table = layui.table;
        var form = layui.form,

            tableIns = table.render({
                elem: '#uesrList'
                ,id:'uesrList'
                , url: '/checkplanscheduled/getCheckPlans'
                , method: 'get' //默认：get请求
                , cellMinWidth: 80
                , page: true
                ,where : {
                    //传值 startDate : startDate,
                    "type":typeflag
                }
                ,request: {
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
                    , {field: 'rateid', title: 'ID', width: 180, hide: true}
                    , {field: 'name', title: '计划名称'}
                    , {field: 'corn', title: '表达式'}
                    //, {field: 'taskflag', title: '任务状态'}
                    , {field: 'memo', title: '描述', minWidth: 80}

                    , {field: 'taskflag', title: '是否在职', width: 295, align: 'center', templet: '#jobTpl'}
                    //, {fixed: 'right', title: '操作', width: 280, align: 'center', toolbar: '#optBar'}
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

        //监听在职操作
        form.on('switch(isJobTpl)', function (obj) {
            //console.log(this.value + ' ' + this.name + '：'+ obj.elem.checked, obj.othis);
            setJobUser(obj, this.value, this.name, obj.elem.checked);
        });
    });

});
//设置用户是否离职
function setJobUser(obj, id, nameVersion, checked) {

    var isJob = checked == 1 ? 0 : 1;
    var flag = checked ? "开启定时" : "关闭定时";
    layer.confirm('您确定要：' + flag + '定时吗?' , {
        btn: ['确认', '返回'] //按钮
    }, function () {
        if(checked){
            openTimer(id,isJob);
        }else {
            closeTimer(id,isJob);
        }
    }, function () {
        layer.closeAll();
        //加载load方法
        load(obj);
    });
}

/**
 * 关闭定时
 * @param id
 * @param isJob
 */
function closeTimer(id,isJob) {
    $.post("/checkplanscheduled/stop", {"taskKey": id, "type": typeflag}, function (data) {
        if (data.status == "1000") {
            //回调弹框
            layer.alert("操作成功！", function () {
                layer.closeAll();
                //加载load方法
                load(obj);
            });
        } else {
            layer.alert(data, function () {
                layer.closeAll();
                //加载load方法
                load(obj);//自定义
            });
        }
    });
}

/**
 * 开启定时
 * @param id
 * @param isJob
 */
function openTimer(id,isJob) {
    $.post("/checkplanscheduled/start", {"taskKey": id, "type": typeflag}, function (data) {
        if (data.status == "1000") {
            //回调弹框
            layer.alert("操作成功！", function () {
                layer.closeAll();
                //加载load方法
                load(obj);
            });
        } else {
            layer.alert(data, function () {
                layer.closeAll();
                //加载load方法
                load(obj);//自定义
            });
        }
    });
}
function load(obj) {
    layui.use([ 'table'], function(){
        var loadtable = layui.table;
        //重新加载table
        loadtable.reload('uesrList',{
            where: obj.field
            , page: {
                curr: pageCurr //从当前页码开始
            }
        });
    });

}
