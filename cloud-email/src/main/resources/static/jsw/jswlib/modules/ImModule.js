(function (dMgr) {
    var jSWProtocol = dMgr.GetModule("jSWProtocol");
    var jSWOptions = dMgr.GetModule("jSWOptions");
    var HTTPNruFile = jSW.DependencyMgr.ClaimModule('HTTPNruFile');

    function innerParsePbIm(desPayload) {
        var data = proto.WEBBVCU.IMMsgPks.deserializeBinary(arguments[0]);
        data = jSWProtocol.IMMsgPksPb2_Json(data);
        return data;
    }


    function innerDownloadBuildData(msgItem) {
        var desMsgItem = {
            iType: msgItem.iType,
            data: msgItem.stFile.szFile,
            nruid: msgItem.stFile.szID,
            cmdMsgType: -1
        }

        switch (msgItem.iType) {
            case jSWProtocol.IMMSGTypes.PICTURE:
                desMsgItem.cmdMsgType = proto.WEBBVCU.MSGType.WEB_CONF_IM_PIC_MSG;
                break;
            case jSWProtocol.IMMSGTypes.AUDIO:
            case jSWProtocol.IMMSGTypes.CONF_AUDIO:
                desMsgItem.iType = jSWProtocol.IMMSGTypes.FILE;
                desMsgItem.cmdMsgType = proto.WEBBVCU.MSGType.WEB_CONF_IM_FILE_MSG;
                break;
        }
        return desMsgItem;
    }

    /**
     * 
     * @param {*} inner_session 
     * @param {*} options 
     */
    function innerSwHttpRecvFile(inner_session, options) {
        return HTTPNruFile.DownloadToLocal(options.msgitems[0].nruid, options.msgitems[0].data);
    }


    function OnHTTPPostFileHasResult(rc, nruId, rp, iFileSize, tag) {
        var _session = tag.session;
        var targetId = tag.targetId;
        var iMsgType = tag.iMsgType;
        var iTargetIndex = tag.targetIndex
        var cb = tag.cb;
        innerImAfterPostFileSendMsg(_session, targetId, iMsgType, nruId, rp, iFileSize, cb, {}, tag.tag, rc, iTargetIndex);
    }

    function OnHTTPPostFileProgress(rc, nruId, rp, iFileSize, tag) {
        var _session = tag;
        innerImAfterPostMsg(_session, nruId, 4, rp, iFileSize, "", tag);
    }


    function UploadMsgItem(inner_session, targetId, targetIndex, msgItem, cb, pcb, tag) {
        var userData = {
            cb: cb,
            pcb: pcb,
            tag: tag,
            targetId: targetId,
            iMsgType: msgItem.iType,
            session: inner_session,
            targetIndex: targetIndex
        };

        var rc = HTTPNruFile.Upload(
            msgItem.data,
            OnHTTPPostFileProgress,
            OnHTTPPostFileHasResult,
            userData);
        return rc;
    }


    function CheckMsgItemsContainsFile(MsgItems) {
        if (MsgItems[0].iType == jSWProtocol.IMMSGTypes.FILE ||
            MsgItems[0].iType == jSWProtocol.IMMSGTypes.PICTURE ||
            MsgItems[0].iType == jSWProtocol.IMMSGTypes.CONF_AUDIO) {
                return true;
        }
        return false;
    }


    /**
     * 
     * @param {*} inner_session SwSession
     * @param {*} options 
     * @param {*} imsgtype 
     * @param {*} _dhcb single ws request multi callback
     * @param {*} imsgtypeAppend strange attr
     */
    function innerSwConfIMSend(inner_session, options, imsgtype, _dhcb, imsgtypeAppend, bNotSendFile) {
        var rc = jSWProtocol.CheckOptions(imsgtype, options);
        if (rc != jSW.RcCode.RC_CODE_S_OK) {
            return rc;
        }

        if (!options._targetid && options.targetid) {
            options._targetid = options.targetid;
        }


        if (typeof options.iapplierid == 'undefined'){
            options.iapplierid = -1;
        }


        if (!CheckMsgItemsContainsFile(options.msgitems) || bNotSendFile || jSWOptions.CheckOcx()) {
            var payload = jSWProtocol.JsIMMSG2Pb(options.msgitems, options.pcallback);
            var rcallback = payload["rcallback"];

            

            rc = jSWProtocol.SendRequest({
                session: inner_session,
                msgtype: imsgtype,
                target: options._targetid,
                payload: payload,
                targetIndex: options.iapplierid,
                cmd: jSWProtocol.RequestHeader.confimsend.cmd,
                callback: options.callback,
                callbackrelay: rcallback,
                tag: options.tag,
                _dhcb: _dhcb,
                _dhcbtag: {
                    imsgtype: imsgtypeAppend ? imsgtypeAppend : payload.iitemmsgtype,
                    msgitems: options.msgitems
                }
            })
        } else {
            rc = UploadMsgItem(inner_session, options._targetid, options.iapplierid, options.msgitems[0],
                options.callback, options.pcallback, options.tag);
        }

        return rc;
    }

    function httpInnerDownloadMsgPCb(statusCode, arrayBufData, tag) {
        if (statusCode == 200) {
            var desImInfo = tag.info;
            var afterCb = tag.cb;
            var desTag = tag.tag;

            if (desImInfo.szmsg[0].iType == jSWProtocol.IMMSGTypes.PICTURE) {
                var uint8ArrayTemp = new Uint8Array(arrayBufData);
                var bufferTemp = "";
                var b64Temp = Uint8ToBase64(uint8ArrayTemp);
                desImInfo.szmsg[0].stFile.szFile = "data:image/jpeg;base64," + b64Temp;
            } else if (desImInfo.szmsg[0].iType == jSWProtocol.IMMSGTypes.AUDIO ||
                desImInfo.szmsg[0].iType == jSWProtocol.IMMSGTypes.CONF_AUDIO) {
                var blob = new Blob([arrayBufData], {
                    type: "audio/mpeg"
                });
                desImInfo.szmsg[0].stFile.szFile = window.URL.createObjectURL(blob)
            }

            afterCb(desImInfo, desTag);
        }
    }


    function innerDownloadMsgPCb(options, response, data) {
        if (data.sEvent.iEvent == jSW.FileTransEvent.FILE_TRANS_CLOSE) {
            var desImInfo = options.tag.info;
            var desTag = options.tag.tag;
            var afterCb = options.tag.cb;

            if (desImInfo.szmsg[0].iType == jSWProtocol.IMMSGTypes.PICTURE) {
                desImInfo.szmsg[0].stFile.szFile = data.sEvent.imgbase64;
            } else if (desImInfo.szmsg[0].iType == jSWProtocol.IMMSGTypes.AUDIO ||
                desImInfo.szmsg[0].iType == jSWProtocol.IMMSGTypes.CONF_AUDIO) {
                desImInfo.szmsg[0].stFile.szFile = data.sEvent.szLocalFilePath;
            }

            afterCb(desImInfo, desTag);
        }
    }


    function innerDownloadMsg(inner_session, imInfo, afterCb, tag) {
        var msgitem = innerDownloadBuildData(imInfo.szmsg[0]);
        var options = {
            msgitems: [msgitem],
            callback: innerCbNothingToDo,
            pcallback: innerDownloadMsgPCb,
            tag: {
                info: imInfo,
                tag: tag,
                cb: afterCb
            }
        }

        var rc = jSW.RcCode.RC_CODE_S_OK;

        if (jSWOptions.CheckHttp()) {
            var stFile = imInfo.szmsg[0].stFile;
            HTTPNruFile.Download(stFile.szID, stFile.szFile, stFile.iFileSize, httpInnerDownloadMsgPCb, options.tag);
        } else {
            rc = innerSwConfIMSend(inner_session, options, msgitem.cmdMsgType, innerCbNothingToDo);
        }
        return rc;
    }

    function innerCbNothingToDo() {

    }

    function innerCheckForImDownload(inner_session, imInfo, afterCb, tag) {
        if (imInfo.szmsg[0].iType == jSWProtocol.IMMSGTypes.PICTURE ||
            imInfo.szmsg[0].iType == jSWProtocol.IMMSGTypes.AUDIO ||
            imInfo.szmsg[0].iType == jSWProtocol.IMMSGTypes.CONF_AUDIO) {
            innerDownloadMsg(inner_session, imInfo, afterCb, tag);
        } else {
            afterCb(imInfo, tag);
        }
    }

    function innerImAfterPostFileSendMsg(inner_session, targetId, iMsgType, nruId, rp, iFileSize, cb, data, tag, sendRc, iTargetIndex) {
        var rc;
        if (sendRc != jSW.RcCode.RC_CODE_S_OK) {
            var options = {
                tag: tag
            };
            var response = {
                emms: {
                    code: sendRc
                }
            };
            cb(options, response);
        } else {
            rc = innerSwConfIMSend(
                inner_session, {
                    msgitems: [{
                        iType: iMsgType,
                        data: rp,
                        nruid: nruId,
                        iduration: iFileSize
                    }],
                    iapplierid: iTargetIndex,
                    callback: function (options, response, mydata) {
                        if (options.tag.cb) {
                            var targetid = options.tag.targetid;
                            var data = options.tag.data;
                            var tag = options.tag.tag;
                            var my_options = {
                                tag: tag
                            };
                            options.tag.cb(my_options, response, data);
                        }
                    },
                    tag: {
                        cb: cb,
                        targetid: targetId,
                        data: data,
                        tag: tag
                    },
                    _targetid: targetId
                },
                proto.WEBBVCU.MSGType.WEB_BVCU_CONF_IM_MSG,
                innerCbNothingToDo,
                null,
                true
            );
        }


        return rc;
    }

    /**
     * 
     * @param {*} inner_session 
     * @param {*} data   
     * data = {
     * sEvent: proto.WEBBVCU.FileTransInfo,
     * sProgressInfo: null
     * };
     * @param {*} attchdata 
     * @param {*} targetid 
     */
    function innerImAfterSendMsg(inner_session, data, attchdata, targetid) {
        var rp = data.sEvent.szRemoteFilePath;
        var nruid = data.sEvent.nruid;
        var iMsgType = attchdata._dhcbtag["imsgtype"];
        var iFileSize = attchdata._dhcbtag["msgitems"][0]["iduration"];

        var rc = innerImAfterPostFileSendMsg(inner_session, targetid, iMsgType,
            nruid, rp, iFileSize,
            attchdata.callback, data, attchdata,
            jSW.RcCode.RC_CODE_S_OK
        );

        return rc;
    }

    jSW.SWSession.SwImModule = function (session, confmanager, pusmodule) {
        this._session = session;
        this._confmanger = confmanager;
        this._pusModules = pusmodule;
        instance_swImModule = this;
    }

    jSW.SWSession.SwImModule.prototype = {
        _swConfIMSend: function (options, imsgtype, cbk) {
            var rc = innerSwConfIMSend(this._session, options, imsgtype, cbk);
            return rc;
        },

        _SwConfFileRecv: function (options) {
            var rc;
            if (jSWOptions.CheckNotOcx()) {
                //console.log("only support ocx");
                //return jSW.RcCode.RC_CODE_E_BVCU_UNSUPPORTED;
                rc = innerSwHttpRecvFile(this._session, options);
            } else {
                rc = innerSwConfIMSend(this._session, options, proto.WEBBVCU.MSGType.WEB_CONF_IM_FILE_MSG, innerCbNothingToDo);
                //immodule._swConfIMSend(options, proto.WEBBVCU.MSGType.             WEB_CONF_IM_FILE_MSG, this._dhcbConIMSend); 
            }

            return rc;
        },

        //通知有消息来了
        _imOnNotify: function (msgType, cmd, response) {
            var imInfo = innerParsePbIm(response.getPayload());
            var tag = {
                imModule: this,
                response: response
            };

            if (jSWOptions.CheckHttp() && imInfo.szmsg) {
                var ___session = this._session;
                imInfo.szmsg.forEach(function (ele) {
                    if (ele.iType == jSWProtocol.IMMSGTypes.FILE) {
                        HTTPNruFile.CreateUrl(ele.stFile, true,
                            ele.stFile.szID, ele.stFile.szFile, ele.stFile.iFileSize, ele.stFile.szFile);
                    }
                })
            }

            innerCheckForImDownload(this._session, imInfo, function (imInfo, tag) {
                var targetid = imInfo.szTargetId;
                var imModule = tag.imModule;
                if (targetid.indexOf("CONF") == 0) {
                    imModule._confmanger._onCmrRecvImMsg(imInfo);
                } else {
                    var response = tag.response;
                    imModule._pusModules._onNotifyImMsg(imInfo, response);
                }
            }, tag);
        },

        //发送图片或文件上传成功
        _imMsgProgress: function (msgType, data, response) {
            var targetid = response.getTargetid();
            innerImAfterSendMsg(this._session, data.data, data.attchdata, targetid);
        }
    };

    dMgr.RegModule("ImModule", {});
})(jSW.DependencyMgr);