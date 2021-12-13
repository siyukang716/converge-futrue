
var pageCurr;
var tableIns;
var control = 'picinfo'
    fromId = control+'Form',
    divId = control + "Div",
    baseUrl = '/product/pic/info/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = '商品图片';

$(function () {
    layui.use(['table', 'laydate','util','upload'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var upload = layui.upload;
        var form = layui.form,
        tableIns = table.render({
            elem: '#picinfoList'
            ,id:'picinfoList'
            , url: listUrl
            , method: 'get' //默认：get请求
            , cellMinWidth: 80
            ,height: 'full-100'
            ,toolbar: '#toolbarDemo'
            ,defaultToolbar: ['filter', 'print', 'exports']
            , page: true,
            where:{'productId':$("#reqproductId").val()},
            title: menuTitle,
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
                , {field: 'productPicId', title: '商品图片ID', width: 180, hide: true}
                , {field: 'productId', title: '商品ID', width: 180, hide: true}
                , {field: 'picDesc', title: '图片描述'}
                , {field: 'picUrl', title: '图片URL',templet:"#picUrl"}
                , {field: 'isMaster', title: '是否主图',templet:function(d){return d.isMaster=='1'?"主图":"非主图"}}
                , {field: 'picOrder', title: '图片排序'}
                , {field: 'picStatus', title: '图片是否有效',templet:function(d){return d.picStatus=='1'?"有效":"无效"}}
                , {field: 'modifiedTime', title: '最后修改时间'}
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
        table.on('tool(picinfoTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                delAddr(data);
            }else {
                editAddr(data);
            }
        });
        //监听提交
        form.on('submit(picinfoSubmit)', function (data) {
            // TODO 校验
            formSubmit(data);
            return false;
        });
        form.on('submit(lookPic)', function (data) {
            // TODO 校验
            lookPic(data);
            return false;
        });


        //拖拽上传
        upload.render({
            elem: '#test10'
            ,url: baseUrl+'upload' //此处用的是第三方的 http 请求演示，实际使用时改成您自己的上传接口即可。
            ,data:{'businessId':$('#productId').val()}
            ,done: function(res){
                layer.msg('上传成功');
                layui.$('#uploadDemoView').removeClass('layui-hide').find('img').attr('src', res.data.url);
                formAssignment(fromId,{"picUrl":res.data.url})
                console.log(res)
            }
        });

    });

});


function reloadpicinfoList() {
    loadTable('picinfoList',{})
}

function addPicinfo() {
    formAssignment(fromId,{'productPicId':'','productId':$("#reqproductId").val()});
    layui.$('#uploadDemoView').find('img').attr('src', '');
    openForm('picinfoDiv',fromId,'添加'+menuTitle);
}

function echoFormCallback(data,formId) {
    openForm('picinfoDiv',formId,'修改'+menuTitle);
    formAssignment(formId,data.data);
    $('#uploadDemoView').removeClass('layui-hide').find('img').attr('src', data.data.picUrl);
}

function formSubmit(data) {
    submitForm(aOrUUrl,divId,fromId,data.field,'POST',reloadpicinfoList);
}
function delAddr(data) {
    submitForm(delUrl,divId,fromId, {'productPicId':data.productPicId},'GET',reloadpicinfoList)
}
function editAddr(data) {
    echoForm(getUrl,{'productPicId':data.productPicId},fromId,'GET');
}

function lookPic(data) {
    //页面层-图片

    layer.photos({
        photos: {
            "title": "", //相册标题
            "id": 123, //相册id
            "start": 0, //初始显示的图片序号，默认0
            "data": [   //相册包含的图片，数组格式
                {
                    "alt": "图片名",
                    "pid": 666, //图片id
                    "src": data.elem.src, //原图地址
                    "thumb": data.elem.src //缩略图地址
                }
            ]
        }
        ,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
    });
}