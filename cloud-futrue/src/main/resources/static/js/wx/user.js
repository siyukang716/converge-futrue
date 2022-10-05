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
            elem: '#getUsers'
            ,id:'getUsers'
            , url: '/w/user/users'
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: '500'
            , page: true
            ,toolbar: 'toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
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
                , {field: 'openid', title: 'openid', width: 180}
                , {field: 'subscribe', title: '是否订阅',formatter: function (value) {
                        if(value === '1'){
                            return "男";
                        } else if(value === '2'){
                            return "女";
                        }else return "未知";
                    }, width: 180}
                , {field: 'nickname', title: '用户的昵称', width: 180}
                , {field: 'city', title: '用户所在城市', width: 180}
                , {field: 'country', title: '用户所在国家', minWidth: 180}
                , {field: 'province', title: '用户所在省份', minWidth: 180}
                , {field: 'language', title: '用户的语言', minWidth: 180}
                , {field: 'headimgurl', title: '用户头像', minWidth: 180}
                , {field: 'subscribe_time', title: '用户关注时间', minWidth: 180}
                , {field: 'remark', title: '公众号运营者对粉丝的备注', minWidth: 180}
                , {field: 'groupid', title: '用户所在的分组ID', minWidth: 180}
                , {field: 'tagid_list', title: '用户被打上的标签ID列表', minWidth: 180}
                , {field: 'subscribe_scene', title: '返回用户关注的渠道来源', minWidth: 180}
                , {field: 'qr_scene', title: '二维码扫码场景', minWidth: 180}
                , {field: 'qr_scene_str', title: '二维码扫码场景描述', minWidth: 180}
                , {fixed: 'right', title: '操作', width: 80, align: 'center', toolbar: '#optBar'}
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
            }
        });
        //监听提交
        form.on('submit(editsubmit)', function (data) {
            console.info("editsubmit")
            formSubmit(data,'/menu/updateMenu');
            return false;
        });
    });
});


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
