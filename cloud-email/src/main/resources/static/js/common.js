/**
 * 公用的js函数文件
 */

/**
 * 伪造http referer信息
 * 用 document.all 来判断当前的浏览器是否是IE， 如果是的话就生成一个link，
 * 然后自动执行 onclick 事件，如果不是的话就用JS 跳转。这样在处理页面就可以得到 HTTP_REFERER
 * @param url
 */
function referURL(url){
    var isIe=(document.all)?true:false;
    //console.info("isIe:"+isIe);
    if(isIe) {
        var linka = document.createElement('a');
        linka.href=url;
        document.body.appendChild(linka);
        linka.click();
    }
}

/**
 * 唯一标识 指定长度和基数
 */
function generateUUID(len, radix) {
    var timeData = new Date().getTime();
    var chars = ('0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz').split('');
    var uuid = [], i;
    radix = radix || chars.length;

    if (len) {
        // Compact form
        for (i = 0; i < len; i++) uuid[i] = chars[0 | (Math.random()*radix)];
    } else {
        // rfc4122, version 4 form
        var r;

        // rfc4122 requires these characters
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
        uuid[14] = '4';

        // Fill in random data.  At i==19 set the high bits of clock sequence as
        // per rfc4122, sec. 4.1.5
        for (i = 0; i < 36; i++) {
            if (!uuid[i]) {
                r = 0 | (Math.random()*16);
                uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
            }
        }
    }
    return uuid.join('');
}

/**
 * GUID是一种由算法生成的二进制长度为128位的数字标识符。
 * GUID 的格式为“xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx”，
 * 其中的 x 是 0-9 或 a-f 范围内的一个32位十六进制数。在理想情况下，任何计算机和计算机集群都不会生成两个相同的GUID。
 * @returns {string}
 */
function uuid() {
    var d = new Date().getTime();
    //var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    var uuid = 'xxxx-yxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
    return uuid;
};
//货币格式化
//Extend the default Number object with a formatMoney() method:
//usage: someVar.formatMoney(decimalPlaces, symbol, thousandsSeparator, decimalSeparator)
//defaults: (2, "$", ",", ".")
Number.prototype.formatMoney = function (places, symbol, thousand, decimal) {
 places = !isNaN(places = Math.abs(places)) ? places : 2;
 symbol = symbol !== undefined ? symbol : "$";
 thousand = thousand || ",";
 decimal = decimal || ".";
 var number = this,
     negative = number < 0 ? "-" : "",
     i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
     j = (j = i.length) > 3 ? j % 3 : 0;
 return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
};

/**
 * 判断是否登录，没登录刷新当前页，促使Shiro拦截后跳转登录页
 * @param result	ajax请求返回的值
 * @returns {如果没登录，刷新当前页}
 */
function isLogin(result){
    if(result && result.code && (result.code == '1101' || result.code=='1102')){
        window.location.reload(true);//刷新当前页
    }
    return true;//返回true
}

/**
 * 获取get请求参数
 * @param name
 * @returns
 */
function GetQueryString(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var search=window.location.search;
    if(search!=null && search!=""){
        var r = search.substr(1).match(reg);
        if(r!=null){
            return  unescape(r[2]);
        }
    }
    return null;
}
/**
 * 获取菜单uri
 * @returns
 */
function getCallback(){
    var pathname = window.location.pathname;
    var param=GetQueryString("callback");
    //console.log("pathname:"+pathname);
    //console.log("param:"+param);
    if(param!=null && param != ""){
        return param;
    }else{
        return pathname;
    }
}


/**
 * 针对不同的错误可结合业务自定义处理方式
 * @param result
 * @returns {Boolean}
 */
function isError(result){
    var flag=true;
    if(result && result.status){
        flag=false;
        if(result.status == '-1' || result.status=='-101' || result.status=='400' || result.status=='404' || result.status=='500'){
            layer.alert(result.data);
        }else if(result.status=='403'){
            layer.alert(result.data,function(){
                //跳转到未授权界面
                window.location.href="/403";
            });
        }
    }
    return flag;//返回true
}

/**
 * 重置表单
 * @param formId  表单id
 */
function clearForm(formId) {
    layui.form.render();
    $("#"+formId)[0].reset();
    //document.getElementById(formId).reset();
    //layui.form.render(null, formId);

}


