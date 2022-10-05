(function (dMgr) {

    function AudioCapture() {

        var everyTime = 0;
        var localStream = null;

        var handleSuccess = function (stream) {
            localStream = stream;
            var context = new AudioContext();
            var input = context.createMediaStreamSource(stream);
            var processor = context.createScriptProcessor(4096, 1, 1);
            input.connect(processor);
            processor.connect(context.destination);
            dataSendIndex = 0;
            processor.onaudioprocess = function (e) {

                if (bStartAudioCapture) {
                    if (dataSend == null) {
                        everyTime = (e.inputBuffer.sampleRate / 8000);
                        dataSend = new Int16Array(perPacketSize);
                    }

                    var szData = e.inputBuffer.getChannelData(0);
                    
                    var reSampleValue = 0, 
                    dataSize = szData.length,
                    modValue = 0;

                    for (var ii = 0; ii < dataSize; ii++) {
                        modValue = ii % everyTime;
                        if(modValue < 1){
                            reSampleValue = szData[ii] * 32768;
                            writeDataToDesArray(Math.round(reSampleValue));
                        }
                    }
                }
            }
        }

        var perPacketSize = 320;
        var iIndexFlag = 0;
        var dataSend = null;
        var iPacketHelper = 0;

        function writeDataToDesArray(valAudio) {
            dataSend[iIndexFlag] = valAudio;
            iIndexFlag++;
            iIndexFlag %= 320;
            if (iIndexFlag == 0) {
                logAudioData(dataSend)
                onAudioHasData(dataSend)
            }
        }

        function logAudioData(data) {
            if (iPacketHelper < 250) {
                iPacketHelper++;
            } else {
                console.log("audio capture anchor");
                iPacketHelper = 0;
            }
        }

        function onAudioHasData(data) {
            if (MyOnCaptureAudioData) {
                MyOnCaptureAudioData(data)
            }
        }

        function handleSuccessWithUserData(userData) {
            return function (stream) {
                handleSuccess(stream)
                onCaptureResultSuccess(userData)
                bStartAudioCapture = true
            }
        }


        function startAudio(userData) {
            if (localStream == null) {
                if (navigator.mediaDevices) {
                    navigator.mediaDevices.getUserMedia({
                            audio: true,
                            video: false
                        })
                        .then(handleSuccessWithUserData(userData))
                        .catch(function (e) {
                            console.log("Capture Audio Fail");
                            console.log(e)
                            onCaptureError(userData);
                        })
                } else {
                    (function () {
                        console.log("not support")
                        onCaptureError(userData)
                    })()
                }
            } else {
                bStartAudioCapture = true;
                onCaptureResultSuccess(userData)
            }
        }

        var bStartAudioCapture = false;
        this.StartCapture = function (userData) {
            if (bStartAudioCapture == false) {
                startAudio(userData);
            }else{
                onCaptureResultSuccess(userData)
            }
        }

        var MyOnCaptureHasResult = null,
            MyOnCaptureAudioData;
        this.SetCallback = function (onCaptureHasResult, onAudioData) {
            if (MyOnCaptureHasResult == null) {
                MyOnCaptureHasResult = onCaptureHasResult
                MyOnCaptureAudioData = onAudioData
            }
        }

        function onCaptureError(userData) {
            var cbResult = MyOnCaptureHasResult;
            if (bStartAudioCapture) {
                bStartAudioCapture = false
            }
            
            if (cbResult) {
                cbResult(jSW.RcCode.RC_CODE_E_SW_IO, userData);
            }
        }

        function onCaptureResultSuccess(userData) {
            var cbResult = MyOnCaptureHasResult;
            if (cbResult) {
                cbResult(jSW.RcCode.RC_CODE_S_OK, userData);
            }
        }


        this.StopCapture = function () {
            if (localStream) {
                bStartAudioCapture = false;
                dataSend = null
                console.log("Track Audio Has Been Stop");
            }
        }
    }

    dMgr.RegModule("AudioCapture", new AudioCapture());
    //var AudioCapture = jSW.DependencyMgr.GetModule("AudioCapture");
})(jSW.DependencyMgr);