var pageCurr;
var tableIns;
var control = 'uploadfile'
fromId = control + 'Form',
    divId = control + "Div",
    baseUrl = '/system/file/',
    aOrUUrl = baseUrl + 'aOrU',
    delUrl = baseUrl + 'del',
    getUrl = baseUrl + 'getById',
    listUrl = baseUrl + 'getList',
    menuTitle = 'sku';

$(function () {
    layui.use(['table', 'laydate', 'util','upload'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form,
            upload = layui.upload;
            tableIns = table.render({
                elem: '#uploadfileList'
                , id: 'uploadfileList'
                , url: listUrl
                //, method: 'get' //默认：get请求
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
                    , {field: 'upFileId', title: 'up_file_id', width: 180, hide: true}
                    , {field: 'path', title: '相对路径'}
                    , {field: 'fileName', title: '文件名'}
                    , {field: 'suffix', title: '文件后缀'}
                    , {field: 'size', title: '文件大小|字节B'}
                    , {field: 'createdAt', title: '文件创建时间'}
                    , {field: 'updatedAt', title: '文件修改时间'}
                    , {field: 'shardIndex', title: '已上传分片'}
                    , {field: 'shardSize', title: '分片大小|B'}
                    , {field: 'shardTotal', title: '分片总数'}
                    , {field: 'fileKey', title: '文件标识'}
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
        table.on('tool(uploadfileTable)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                del(data);
            } else {
                edit(data);
            }
        });
        //监听提交
        form.on('submit(uploadfileSubmit)', function (data) {
            // TODO 校验
            check();
            return false;
        });
        /**
         * toolbar监听事件
         */
        table.on('toolbar(uploadfileTable)', function (obj) {
            if (obj.event === 'adduploadfile') {
                adduploadfile();
            }else if (obj.event === 'singleuploadfile'){
                singleuploadfile();
            }else if (obj.event === 'fastUpfile'){
                fastUpfile();
            }
        });

        upload.render({
            elem: '#test8'
            ,url: '/system/file/singleFile' //此处配置你自己的上传接口即可
            ,auto: true
            ,data : {"key":"contractor" }
            //,multiple: true
            //,bindAction: '#test9'
            ,done: function(res){
                layer.msg('上传成功');
                console.log(res)
            }
        });
        upload.render({
            elem: '#fastSubmin'
            ,url: '/system/file/fastUpload' //此处配置你自己的上传接口即可
            ,auto: true
            //,data : {"key":"contractor" }
            //,multiple: true
            //,bindAction: '#test9'
            ,done: function(res){
                layer.msg('上传成功');
                console.log(res)
            }
        });

    });
});


function reloaduploadfileList() {
    loadTable('uploadfileList', {})
}

function adduploadfile() {
    formAssignment(fromId, {})
    //echoForm(,{},fromId,'GET')
    openForm('uploadfileDiv', fromId, '添加' + menuTitle);
}
function singleuploadfile() {
    openForm('singleuploadfileDiv', 'singleuploadfileForm', '添加' + menuTitle);
}

function fastUpfile() {
    openForm('fastUpfileDiv', 'fastUpfileForm', '添加' + menuTitle);
}

function echoFormCallback(data, formId) {
    openForm('uploadfileDiv', formId, '修改' + menuTitle);
    formAssignment(formId, data.data);
}

function formSubmit(data) {
    submitForm(aOrUUrl, divId, fromId, data.field, 'POST', reloaduploadfileList);
}

function del(data) {
    submitForm(delUrl, divId, fromId, {'mavId': data.mavId}, 'GET', reloaduploadfileList)
}

function edit(data) {
    echoForm(getUrl, {'mavId': data.mavId}, fromId, 'GET');
}




//上传文件的话 得 单独出来
function test1(shardIndex) {
    debugger;
    console.log(shardIndex);
    //永安里from表单提交
    var fd = new FormData();
    //获取表单中的file
    var file=$('#inputfile').get(0).files[0];
    //文件分片 以20MB去分片
    var shardSize = 20 * 1024 * 1024;
    //定义分片索引
    var shardIndex = shardIndex;
    //定义分片的起始位置
    var start = (shardIndex-1) * shardSize;
    //定义分片结束的位置 file哪里来的?
    var end = Math.min(file.size,start + shardSize);
    //从文件中截取当前的分片数据
    var fileShard = file.slice(start,end);
    //分片的大小
    var size = file.size;
    //总片数
    var shardTotal = Math.ceil(size / shardSize);
    //文件的后缀名
    var fileName = file.name;
    var suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length).toLowerCase();
    //把视频的信息存储为一个字符串
    var filedetails=file.name+file.size+file.type+file.lastModifiedDate;
    //使用当前文件的信息用md5加密生成一个key 这个加密是根据文件的信息来加密的 如果相同的文件 加的密还是一样的
    //var key = hex_md5(filedetails);
    //var key10 = parseInt(key,16);
    //把加密的信息 转为一个64位的
    var key62 = filedetails;//Tool._10to62(key10);
    //前面的参数必须和controller层定义的一样
    fd.append('file',fileShard);
    fd.append('suffix',suffix);
    fd.append('shardIndex',shardIndex);
    fd.append('shardSize',shardSize);
    fd.append('shardTotal',shardTotal);
    fd.append('size',size);
    fd.append("key",key62)
    $.ajax({
        url:"/system/file/upload",
        type:"post",
        cache: false,
        data:fd,
        processData: false,
        contentType: false,
        success:function(data){
            //这里应该是一个递归调用
            if(shardIndex < shardTotal){
                var index=shardIndex +1;
                test1(index);
            }else
            {
                alert(data)
            }

        },
        error:function(){
            //请求出错处理
        }
    })
    //发送ajax请求把参数传递到后台里面
}

//判断这个加密文件存在不存在
function check() {
    var file=$('#inputfile').get(0).files[0];
    //把视频的信息存储为一个字符串
    var filedetails=file.name+file.size+file.type+file.lastModifiedDate;
    //使用当前文件的信息用md5加密生成一个key 这个加密是根据文件的信息来加密的 如果相同的文件 加的密还是一样的
    ///var key = hex_md5(filedetails);
    //var key10 = parseInt(key,16);
    //把加密的信息 转为一个64位的
    //var key62 = Tool._10to62(key10);
    //检查这个key存在不存在
    $.ajax({
        url:"/system/file/check",
        type:"post",
        data:{'key':filedetails},
        success:function (data) {
            console.log(data);
            if(data.status==1001){
                //这个方法必须抽离出来
                test1(1);
            }
            else
            {
                if(data.data.shardIndex == data.data.shardTotal)
                {
                    alert("极速上传成功");
                }else
                {
                    //找到这个是第几片 去重新上传
                    test1(parseInt(data.data.shardIndex));
                }
            }
        }
    })
}