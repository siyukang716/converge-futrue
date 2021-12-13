/**
 * Module Template Used To Create Module, Dependency Inject Will Support Soon
 */
(function (dMgr) {
    var jSWProtocol = dMgr.GetModule("jSWProtocol");
    //var jSWUtils = dMgr.GetModule("jSWUtils");
    var jSWOptions = dMgr.GetModule("jSWOptions");

    var _audioCapture = null;

    function GetAudioCapture() {
        if (null == _audioCapture) {
            _audioCapture = jSW.DependencyMgr.GetModule("AudioCapture");
            _audioCapture.SetCallback(OnCaptureAudioHasResult, OnAudioData)
        }
        return _audioCapture;
    }

    var _audioNetIO = null;

    function GetAudioNetIO() {
        if (null == _audioNetIO) {
            _audioNetIO = jSW.DependencyMgr.GetModule("Audio");
        }
        return _audioNetIO;
    }


    function ConnectAudioWs(afterHandle, tag) {
        if (jSWOptions.CheckNotOcx()) {
            var audio = jSW.DependencyMgr.GetModule("Audio");
            var url = jSWProtocol.GetWsUrl();
            audio.CreateWs(WebSocket, url, function () {
                var request = new jSWProtocol.JsonParamNoAttach(__session._p_emms,
                    proto.WEBBVCU.MSGType.WEB_REG_WEBSOCKET_DATA);
                var data = request.serializeBinary();
                GetAudioNetIO().SendRegDataWs(data.buffer);
                if (afterHandle) {
                    afterHandle(tag);
                }
            }, tag);
        }
    }

    function AudioMgr() {
        this.iOpenCount = 0;
    }

    AudioMgr.prototype.ConnectWsAudio = ConnectAudioWs

    var __session = null;
    AudioMgr.prototype.OnLoginOk = function (_session) {
        __session = _session;
    }

    AudioMgr.prototype.StartAudio = function (params) {
        let scope = this
        ConnectAudioWs(function () {
            var audioCapture = GetAudioCapture();
            audioCapture.StartCapture(PutCbkInfoToUserData(params));
            scope.iOpenCount++
        })
    }

    AudioMgr.prototype.StopAudio = function () {
        let scope = this
        scope.iOpenCount--
        if (scope.iOpenCount == 0) {
            var audioCapture = GetAudioCapture();
            audioCapture.StopCapture();
        }
    }

    function OnAudioData(data) {
        var sRc = GetAudioNetIO().SendRegDataWs(data.buffer);
        if (jSW.RcCode.bFailed(sRc)) {
            GetAudioCapture().StopCapture();
        }
    }

    function PutCbkInfoToUserData(params) {
        return {
            cb: params ? params.cb : null,
            userData: params ? params.userData : null
        }
    }

    function DispatchUserData(rc, userData) {
        if (userData && userData.cb) {
            userData.cb(rc, userData)
        }
    }

    function OnCaptureAudioHasResult(rc, userData) {
        DispatchUserData(rc, userData)
    }

    //var HTTPNruFile = jSW.DependencyMgr.ClaimModule('HTTPNruFile')
    dMgr.RegClaimedModule("AudioMgr", AudioMgr);

    //var AudioMgr = jSW.DependencyMgr.GetModule("AudioMgr");
})(jSW.DependencyMgr);