/**
 * 用户管理
 */
var pageCurr;
var tableIns;
var form,table;
$(function () {
    layui.use(['table', 'laydate','util','form', 'layer'], function () {
        table = layui.table;
        form = layui.form,
        tableIns = table.render({
            elem: '#getMenus'
            ,id:'getMenus'
            , url: '/menu/getMenus'
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            , page: true,
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
                , {field: 'id', title: 'ID', width: 180, hide: true}
                , {field: 'accountId', title: '账户id', hide: true}
                , {field: 'parentId', title: '父级id'}
                , {field: 'parentName', title: '父级'}
                , {field: 'name', title: '菜单名称', minWidth: 80}
                , {field: 'type', title: '菜单类型', minWidth: 80}
                , {field: 'key', title: '菜单KEY值', minWidth: 80}
                , {field: 'url', title: '网页 链接', minWidth: 80}
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
            }
        });
        //监听工具条
        table.on('tool(userTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delMenu(data);
            } else if (obj.event === 'edit') {
                //编辑
                $("#menuForm")[0].reset();
                //layui.form.render();
                openMenu( "编辑菜单");
                form.val("menuForm", {
                    id: data.id,
                    parentId:data.parentId,
                    parentName:data.parentName,
                    name:data.name,
                    type:data.type,
                    key:data.key,
                    url:data.url
                });
                $("#menusubmitbutton").attr("lay-filter","editsubmit");


            }else if (obj.event === 'add') {
                openMenu( "新增菜单");
                $("#menuForm")[0].reset();
                form.val("menuForm", {
                    id:'',
                    parentId:data.id,
                    parentName:data.name,
                });
                $("#menusubmitbutton").attr("lay-filter","addsubmit");
            }
        });
        //监听提交
        form.on('submit(editsubmit)', function (data) {
            console.info("editsubmit")
            formSubmit(data,'/menu/updateMenu');
            return false;
        });
        form.on('submit(addsubmit)', function (data) {
            console.info("addsubmit")
            formSubmit(data,'/menu/addMenu');
            return false;
        });
    });
});

/**
 * delOnLineMenu  删除线上菜单
 * @param obj
 */
function delOnLineMenu() {
    $.get("/menu/delete", {}, function (data) {
        layer.alert(data.message, function () {
            layer.closeAll();
        });
    });
}

/**
 * 发布线上菜单
 */
function addOnLineMenu() {
    $.get("/menu/create", {}, function (data) {
        layer.alert(data.message, function () {
            layer.closeAll();
        });
    });
}

/**
 * 删除微信菜单
 * @param obj
 */
function delMenu(obj) {
    if (null != id) {
        layer.confirm('您确定要删除' + obj.name + '菜单吗？', {
            btn: ['确认', '返回'] //按钮
        }, function () {
            $.post("/menu/removeMenu", {"id": obj.id}, function (data) {
                layer.alert(data.message, function () {
                    layer.closeAll();
                    load(null);
                });
            });
        }, function () {
            layer.closeAll();
        });
    }
}
/**
 * 打开弹出层
 * @param title
 */
function openMenu(title) {
    layer.open({
        type: 1,
        title: title,
        fixed: false,
        resize: false,
        //shadeClose: true,
        area: ['550px'],
        content: $('#setMenu'),
        end: function () {

        }
        ,cancel: function(index, layero){
            layer.close(index)
            $("#setMenu").hide();
            return false;
        }
    });
}
//提交表单
function formSubmit(obj,url) {
    $.ajax({
        type: "post",
        data: obj.field,
        url: url,
        dataType:'json',
        success: function (data) {
            layer.alert(data.message, function () {
                layer.closeAll();
                load(null);
            });
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                load();//自定义
            });
        }
    });
}

function load(obj) {
    layui.use([ 'table'], function(){
        var loadtable = layui.table;
        //重新加载table
        loadtable.reload('getMenus',{
            page: {
                curr: pageCurr //从当前页码开始
            }
        });
    });

}
