/**
 * Module Template Used To Create Module, Dependency Inject Will Support Soon
 */
(function (dMgr) {
    function _CreateJSWOptions(srcJSWOptions) {
        var aimJswOptions = {
            /**全局配置*/
            http: 'http://127.0.0.1',
            ip: '192.168.6.32',
            port: 8181,
            url: '/jswapi',
            appkey: 'FE04553E-B6D8-4CCB-AAAB-FF7F6B64E006',
            version: '1.0.1',
            calltype: 0, //jSW.CallProtoType.AUTO
            ocxWebsocketPort: 0,
            session_list: [], // Array(jSW.SWSession)
            init_options: null,
            CheckOcx: function () {
                return jSW.CallProtoType.CheckOcx(this.calltype);
            },
            CheckNotOcx: function () {
                return !this.CheckOcx();
            },
            CheckHttp: function () {
                return jSW.CallProtoType.HTTP == this.calltype;
            }
        }

        for (var iIndex in aimJswOptions) {
            srcJSWOptions[iIndex] = aimJswOptions[iIndex];
        }
    }
    dMgr.RegClaimedModuleProxy("jSWOptions", _CreateJSWOptions);
})(jSW.DependencyMgr);


(function (dMgr) {
    var jSWOptions = dMgr.GetModule("jSWOptions");
    var jSWUtils = dMgr.GetModule("jSWUtils");

    // ---- jSWUtils ----
    (function (jSWUtils) {

        jSWUtils.ActiveAMDOrModule = (function () {
            var bModuleExport = "object" == typeof exports && "undefined" != typeof module
            var bAMDExport = "function" == typeof define && define.amd

            if (bModuleExport) {
                var saveExport = {
                    exports: exports,
                    module: module
                }
                exports = module = undefined
            }

            if (bAMDExport) {
                var saveDefine = define
                define = undefined
            }


            return function () {
                if (bModuleExport) {
                    exports = saveExport.exports
                    module = saveExport.module
                }

                if (bAMDExport) {
                    define = saveDefine
                }
            }
        })();

        jSWUtils.MsgTypeToPresent = function (iMsgType) {
            for (var iIndex in proto.WEBBVCU.MSGType) {
                if (proto.WEBBVCU.MSGType[iIndex] == iMsgType) {
                    return iIndex
                }
            }
            return ""
        }

        var ForSnapshotCanvas = null
        jSWUtils.Snapshot = function (image, iWidth, iHeight) {
            if (image == null || isNaN(iWidth) || isNaN(iHeight)) {
                return null
            }

            if (ForSnapshotCanvas == null) {
                ForSnapshotCanvas = document.createElement("canvas");
            }

            var canvas = ForSnapshotCanvas;

            canvas.width = iWidth;
            canvas.height = iHeight;
            canvas.getContext('2d').drawImage(image, 0, 0, canvas.width, canvas.height);
            var strDataURL = canvas.toDataURL("image/" + ".png")

            if (strDataURL.length < 7) {
                return ""
            }

            var arr = strDataURL.split(','),
                mime = arr[0].match(/:(.*?);/)[1],
                bstr = atob(arr[1]),
                n = bstr.length,
                u8arr = new Uint8Array(n);
            while (n--) {
                u8arr[n] = bstr.charCodeAt(n);
            }
            var blob = new Blob([u8arr], {
                type: mime
            });
            var url = window.URL.createObjectURL(blob)
            return url
        }


        function decodeUtf8(bytes) {
            var encoded = "";
            for (var i = 0; i < bytes.length; i++) {
                encoded += '%' + bytes[i].toString(16);
            }
            return decodeURIComponent(encoded);
        }

        var str2utf8 = window.TextEncoder ? function (str) {
            var encoder = new TextEncoder('utf8');
            var bytes = encoder.encode(str);

            return bytes;
        } : function (str) {
            return eval('\'' + encodeURI(str).replace(/%/gm, '\\x') + '\'');
        }


        jSWUtils.Uint8Array2UTF16 = function (bytes) {
            return decodeUtf8(bytes);
        }

        jSWUtils.UTF162UTF8UintArray = function (str) {
            return str2utf8(str);
        }

        //UTF16 -> UTF8 Uint8Array
        jSWUtils.string2Uint8Array = function (str) {
            var bytes = [];
            var len, c;
            len = str.length;
            for (var i = 0; i < len; i++) {
                c = str.charCodeAt(i);
                if (c >= 0x010000 && c <= 0x10FFFF) {
                    bytes.push(((c >> 18) & 0x07) | 0xF0);
                    bytes.push(((c >> 12) & 0x3F) | 0x80);
                    bytes.push(((c >> 6) & 0x3F) | 0x80);
                    bytes.push((c & 0x3F) | 0x80);
                } else if (c >= 0x000800 && c <= 0x00FFFF) {
                    bytes.push(((c >> 12) & 0x0F) | 0xE0);
                    bytes.push(((c >> 6) & 0x3F) | 0x80);
                    bytes.push((c & 0x3F) | 0x80);
                } else if (c >= 0x000080 && c <= 0x0007FF) {
                    bytes.push(((c >> 6) & 0x1F) | 0xC0);
                    bytes.push((c & 0x3F) | 0x80);
                } else {
                    bytes.push(c & 0xFF);
                }
            }

            return new Uint8Array(bytes);
        };

        jSWUtils.getJsonString = function (jsonObj) {
            return JSON.stringify(jsonObj);
        };

        jSWUtils.getJsonObject = function (jsonStr) {
            return jQuery.parseJSON(jsonStr);
        };

        jSWUtils.isIE = function () {
            if (!!window.ActiveXObject || "ActiveXObject" in window)
                return true;
            else
                return false;
        };

        jSWUtils.supportFlash = function () {
            if (jSWUtils.isIE()) {
                try {
                    var swf1 = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
                    return true;
                } catch (e) {}
            } else {
                try {
                    var swf2 = navigator.plugins['Shockwave Flash'];
                    if (swf2 != undefined) {
                        return true;
                    }
                } catch (e) {}
            }

            return false;
        }

        jSWUtils.RetrieveMsgTypeNameByMagic = function (value) {
            for (var key in proto.WEBBVCU.MSGType) {
                if (proto.WEBBVCU.MSGType[key] == value) {
                    return key;
                }
            }
            return ""
        }

        jSWUtils.ManEmitCallback = function (options, rc, data) {
            var opts = {
                tag: options.tag
            };
            var resp = {
                emms: rc
            };
            var callback = options.callback;
            setTimeout(function () {
                callback(opts, resp, data);
            }, 50);
        }

        jSWUtils.CheckAsyncOption = function (options, afterCheck) {
            var rc = jSW.RcCode.RC_CODE_E_INVALIDARG;
            if (options && options.callback) {
                rc = afterCheck();
            }
            return rc;
        }

        jSWUtils.consoleLog = function (str, rc) {
            console.log(str + (rc == jSW.RcCode.RC_CODE_S_OK ? 'Ok' : 'Failed'));
        }

        jSWUtils.debugLog = function (str, rc) {
            if (jSWUtils._IsDebug) {
                this.consoleLog(str, rc);
            }
        }

        jSWUtils.debugLogInfo = function (str) {
            if (jSWUtils._IsDebug) {
                console.log(str);
            }
        }

        jSWUtils._IsDebug = false;

        jSWUtils.ManualAsyncReply = function (callback, rcCode, data, tag) {
            setTimeout(function () {
                callback({
                    tag: tag
                }, {
                    emms: {
                        code: rcCode
                    }
                }, data);
            }, 100);
        }

        /**
        swAjax ( {
            data: {age: 20, name: 'tony'},
            type: 'post',
            cache: false,
            dataType: 'json',
            success: function (data, status) { },
            error: function (xhr, errMsg, errorThrown) { } // errMsg: null, "timeout", "error", "notmodified"、"parsererror"
        } );*/
        jSWUtils.ajax = function (url, options) {
            jQuery.ajax(url, options);
        };

        jSWUtils.extend = function (dst, dft, src) {
            return jQuery.extend(dst, dft, src);
        };

        jSWUtils._getSessionById = function (sessionId) {
            var sessionList = jSWOptions.session_list;
            var session = null;
            var i = 0;
            for (i = 0; i < sessionList.length; i++) {
                session = sessionList[i];
                if (session && session._p_emms.getSession()) {
                    if (sessionId == session._p_emms.getSession()) {
                        return session;
                    }
                }
            }

            return null;
        };

        jSWUtils.generateUUID = function () {
            var d = new Date().getTime();
            var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
            return uuid;
        };

        jSWUtils.FilterPath = function (path) {
            var srcPath = path;
            srcPath = srcPath.replace("\:", "");
            srcPath = srcPath.replace(/\\\\/g, "/");
            srcPath = srcPath.replace(/\\/g, "/");
            var aa = srcPath.split("/");
            var desPath = "";
            for (var iIndex = 0; iIndex < aa.length; iIndex++) {
                if (iIndex != 1) {
                    desPath += aa[iIndex];
                    if (iIndex != aa.length - 1) {
                        desPath += "/";
                    }
                }
            }
            return desPath;
        }

        jSWUtils.PathParse = function (path) {
            var srcPath = path;
            srcPath = srcPath.replace("\:", "");
            srcPath = srcPath.replace(/\\\\/g, "/");
            srcPath = srcPath.replace(/\\/g, "/");
            var aa = srcPath.split("/");
            var fileName = aa[aa.length - 1];
            var bb = fileName.split("_");
            return {
                szpuid: bb[0] + "_" + bb[1],
                ichannelindex: bb[2]
            };
        }

        jSWUtils.CheckOptionsCallback = function (options, afterHandle, scope) {
            var errInfo = ""
            if (options == null) {
                errInfo = "options is null"
            }

            if (typeof options == "undefined") {
                errInfo = "options is undefined"
            }

            if (typeof options.callback != "function") {
                errInfo = "options callback is" + typeof options.callback
            }

            if (typeof afterHandle != "function") {
                errInfo = "afterHandle " + typeof afterHandle
            }

            if (errInfo.length != "") {
                return jSW.RcCode.RC_CODE_E_FAIL
            }

            return afterHandle.bind(scope)()
        }

    })(jSWUtils);


    (function (mMgr) {
        var bOcxWs = false;
        mMgr.bUseOcx = function () {
            return (bOcxWs) && jSWOptions.CheckOcx();
        }
        mMgr.bWsTest = function () {
            return false;
        }

        var bSeted = false;
        mMgr.setBUseOcxWs = function (bUse) {
            if (!bSeted) {
                bSeted = true;
                bOcxWs = bUse;
            } else {
                if (bOcxWs != bUse) {
                    console.log("You Cann't Change The Ws Mode Right Now");
                }
            }
        }

        var bLoaded = false,
            bLoading = false;
        var swfobjectDir = "modules/oxWebSocket.js";
        mMgr.loadWebSocket = function (onLoadWsHasResult, session, tag) {
            if (mMgr.bUseOcx()) {
                if (!bLoaded && !bLoading) {
                    bLoading = true;
                    var brc = jSW.DependencyMgr.loadDependsProxy([swfobjectDir], null, function () {
                        console.log("using ocx websocket");
                        onLoadWsHasResult(session, tag, ocxWebSocketClass);
                        bLoaded = true;
                    }, null);
                    if (!brc) {
                        console.error("bad ocxWebsocketLoad");
                    }
                } else if (bLoaded) {
                    onLoadWsHasResult(session, tag, ocxWebSocketClass);
                }
            } else {
                console.log("using broswer websocket");
                onLoadWsHasResult(session, tag, WebSocket);
            }
        }

        mMgr.oWsOpen = function (ws) {
            if (this.bUseOcx()) {
                ws.open();
            }
        }

        var _ocxWsModuleTag = null,
            ocxWebSocketClass = null;
        mMgr.regOcxWebSocket = function (cWS, tag) {
            ocxWebSocketClass = cWS;
            ocxWebSocketClass.SetOcxHandle(_ocxHandle);
            _ocxWsModuleTag = tag;
        }

        mMgr.GetOcxWsTag = function () {
            return _ocxWsModuleTag;
        }

        var _ocxHandle = null;
        mMgr.RegOcxHandle = function (ocxHanlde) {
            _ocxHandle = ocxHanlde;
        }
    })(jSWUtils._mMgr = jSWUtils._mMgr || {});


    //var HTTPNruFile = jSW.DependencyMgr.ClaimModule('HTTPNruFile')
    dMgr.RegClaimedModule("jSWUtils");

    //var PuLoad = jSW.DependencyMgr.GetModule("PuLoad");
})(jSW.DependencyMgr);



