var pageCurr;
var tableIns;
var menuTitle = '公共字典';

$(function () {
    layui.use(['table', 'laydate', 'util'], function () {
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        //展示已知数据
        table.render({
            elem: '#fileOnPuList'
            , id: 'fileOnPuList'
            ,cols: [[ //标题栏
                ,{field: 'iFileSize', title: 'iFileSize', width: 180}
                ,{field: 'iFileType', title: '文件类型', width: 180, templet: function (d) {
                        if (d.iFileType == 8)return "音频文件";
                        if (d.iFileType == 1)return "视频文件";
                        if (d.iFileType == 2)return "图片文件";
                        return "未识别";
                    }}
                ,{field: 'iTimeBegin', title: 'iTimeBegin', width:180,sort:true,
                    templet: "<div>{{layui.util.toDateString(d.iTimeBegin * 1000, 'yyyy-MM-dd HH:mm:ss')}}</div>"}
                ,{field: 'iTimeEnd', title: 'iTimeEnd', width: 180,
                    templet: "<div>{{layui.util.toDateString(d.iTimeEnd * 1000, 'yyyy-MM-dd HH:mm:ss')}}</div>"}
                ,{field: 'szPuid', title: '设备id', width: 180}
                ,{field: 'szFile', title: 'szFile', width: 180,toolbar: '#optBar'}
            ]]
            ,data: []
            //,skin: 'line' //表格风格
            ,even: true
            //,page: true //是否显示分页
            //,limits: [5, 7, 10]
            ,limit: 500 //每页默认显示的数量
        });

    });
});

window.onload = function () {
    jSW.swInit({
        url: "http://61.191.27.18:8081", // bv_nginx.exe服务器地址
        //url: "https://61.191.27.18:9443", // bv_nginx.exe服务器地址
        calltype: jSW.CallProtoType.AUTO, // AUTO: IE优先使用OCX, 如果希望IE仍然使用HTTP通信, 请使用jSW.CallProtoType.HTTP
    });

}
window.onbeforeunload = function () {
    jSW.swDeInit();
}

var session = null;

function jswLogin() {
    if (session) {
        delete session;
    }
    session = new jSW.SWSession({
        server: '47.114.130.118', // 如果是jSW.CallProtoType.OCX方式，这里需要填写具体的IP
        port: 9701,
        onopen: function (sess) {
            sess.swLogin({
                user: 'jinse',
                password: '123456'
            });
        }
    });

    // 注册事件的回调函数
    session.swAddCallBack('login', sessionCallback);
    //session.swAddCallBack('logout', sessionCallback);
}

function jswLogout() {
    if (session) {
        session.swLogout();
    }
}

function sessionCallback(sender, event, json) {
    var info = '';
    if ('login' == event) {
        info += '登录';
    } else if ('logout' == event) {
        info += '退出';
    }

    if (json.code == jSW.RcCode.RC_CODE_S_OK) {
        info += '成功';
    } else {
        info += '失败, error code: ' + json.code;
    }

    document.getElementById('msg').innerHTML = info;// + '<br/><textarea rows="18" cols="100" readonly="readonly"> ' + JSON.stringify(json, null, 4) + '</textarea>';
}

$(function () {
    setTimeout(function () {
        jswLogin();
    }, 1000);
});


function SearchRecordOnPu() {
    var beginTimer = dateChange(-30);//document.getElementById('frompuibegintimeid');
    var endTimer = '2021/11/09 00:00:00';// document.getElementById('frompuiendtimeid');
    var itimeBegin =  Math.round(new Date(beginTimer).getTime() / 1000);
    var itimeEnd = Math.round(new Date().getTime() / 1000);

    // var puid = document.getElementById('frompupuidid').value;

    if (session) {
        var rc = session.swSearchFileOnPu({
            stSearchInfo: {
                iType: 1,
                iPostition: 0,
                iCount: 50
            },
            stFilter: {
                szPUID: 'PU_26223781',
                iChannelIndex: -1,
                iFileType: 0,
                iTimeBegin: itimeBegin,
                iTimeEnd: itimeEnd,
                iFileSizeMin: 0,
                iFileSizeMax: 0,
                iRecordType: 0,
            },
            callback: function (options, response, data) {
                if (response.getCode() == jSW.RcCode.RC_CODE_S_OK) {
                    var aadata = new Date();
                    var strTime;
                    var trtag = "";
                    trtag = "";
                    var icount = data.content.length < 20 ? data.content.length : 20;
                    debugger;
                    layui.table.reload('fileOnPuList',{
                        data:data.content
                    });
                }else {
                    alert("设备不在线");
                }
            },
            tag: null
        });

    }
}