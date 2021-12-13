(function (dMgr) {
    var INT_MINUS1 = 4294967295

    var desModule = function () {
        var jSWProtocol = dMgr.GetModule("jSWProtocol");
        var jSWUtils = dMgr.GetModule("jSWUtils");
        var jSWOptions = dMgr.GetModule("jSWOptions");
        var PuLoad = dMgr.GetModule('PuLoad');
        var HTTPNruFile = dMgr.ClaimModule('HTTPNruFile');
        var AudioMgr = dMgr.ClaimModule('AudioMgr');


        this.handleNotify = function (desSession, options, response) {
            var msgType = response.getMsgtype();

            var data = {
                target: null,
                targetindex: -1,
                content: null
            };

            switch (msgType) {
                case proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_PU_ADDGROUP:
                case proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_PU_DELGROUP:
                case proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_PU_MODGROUP: {
                    if (response.getPayload().length > 0) {
                        data = proto.BVCU.PUConfig.GroupInfo.deserializeBinary(response.getPayload()).toObject();

                        if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_PU_DELGROUP) {
                            data = {
                                groupid: data.szid
                            }
                        }
                    }

                    options.cmd = jSWUtils.RetrieveMsgTypeNameByMagic(msgType);
                }
                break;
            }


            if (proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_DIALOG_CLOSED_ACCIDENTTLY == msgType) {
                var closeInfo = proto.WEBBVCU.BVCU_DialogParam.deserializeBinary(response.getPayload_asU8()).toObject()

                var targetDlg = desSession._commDlgMgr.get(closeInfo.hdlg)
                if (targetDlg == null) {
                    return
                }

                var chanel = desSession.swGetPuChanel(closeInfo.id, closeInfo.major);
                desSession._commDlgMgr.free(closeInfo.hdlg)
                options.cmd = "notify_channel_closed"
                data = {
                    chanel: chanel,
                    hdlg: closeInfo.hdlg,
                    beclosedbyuser: closeInfo.proto == 0 ? false : true
                }
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_PUGPS_CHANNEL_DATA) {

                var targetId = response.getTargetid()
                var targetIndex = response.getTargetindex()

                var gpsdataResponse = proto.BVCU.PUConfig.GPSData.deserializeBinary(response.getPayload());
                var gpsData = {
                    lat: jSWProtocol.ParseNumberUint2Int(gpsdataResponse.getIlatitude()),
                    long: jSWProtocol.ParseNumberUint2Int(gpsdataResponse.getIlongitude()),
                    height: gpsdataResponse.getIheight(),
                    angle: gpsdataResponse.getIangle(),
                    speed: gpsdataResponse.getIspeed(),
                    starcount: jSWProtocol.ParseNumberUint2Int(gpsdataResponse.getIstarcount()),
                    antennastate: gpsdataResponse.getBantennastate(),
                    orientationstate: gpsdataResponse.getBorientationstate(),
                    satellitesignal: gpsdataResponse.getIsatellitesignal(),
                    sttime: jSWProtocol.Walltime2Json(gpsdataResponse.getSttime().toObject())
                };

                desSession._OnNotifyGpsData(targetId, targetIndex, gpsData)
                return;
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_RECORDSTATUS) {
                options.cmd = jSWProtocol.RequestHeader.notifys.notify_pu_record_status.cmd;
                data.target = response.getTargetid()
                data.content = proto.BVCU.PUConfig.RecordStatus.deserializeBinary(response.getPayload()).toObject()
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_SESSION_GETPULIST) {
                var notifycpi = proto.WEBBVCU.Current_Pulist_Info.deserializeBinary(response.getPayload());
                return;
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_DISCONNECT) {
                desSession._onServerDisconnect();
                return;
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_PU_ONOFFLINE) {
                options.cmd = jSWProtocol.RequestHeader.notifys.notify_pu_onoffline.cmd;
                var pulist = proto.WEBBVCU.BVCU_GetPulist_Response.deserializeBinary(response.getPayload()).toObject().pulistList;
                var pulistNotify = desSession._internalOnResponsePulist(pulist);
                if (desSession._confManager != null) {
                    desSession._confManager._onNotifyUserInfo(msgType, pulistNotify);
                }
                var onofflinepu = pulistNotify[0];
                data = {
                    onlinestatus: onofflinepu._info_pu.onlinestatus,
                    content: onofflinepu
                };
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_TSP_DATA) {
                var tsp_data = proto.WEBBVCU.Notify_TSP_Data.deserializeBinary(response.getPayload());
                options.cmd = jSWProtocol.RequestHeader.notifys.notify_tsp_data.cmd;

                var desdata = tsp_data.getData();

                var data = {
                    target: tsp_data.getTargetid(),
                    targetindex: tsp_data.getTargetindex(),
                    content: {
                        hex: jSWProtocol.uint8ArrayToHexArray(desdata)
                    }
                };
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_RECORD_PLAY_RESPONSE) {
                record_play_res = proto.WEBBVCU.MSG_WEB_BVCU_RECORD_PLAY_RESPONSE.deserializeBinary(response.getPayload());

                if (record_play_res) {
                    record_player = desSession.swGetRecordPlayer(record_play_res.getDialogid())
                    if (record_player) {
                        // 收到pts, 更新播放进度
                        record_player._on_update_pts(record_play_res.getPts())
                    } else {
                        console.warn('record play, not found by dialogId_: ' + record_play_res.getDialogid());
                    }
                }
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_CONF_START ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_CONF_CREATE ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_CONF_DELETE ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_CONF_STOP ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_ADD ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_REMOVE ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_APPLYFOR_ENDSPEAK ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_APPLYFOR_STARTSPEAK ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_INVITE_SPEAK ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_TERMINATE_SPEAK ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_LEAVE ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_RETURN ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_MODIFY ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_JOIN ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_EXIT ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_BASEINFO ||
                msgType == proto.WEBBVCU.MSGType.WEB_UTILS_AAC_OPERATE) {

                var cmd = null;
                switch (msgType) {
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_CONF_CREATE:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyconfcreate.cmd;
                        return; //别人创建的会议我不再处理
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_CONF_START:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyconfstart.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_CONF_DELETE:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyconfdelete.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_CONF_STOP:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyconfstop.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_ADD:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipartoradd.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_JOIN:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipartorjoin.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_REMOVE:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipartorremove.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_EXIT:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipartorexit.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_APPLYFOR_ENDSPEAK:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyapplyforendspeak.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_APPLYFOR_STARTSPEAK:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyapplyforstartspeak.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_INVITE_SPEAK:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyinvitespeak.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_TERMINATE_SPEAK:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyterminatespeak;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_LEAVE:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipartorleave;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_RETURN:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipartorreturn.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_MODIFY:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipatormodify.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_BASEINFO:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifybaseinfo.cmd;
                        break;
                    case proto.WEBBVCU.MSGType.WEB_UTILS_AAC_OPERATE:
                        cmd = jSWProtocol.RequestHeader.confnotify.notifyaacoperate.cmd;
                        break;
                }
                if (desSession._confManager != null) {
                    desSession._confManager._onCMrNotify(msgType, cmd, response);
                    return;
                }
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_IM_MSG) {
                if (desSession._immodule) {
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyimmsg.cmd;
                    desSession._immodule._imOnNotify(msgType, cmd, response);
                }
                return;
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_USERLOGIN ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_USERLOGOUT) {
                if (desSession._confManager != null) {
                    desSession._confManager._onNotifyUserInfo(msgType, response);
                }
                return;
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_EVENT_SOURCE) {
                options.cmd = jSWProtocol.RequestHeader.notifys.notify_event_source.cmd;
                data = jSWProtocol.EventSourcePb2_Json(response.getPayload());
            } else if (
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_USER_ADDGROUP ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_USER_ADDUSER ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_USER_DELGROUP ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_USER_DELUSER ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_USER_MODGROUP ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_USER_MODUSER) {
                if (desSession._userManager != null) {
                    desSession._userManager._onNotify(msgType, response, response.getPayload());
                    return;
                }
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_NOTIFY_BVCU_EVENT_STORAGE_FILE_OPEN ||
                msgType == proto.WEBBVCU.MSGType.WEB_NOTIFY_BVCU_EVENT_STORAGE_FILE_CLOSE ||
                msgType == proto.WEBBVCU.MSGType.WEB_NOTIFY_BVCU_EVENT_STORAGE_ERROR) {
                var recordtargetid = response.getTargetid();
                var recordtargetindex = response.getTargetindex();
                var videoChannel = desSession.swGetPuChanel(recordtargetid, recordtargetindex);
                var eventStorage = jSWProtocol.BVCU_EventStoragePb2_Json(response.getPayload());
                videoChannel._notifyDialogEventStorage(msgType, eventStorage);
                if (msgType == proto.WEBBVCU.MSGType.WEB_NOTIFY_BVCU_EVENT_STORAGE_FILE_CLOSE ||
                    msgType == proto.WEBBVCU.MSGType.WEB_NOTIFY_BVCU_EVENT_STORAGE_ERROR) {
                    console.log("本地录像 " + recordtargetid + "" + recordtargetindex + "录像" + eventStorage.szfilename + "结果" + (eventStorage.iresult == 0 ? "Ok!" : ("faild!" + eventStorage.iresult)));
                }
                return;
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_FILE_TRANS ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_IM_MSG ||
                msgType == proto.WEBBVCU.MSGType.WEB_CONF_IM_PIC_MSG ||
                msgType == proto.WEBBVCU.MSGType.WEB_CONF_IM_FILE_MSG ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_UPLOAD_FILE_TONRU) {

                var data = jSWProtocol.ParseFileTranInfo(response.getPayload());

                if (data.sEvent && data.sEvent.nruid.indexOf("PU") == 0) {
                    options.cmd = jSWProtocol.RequestHeader.notifys.notify_down_from_pu.cmd;
                } else if (data.sEvent && data.sEvent.nruid.indexOf("NRU") == 0) {
                    options.cmd = jSWProtocol.RequestHeader.notifys.notify_down_from_nru.cmd;
                }

                var myattchdataid = response.getRelayid();
                var attchdata = jSWProtocol.txGetRequestHelperOption(myattchdataid);

                if (jSW.FileTransEvent.FILE_TRANS_CLOSE == data.sEvent.iEvent) {
                    jSWProtocol.txSetRequestHelperOption(myattchdataid, null);
                }

                if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_IM_MSG) {
                    if (jSW.FileTransEvent.FILE_TRANS_CLOSE == data.sEvent.iEvent) {
                        var rccode = response.getCode();
                        if (jSW.RcCode.RC_CODE_S_OK == rccode) {
                            if (desSession._immodule) {
                                desSession._immodule._imMsgProgress(msgType, {
                                    data: data,
                                    attchdata: attchdata
                                }, response);
                            }
                            return;
                        }
                    }
                }

                if (attchdata) {
                    if ((typeof attchdata.callback) == 'function') {
                        attchdata.callback(attchdata.options, response, data);
                    }
                }
            } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_PU_OPENDIALOG_CMD) {
                options.cmd = jSWProtocol.RequestHeader.notifys.notify_pu_apply_for_opening_dialog.cmd;
                var descontent = jSWProtocol.SWOpenDialogPb2_Json(response.getPayload());
                descontent.szSourceId = response.getTargetid();
                data.content = descontent;
            }


            /**调用命令的回调函数*/
            if (options && ((typeof options.callback) == 'function')) {
                options.callback(options, response);
            }

            /**通知调用者*/
            desSession._callbackManager.dispatchCallback("notify", {
                code: response.getCode(),
                msg: options.cmd,
                content: data
            });
        }

        this.internalOnResponseSuccess = function (desSession, options, response, status) {
            var cmd = options.cmd;
            var request = options.request;
            var userTag = options.tag;
            var msgType = response.getMsgtype();
            var payload = response.getPayload();
            var payloadRequest = request.getPayload();
            var resultCode = response.getCode();
            var data = null;
            var bDoAsyncCallback = false

            request.param = {
                id: request.getTargetid(),
                major: request.getTargetindex(),
                targetid: request.getTargetid(),
                targetindex: request.getTargetindex(),
                hdlg: request.getHdlg()
            };

            try {
                switch (msgType) {

                    case proto.WEBBVCU.MSGType.WEB_BVCU_SET_CONF_INFO: {

                    }
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_SEARCH_IM_MSG: {
                    data = proto.BVCU.Search.Search_Response.deserializeBinary(payload).toObject();
                    data = {
                        info: data.stsearchinfo,
                        content: data.pimmsglistList
                    }

                    let lastMsg = null
                    let currMsg = null
                    for (let msgIndex in data.content) {
                        currMsg = data.content[msgIndex]

                        for (let indexField in currMsg) {
                            if (typeof currMsg[indexField] == 'undefined') {
                                currMsg[indexField] = lastMsg[indexField]
                            }
                        }

                        lastMsg = currMsg

                        if (lastMsg.stmsg.itype == 4 ||
                            lastMsg.stmsg.itype == 5 ||
                            lastMsg.stmsg.itype == 6 ||
                            lastMsg.stmsg.itype == 8) {
                            var nruId = lastMsg.stmsg.stfile.szid
                            var rp = lastMsg.stmsg.stfile.szfilepath
                            var ifs = lastMsg.stmsg.stfile.ifilesize
                            HTTPNruFile.CreateUrl(lastMsg.stmsg.stfile, true, nruId, rp, ifs, rp)
                        }

                        if (lastMsg.icombmsgorder == INT_MINUS1) {
                            lastMsg.icombmsgorder = -1
                        }
                    }
                }
                break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_GROUPLIST: {
                    if (payload.length > 0) {
                        var dataTemp = proto.WEBBVCU.RepeatGroupItem.deserializeBinary(payload).toObject();
                        data = []
                        var tempItem = null;
                        var tempData = []

                        for (var item in dataTemp.groupitemlistList) {
                            tempItem = dataTemp.groupitemlistList[item]
                            data.push({
                                szid: tempItem.szid,
                                szname: tempItem.szname,
                                szparentid: tempItem.szparentid,
                                items: []
                            })

                            tempData.push({
                                szid: tempItem.szid,
                                szname: tempItem.szname,
                                szparentid: tempItem.szparentid
                            })
                        }

                        var ItemsHelper = []
                        var ItemsHelper1 = []

                        for (var index in data) {
                            if (data[index].szparentid == "") {
                                ItemsHelper.push(data[index])
                            } else {
                                ItemsHelper1.push(data[index])
                            }
                        }

                        var selectData = function (items, freeItems) {
                            var itemId = ""
                            var tempFreeNode = null
                            var itemTemp = null

                            for (var index in items) {
                                itemTemp = items[index]
                                itemId = itemTemp.szid
                                var tempRootArray = []
                                for (var freeIndex in freeItems) {
                                    tempFreeNode = freeItems[freeIndex]

                                    if (tempFreeNode && itemId == tempFreeNode.szparentid) {
                                        itemTemp.items.push(tempFreeNode)
                                        freeItems[freeIndex] = null
                                        tempRootArray.push(tempFreeNode)
                                    }
                                }
                                selectData(tempRootArray, freeItems)
                            }
                        }

                        selectData(ItemsHelper, ItemsHelper1)

                        data = {
                            list: tempData,
                            struct: ItemsHelper
                        }
                    } else {
                        data = {
                            list: [],
                            struct: []
                        }
                    }
                }
                break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_GROUPINFO:
                case proto.WEBBVCU.MSGType.WEB_BVCU_PU_ADDGROUP:
                case proto.WEBBVCU.MSGType.WEB_BVCU_PU_DELGROUP:
                case proto.WEBBVCU.MSGType.WEB_BVCU_PU_MODGROUP: {
                    if (payload.length > 0) {
                        data = proto.BVCU.PUConfig.GroupInfo.deserializeBinary(payload).toObject();
                    }
                }
                break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_SEARCH_UALIST: {
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var searchResponse = proto.BVCU.Search.Search_Response.deserializeBinary(payload);
                        data = searchResponse.toObject()
                        data = data.pualistList
                    }
                }
                break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_SUBMETHOD_CONF_PARTICIPATOR_INVITE_JOIN: {

                }
                break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_SUBMETHOD_CONF_PARTICIPATOR_KICKOUT: {

                }
                break;
                case proto.WEBBVCU.MSGType.WEB_GET_DIALOG_INFO: {
                    data = proto.WEBBVCU.DialogInfo.deserializeBinary(payload).toObject();
                }
                break;
                case proto.WEBBVCU.MSGType.WEB_GET_VERSION_INFO: {
                    jSWUtils.debugLog("get version info", resultCode);
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var versionInfo = proto.WEBBVCU.VersionInfo.deserializeBinary(payload);
                        var iprototype = versionInfo.getIprototype();
                        var szVersion = jSWProtocol.HexToString(versionInfo.getSzversionid());
                        if (szVersion.length > 0) {
                            var desTime = szVersion.split(" ");
                            var tempTime = desTime[1].split(':');
                            szVersion = desTime[0] + " ";
                            for (var iIndex in tempTime) {
                                if (tempTime[iIndex].length == 1) {
                                    szVersion += '0';
                                }
                                szVersion += (tempTime[iIndex] + (iIndex == 2 ? "" : ":"));
                            }
                        }
                        data = {
                            szprototype: (iprototype == jSW.CallProtoType.OCX) ? "OCX" : "HTTP",
                            szversionid: szVersion
                        }
                    }
                }
                break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY: {
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {

                    }
                }
                break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_GET_PUBKEY: {
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var publicKey = proto.WEBBVCU.PublicKey.deserializeBinary(payload);
                        desSession.pubkey_d = publicKey.getD();
                        desSession.pubkey_n = publicKey.getN();
                        data = {
                            pubkey_d: desSession.pubkey_d,
                            pubkey_n: desSession.pubkey_n
                        };
                    }
                }
                break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_LOGIN: {
                    console.log('login' + ((jSW.RcCode.RC_CODE_S_OK == resultCode) ? ' Ok' : ' Failed'));

                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var loginResponse = proto.WEBBVCU.BVCU_Login_Response.deserializeBinary(payload);
                        desSession._serverConfig = {
                            keepliveinterval: loginResponse.getKeepliveinterval(),
                            sessionidletime: loginResponse.getSessionidletime(),
                            dialogidletime: loginResponse.getDialogidletime(),
                            cryptokey: loginResponse.getCryptokey(),
                            cryptoiv: loginResponse.getCryptoiv()
                        };
                        desSession._p_emms.setSession(loginResponse.getSession());
                        desSession._emms.session = loginResponse.getSession();
                        desSession._bLogout = false;

                        {
                            if (desSession._timer_keeplive) {
                                clearInterval(desSession._timer_keeplive);
                            }

                            var callback_keeplive = function (session) {
                                if (session._p_emms.getSession() && session._p_emms.getSession().length > 0) {
                                    var interval = 10 * 1000;
                                    try {
                                        session.swKeepLive();

                                        for (var i = 0; i < session._arr_dialog.length; i++) {
                                            session._arr_dialog[i].swKeepLive();
                                        }

                                        if (session._serverConfig && session._serverConfig.keepliveinterval > 5000) {
                                            interval = session._serverConfig.keepliveinterval;
                                        }
                                    } catch (e) {
                                        console.error('-- keeplive -- error ' + e);
                                    }

                                    //desSession._timer_keeplive = setTimeout(callback_keeplive, interval, session);

                                } else {
                                    if (desSession._timer_keeplive) {
                                        clearInterval(desSession._timer_keeplive);
                                    }
                                }
                            };

                            var interval = 10 * 1000;
                            if (desSession._serverConfig && desSession._serverConfig.keepliveinterval > 5000) {
                                interval = desSession._serverConfig.keepliveinterval;
                            }


                            if (jSWUtils._mMgr.bUseOcx() == false) {
                                if (true) {
                                    interval = 30 * 1000;
                                }
                                desSession._timer_keeplive = setInterval(callback_keeplive, interval, desSession);
                            }

                            HTTPNruFile.OnLoginOk(desSession);
                            PuLoad.OnLoginOk(desSession);
                            AudioMgr.OnLoginOk(desSession);
                        }
                    } else {
                        //console.error("登录结果序列化失败:服务器也许不在线");
                    }
                }
                break;
                }

                if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_SESSION_GETPULIST) {
                    //收到登录成功信息后发送的让中间件获取设备列表
                    return;
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PULIST ||
                    msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SEARCH_GET_PULIST) {
                    /**设备列表命令*/
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var pulist = dMgr.GetModule('PuLoad').OnSearchPuListResponse(msgType, payload);
                        data = pulist;
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PUINFO_BY_PUID) {
                    desSession._internalOnResponsePulist(response);
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_UPDATESTATUS) {
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        data = proto.BVCU.PUConfig.UpdateStatus.deserializeBinary(payload).toObject()
                        if (data) {
                            if (data.idownloadpercent > 2147483647) {
                                data.idownloadpercent = data.idownloadpercent - 4294967296
                            }
                            if (data.ispeed > 2147483647) {
                                data.ispeed = data.ispeed - 4294967296
                            }
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_KEEPALIVE) {
                    /**保活*/
                    if (jSW.RcCode.RC_CODE_S_HTTP_HAVE_NOTIFY == resultCode) {
                        // session.swGetPuList();
                        desSession._internalOnResponsePulist(response);

                        //response.request = request.request = jSWProtocol.RequestHeader.pulist.requestHeader;
                        response.emms.code = jSW.RcCode.RC_CODE_S_OK;
                        desSession._callbackManager.dispatchCallback(jSWProtocol.RequestHeader.pulist.cmd, {
                            code: resultCode,
                            request: request,
                            response: response,
                            msg: ''
                        });
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_OPEN_CHANLE ||
                    msgType == proto.WEBBVCU.MSGType.WEB_BVCU_OPEN_CHANLE_TSP) {
                    /**打开视频命令*/
                    console.log("on open chanle, result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (jSW.RcCode.RC_CODE_S_OK == resultCode) {
                        var param = proto.WEBBVCU.BVCU_DialogParam.deserializeBinary(payloadRequest);
                        var openDialogResponse = proto.WEBBVCU.Open_Dialog_Response.deserializeBinary(payload);
                        var hdlg = openDialogResponse.getHdlg();
                        var codecid = openDialogResponse.getCodecid();
                        var chanel = desSession.swGetPuChanel(param.getId(), param.getMajor());
                        var commonDlg = desSession._commDlgMgr.createAndSave(chanel, hdlg);
                        commonDlg.bh265 = codecid != 0x0001C;
                        /**保活chanel, 将该chanel添加到保活队列中*/
                        var isFind = false;
                        for (var i = 0; i < desSession._arr_dialog.length; i++) {
                            if (chanel.compare(desSession._arr_dialog[i])) {
                                isFind = true;
                            }
                        }
                        if (false == isFind) {
                            desSession._arr_dialog.push(chanel);
                        }
                        data = commonDlg;

                        if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_OPEN_CHANLE_TSP) {
                            chanel._OnTspDialog(data)
                        }
                    } else {
                        if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_OPEN_CHANLE) {
                            var param = proto.WEBBVCU.BVCU_DialogParam.deserializeBinary(payloadRequest);
                            var chanel = desSession.swGetPuChanel(param.getId(), param.getMajor());
                            chanel._onOpenFail()
                        }
                    }
                    //options.tag = options.tag.userdata;
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CLOSE_CHANLE) {
                    console.log("on close chanle, result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (jSW.RcCode.RC_CODE_S_OK == resultCode) {
                        var param = proto.WEBBVCU.BVCU_DialogParam.deserializeBinary(payloadRequest);
                        /**关闭视频命令*/
                        var chanel = desSession.swGetPuChanel(param.getId(), param.getMajor());
                        /**结束chanel保活， 将该chanel从保活队列中移除*/
                        for (var i = 0; i < desSession._arr_dialog.length; i++) {
                            if (chanel.compare(desSession._arr_dialog[i])) {
                                desSession._arr_dialog.splice(i, 1);
                                break;
                            }
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_RECORDSTATUS) {
                    if (jSW.RcCode.RC_CODE_S_OK == resultCode) {
                        data = proto.BVCU.PUConfig.RecordStatus.deserializeBinary(response.getPayload()).toObject()
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_OPEN_PUGPS_CHANNEL) {
                    console.log('open gps channel', resultCode)
                    var targetId = response.getTargetid()
                    var targetIndex = response.getTargetindex()
                    var commondata = proto.WEBBVCU.CommonData.deserializeBinary(response.getPayload());
                    var hdlg = commondata.getIdata1();
                    desSession._OnOpenGpsChannel(targetId, targetIndex, resultCode, hdlg)
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PUGPSDATA) {
                    /**获取GPS数据*/
                    console.log("on get pugpsdata, result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (jSW.RcCode.RC_CODE_S_OK == resultCode) {
                        var gpsdataResponse = proto.BVCU.PUConfig.GPSData.deserializeBinary(payload);
                        response.gps = {
                            lat: jSWProtocol.ParseNumberUint2Int(gpsdataResponse.getIlatitude()),
                            long: jSWProtocol.ParseNumberUint2Int(gpsdataResponse.getIlongitude()),
                            height: gpsdataResponse.getIheight(),
                            angle: gpsdataResponse.getIangle(),
                            speed: gpsdataResponse.getIspeed(),
                            starcount: jSWProtocol.ParseNumberUint2Int(gpsdataResponse.getIstarcount()),
                            antennastate: gpsdataResponse.getBantennastate(),
                            orientationstate: gpsdataResponse.getBorientationstate(),
                            satellitesignal: gpsdataResponse.getIsatellitesignal(),
                            sttime: jSWProtocol.Walltime2Json(gpsdataResponse.getSttime().toObject())
                        };

                        data = response.gps;

                        console.log('gps ' + "{lat:" + response.gps.lat + ' ' + "long:" + response.gps.long + "}");
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_GPS) {
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var gpsattrpb = proto.BVCU.PUConfig.GPSParam.deserializeBinary(payload);
                        var gpsattrjson = {
                            szname: gpsattrpb.getSzname(),
                            isupportsatellitesignal: gpsattrpb.getIsupportsatellitesignal(),
                            isetupsatellitesignal: gpsattrpb.getIsetupsatellitesignal(),
                            benable: (gpsattrpb.getBenable() == 0 ? false : true),
                            ireportinterval: gpsattrpb.getIreportinterval(),
                            ireportdistance: gpsattrpb.getIreportdistance()
                        }

                        gpsattrjsonresult = {
                            szname: gpsattrjson.szname,
                            supportsatellitesignal: {
                                isgps: ((gpsattrjson.isupportsatellitesignal & jSW.SWPu.SATELLITE.GPS) == jSW.SWPu.SATELLITE.GPS),
                                isbds: ((gpsattrjson.isupportsatellitesignal & jSW.SWPu.SATELLITE.BDS) == jSW.SWPu.SATELLITE.BDS),
                                isglonass: ((gpsattrjson.isupportsatellitesignal & jSW.SWPu.SATELLITE.GLONASS) == jSW.SWPu.SATELLITE.GLONASS),
                                isgalileo: ((gpsattrjson.isupportsatellitesignal & jSW.SWPu.SATELLITE.GALILEO) == jSW.SWPu.SATELLITE.GALILEO),
                                isqzss: ((gpsattrjson.isupportsatellitesignal & jSW.SWPu.SATELLITE.QZSS) == jSW.SWPu.SATELLITE.QZSS)
                            },
                            setupsatellitesignal: {
                                isgps: ((gpsattrjson.isetupsatellitesignal & jSW.SWPu.SATELLITE.GPS) == jSW.SWPu.SATELLITE.GPS),
                                isbds: ((gpsattrjson.isetupsatellitesignal & jSW.SWPu.SATELLITE.BDS) == jSW.SWPu.SATELLITE.BDS),
                                isglonass: ((gpsattrjson.isetupsatellitesignal & jSW.SWPu.SATELLITE.GLONASS) == jSW.SWPu.SATELLITE.GLONASS),
                                isgalileo: ((gpsattrjson.isetupsatellitesignal & jSW.SWPu.SATELLITE.GALILEO) == jSW.SWPu.SATELLITE.GALILEO),
                                isqzss: ((gpsattrjson.isetupsatellitesignal & jSW.SWPu.SATELLITE.QZSS) == jSW.SWPu.SATELLITE.QZSS)
                            },
                            benable: gpsattrjson.benable,
                            ireportinterval: gpsattrjson.ireportinterval,
                            ireportdistance: gpsattrjson.ireportdistance
                        }

                        data = gpsattrjsonresult;
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_SEARCH_LIST) {
                    /**获取文件查询列表*/
                    console.log('search file list ' + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                    var targetId = response.getTargetid();
                    var bSearchOnNRU = (targetId == "NRU_");
                    var searchResponse = proto.BVCU.Search.Search_Response.deserializeBinary(payload);
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        data = [];
                        var searchinfo = searchResponse.getStsearchinfo();
                        data = {
                            info: {
                                iPostition: searchinfo.getIpostition(),
                                iCount: searchinfo.getIcount(),
                                iTotalCount: searchinfo.getItotalcount()
                            },
                            content: []
                        };

                        var filePath = "";
                        var fileInfoList = searchResponse.getPfileinfoList();
                        var filePathSrc = null;
                        var fileItem = null;
                        var puinfo = null;
                        for (var i = 0; i < searchResponse.getStsearchinfo().getIcount(); i++) {
                            fileItem = fileInfoList[i];
                            filePathSrc = fileItem.getSzfilepath();
                            // filePath = (jSWOptions.CheckNotOcx() && bSearchOnNRU && false) ? (jSWOptions.http + '/' + jSWUtils.FilterPath(filePathSrc)) :
                            //     filePathSrc.replace(/\\\\/g, "\\");
                            filePath = filePathSrc.replace(/\\\\/g, "\\");
                            filePath = filePath.replace(/\\/g, '/');
                            puinfo = jSWUtils.PathParse(filePathSrc);
                            var aaa = fileItem.getItimerecord();
                            if (aaa > 0x1f00000000000000) {
                                aaa = -1;
                            }
                            data.content[i] = {
                                szPuid: puinfo.szpuid,
                                iChannelIndex: puinfo.ichannelindex,
                                szFilePath: filePath,
                                szFilePathSrc: filePathSrc,
                                iRecordType: fileItem.getIrecordtype(),
                                iRecordID: fileItem.getIrecordid(),
                                iFileType: fileItem.getIfiletype(),
                                iFileSize: fileItem.getIfilesize(),
                                iTimeRecord: aaa,
                                iTimeBegin: fileItem.getItimebegin(),
                                iTimeEnd: fileItem.getItimeend(),
                                szDesc1: fileItem.getSzdesc1(),
                                szDesc2: fileItem.getSzdesc2(),
                                szSourceID: fileItem.getSzsourceid(),
                            };

                            if (jSWOptions.CheckNotOcx()) {
                                HTTPNruFile.CreateUrl(data.content[i], true, targetId, filePathSrc, fileItem.getIfilesize(), filePathSrc)
                            }

                            if (bSearchOnNRU) {
                                //data.content[i].szFilePath = jSWUtils.FilterPath(data.content[i].szFilePath);
                                //desSession
                            }
                            console.log(data.content[i].szFilePath);
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_SEARCH_ONOFFLINE) {
                    /**获取日志查询列表*/
                    console.log('get pu login log ' + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var searchResponse = proto.BVCU.Search.Search_Response.deserializeBinary(payload);
                        //var operateLog = jSWProtocol.OperateSearchLogs2_Json(payload);
                        let jsonRsp = searchResponse.toObject();
                        data = {
                            info: {
                                iPostition: jsonRsp.stsearchinfo.ipostition,
                                iCount: jsonRsp.stsearchinfo.icount,
                                iTotalCount: jsonRsp.stsearchinfo.itotalcount
                            },
                            result: jsonRsp.ppuloginlogList
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_SEARCH_OPERATE) {
                    /**获取日志查询列表*/
                    console.log('get operate log ' + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var searchResponse = proto.BVCU.Search.Search_Response.deserializeBinary(payload);
                        var operateLog = jSWProtocol.OperateSearchLogs2_Json(payload);
                        data = operateLog;
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_CONF_LIST) {
                    console.log("on get conf list, result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    var confBaseInfoResponse = proto.WEBBVCU.Conf_List_Response.deserializeBinary(payload);
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        response.confList = [];
                        var confBaseInfo = null;
                        for (var i = 0; i < confBaseInfoResponse.getSzconflistList().length; i++) {
                            confBaseInfo = null;
                            confBaseInfo = confBaseInfoResponse.getSzconflistList()[i];
                            if (confBaseInfo != null) {
                                response.confList[i] =
                                    jSW.SwConfManager._UTILS.confBaseInfoToLocalInfo(confBaseInfo);
                            }
                        }

                        desSession._internalOnConfResponse(msgType, response.confList);
                        data = response.confList;
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_CONF_INFO ||
                    msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_CONF_CREATE) {

                    var confInfo = proto.BVCU.ConfInfo.deserializeBinary(payload);
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        response.confInfo = {
                            baseinfo: null,
                            particulars: [],
                            icreater: null
                        };

                        var confBaseInfo = confInfo.getBaseinfo();

                        response.confInfo.baseinfo =
                            jSW.SwConfManager._UTILS.confBaseInfoToLocalInfo(confBaseInfo);

                        var particularInfo = confInfo.getPparticipatorsList();

                        for (var i = 0; i < particularInfo.length; i++) {
                            response.confInfo.particulars[i] =
                                jSW.SwConfManager._UTILS.participatorToLocalUser(desSession, particularInfo[i]);
                        }

                        response.confInfo.icreater = confInfo.getPcreator() & 0x0000ffff;
                        response.confInfo.icurrentuserindex = (confInfo.getPcreator() & 0xffff0000) >> 16;

                        data = desSession._internalOnConfResponse(msgType, response.confInfo);

                        if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_CONF_CREATE) {
                            console.log("create conference: " + request.getTargetid() + " , result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? 'Ok' : 'Failed'));
                        } else {
                            console.log("on get conf: " + request.getTargetid() + " info, result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? 'Ok' : 'Failed'));
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_CONF_DELETE) {
                    console.log("delete conf, result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var targetId = request.getTargetid();
                        desSession._internalOnConfResponse(msgType, targetId);
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_ADD ||
                    msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_REMOVE) {
                    console.log(cmd + " result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var addResult = proto.BVCU.ConfParticipatorAddResult.deserializeBinary(payload);

                        var resultlist = addResult.getIresultbitsList();
                        var particularsrclist = proto.WEBBVCU.Conf_Participator_ADD.deserializeBinary(request.getPayload());
                        particularsrclist = particularsrclist.getSzparticcipatorList();
                        var resultIndex;
                        var iresultLeft;
                        var iResult;
                        var particularAddResult = {
                            resultlist: [],
                            targetid: null
                        };
                        particularAddResult.targetid = request.getTargetid();

                        var localuser = null;
                        for (var iIndex = 0; iIndex < particularsrclist.length; iIndex++) {
                            resultIndex = iIndex / 32;
                            iresultLeft = iIndex % 32;
                            iResult = ((resultlist[resultIndex] >>> 0) << iresultLeft) >> (31 - iresultLeft) == -1;
                            localuser = jSW.SwConfManager._UTILS.participatorToLocalUser(desSession._confManager, particularsrclist[iIndex]);
                            particularAddResult.resultlist[iIndex] = {
                                particular: localuser,
                                result: iResult
                            };
                        }

                        desSession._internalOnConfResponse(msgType, particularAddResult);
                        data = particularAddResult;
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_START ||
                    msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_STOP) {

                    var strOperate = msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_START ? ' start' : ' stop';

                    console.log('conf: ' + request.getTargetid() + strOperate + ', result' + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));

                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var targetId = request.getTargetid();
                        desSession._internalOnConfResponse(msgType, {
                            targetid: targetId,
                            bIsStarted: msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_START
                        });
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_APPLYFOR_STARTSPEAK ||
                    msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_APPLYFOR_ENDSPEAK) {
                    console.log("conf apply for, result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));

                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var targetId = request.getTargetid();
                        desSession._internalOnConfResponse(msgType, {
                            targetid: targetId,
                            bIsSpeak: msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_APPLYFOR_STARTSPEAK
                        });
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_LEAVE ||
                    msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_RETURN) {

                    var strMsgType = msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_LEAVE ? 'leave' : 'return';

                    console.log(strMsgType + "conf " + request.getTargetid() + ", result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));

                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var targetId = request.getTargetid();
                        desSession._internalOnConfResponse(msgType, {
                            targetid: targetId,
                            bIsSpeak: msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_RETURN
                        });
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_DEVICEINFO) {
                    var strMsgType = "get pu deviceinfo";

                    console.log(strMsgType + "pu: " + request.getTargetid() + ", result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));

                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var deviceinfo = proto.BVCU.PUConfig.DeviceInfo.deserializeBinary(payload);
                        data = {
                            szID: deviceinfo.getSzid(),
                            szManufacturer: deviceinfo.getSzmanufacturer(),
                            szProductName: deviceinfo.getSzproductname(),
                            szSoftwareVersion: deviceinfo.getSzsoftwareversion(),
                            szHardwareVersion: deviceinfo.getSzhardwareversion(),
                            iPUType: deviceinfo.getIputype(),
                            languages: [],
                            languageSelected: '',
                            Ilanguageindex: deviceinfo.getIlanguageindex(),
                            szName: deviceinfo.getSzname(),
                            iWIFICount: deviceinfo.getIwificount(),
                            iRadioCount: deviceinfo.getIradiocount(),
                            iChannelCount: deviceinfo.getIchannelcount(),
                            iVideoInCount: deviceinfo.getIvideoincount(),
                            iAudioInCount: deviceinfo.getIaudioincount(),
                            iAudioOutCount: deviceinfo.getIaudiooutcount(),
                            iPTZCount: deviceinfo.getIptzcount(),
                            iSerialPortCount: deviceinfo.getIserialportcount(),
                            iAlertInCount: deviceinfo.getIalertincount(),
                            iAlertOutCount: deviceinfo.getIalertoutcount(),
                            iStorageCount: deviceinfo.getIstoragecount(),
                            iGPSCount: deviceinfo.getIgpscount(),
                            bSupportSMS: deviceinfo.getBsupportsms() == 1,
                            iPresetCount: deviceinfo.getIpresetcount(),
                            iCruiseCount: deviceinfo.getIcruisecount(),
                            iAlarmLinkActionCount: deviceinfo.getIalarmlinkactioncount(),
                            iLongitude: deviceinfo.getIlongitude(),
                            iLatitude: deviceinfo.getIlatitude(),
                            szBluetoothAddr: deviceinfo.getSzbluetoothaddr()
                        };

                        var szLanguage = "";
                        var sziLanguages = deviceinfo.getIlanguageList();
                        for (iLanguage in sziLanguages) {
                            szLanguage = jSWProtocol.GetPuLanguageByIndex(sziLanguages[iLanguage]);
                            data.languages.push(szLanguage);
                        }

                        data.languageSelected = jSWProtocol.GetPuLanguageByIndex(sziLanguages[data.Ilanguageindex]);
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_PU_DEVICEINFO) {
                    var strMsgType = "set pu deviceinfo";

                    console.log(strMsgType + "pu: " + request.getTargetid() + ", result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_ETHERNET) {
                    var strMsgType = "get pu ethernet";

                    console.log(strMsgType + "pu: " + request.getTargetid() + ", result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));

                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var ethernetinfo = proto.BVCU.PUConfig.Ethernet.deserializeBinary(payload);
                        data = {
                            bDHCP: ethernetinfo.getBdhcp() == 1,
                            bPPPoE: ethernetinfo.getBpppoe() == 1,
                            bAutoDNS: ethernetinfo.getBautodns() == 1,
                            szIP: ethernetinfo.getSzip(),
                            szNetMask: ethernetinfo.getSznetmask(),
                            szGateway: ethernetinfo.getSzgateway(),
                            szDNS: ethernetinfo.getSzdnsList(),
                            szPPPoEUserName: ethernetinfo.getSzpppoeusername(),
                            szPPPoEPassword: ethernetinfo.getSzpppoepassword(),
                            iStatus: ethernetinfo.getIstatus()
                        };
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_RADIONETWORK) {
                    var strMsgType = "get pu radionetwork";

                    console.log(strMsgType + "pu: " + request.getTargetid() + ", result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));

                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var radionetworkAll = proto.BVCU.PUConfig.RadioNetworkAll.deserializeBinary(payload);
                        var radionetworklist = radionetworkAll.getStradionetworkList();

                        var radionetinfo = null;
                        data = [];

                        var radio = null;
                        var protowalltime = null;
                        for (radioindex in radionetworklist) {
                            radio = radionetworklist[radioindex];
                            protowalltime = radio.getStlasttime();
                            radionetinfo = {
                                bEnable: radio.getBenable() == 1, //是否使用该模块。0-不
                                iTypeAll: radio.getItypeallList(), //模块支持的所有网
                                iTypeIndex: radio.getItypeindex(), //当前使用的网络类型
                                szModuleName: radio.getSzmodulename(),
                                szUserName: radio.getSzusername(),
                                szPassword: radio.getSzpassword(),
                                szAPN: radio.getSzapn(),
                                szAccessNum: radio.getSzaccessnum(),
                                szCardNum: radio.getSzcardnum(),
                                bOnline: radio.getBonline() == 1, //是否在线。0-不在线，
                                iSignalLevel: radio.getIsignallevelList(), //每种网络类型
                                iOnlineTime: radio.getIonlinetime(), //上线时间，单位秒
                                iTrafficDownload: radio.getItrafficdownload(), //下载的网络
                                iTrafficUpload: radio.getItrafficupload(), //上传的网络流
                                iSpeedDownload: radio.getIspeeddownload(), //当前下载速度
                                iSpeedUpload: radio.getIspeedupload(), //当前上载速度，单
                                iTrafficYear: radio.getItrafficyear(), // 本年消耗总流
                                iTrafficMonth: radio.getItrafficmonth(), // 本月消耗总流
                                iTrafficDay: radio.getItrafficday(), // 本日目前消耗
                                iTrafficLastTime: radio.getItrafficlasttime(), // 上次上线
                                stLastTime: {
                                    iyear: protowalltime.getIyear(),
                                    imonth: protowalltime.getImonth(),
                                    iday: protowalltime.getIday(),
                                    ihour: protowalltime.getIhour(),
                                    iminute: protowalltime.getIminute(),
                                    isecond: protowalltime.getIsecond(),
                                }, // 上次
                                iMonthDay: radio.getImonthday(), // 每月计费日(月
                                bRebootAfterDialingFatalErr: radio.getBrebootafterdialingfatalerr() == 1
                            };

                            data.push(radionetinfo);
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_WIFI) {
                    var strMsgType = "get pu wifi";

                    console.log(strMsgType + "pu: " + request.getTargetid() + ", result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                    var pucfgwifi = proto.BVCU.PUConfig.Wifi.deserializeBinary(payload);
                    var wifigeneral = pucfgwifi.getStgeneral();
                    var hostspot = pucfgwifi.getSthostspot();
                    var wifiap = pucfgwifi.getStwifiap();
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        data = {
                            bEnable: pucfgwifi.getBenable() == 1, //连接AP热点是否使能：0
                            iMode: pucfgwifi.getImode(), //连接AP热点方式：0-普通
                            iSignalLevel: pucfgwifi.getIsignallevel(), //信号强度。0~100
                            stGeneral: {
                                szSSID: wifigeneral.getSzssid(),
                                iSecurityType: wifigeneral.getIsecuritytype(),
                                iCryptType: wifigeneral.getIcrypttype(), //
                                szWEPKey: wifigeneral.getSzwepkeyList(),
                                szWPAKey: wifigeneral.getSzwpakey(),
                                bDHCP: wifigeneral.getBdhcp() == 1, //是否使
                                bPPPoE: wifigeneral.getBpppoe() == 1, //是否
                                bAutoDNS: wifigeneral.getBautodns() == 1, //自
                                szIP: wifigeneral.getSzip(), //i
                                szNetMask: wifigeneral.getSznetmask(),
                                szGateway: wifigeneral.getSzgateway(),
                                szDNS: wifigeneral.getSzdnsList(),
                                iStatus: wifigeneral.getIstatus(), // 
                            },
                            stHostSpot: {
                                szProviderAll: hostspot.getSzproviderallList(),
                                iProviderIndex: hostspot.getIproviderindex(),
                                szAreaAll: hostspot.getSzareaallList(),
                                iAreaIndex: hostspot.getIareaindex(),
                                szUserName: hostspot.getSzusername(),
                                szPassword: hostspot.getSzpassword()
                            },
                            stWifiAP: {
                                bEnable: wifiap.getBenable() == 1,
                                bHideSSID: wifiap.getBhidessid() == 1,
                                szSSID: wifiap.getSzssid(),
                                iSafeType: wifiap.getIsafetype(),
                                szPassWord: wifiap.getSzpassword(),
                                iChannel: wifiap.getIchannel(),
                                bAutoChannelSelect: wifiap.getBautochannelselect() == 1,
                                szIP: wifiap.getSzip(),
                                szSubMark: wifiap.getSzsubmark(),
                                bDHCP: wifiap.getBdhcp() == 1,
                                szDHCPStartAddr: wifiap.getSzdhcpstartaddr(),
                                szDHCPEndAddr: wifiap.getSzdhcpendaddr()
                            }, //wif
                            szMacAddr: pucfgwifi.getSzmacaddr()
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_SERVERS) {
                    var strMsgType = "get pu servers";

                    console.log(strMsgType + "pu: " + request.getTargetid() + ", result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                    var puservers = proto.BVCU.PUConfig.Servers.deserializeBinary(payload);
                    var regserver = puservers.getStregisterserver();
                    var updateserver = puservers.getStupdateserver();
                    var timesource = puservers.getSttimesource();
                    var ddns = puservers.getStddns();
                    var emailserver = puservers.getStemailserver();
                    var reserveserver = puservers.setStreserveserver();
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        data = {
                            stRegisterServer: {
                                szAddr: regserver.getSzaddr(),
                                iPort: regserver.getIport(),
                                iProto: regserver.getIproto(),
                                iKeepAliveTimeout: regserver.getIkeepalivetimeout(),
                                szUserName: regserver.getSzusername(),
                                szPassword: regserver.getSzpassword(),
                                iStreamPathWay: regserver.getIstreampathway()
                            },
                            stUpdateServer: {
                                szAddr: updateserver.getSzaddr(),
                                iPort: updateserver.getIport(),
                                szUserName: updateserver.getSzusername(),
                                szPassword: updateserver.getSzpassword(),
                                iProto: updateserver.getIproto(),
                                szPath: updateserver.getSzpath()
                            },
                            stTimeSource: {
                                iTimeZone: timesource.getItimezone(), //本地时区
                                bDST: timesource.getBdst() == 1, //是否使用夏令时。0-不使用
                                stNTPServer: timesource.getStntpserver(),
                                iNTP: timesource.getIntp(), //NTP优先级1-100, <=0表
                                iGPS: timesource.getIgps(), //GPS优先级1-100, <=0表
                            },
                            stDDNS: {
                                bDDNS: ddns.getBddns() == 1, //动态域名。
                                szDDNSProvider: ddns.getSzddnsprovider(),
                                szDDNSAddr: ddns.getSzddnsaddr(),
                                szDDNSUserName: ddns.getSzddnsusername(),
                                szDDNSPassword: ddns.getSzddnspassword(),
                                szDynamicName: ddns.getSzdynamicname()
                            },
                            stEmailServer: {
                                szServerAddr: (emailserver == null ? "" : emailserver.getSzserveraddr()),
                                iServerPort: (emailserver == null ? "" : emailserver.getIserverport()),
                                szUserName: (emailserver == null ? "" : emailserver.getSzusername()),
                                szPassword: (emailserver == null ? "" : emailserver.getPassword()),
                                szSenderAddr: (emailserver == null ? "" : emailserver.getSzsenderaddr()),
                                bSSLEnable: (emailserver == null ? "" : emailserver.getBsslenable())
                            },
                            stReserveServer: {
                                szAddr: (reserveserver == null ? "" : reserveserver.getSzaddr()),
                                iPort: (reserveserver == null ? "" : reserveserver.getIport()),
                                iProto: (reserveserver == null ? "" : reserveserver.getIproto()),
                                iKeepAliveTimeout: (reserveserver == null ? "" : reserveserver.getIkeepalivetimeout()),
                                szUserName: (reserveserver == null ? "" : reserveserver.getSzusername()),
                                szPassword: (reserveserver == null ? "" : reserveserver.getSzpassword()),
                                iStreamPathWay: (reserveserver == null ? "" : reserveserver.getIstreampathway())
                            }
                        };
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_ONLINECONTROL) {
                    var strMsgType = "get pu onlinecontrol";

                    console.log(strMsgType + "pu: " + request.getTargetid() + ", result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                    var onlinectrl = proto.BVCU.PUConfig.OnlineControl.deserializeBinary(payload);
                    var days = onlinectrl.getStweekList();
                    var stresms = onlinectrl.getStresms();
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        data = {
                            bPowerOnOnline: onlinectrl.getBpowerononline() == 1,
                            stWeek: {
                                sunday: jSWProtocol.BVCU_DayTimeSlice_Pb2Json(days[0]),
                                monday: jSWProtocol.BVCU_DayTimeSlice_Pb2Json(days[1]),
                                tuesday: jSWProtocol.BVCU_DayTimeSlice_Pb2Json(days[2]),
                                wednesday: jSWProtocol.BVCU_DayTimeSlice_Pb2Json(days[3]),
                                thurday: jSWProtocol.BVCU_DayTimeSlice_Pb2Json(days[4]),
                                friday: jSWProtocol.BVCU_DayTimeSlice_Pb2Json(days[5]),
                                saturday: jSWProtocol.BVCU_DayTimeSlice_Pb2Json(days[6])
                            }, //一周中的时间片段
                            stRESMS: {
                                szCardNum: stresms.getSzcardnumList(),
                                szContent: stresms.getSzcontent(),
                                bReply: stresms.getBreply() == 1
                            }, //短信触发的配置
                        };
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_POWER) {

                    var strMsgType = "get pu power";

                    console.log(strMsgType + "pu: " + request.getTargetid() + ", result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                    var power = proto.BVCU.PUConfig.Power.deserializeBinary(payload);
                    var stTrunon = power.getStturnon().getStweekList();
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        data = {
                            iTurnOffDelay: power.getIturnoffdelay(),
                            bEnableTimer: power.getBenabletimer() == 1,
                            stTurnOn: {
                                sunday: jSWProtocol.BVCU_DayTimeSlice_Pb2JsonOnly(stTrunon[0]),
                                monday: jSWProtocol.BVCU_DayTimeSlice_Pb2JsonOnly(stTrunon[1]),
                                tuesday: jSWProtocol.BVCU_DayTimeSlice_Pb2JsonOnly(stTrunon[2]),
                                wednesday: jSWProtocol.BVCU_DayTimeSlice_Pb2JsonOnly(stTrunon[3]),
                                thurday: jSWProtocol.BVCU_DayTimeSlice_Pb2JsonOnly(stTrunon[4]),
                                friday: jSWProtocol.BVCU_DayTimeSlice_Pb2JsonOnly(stTrunon[5]),
                                saturday: jSWProtocol.BVCU_DayTimeSlice_Pb2JsonOnly(stTrunon[6])
                            },
                            iBatteryPower: power.getIbatterypower(),
                            iBatteryStatus: power.getIbatterystatus()
                        };
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_ZFYINFO) {
                    var strMsgType = "get pu zfyinfo";

                    console.log(strMsgType + "pu: " + request.getTargetid() + ", result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                    var zfyinfo = proto.BVCU.PUConfig.ZFYInfo.deserializeBinary(payload);
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        data = {
                            szDevModel: zfyinfo.getSzdevmodel(),
                            szIMEI_MEID: zfyinfo.getSzimeiMeid(),
                            szSerial: zfyinfo.getSzserial(),
                            szUserNo: zfyinfo.getSzuserno(),
                            szUserName: zfyinfo.getSzusername(),
                            szUserDescribe: zfyinfo.getSzuserdescribe(),
                            szUnitNo: zfyinfo.getSzunitno(),
                            szUnitName: zfyinfo.getSzunitname(),
                            szDefaultConference: zfyinfo.getSzdefaultconference(),
                            szCurrentConference: zfyinfo.getSzcurrentconference()
                        };
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_PU_ZFYINFO) {
                    var strMsgType = "set pu zfyinfo";

                    console.log(strMsgType + "pu: " + request.getTargetid() + "result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PU_CARINFO) {
                    var strMsgType = "get pu carinfo";

                    console.log(strMsgType + "pu: " + request.getTargetid() + "result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                    var carinfo = proto.BVCU.PUConfig.CarInfo.deserializeBinary(payload);
                    var licenceplate = carinfo.getStlicenceplate();
                    var drivers = carinfo.getStdriverList();
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        data = {
                            stLicencePlate: {
                                iCityID: (licenceplate == null ? "" : licenceplate.getIcityid()),
                                iColor: (licenceplate == null ? "" : licenceplate.getIcolor()),
                                iType: (licenceplate == null ? "" : licenceplate.getItype()),
                                szNumber: (licenceplate == null ? "" : licenceplate.getSznumber()),
                                szVAO: (licenceplate == null ? "" : licenceplate.getSzvao())
                            },
                            stDriver: jSWProtocol.Drivers2Json(drivers),
                            szVehicleType: carinfo.getSzvehicletype(),
                            szVIN_EngineNo: carinfo.getSzvinEngineno(),
                            szOwner: carinfo.getSzowner()
                        };
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_USER_GROUPLIST) {
                    var strMsgType = "get user grouplist";

                    console.log(strMsgType + "pu: " + request.getTargetid() + "result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                    var userGroupList = proto.WEBBVCU.UserGroupList.deserializeBinary(payload).getUsergroupsList();
                    var userGroup = null;
                    var userGroupJson = null;
                    data = [];
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        for (userGroupIndex in userGroupList) {
                            userGroup = userGroupList[userGroupIndex];
                            userGroupJson = jSWProtocol.GroupPb_2Json(userGroup);
                            data.push(userGroupJson);
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_USER_GROUPINFO) {
                    var strMsgType = "get user groupinfo";
                    console.log(strMsgType + ": " + request.getTargetid() + "result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var userGroupInfo = proto.BVCU.UserConfig.UserGroupInfo.deserializeBinary(payload);
                        data = jSWProtocol.GroupInfoPb_2Json(userGroupInfo, desSession);
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_USER_USERLIST) {
                    var strMsgType = "get user userlist";

                    console.log(strMsgType + "pu: " + request.getTargetid() + "result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));
                    var userlist = proto.WEBBVCU.UserList.deserializeBinary(payload).getUsersList();
                    data = [];
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var userpb = null;
                        var user = null;
                        for (userIndex in userlist) {
                            userpb = userlist[userIndex];
                            user = jSWProtocol.USER_USERPb_2Json(userpb);
                            data.push(user);
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_USER_USERINFO) {
                    var strMsgType = "get user userinfo";
                    console.log(strMsgType + "pu: " + request.getTargetid() + "result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? "Ok" : "Failed"));

                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var userinfo = proto.BVCU.UserConfig.UserInfo.deserializeBinary(payload);
                        data = jSWProtocol.USER_USERINFO_Pb2Json(userinfo, desSession);
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_USER_ONLINE) {
                    console.log("on get online users, result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));

                    var onlineUserResponse = proto.WEBBVCU.Conf_OnlineUser_Response.deserializeBinary(payload);
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        response.onlineUsers = [];
                        var onlineUserList = onlineUserResponse.getSzonlineuserList();
                        for (var i = 0; i < onlineUserResponse.getSzonlineuserList().length; i++) {
                            response.onlineUsers[i] =
                                jSW.SwConfManager._UTILS.onlineUserToLocal(onlineUserList[i]);
                        }

                        data = response.onlineUsers;
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_USER_ONLINEINFO) {
                    console.log("on get online users, result" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));

                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {

                        var onlineUserInfo = proto.BVCU.UserConfig.UserOnlineInfo.deserializeBinary(payload);
                        var onlineUser = onlineUserInfo.getStbase();
                        var userDialogs = onlineUserInfo.getPpudialogList();
                        var dialogpb = null;
                        var channelspb = null;
                        var channelpb = null;
                        var channels = null;
                        var channel = null;
                        var dialog = null;
                        var ppudialog = [];
                        for (dialogindex in userDialogs) {
                            dialogpb = userDialogs[dialogindex];
                            channels = [];
                            channelspb = dialogpb.getPdialogList();
                            for (channlIndex in channelspb) {
                                channelpb = channelpbs[channlIndex];
                                channel = {
                                    ichannelindex: channelpb.getIchannelindex(),
                                    istreamindex: channelpb.getIstreamindex(),
                                    iavstreamdir: channelpb.getIavstreamdir()
                                };
                                channels.push(channel);
                            }

                            dialog = {
                                puid: dialogpb.getSzid(),
                                channel: channels
                            };
                            ppudialog.push(dialog);
                        }
                        data = {
                            baseinfo: jSW.SwConfManager._UTILS.onlineUserToLocal(onlineUser),
                            ppudialog: ppudialog
                        };
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_PU_GPS) {
                    console.log("on set gps attr, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_PU_MANUALRECORD) {
                    console.log("on set pu manualrecord, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_PU_SNAPSHOT) {
                    console.log("on set pu snapshot, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_LOCAL_RECORD) {
                    data = {
                        szPath: '',
                        ifileleninseconds: -1
                    };
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var datapb = proto.WEBBVCU.LocalStorage.deserializeBinary(payload);
                        var szpath = jSWProtocol.HexToString(datapb.getDir());
                        var ifileleninseconds = datapb.getIfileleninseconds();
                        data = {
                            szPath: szpath.length == 0 ? '' : szpath,
                            ifileleninseconds: szpath.length == 0 ? -1 : ifileleninseconds
                        };
                    }
                    console.log("on get local record, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_LOCAL_RECORD) {
                    console.log("on set local record, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_SUBMETHOD_NRU_MANUALRECORD) {
                    console.log("on set nru record, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_NRU_LIST) {
                    console.log("on get nru list, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var nruAllpb = proto.WEBBVCU.NRUList.deserializeBinary(payload);
                        var nrulistpb = nruAllpb.getNrusList();

                        data = [];
                        var dataitem = null;
                        var nrupb = null;
                        for (var nruindex in nrulistpb) {
                            nrupb = nrulistpb[nruindex];
                            dataitem = {
                                szid: nrupb.getSzid(),
                                szname: nrupb.getSzname(),
                                istoragemediacount: nrupb.getIstoragemediacount(),
                                ionlinestatus: nrupb.getIonlinestatus()
                            };
                            data.push(dataitem);
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_GET_GPS_RECORD) {
                    console.log("on get gps record fileinfo, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var gpsFileInfoAllpb = proto.WEBBVCU.FTPFileInfoList.deserializeBinary(payload);
                        var gpsFileInfolistpb = gpsFileInfoAllpb.getFileinfosList();

                        data = [];
                        var dataitem = null;
                        var gpsfileinfopb = null;
                        for (var fileIndex in gpsFileInfolistpb) {
                            gpsfileinfopb = gpsFileInfolistpb[fileIndex];
                            dataitem = jSWProtocol.FTPFileInfo2Json(gpsfileinfopb);
                            data.push(dataitem);
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_DOWNLOAD_GPS_RECORD ||
                    msgType == proto.WEBBVCU.MSGType.WEB_BVCU_DOWNLOAD_GPS_RECORD_SERVERC) {
                    console.log("on download gps record file, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var fTPFileInfoGPSInfopb = proto.WEBBVCU.FTPFileInfoGPSInfo.deserializeBinary(payload);
                        data = jSWProtocol.FTPFileInfoGPSInfo2Json(fTPFileInfoGPSInfopb);
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_VOD) {
                    console.log("on set vod, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    response.url = "";
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        response.url = jSWProtocol.HexToString(payload);
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_USER_MODUSER) {
                    console.log("on mod user, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        if (desSession._userManager != null) {
                            desSession._userManager._onNotify(proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_USER_MODUSER, response, payloadRequest);
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_USER_MODGROUP) {
                    console.log("on mod group, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        if (desSession._userManager != null) {
                            desSession._userManager._onNotify(proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_USER_MODGROUP, response, payloadRequest);
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_USER_ADDGROUP) {
                    console.log("on add group, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        if (desSession._userManager != null) {
                            var userGroup = proto.BVCU.UserConfig.UserGroupInfo.deserializeBinary(payloadRequest)
                            userGroup.setSid(proto.BVCU.UserConfig.UserGroup.deserializeBinary(payload).toObject().sid)
                            payloadRequest = userGroup.serializeBinary()
                            desSession._userManager._onNotify(proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_USER_ADDGROUP, response, payloadRequest);
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_USER_ADDUSER) {
                    console.log("on add user, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        if (desSession._userManager != null) {
                            desSession._userManager._onNotify(proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_USER_ADDUSER, response, payloadRequest);
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_USER_DELGROUP) {
                    console.log("on del group, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        if (desSession._userManager != null) {
                            desSession._userManager._onNotify(proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_USER_DELGROUP, response, payloadRequest);
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_USER_DELUSER) {
                    console.log("on del user, result:" + (resultCode == jSW.RcCode.RC_CODE_S_OK ? ' Ok' : ' Failed'));
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        if (desSession._userManager != null) {
                            desSession._userManager._onNotify(proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_USER_DELUSER, response, payloadRequest);
                        }
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_USER_MODPASSWD) {
                    jSWUtils.consoleLog("on mod user pwd, result:", resultCode);
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_VIDEO_DIALOG_ROTATE ||
                    msgType == proto.WEBBVCU.MSGType.WEB_VIDEO_DIALOG_STRETCH ||
                    msgType == proto.WEBBVCU.WEB_BVCU_SET_LOCAL_SNAPSHOT) {} else if (msgType == proto.WEBBVCU.MSGType.WEB_GET_CMS_DIALOGINFO) {
                    jSWUtils.consoleLog("on get cms dialog info:", resultCode);
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var pb = proto.WEBBVCU.CmsDialogInfoList.deserializeBinary(payload);
                        data = jSWProtocol.CmsDialogInfos2Json(pb);
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_OPEN_INTERCOM) {
                    jSWUtils.consoleLog("on get open intercom:", resultCode);
                    var param = request.param;
                    var chanel = desSession.swGetPuChanel(param.targetid, param.targetindex);
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var commondata = proto.WEBBVCU.CommonData.deserializeBinary(payloadRequest);
                        var imedir = commondata.getIdata1();
                        commondata = proto.WEBBVCU.CommonData.deserializeBinary(payload);
                        var hdlg = commondata.getIdata1();

                        var InterComParams = {
                            bresult: resultCode == jSW.RcCode.RC_CODE_S_OK,
                            iMedia: imedir,
                            hDlg: hdlg,
                            cb: getAsyncCallback(desSession, response, options),
                            userData: null
                        }

                        if (jSW.RcCode.RC_CODE_S_OK == chanel.onIntercomOpenOk(InterComParams) && jSWOptions.CheckNotOcx()) {
                            bDoAsyncCallback = true
                        }
                    } else {
                        chanel.onIntercomOpenOk({
                            bresult: false
                        });
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CLOSE_INTERCOM) {
                    jSWUtils.consoleLog("on get close intercom:", resultCode);
                    var param = request.param;
                    var chanel = desSession.swGetPuChanel(param.targetid, param.targetindex);
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var commondata = proto.WEBBVCU.CommonData.deserializeBinary(payloadRequest);
                        var imedir = commondata.getIdata1();
                        chanel.onIntercomCloseOK(true, imedir);
                    } else {
                        chanel.onIntercomCloseOK(false);
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_ALTER_VIDEO_DIALOG_DIR) {
                    jSWUtils.consoleLog("on get alert video dir:", resultCode);
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var param = request.param;
                        var chanel = desSession.swGetPuChanel(param.targetid, param.targetindex);
                        var commondata = proto.WEBBVCU.CommonData.deserializeBinary(payloadRequest);
                        var imedir = commondata.getIdata1();
                        chanel.onAlterMedirDirOk(true, imedir, param.hdlg);
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_ALTER_AUDIO_DIALOG_DIR) {
                    jSWUtils.consoleLog("on get alert video dir:", resultCode);
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var param = request.param;
                        var chanel = desSession.swGetPuChanel(param.targetid, param.targetindex);
                        var commondata = proto.WEBBVCU.CommonData.deserializeBinary(payloadRequest);
                        var imedir = commondata.getIdata1();
                        chanel.onAlterMedirDirOk(false, imedir, param.hdlg);
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_ALTER_VIDEO_VOLUME) {
                    jSWUtils.consoleLog("on set alert volume :", resultCode);
                    if (resultCode == jSW.RcCode.RC_CODE_S_OK) {
                        var param = request.param;
                        var chanel = desSession.swGetPuChanel(param.targetid, param.targetindex);
                        var volumepb = proto.WEBBVCU.BVCU_DialogControlParam_Render.deserializeBinary(payloadRequest);
                        var volume = {
                            icapture: volumepb.getIcapturevolume(),
                            iplay: volumepb.getIplackbackvolume()
                        }
                        chanel.onAlterVolumeOk(volume);
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_RECORD_PLAY_RESPONSE) {
                    jSWUtils.consoleLog("on open record play :", resultCode);
                    record_player_res = proto.WEBBVCU.MSG_WEB_BVCU_RECORD_PLAY_RESPONSE.deserializeBinary(payload);

                    record_player = desSession._swGetRecordPlayer(record_player_res.getPlayerId())
                    if (record_player) {
                        // 收到录像回放打开事件
                        record_player._dialog_id = record_player_res.getDialogid()
                        console.log('record play, set dialog id: ' + record_player._dialog_id)
                        record_player._on_open_has_result(resultCode)
                    } else {
                        console.warn('record play, not found by dialogId: ' + record_player_res.getPlayerId())
                    }

                    data = {
                        playerId: record_player_res.getPlayerId(),
                        id: record_player_res.getDialogid(),
                        player: record_player,
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_RECORD_PLAY_CRTL_RESPONSE) {
                    jSWUtils.consoleLog("on open record play crtl: ", resultCode);
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SET_PU_REBOOT_OR_SHUTDOWN) {
                    jSWUtils.consoleLog("on set pu shutdown or reboot :", resultCode);
                    var param = request.param;
                    var puconrtol = proto.WEBBVCU.PU_CONTROL.deserializeBinary(payload);
                    data = {
                        puid: param.targetid,
                        ioption: puconrtol.getIoption()
                    }
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_IM_MSG) {
                    jSWUtils.consoleLog("on send im msg :", resultCode);
                    data = proto.BVCU.IM_Msgs.deserializeBinary(payloadRequest);
                    data = jSWProtocol.IMMsgPb2_Json(data);
                    //desSession._confManager.onSendMsgGetResult(request.param.id, data);
                } else if (msgType == proto.WEBBVCU.MSGType.WEB_UTILS_AAC_OPERATE) {
                    var pbAudioOperate = proto.WEBBVCU.AudioOperate.deserializeBinary(payloadRequest);
                    if (pbAudioOperate.getIoperatecode() != jSWProtocol.AUDIO_OP_CODES.CAPTURE_BEGIN) {
                        var pbAudioOperate = proto.WEBBVCU.AudioOperate.deserializeBinary(response.getPayload());
                        data = jSWProtocol.AACOperatePb2_Json(pbAudioOperate);
                    }
                }
            } catch (e) {
                resultCode: console.log(e);
            }

            if (false == bDoAsyncCallback) {
                responseDoCallback(desSession, response, options, data)
            }
        }



        function responseDoCallback(desSession, response, options, data) {
            var cmd = options.cmd;
            var request = options.request;
            var msgType = response.getMsgtype();
            var resultCode = response.getCode();

            response.emms = {
                code: resultCode
            }

            if (PuLoad.CheckPuLoadShouldNotNotify(msgType)) {
                return;
            }

            try {
                /**调用命令的回调函数*/
                if (options && ((typeof options.callback) == 'function')) {
                    options.callback(options, response, data);
                }
            } catch (e) {
                if (console.error) {
                    console.error(e)
                }
            }

            /**通知调用者*/
            desSession._callbackManager.dispatchCallback(cmd, {
                code: resultCode,
                request: request,
                response: response,
                msg: ''
            });

            if (jSW.RcCode.RC_CODE_S_OK != resultCode && jSW.RcCode.RC_CODE_S_HTTP_HAVE_NOTIFY != resultCode) {
                var err = 'cmd: ' + jSWUtils.MsgTypeToPresent(msgType) + ' fail ' + jSW.RcCode.trans(resultCode);
                console.error(err);
                desSession._internalOnResponseFail(options, '', err);
            }
        }

        function getAsyncCallback(desSession, response, options) {
            return function (rc) {
                response.setCode(rc)
                responseDoCallback(desSession, response, options)
            }
        }
    }

    dMgr.RegModule("HandleResponse", new desModule());
})(jSW.DependencyMgr);