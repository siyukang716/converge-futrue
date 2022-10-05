/**
 * HTTP NRU FILE DOWNLOAD OR UPLOAD
 */
(function (dMgr) {
    var jSWProtocol = dMgr.GetModule("jSWProtocol");
    var jSWUtils = dMgr.GetModule("jSWUtils");
    var jSWOptions = dMgr.GetModule("jSWOptions");

    function decodeUtf8(bytes) {
        var encoded = "";
        for (var i = 0; i < bytes.length; i++) {
            encoded += '%' + bytes[i].toString(16);
        }
        return decodeURIComponent(encoded);
    }

    function CreateHttpFileUrl(bDownload, desSession, nruId, remotePath, iFileSize) {
        var filename = bDownload ? remotePath.slice(remotePath.lastIndexOf('/')) : remotePath;
        var opStr = bDownload ? "download" : "upload";
        var queryWord = bDownload ? ("&nid=" + nruId) : "";
        var szEncoderUtf8 = jSWUtils.UTF162UTF8UintArray(remotePath);
        var queryInfo = "sid=" + desSession._emms.session + queryWord + "&rp=" + Uint8ToBase64(szEncoderUtf8) + "&ifs=" + iFileSize;
        var url = jSWOptions.http + "/jswfile/" + opStr + "/" + filename + "?" + "p=" + btoa(queryInfo);
        return url;
    }

    function HTTPNruFile() {

    }

    var __desSession = null;
    HTTPNruFile.prototype.OnLoginOk = function (desSession) {
        __desSession = desSession;
    }


    function AsyncJavaScriptAndXmlGet(url, cb, tag) {
        var xmlReqGet = new XMLHttpRequest();
        xmlReqGet.addEventListener("loadend", onGetReqLoaded);
        xmlReqGet.open("GET", url);
        xmlReqGet.responseType = "arraybuffer";
        xmlReqGet.userData = {
            cb: cb,
            tag: tag
        };

        xmlReqGet.send();
    }

    function onGetReqLoaded() {
        var uint8Temp = new Uint8Array(this.response);
        if (this.userData.cb) {
            this.userData.cb(this.status, uint8Temp, this.userData.tag);
        }
    }

    /**
     * options
     */
    HTTPNruFile.prototype.CreateUrl = function (obj, bDownload, nruId, rp, ifs, cb, tag) {
        var url = CreateHttpFileUrl(bDownload, __desSession, nruId, rp, ifs);
        obj["szSrcFile"] = rp;
        obj["szFile"] = url;
        return url;
    }


    /**
     * options
     */
    HTTPNruFile.prototype.Download = function (nruId, rp, ifs, cb, tag) {
        var url = CreateHttpFileUrl(true, __desSession, nruId, rp, ifs);
        AsyncJavaScriptAndXmlGet(url, cb, tag);
    }

    /**
     * options
     */
    HTTPNruFile.prototype.DownloadToLocal = function (nruId, rp, ifs, cb, tag) {
        var url = rp;
        var link = document.createElement("a");
        link.href = url;
        link.target = "_blank";
        link.click();
        return jSW.RcCode.RC_CODE_S_OK;
    }

    function onDownloadLoaded(){
        console.log("asdfasdfasdfasdf");
    }

    function AsyncJavaScriptAndXmlPost(url, data, cb, pcb, tag) {
        var xmlReqPost = new XMLHttpRequest();
        xmlReqPost.addEventListener("loadend", onPostReqLoaded);
        xmlReqPost.addEventListener("progress", onPostReqProgress);
        xmlReqPost.open("POST", url);
        xmlReqPost.userData = {
            pcb: pcb,
            cb: cb,
            tag: tag,
            iFileSize: data.size
        };
        xmlReqPost.send(data);
    }


    function onPostReqLoaded() {
        console.log(this.userData);
        if (this.userData.cb) {

            if(this.status == 201){
                var responseLocation = this.getResponseHeader("Location");
                var temp = responseLocation.slice(responseLocation.lastIndexOf("/") + 1);
    
                var tempUint8 = new Uint8Array(temp.length);
                for(var ii = 0; ii < temp.length; ii ++){
                    tempUint8[ii] = temp.charCodeAt(ii);
                }
                var resultStr = jSWUtils.Uint8Array2UTF16(tempUint8);
                responseLocation = responseLocation.replace(temp, resultStr);
    
                var szInfo = responseLocation.split("/");
                var nruId = szInfo[1];
                var szRp = "/";
                for (var i = 0; i < szInfo.length; i++) {
                    if (i > 1) {
                        szRp += (szInfo[i]);
                        if (i < (szInfo.length - 1)) {
                            szRp += "/";
                        }
                    }
                }
            }
            
            this.userData.cb(this.status, nruId, szRp, this.userData.iFileSize, this.userData.tag);
        }
    }

    function onPostReqProgress() {
        console.log(this.userData);
        if (this.userData.pcb) {
            this.userData.pcb();
        }
    }



    /**
     * options
     */
    HTTPNruFile.prototype.Upload = function (data, pcb, cb, tag) {
        var url = CreateHttpFileUrl(false, __desSession, "NRU_", data.name, 0);
        var userData = {
            pcb: pcb,
            cb: cb,
            tag: tag
        }
        AsyncJavaScriptAndXmlPost(url, data, onPostFileHasResult, onPostFileProgress, userData)
        return jSW.RcCode.RC_CODE_S_OK;
    }

    HTTPNruFile.prototype.UploadToRoot = function(data, pcb, cb, tag){
        var url = CreateHttpFileUrl(false, __desSession, "NRU_", data.name, -1);
        var userData = {
            pcb: pcb,
            cb: cb,
            tag: tag
        }
        AsyncJavaScriptAndXmlPost(url, data, onPostFileHasResult, onPostFileProgress, userData)
        return jSW.RcCode.RC_CODE_S_OK;
    }

    function onPostFileHasResult(status, nruId, path, iFileSize, tag) {
        if (tag && tag.cb) {
            var rc = (status == 201) ? jSW.RcCode.RC_CODE_S_OK : jSW.RcCode.RC_CODE_E_BUSY;
            tag.cb(rc, nruId, path, iFileSize, tag.tag);
        }
    }

    function onPostFileProgress() {

    }


    //var HTTPNruFile = jSW.DependencyMgr.ClaimModule('HTTPNruFile')
    dMgr.RegClaimedModule("HTTPNruFile", HTTPNruFile);

    //var HTTPNruFile = jSW.DependencyMgr.GetModule("HTTPNruFile");
})(jSW.DependencyMgr);