/**
 * 打开form表单
 * @param divId
 * @param formId  表单id
 * @param title   表单标题
 * @param width  表单宽度
 * @param height  表单高度
 */
function openForm(divId,formId, title,width,height) {
    //clearForm(formId);
    layer.open({
        type: 1,
        title: title,
        fixed: false,
        resize: false,
        scrollbar:false,
        shadeClose: false,
        area: [width!=''&&width!=null&&width!=undefined?width:'700px', height!=''&&height!=null&&height!=undefined?height:'80%'],//
        content:  $('#'+divId),
        end: function () {
            if (formId != '' && formId != null)
                clearForm(formId);
        },
        success: function(layero, index){
            //console.log(layero, index);
            layui.form.render();
        },
        yes: function(index, layero){
            //do something
            //layer.close(index); //如果设定了yes回调，需进行手工关闭
        },
        cancel: function(index, layero){
            layer.close(index)
            $("#"+divId).hide();
            return false;
        }
    });
}

/**
 * 查询后台数据 通过方法echoFormCallback回调进行处理
 * 做数据回显
 * @param url  请求地址
 * @param data 请求条件
 * @param formId 表单id
 * @param reqType 请求方式
 * @param callback  默认回调echoFormCallback 函数  可以自定义函数回调
 */
function echoForm(url,data,formId,reqType,callback) {
    $.ajax({
        type: reqType == "GET"?"GET":"POST",
        data: data,
        url: url,
        success: function (data) {
            if (data.status != '1000'){
                layer.msg(data.message, {icon: 6});
            }else {
                if (isFunction('echoFormCallback'))
                    echoFormCallback(data,formId);
                if (callback!=''&&callback!=null&&callback!=undefined)
                    callback.call(null);
            }

        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                //load({});//自定义
            });
        }
    });
}

/**
 * 提交表单  新增修改
 * @param url
 * @param formId
 * @param reqType
 * 控件
 */
function submitForm(url,divId,formId,formData,reqType,callback) {
    $.ajax({
        type: reqType == "GET"?"GET":"POST",
        data: formData,
        url: url,
        success: function (data) {
            if (data.status == '1000'){
                layer.closeAll();
                if (divId != null && divId != '')
                    $('#'+divId).hide();
                layer.msg(data.message=='' || data.message == null || data.message==undefined?"操作成功":data.message, {icon: 6});
                if (callback!=''&&callback!=null&&callback!=undefined)
                    callback.call(null,data);

            }else {
                layer.msg(data.message, {icon: 5});
            }

        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                //load({});//自定义
                $("#"+divId).hide();
            });
        }
    });
}




/**
 * 表单赋值
 * @param formId  表单id
 * @param data  表单数据数据
 */
function formAssignment(formId,data){
    //console.info('formId:'+formId);
    //console.info('表单数据数据'+data);
    //给表单赋值
    layui.form.val(formId, data);//{ //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值}

}


/**
 * 上传文件内容 文件名 大小
 * @param formId  表单id
 * @param data  表单数据数据
 */
function uploadFileInfo(that,obj){
    var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
    //读取本地文件
    obj.preview(function(index, file, result) {
        var tr = $(['<tr id="upload-' + index + '">'
            , '<td>' + file.name + '</td>'
            , '<td>' + (file.size / 1014).toFixed(1) + 'kb</td>'
            , '<td><div class="layui-progress" lay-filter="progress-demo-' + index + '"><div class="layui-progress-bar" lay-percent=""></div></div></td>'
            , '<td>'
            , '<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
            , '<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
            , '</td>'
            , '</tr>'].join(''));

        //单个重传
        tr.find('.demo-reload').on('click', function () {
            obj.upload(index, file);
        });

        //删除
        tr.find('.demo-delete').on('click', function () {
            delete files[index]; //删除对应的文件
            tr.remove();
            uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
        });

        that.elemList.append(tr);
        element.render('progress'); //渲染新加的进度条组件
    })
}

/**
 *  判断是否为函数
 * @param FunName
 */
function isFunction(FunName) {
    try {
        if(typeof(eval(FunName))=="function") { //是函数    其中 FunName 为函数名称
            return true;
        } else { //不是函数
            return false;
        }
    } catch(e) {
        console.info(FunName+"not is function");
        return false;
    }
}

/**
 * 刷新列表
 * @param tableId  列表id
 * @param condition  筛选条件
 */