(function (dMgr) {
    var jSWUtils = dMgr.GetModule("jSWUtils");

    // ---- SWPlayer  播放器, 录像回放----
    jSW.SWPlayer = function (parent, options) {
        this._parent = parent;

        this._nru_id = options.nruId;
        this._file = options.file;
        this._dialog_id = options.dialogId;
        this._parent_div = options.parentDiv;
        this._player_id = options.playerId;
        this._time_begin = options.time_begin;
        this._time_end = options.time_end;
        this._bar = options.bar;

        this._bar.restTime(this.swGetDuration() * 1000);
        this._bar.setUserData(this);
        this._closecallback = options.callback;
        this._tag = options.tag;
    }

    jSW.SWPlayer.prototype = {
        _parent: null,
        /** session */
        _file: null,
        /** 文件 */
        _nru_id: null,
        /** nru id*/
        _dialog_id: -1,
        /** 会话ID, 唯一标识 */
        _player_id: null,
        /** 播放器ID */
        _parent_div: null,
        /** 父div */
        _current_pts: -1,
        _time_begin: -1,
        _time_end: -1,
        _bar: null,

        _on_update_pts: function (pts) {
            this._current_pts = pts;

            // var date = new Date(pts)
            // console.log('pts: ' + pts + ' date: ' + date)
            this._bar.changeBar(this.swGetCurrentTime() * 1000);
        },

        _on_open_has_result: function (rc) {
            if (jSW.RcCode.bFailed(rc)) {
                this.swClose({})
            }
        },

        /** 视频时长 */
        swGetDuration: function () {
            return this._time_end - this._time_begin;
        },

        /** 当前播放进度 */
        swGetCurrentTime: function () {
            if (this._current_pts == -1) {
                return 0;
            }
            return Math.round(this._current_pts / 1000) - this._time_begin; //8时区, pts是北京时间, time_begin是utc时间
        },

        /** 单帧步进 */
        swStep: function (options) {

            options = jSWUtils.extend({}, {}, options || {});

            options.cmdtype = 'STEP'
            options.dialogId = this._dialog_id;

            return this._parent.swRecordPlayCtrl(options);
        },

        /** 跳转 */
        swJump: function (options) {

            options = jSWUtils.extend({}, {}, options || {});
            options.cmdtype = 'PLAY';
            options.dialogId = this._dialog_id;

            return this._parent.swRecordPlayCtrl(options);
        },

        /** 播放 */
        swPlay: function (options) {

            options = jSWUtils.extend({}, {}, options || {});
            options.cmdtype = 'PLAY';
            options.dialogId = this._dialog_id;

            return this._parent.swRecordPlayCtrl(options);
        },

        /** 暂停 */
        swPause: function (options) {

            options = jSWUtils.extend({}, {}, options || {});
            options.cmdtype = 'PAUSE';
            options.dialogId = this._dialog_id;

            return this._parent.swRecordPlayCtrl(options);
        },

        /** 关闭 */
        swClose: function (options) {

            options = jSWUtils.extend({}, {}, options || {});
            options.cmdtype = 'TEARDOWN';
            options.dialogId = this._dialog_id;

            var rc = this._parent.swRecordPlayCtrl(options);

            var playObj = document.getElementById(this._parent_div.playObjectId)
            if (playObj) {
                jSW.SWVideoChanel._Ocx_PlayObjMgr.FreePlayObj(playObj)
            }
            this._parent_div.playObjectId = ""

            this._parent_div.innerHTML = '';
            var eleScript = document.getElementById(this._parent_div.bv_playBackOScriptId)
            if (eleScript) {
                eleScript.parentElement.removeChild(eleScript)
            }
            this._parent_div.bv_playBackOScriptId = ""

            this._parent._swDelRecordPlayer(options.dialogId)
            if (this._closecallback) {
                this._closecallback(this._tag);
            }
            return rc
        }
    }

    // ---- SWCallbackManager ----
    jSW.SWCallbackManager = function (owner, events) {
        this.owner = owner;
        this.callbacks = {};
        for (var i = 0; i < events.length; i++) {
            this.callbacks[events[i]] = [];
        }
    }

    jSW.SWCallbackManager.prototype = {
        // The element on which callbacks will be triggered.
        owner: null,

        // An object of callbacks in the form
        //
        //     { event: function }
        callbacks: null,

        // Add a callback to this object - where the `event` is a string of
        // the event name and `callback` is a function.
        addCallback: function (event, callback) {
            if (typeof (callback) == 'function' && this.callbacks[event]) {
                this.callbacks[event].push(callback);
            }
        },

        // Remove a callback. The given function needs to be equal (`===`) to
        // the callback added in `addCallback`, so named functions should be
        // used as callbacks.
        removeCallback: function (event, callback) {
            if (typeof (callback) == 'function' && this.callbacks[event]) {
                var cbs = this.callbacks[event],
                    len = cbs.length;
                for (var i = 0; i < len; i++) {
                    if (cbs[i] === callback) {
                        cbs.splice(i, 1);
                        break;
                    }
                }
            }
        },

        // Trigger a callback, passing it an object or string from the second
        // argument.
        dispatchCallback: function (event, message) {
            if (this.callbacks[event]) {
                for (var i = 0; i < this.callbacks[event].length; i += 1) {
                    try {
                        this.callbacks[event][i](this.owner, event, message);
                    } catch (e) {
                        console.error(e);
                        // meh
                    }
                }
            }
        }
    }

})(jSW.DependencyMgr)