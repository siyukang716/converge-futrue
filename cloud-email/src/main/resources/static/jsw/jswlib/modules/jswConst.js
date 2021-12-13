(function (dMgr) {
    jSW.StorageFileType = {
        ALL: 0,      // 不限定文件类型
        RECORD: 1 << 0,   // 录像文件
        CAPTURE: 1 << 1,  // 图片文件
        GPS: 1 << 2, // 固件文件
        AUDIO: 1 << 3,// 音频文件
        FIRMWARE: 1 << 8      // GPS文件
    };

    jSW.StorageRecordType = {
        NONE: 0,
        MANUAL: (1 << 0),
        ONTIME: (1 << 1),
        ONALARM: (1 << 2)
    };

    jSW.SW_CHANEL_STATUS = {
        OPENED: 0,      //打开成功
        OPENING: 1,     //正在打开
        CLOSED: 2,      //关闭
        HLS_OPEN: 3,    //打开HLS
        HLS_M3U8: 4     //产生m3u8文件
    };

    jSW.MEDIADIR = {
        ALLNULL: 0,
        VIDEOSEND: (1 << 0), /**值为1，视频发送，暂不支持*/
        VIDEORECV: (1 << 1), /**值为2，视频接收*/
        AUDIOSEND: (1 << 2), /**值为4，音频发送，仅OCX支持*/
        AUDIORECV: (1 << 3), /**值为8，音频接收*/
        DATASEND: (1 << 4),  /**值为16，数据发送*/
        DATARECV: (1 << 5),  /**值为32，数据接收*/
    };

    jSW.FileTransEvent = {
        DEFAULT: 0,
        FILE_TRANS_OPEN: 1,
        FILE_TRANS_CLOSE: 2,
        FILE_TRANS_PROCESS: 3
    };

    jSW.RcCode = {
        RC_CODE_S_OK: 0, /**成功*/
        RC_CODE_S_OTHER: 1,

        RC_CODE_E_FAIL: -1, /**一般错误*/
        RC_CODE_E_INVALIDARG: -2, /**参数*/
        RC_CODE_E_USERNAME: -3, /**用户名*/
        RC_CODE_E_PASSWORD: -4, /**密码*/
        RC_CODE_E_NOTFOUND: -5, /**未找到*/
        RC_CODE_E_UNSUPPORTED: -6, /**不支持*/
        RC_CODE_E_BUSY: -7, /**繁忙*/
        RC_CODE_E_NOTINITILIZED: -8, /**未初始化*/
        RC_CODE_E_TIMEOUT: -9, /**超时*/
        RC_CODE_E_ALREADYEXIST: -10, /**已存在*/
        RC_CODE_E_DISCONNECTED: -11, /**未连接*/
        RC_CODE_E_OTHER: -12,


        RC_CODE_E_BVCU_FAILED: 20101,   /*general error*/
        RC_CODE_E_BVCU_INVALIDARG: 20101 + 1,         /*invalid argument*/
        RC_CODE_E_BVCU_UNSUPPORTED: 20101 + 2,        /*unsupported functions*/
        RC_CODE_E_BVCU_ALLOCMEMFAILED: 20101 + 3,    /*allocate memory failed*/
        RC_CODE_E_BVCU_MEMALIGNMENT: 20101 + 4,      /*memory alignment is not satisfied*/
        RC_CODE_E_BVCU_NOTFOUND: 20101 + 5,          /*not found*/
        RC_CODE_E_BVCU_NOTALLOWED: 20101 + 6,        /*the requested access is not allowed*/
        RC_CODE_E_BVCU_IO: 20101 + 7,                /*I/O error*/
        RC_CODE_E_BVCU_EOF: 20101 + 8,               /*End of file*/
        RC_CODE_E_BVCU_INVALIDDATA: 20101 + 9,       /*Invalid data found when processing input*/
        RC_CODE_E_BVCU_NOTIMPL: 20101 + 10,           /*not implemented*/
        RC_CODE_E_BVCU_BUSY: 20101 + 11,              /*busy.deny service now*/
        RC_CODE_E_BVCU_INUSE: 20101 + 12,              /*device in use*/
        RC_CODE_E_BVCU_BADREQUEST: 20101 + 13,        /*bad request*/
        RC_CODE_E_BVCU_AUTHORIZE_FAILED: 20101 + 14,    /*authorize failed。登录/发送命令等的OnEvent回调中使用*/
        RC_CODE_E_BVCU_BADSTATE: 20101 + 15,          /*bad internal state*/
        RC_CODE_E_BVCU_NOTINITILIZED: 20101 + 16,      /*not initialized*/
        RC_CODE_E_BVCU_FATALERROR: 20101 + 17,        /*fatal error. BVCU should be closed*/
        RC_CODE_E_BVCU_OUTOFSPACE: 20101 + 18,        /*out of space*/
        RC_CODE_E_BVCU_DISCONNECTED: 20101 + 19,      /*disconnected*/
        RC_CODE_E_BVCU_TIMEOUT: 20101 + 20,            /*time out*/
        RC_CODE_E_BVCU_CONNECTFAILED: 20101 + 21,      /*connect failed*/
        RC_CODE_E_BVCU_ABORTED: 20101 + 22,            /*request aborted*/
        RC_CODE_E_BVCU_THRAEDCONTEXT: 20101 + 23,      /*can not execute in the specified thread context*/
        RC_CODE_E_BVCU_UNAVAILABLE: 20101 + 24,        /*unavailable, eg: initialize a dialog with an offlined PU*/
        RC_CODE_E_BVCU_ALREADYEXIST: 20101 + 25,       /*already exist*/
        RC_CODE_E_BVCU_SEVERINTERNAL: 20101 + 26,      /*Server internal error*/
        RC_CODE_E_BVCU_MAXRETRIES: 20101 + 27,              /*达到最大重试次数*/
        RC_CODE_E_BVCU_AAA_OBJECTNOTFOUND: 20101 + 28,/*AAA 用户/用户组等不存在。SESSION_CLOSE事件的OnEvent回调中使用*/

        RC_CODE_S_BVCU_OK: 20101 + 29,                /*succeed*/
        RC_CODE_S_BVCU_IGNORE: 20101 + 30,            /*succeed,but something can not handle is ignored.*/
        RC_CODE_S_BVCU_PENDING: 20101 + 31,           /*operation is pending.*/
        RC_CODE_S_BVCU_CONTINUE: 20101 + 32,          /*表示后续还有FTP数据*/

        RC_CODE_S_SAV_OPEN_FAIL: 20101 + 33,          /*SAV打开容器失败*/


        RC_CODE_E_REDIS_CONNECTED_FAIL: 30601, /**连接redis失败*/
        RC_CODE_E_REDIS_SET_FAIL: 30601 + 1, /**redis设置失败*/
        RC_CODE_E_REDIS_GET_FAIL: 30601 + 2, /**redis查询失败*/
        RC_CODE_E_REDIS_GET_NIL: 30601 + 3, /**redis查询的值为空*/

        RC_CODE_E_SW_CONNECTION_EXIT: 30701, /**连接已存在*/
        RC_CODE_E_SW_CONNECTION_NOT_EXIT: 30701 + 1,
        RC_CODE_E_SW_PU_NOT_EXIT: 30701 + 2,
        RC_CODE_E_SW_CHANEL_NOT_EXIT: 30701 + 3,
        RC_CODE_E_SW_CREATE_THREAD_FAIL: 30701 + 4, /**线程创建失败*/
        RC_CODE_E_SW_LISTENER_INIT_FAIL: 30701 + 5, /**监听初始化失败*/
        RC_CODE_E_SW_HIREDIS_INIT_FAIL: 30701 + 6, /**redis初始化失败*/
        RC_CODE_E_SW_JSON_PARSE: 30701 + 7,
        RC_CODE_E_SW_HAVE_LOGIN: 30701 + 8,
        RC_CODE_E_SW_LOGIN_ING: 30701 + 9,
        RC_CODE_E_SW_CHANEL_HAVE_OPEN: 30701 + 10,
        RC_CODE_E_SW_CHANEL_OPENING: 30701 + 11,
        RC_CODE_E_SW_UN_SUPPORTED: 30701 + 12,
        RC_CODE_E_SW_WAIT: 30701 + 13,
        RC_CODE_E_SW_TIME_OUT: 30701 + 14,
        RC_CODE_E_SW_INVALID_URL: 30701 + 15,
        RC_CODE_E_SW_FILE_NOT_EXIST: 30701 + 16,
        RC_CODE_E_SW_IO: 30701 + 17,
        RC_CODE_E_SW_CHANEL_TYPE: 30701 + 18,

        RC_CODE_S_HTTP_HAVE_NOTIFY: 30701 + 19,

        RC_CODE_E_SOCKET_FAIL: 30801,
        RC_CODE_E_SOCKET_CREATE_OBJECT: 30801 + 1, /**创建对象失败*/
        RC_CODE_E_SOCKET_BIND: 30801 + 2,
        RC_CODE_E_SOCKET_REGISTER_SIGNAL: 30801 + 3,
        RC_CODE_E_SOCKET_SEND: 30801 + 4,

        RC_CODE_E_INVALIDIP: 30201, /**无效的IP*/
        RC_CODE_E_INVALIDPORT: 30202, /**无效的端口号*/

        RC_CODE_E_SERVICE_NOT_START: 10501, /**服务未启动*/
        bSuccess: function (rc) {
            return this.RC_CODE_S_OK == rc;
        },
        bFaild: function (rc) {
            return this.RC_CODE_S_OK != rc;
        },
        bFailed: function (rc) {
            return this.RC_CODE_S_OK != rc;
        },
        trans: function (rc) {
            for (var iindex in this) {
                if (this[iindex] == rc) {
                    return iindex;
                }
            }
            return "";
        }
    };

    jSW.RcCodeTransInfo = {
        RC_CODE_S_OK: "操作成功", /**成功*/

        RC_CODE_E_FAIL: "失败", /**一般错误*/
        RC_CODE_E_INVALIDARG: "无效的参数", /**参数*/
        RC_CODE_E_USERNAME: "用户名错误", /**用户名*/
        RC_CODE_E_PASSWORD: "密码错误", /**密码*/
        RC_CODE_E_NOTFOUND: "未找到", /**未找到*/
        RC_CODE_E_UNSUPPORTED: "不支持", /**不支持*/
        RC_CODE_E_BUSY: "繁忙", /**繁忙*/
        RC_CODE_E_NOTINITILIZED: "未初始化", /**未初始化*/
        RC_CODE_E_TIMEOUT: "超时", /**超时*/
        RC_CODE_E_ALREADYEXIST: "已存在", /**已存在*/
        RC_CODE_E_DISCONNECTED: "与服务器未连接，或连接断开", /**未连接*/
        RC_CODE_E_OTHER: -12,


        RC_CODE_E_BVCU_FAILED: "服务器一般错误",   /*general error*/
        RC_CODE_E_BVCU_INVALIDARG: "服务器返回无效参数",         /*invalid argument*/
        RC_CODE_E_BVCU_UNSUPPORTED:  "服务器返回操作不支持",        /*unsupported functions*/
        RC_CODE_E_BVCU_ALLOCMEMFAILED: "服务器分配内存失败",    /*allocate memory failed*/
        RC_CODE_E_BVCU_MEMALIGNMENT: "服务器内存对齐未满足要求",      /*memory alignment is not satisfied*/
        RC_CODE_E_BVCU_NOTFOUND: "服务器未找到操作对象",          /*not found*/
        RC_CODE_E_BVCU_NOTALLOWED: "服务器不允许的操作",        /*the requested access is not allowed*/
        RC_CODE_E_BVCU_IO: "服务器IO错误",                /*I/O error*/
        RC_CODE_E_BVCU_EOF: "服务器遇到文件结尾",               /*End of file*/
        RC_CODE_E_BVCU_INVALIDDATA: "服务器处理数据发现数据有问题",       /*Invalid data found when processing input*/
        RC_CODE_E_BVCU_NOTIMPL: "服务器未实现",           /*not implemented*/
        RC_CODE_E_BVCU_BUSY: "服务器忙",              /*busy.deny service now*/
        RC_CODE_E_BVCU_INUSE: "服务器操作被占用",              /*device in use*/
        RC_CODE_E_BVCU_BADREQUEST: "错误的请求",        /*bad request*/
        RC_CODE_E_BVCU_AUTHORIZE_FAILED: "身份认证失败",    /*authorize failed。登录/发送命令等的OnEvent回调中使用*/
        RC_CODE_E_BVCU_BADSTATE: "服务器内部状态错误",          /*bad internal state*/
        RC_CODE_E_BVCU_NOTINITILIZED: "服务器未初始化",      /*not initialized*/
        RC_CODE_E_BVCU_FATALERROR: "服务器遇到致命错误，需要重启服务器",        /*fatal error. BVCU should be closed*/
        RC_CODE_E_BVCU_OUTOFSPACE: "服务器资源溢出",        /*out of space*/
        RC_CODE_E_BVCU_DISCONNECTED: "与服务器断开连接",      /*disconnected*/
        RC_CODE_E_BVCU_TIMEOUT: "服务器处理超时",            /*time out*/
        RC_CODE_E_BVCU_CONNECTFAILED: "连接媒体服务器失败",      /*connect failed*/
        RC_CODE_E_BVCU_ABORTED: "请求被终止处理",            /*request aborted*/
        RC_CODE_E_BVCU_THRAEDCONTEXT: "服务器上下文错误",      /*can not execute in the specified thread context*/
        RC_CODE_E_BVCU_UNAVAILABLE: "服务器服务不可达",        /*unavailable, eg: initialize a dialog with an offlined PU*/
        RC_CODE_E_BVCU_ALREADYEXIST: "服务器操作已存在",       /*already exist*/
        RC_CODE_E_BVCU_SEVERINTERNAL: "服务器内部错误",      /*Server internal error*/
        RC_CODE_E_BVCU_MAXRETRIES: "达到服务器最大重试次数",              /*达到最大重试次数*/
        RC_CODE_E_BVCU_AAA_OBJECTNOTFOUND: "用户组不存在",/*AAA 用户/用户组等不存在。SESSION_CLOSE事件的OnEvent回调中使用*/

        RC_CODE_S_BVCU_OK: "成功",                /*succeed*/
        RC_CODE_S_BVCU_IGNORE: "成功，但操作未被执行",            /*succeed,but something can not handle is ignored.*/
        RC_CODE_S_BVCU_PENDING: "操作正在等待",           /*operation is pending.*/
        RC_CODE_S_BVCU_CONTINUE: "操作未结束，后续还有数据",          /*表示后续还有FTP数据*/

        RC_CODE_S_SAV_OPEN_FAIL: "媒体数据容器打开失败",          /*SAV打开容器失败*/


        RC_CODE_E_REDIS_CONNECTED_FAIL: 30601, /**连接redis失败*/
        RC_CODE_E_REDIS_SET_FAIL: 30601 + 1, /**redis设置失败*/
        RC_CODE_E_REDIS_GET_FAIL: 30601 + 2, /**redis查询失败*/
        RC_CODE_E_REDIS_GET_NIL: 30601 + 3, /**redis查询的值为空*/

        RC_CODE_E_SW_CONNECTION_EXIT: "连接已存在", /**连接已存在*/
        RC_CODE_E_SW_CONNECTION_NOT_EXIT: "连接已存在,未退出",
        RC_CODE_E_SW_PU_NOT_EXIT: "设备未退出",
        RC_CODE_E_SW_CHANEL_NOT_EXIT: "通道已存在",
        RC_CODE_E_SW_CREATE_THREAD_FAIL: "线程创建失败", /**线程创建失败*/
        RC_CODE_E_SW_LISTENER_INIT_FAIL: "监听初始化失败", /**监听初始化失败*/
        RC_CODE_E_SW_HIREDIS_INIT_FAIL: "", /**redis初始化失败*/
        RC_CODE_E_SW_JSON_PARSE: "JSON 解析失败",
        RC_CODE_E_SW_HAVE_LOGIN: "已登录",
        RC_CODE_E_SW_LOGIN_ING: "登录中",
        RC_CODE_E_SW_CHANEL_HAVE_OPEN: "通道已打开",
        RC_CODE_E_SW_CHANEL_OPENING: "通道打开中",
        RC_CODE_E_SW_UN_SUPPORTED: "Web 中间件不支持",
        RC_CODE_E_SW_WAIT: "Web 中间件等待",
        RC_CODE_E_SW_TIME_OUT:"Web 中间件超时",
        RC_CODE_E_SW_INVALID_URL: "Web 中间件无效的URL",
        RC_CODE_E_SW_FILE_NOT_EXIST: "Web 中间件文件不存在",
        RC_CODE_E_SW_IO: "IO 错误",
        RC_CODE_E_SW_CHANEL_TYPE: "通道类型错误",

        RC_CODE_S_HTTP_HAVE_NOTIFY: 30701 + 19,

        RC_CODE_E_SOCKET_FAIL: 30801,
        RC_CODE_E_SOCKET_CREATE_OBJECT: 30801 + 1, /**创建对象失败*/
        RC_CODE_E_SOCKET_BIND: 30801 + 2,
        RC_CODE_E_SOCKET_REGISTER_SIGNAL: 30801 + 3,
        RC_CODE_E_SOCKET_SEND: 30801 + 4,

        RC_CODE_E_INVALIDIP: "无效的IP", /**无效的IP*/
        RC_CODE_E_INVALIDPORT: "无效的端口号", /**无效的端口号*/

        RC_CODE_E_SERVICE_NOT_START: "服务未启动", /**服务未启动*/
    }

    jSW.RcCodeTrans = function(rcCode){
        var rcName = jSW.RcCode.trans(rcCode);
        if(rcName.length > 0){
            return jSW.RcCodeTransInfo[rcName];
        }
        return "";
    }

    jSW.PTZCommand =
        {
            //方向操作
            BVCU_PTZ_COMMAND_UP: 0,     //向上。iParam1：unused;iParam2: 速度;iParam3:unused
            BVCU_PTZ_COMMAND_DOWN: 1,   //向下。iParam1：unused;iParam2: 速度;iParam3:unused
            BVCU_PTZ_COMMAND_LEFT: 2,  //向左。iParam1：unused;iParam2: 速度;iParam3:unused
            BVCU_PTZ_COMMAND_RIGHT: 3, //向右。iParam1：unused;iParam2: 速度;iParam3:unused
            BVCU_PTZ_COMMAND_LEFTTOP: 4,  //左上。iParam1：垂直速度;iParam2: 水平速度;iParam3:unused
            BVCU_PTZ_COMMAND_RIGHTTOP: 5,  //右上。iParam1：垂直速度;iParam2: 水平速度;iParam3:unused
            BVCU_PTZ_COMMAND_LEFTDOWN: 6,  //左下。iParam1：垂直速度;iParam2: 水平速度;iParam3:unused
            BVCU_PTZ_COMMAND_RIGHTDOWN: 7,  //右下。iParam1：垂直速度;iParam2: 水平速度;iParam3:unused

            //镜头操作
            BVCU_PTZ_COMMAND_ZOOM_INC: 8,  //增加放大倍数。iParam1：unused;iParam2: unused;iParam3:unused
            BVCU_PTZ_COMMAND_ZOOM_DEC: 9,  //减小放大倍数。iParam1：unused;iParam2: unused;iParam3:unused
            BVCU_PTZ_COMMAND_FOCUS_INC: 10, //焦距调远。iParam1：unused;iParam2: unused;iParam3:unused
            BVCU_PTZ_COMMAND_FOCUS_DEC: 11, //焦距调近。iParam1：unused;iParam2: unused;iParam3:unused
            BVCU_PTZ_COMMAND_APERTURE_INC: 12, //光圈放大。iParam1：unused;iParam2: unused;iParam3:unused
            BVCU_PTZ_COMMAND_APERTURE_DEC: 13, //光圈缩小。iParam1：unused;iParam2: unused;iParam3:unused

            //预置点操作
            BVCU_PTZ_COMMAND_PRESET_GO: 14,  //转到预置点。iParam1：预置点号;iParam2: 垂直速度;iParam3:水平速度
            BVCU_PTZ_COMMAND_PRESET_SET: 15, //把当前位置设置为预置点。iParam1：预置点号;iParam2: 预置点名;iParam3:unused
            BVCU_PTZ_COMMAND_PRESET_SETNAME: 16, //更改预置点名字。iParam1：预置点号;iParam2: 预置点名;iParam3:unused
            BVCU_PTZ_COMMAND_PRESET_DEL: 17, //删除预置点。iParam1：预置点号;iParam2: unused;iParam3:unused

            //巡航路线操作
            BVCU_PTZ_COMMAND_CRUISE_GO: 18,//启动巡航。iParam1：巡航路线号;iParam2: unused;iParam3:unused
            BVCU_PTZ_COMMAND_CRUISE_STOP: 19,//停止巡航。iParam1：巡航路线号;iParam2: unused;iParam3:unused
            BVCU_PTZ_COMMAND_CRUISE_SET: 20,//设置整个巡航路线。iParam1：巡航路线号;iParam2: BVCU_PUCFG_CRUISE指针;iParam3:unused
            BVCU_PTZ_COMMAND_CRUISE_DEL: 21,//删除巡航路线。iParam1：巡航路线号;iParam2: unused;iParam3:unused

            //辅助功能操作
            BVCU_PTZ_COMMAND_AUX: 22,//打开/关闭辅助功能开关，Param1：辅助号;iParam2: 0-关闭,1-开启;iParam3:unused

            //锁操作
            //如果锁定超过60秒后，用户没有手工解除锁定，Server会自动解除锁定。
            BVCU_PTZ_COMMAND_LOCK: 23,//锁定/解锁云台。iParam1：unused;iParam2: unused;iParam3:unused
        };

    dMgr.RegModule("jswConst", {});
})(jSW.DependencyMgr);