function loadTable(tableId,condition) {
    //重新加载table
    layui.table.reload(tableId,{
        where: condition
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}
/**
 * 刷新无分页列表
 * @param tableId  列表id
 * @param condition  筛选条件
 */
function loadNotPageTable(tableId,condition) {
    //重新加载table
    layui.table.reload(tableId,{
        where: condition
        , page: false
    });
}

/**
 * 获取表单区域所有值
 * @param formId
 * @returns {*}
 */
function getFormVal(formId) {
    return layui.form.val(formId);
}


/**
 * 查询数据
 * @param url 请求地址
 * @param data 查询条件
 * @param callback
 * @param p1
 * @param p2
 * @param p3
 * @param p4
 * @param p5
 */
function getData(url,data,callback,p1,p2,p3,p4,p5) {
    $.ajax({
        type: "GET",
        data: data,
        url: url,
        async:false,
        success: function (data) {
            //if(data.message != undefined && data.message != '')
                //layer.msg(data.message, {icon: 6});
            if (callback!=''&&callback!=null&&callback!=undefined)
                callback.call(null,data,p1,p2,p3,p4,p5);

        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                //load({});//自定义
            });
        }
    });
}
function ajaxPostData(url,data,callback,p1,p2,p3,p4,p5) {
    $.ajax({
        type: "POST",
        data: data,
        url: url,
        async:false,
        success: function (data) {
            //if(data.message != undefined && data.message != '')
            //layer.msg(data.message, {icon: 6});
            if (callback!=''&&callback!=null&&callback!=undefined)
                callback.call(null,data,p1,p2,p3,p4,p5);

        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                //load({});//自定义
            });
        }
    });
}

/**
 * 重置表单 select
 */
function renderSelect() {
    layui.use(['table', 'laydate','util','colorpicker','form'], function () {
        var form = layui.form;
        form.render('select');
    });
}


/**
 * select 标签数据回显
 * @param data
 * @param sId
 */
function selectEcho(data,sId,oId,oName) {
    $('#'+sId).empty();
    let select  = document.getElementById(sId);
    data.data.forEach(function(element) {
        //console.log(element);
        select.options.add(new Option(element[oName],element[oId]));
    });
    renderSelect();
}


/**
 * 加
  * @param arg1
 * @param arg2
 * @returns {number}
 */
function floatAdd(arg1,arg2){
    var r1,r2,m;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    return (arg1*m+arg2*m)/m;
}

/**
 * 减
 * @param arg1
 * @param arg2
 * @returns {string}
 */
function floatSub(arg1,arg2){
    var r1,r2,m,n;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    //动态控制精度长度
    n=(r1>=r2)?r1:r2;
    return ((arg1*m-arg2*m)/m).toFixed(n);
}

/**
 * 乘
 * @param arg1
 * @param arg2
 * @returns {number}
 */
function floatMul(arg1,arg2) {
    var m=0,s1=arg1.toString(),s2=arg2.toString();
    try{m+=s1.split(".")[1].length}catch(e){}
    try{m+=s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
}


/**
 * 除
  * @param arg1
 * @param arg2
 * @returns {number}
 */
function floatDiv(arg1,arg2){
    var t1=0,t2=0,r1,r2;
    try{t1=arg1.toString().split(".")[1].length}catch(e){}
    try{t2=arg2.toString().split(".")[1].length}catch(e){}

    r1=Number(arg1.toString().replace(".",""));

    r2=Number(arg2.toString().replace(".",""));
    return (r1/r2)*Math.pow(10,t2-t1);
}

/**
 * 打印
 */
function printPage() {
    window.print();
}

/**
 * 日期加减
 * @param num
 * @param date
 * @returns {string}
 */
function dateChange(num = 1,date = false) {
    if (!date) {
        date = new Date();//没有传入值时,默认是当前日期
        date = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
    }
    date += " 00:00:00";//设置为当天凌晨12点
    date = Date.parse(new Date(date))/1000;//转换为时间戳
    date += (86400) * num;//修改后的时间戳
    var newDate = new Date(parseInt(date) * 1000);//转换为时间
    return newDate.getFullYear() + '-' + (newDate.getMonth() + 1) + '-' + newDate.getDate();
}

/**
 * 获取文件ip地址
 * @returns {Document.ipAddress|string|*}
 */
function getIpAddr(){
    return window.parent.ipConfig.ipAddress;
}

