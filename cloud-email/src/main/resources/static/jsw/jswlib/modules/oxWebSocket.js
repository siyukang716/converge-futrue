(function () {
    var jSWUtils = jSW.DependencyMgr.GetModule("jSWUtils");
    jSW._mMgr_OCX = jSWUtils._mMgr;

    var _ocxHandle = null;
    var szOcxWs = [];
    var ocxWebSocket = function (url) {
        this._url = url.replace(/ws:\/\//g, "");
        this.onopen = null;
        this.onerror = null;
        this.onmessage = null;
        this.onclose = null;
        this.m_id = -1;
        this.readyState = 0;
    }

    ocxWebSocket.prototype = {
        open: function () {
            try {
                var rt = _ocxHandle.oxWebSocket("connect", this._url);
                //var rt = _ocxHandle.oxWebSocket("connect", "127.0.0.1:49537");
                if (rt < 0) {
                    this.onerror(rt);
                } else {
                    this.m_id = rt;
                    szOcxWs[this.m_id] = this;
                }
            }
            catch (e) {
                this.onerror(e);
            }
        },
        onEvent: function (eventType, data) {
            switch (eventType) {
                case "onConnected":
                    this.readyState = 1;
                    this.onopen();
                    break;

                case "wsOnClosed":
                    szOcxWs[this.m_id] = null;
                    var e = {
                        code: -1,
                        reason: "no reason"
                    }
                    if (this.readyState == 1 || this.readyState == 0) {
                        this.readyState = 2;
                        this.onclose(e);
                    }
                    break;

                case "wsOnMessage":
                    var uint8arrayData = Base64Decode(data);
                    var message = {
                        data: uint8arrayData
                    };
                    this.onmessage(message);
                    break;
            }
        },
        send: function (data) {
            if (this.m_id != -1) {
                var desdata = Uint8ToBase64(data);
                _ocxHandle.oxWebSocket("send:" + this.m_id, desdata);
            }
        },
        close: function (bNoCallback) {
            if (this.m_id != -1) {
                if (bNoCallback) {
                    _ocxHandle.oxWebSocket("close:" + this.m_id, "1");
                } else {
                    _ocxHandle.oxWebSocket("close:" + this.m_id, "0");
                }
                szOcxWs[this.m_id] = null;
            }
        }
    }
    
    ocxWebSocket.SetOcxHandle = function (ocxHandle) {
        _ocxHandle = ocxHandle;
        var jswOcxWsId = "jsw_ocx_ws_handle";
        var oScript = document.getElementById(jswOcxWsId);
        if (oScript == null) {
            oScript = document.createElement("script");
            oScript.type = "text/javascript";
            oScript.setAttribute('for', 'jsw_ocx');
            oScript.setAttribute('event', 'oxWebSocketEvent(rc, data)');
            oScript.text = 'jSW._mMgr_OCX.GetOcxWsTag()[0](rc, data);';
            oScript.id = jswOcxWsId;
        }
        var parentEle = jSW && jSW["DependencyMgr"] ? jSW.DependencyMgr.GetJsEleContainer() : document.body;
        parentEle.appendChild(oScript);
    }

    function gbEvent(handle, data) {
        var strhandle = handle;
        var eventType = strhandle.split(':')[0];
        var ihandle = strhandle.split(':')[1];
        var ws = jSWUtils._mMgr.GetOcxWsTag()[1][ihandle];
        if (ws) {
            ws.onEvent(eventType, data);
        }
    }

    jSWUtils._mMgr.regOcxWebSocket(ocxWebSocket, [gbEvent, szOcxWs]);

    window.addEventListener("unload", function () {
    });
})();