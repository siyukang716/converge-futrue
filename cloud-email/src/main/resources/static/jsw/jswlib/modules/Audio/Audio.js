(function (dMgr) {
    var wss = null;
    var myBReady = false;
    var arraybuffer16 = new Int16Array();

    function Audio() {

        this.CreateWs = function (myws, url, cb, tag) {
            _CreateWs(_CreateWsUrl(url), myws, cb, tag);
        }

        this.DestroyWs = function () {
            if(wss){
                wss.close();
            }
            myPcmPlayer = null;
            myBReady = false;
        }

        this.SendRegDataWs = function (data) {
            if(wss){
                try{
                    wss.send(data);
                    return jSW.RcCode.RC_CODE_S_OK;
                }catch(e){
                    return jSW.RcCode.RC_CODE_E_FAIL;
                }
            }
            return jSW.RcCode.RC_CODE_S_OK;
        }

        function _CreateWsUrl(url) {
            return (url);
        }

        function _CreateWs(url, myws, cb, tag) {
            if(wss == null){
                wss = new myws(url);
                wss.binaryType = 'arraybuffer'
                wss.onopen = _onOpen;
                wss.onerror = _onError;
                wss.onmessage = _onMessage;
                wss.onclose = _onClose;
                SetCbInfoToWss(cb, tag);
            }else{
                SetCbInfoToWss(cb, tag);
                invokeCbInfoFromWss();
            }
        }

        function SetCbInfoToWss(cb, tag){
            if(wss){
                wss.createResultCb = cb;
                wss.createResultCbTag = tag;
            }
        }

        function invokeCbInfoFromWss(){
            if(wss){
                if(wss.createResultCb){
                    wss.createResultCb(wss.createResultCbTag);
                }
            }
        }

        var bufferKeepalive = new ArrayBuffer(1); bufferKeepalive[0] = 0;
        function wsKeepAliveTick() {
            wss.send(bufferKeepalive);
            console.log("Audio Ws KeepAlive");
        }

        var intervalId = -1;
        function startWsKeepalive() {
            intervalId = setInterval(wsKeepAliveTick, 20000);
        }

        function stopWsKeepalive() {
            if (intervalId > 0) {
                clearInterval(intervalId);
                intervalId = -1;
            }
        }

        function _onOpen() {
            console.log('Audio http websoket connect success!');
            startWsKeepalive();
            invokeCbInfoFromWss();
        }

        function _onError(e) {
            console.log('Audio http websoket error' + e);
        }

        function _onClose(e) {
            console.log('Audio http websoket close' + e);
            stopWsKeepalive();
            wss = null;
        }

        function _onMessage(msg) {
            if (myBReady) {
                onAudioPCM(msg.data);
            } else {
                myBReady = true;
                audioPlayInit();
            }
        }

        function onAudioPCM(pcm) {
            var data = new Uint8Array(pcm);
            myPcmPlayer.feed(data);
        }

        var myPcmPlayer = null;
        function audioPlayInit() {
            if (myPcmPlayer == null) {
                myPcmPlayer = new PCMPlayer({
                    encoding: '16bitInt',
                    channels: 1,
                    sampleRate: 8000,
                    flushingTime: 1000
                });
            }
        }
    }


    var audioInstance = new Audio();

    dMgr.RegModule("Audio", audioInstance);
    //var audio = jSW.DependencyMgr.GetModule("Audio");
})(jSW.DependencyMgr);