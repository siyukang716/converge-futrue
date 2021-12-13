/**
 * Module Template Used To Create Module, Dependency Inject Will Support Soon
 */
(function (dMgr) {
    var jSWUtils = dMgr.GetModule("jSWUtils");
    var jSWProtocol = dMgr.GetModule("jSWProtocol");
    var jSWOptions = dMgr.GetModule("jSWOptions");

    // ---- 通信协议jSWProtocol ----
    (function (jSWProtocol) {

        jSWProtocol.GetWsUrl = function () {
            var protocolWs = window.location.protocol == "https:" ? "wss" : "ws";
            var desurl = "";
            if (jSWOptions.CheckHttp()) {

                if (jSWOptions.port > 0) {
                    desurl = protocolWs + "://" + jSWOptions.ip + ":" + jSWOptions.port + "/jswapi";
                } else {
                    desurl = protocolWs + "://" + jSWOptions.ip + "/jswapi";
                }

            } else if (jSWOptions.CheckOcx()) {
                if (jSWOptions.ocxWebsocketPort <= 0) {
                    // 尝试使用ocx获取websocket端口号(有可能注册的ocx事件未被调用)
                    try {
                        var jsw_ocx = document.getElementById('jsw_ocx');
                        if (jsw_ocx != null) {
                            jSWOptions.ocxWebsocketPort = jsw_ocx.oxGetWebSocketPort();
                        } else {
                            console.error('not found jsw_ocx');
                        }
                    } catch (e) {
                        console.error('oxGetWebSocketPort fail ' + e);
                    }
                }

                if (jSWOptions.ocxWebsocketPort <= 0) {
                    this._onWsError(options, 'ocx not init or init fail');
                    return;
                }

                desurl = 'ws://127.0.0.1:' + jSWOptions.ocxWebsocketPort;
                if (jSWUtils._mMgr.bWsTest()) {
                    desurl = 'ws://127.0.0.1:8888';
                }
            }

            console.log('ws url: ' + desurl);
            return desurl;
        }

        jSWProtocol.AutoReqSendProxy = function (session, options, dft_opts, pGA) {
            if (!dft_opts) {
                dft_opts = {};
            }
            var rc = jSW.RcCode.RC_CODE_E_FAIL;
            options = jSWUtils.extend({}, dft_opts, options || {});
            var dft_args = {
                session: session,
                msgtype: -1,
                target: "",
                targetIndex: -1,
                payload: null,
                cmd: "",
                callback: options.callback ? options.callback : null,
                tag: options.tag ? options.tag : null
            };
            var des_args = pGA(options);
            des_args = jSWUtils.extend({}, dft_args, des_args || {});
            rc = jSWProtocol.SendRequest(des_args);
            return rc;
        }


        jSWProtocol.JudgeIdType = function (id) {
            var judgeResult = {
                bPu: false,
                bCu: false,
                bUa: false,
                bUx: false
            };

            if (id.indexOf("PU_") == 0) {
                judgeResult.bPu = true;
            } else if (id.indexOf("CU_") == 0) {
                judgeResult.bCu = true;
            } else if (id.indexOf("UA_") == 0) {
                judgeResult.bUa = true;
            } else if (id.indexOf("UX_") == 0) {
                judgeResult.bUx = true;
            }
            return judgeResult;
        }

        jSWProtocol.BuildEventOptions = function (cmd, request, session, tag) {
            var eventOptions = {
                cmd: cmd,
                request: request,
                session: session,
                tag: tag
            };
            return eventOptions;
        }

        jSWProtocol.BuildResponse = function (iResult, request) {
            var response = {
                emms: {
                    code: iResult
                },
                request: request
            };
            return response;
        }

        /**通道类型*/
        jSWProtocol.ChanelType = {
            unknow: 'type_unknow',
            chanel: 'type_chanel',
            gps: 'type_gps',
            tsp: 'type_tsp',
            custom: 'type_custom',

            getType: function (major) {
                //DialogTarget.iIndexMajor取值范围与通道类型
                var BVCU_SUBDEV_INDEXMAJOR_MIN_CHANNEL = 0; //音视频通道
                var BVCU_SUBDEV_INDEXMAJOR_MAX_CHANNEL = 0x00FFFF;
                var BVCU_SUBDEV_INDEXMAJOR_MIN_GPS = 0x010000; //GPS设备数据
                var BVCU_SUBDEV_INDEXMAJOR_MAX_GPS = 0x0100FF;
                var BVCU_SUBDEV_INDEXMAJOR_MIN_TSP = 0x010100; //透明串口设备数据
                var BVCU_SUBDEV_INDEXMAJOR_MAX_TSP = 0x0101FF;
                var BVCU_SUBDEV_INDEXMAJOR_MIN_CUSTOM = 0xF00000; //自定义设备数据
                var BVCU_SUBDEV_INDEXMAJOR_MAX_CUSTOM = 0xF000FF;

                if (major >= BVCU_SUBDEV_INDEXMAJOR_MIN_CHANNEL &&
                    major <= BVCU_SUBDEV_INDEXMAJOR_MAX_CHANNEL) {
                    return jSWProtocol.ChanelType.chanel;
                } else if (major >= BVCU_SUBDEV_INDEXMAJOR_MIN_GPS &&
                    major <= BVCU_SUBDEV_INDEXMAJOR_MAX_GPS) {
                    return jSWProtocol.ChanelType.gps;
                } else if (major >= BVCU_SUBDEV_INDEXMAJOR_MIN_TSP &&
                    major <= BVCU_SUBDEV_INDEXMAJOR_MAX_TSP) {
                    return jSWProtocol.ChanelType.tsp;
                } else if (major >= BVCU_SUBDEV_INDEXMAJOR_MIN_CUSTOM &&
                    major <= BVCU_SUBDEV_INDEXMAJOR_MAX_CUSTOM) {
                    return jSWProtocol.ChanelType.custom;
                } else {
                    return jSWProtocol.ChanelType.unknow;
                }
            },
        };

        /**请求命令*/
        jSWProtocol.RequestHeader = {
            notifys: {
                notify: {
                    cmd: 'notify'
                },
                notify_pu_onoffline: {
                    cmd: 'notify_pu_onoffline'
                },
                notify_event_source: {
                    cmd: 'notify_event_source'
                },
                notify_tsp_data: {
                    cmd: 'notify_tsp_data'
                },
                notify_down_from_pu: {
                    cmd: 'notify_down_from_pu'
                },
                notify_pu_apply_for_opening_dialog: {
                    cmd: 'notify_pu_apply_for_opening_dialog'
                },
                notifyimmsg: {
                    cmd: 'notifyimmsg'
                },
                notify_down_from_nru: {
                    cmd: 'notify_down_from_nru'
                },
                notify_pu_record_status: {
                    cmd: 'notify_pu_record_status'
                }
            },
            confnotify: {
                notifyconfcreate: {
                    cmd: 'notifyconfcreate'
                },
                notifyconfstart: {
                    cmd: 'notifyconfstart'
                },
                notifyconfdelete: {
                    cmd: 'notifyconfdelete'
                },
                notifyconfstop: {
                    cmd: 'notifyconfstop'
                },
                notifyparticipartoradd: {
                    cmd: 'notifyparticipartoradd'
                },
                notifyparticipartorjoin: {
                    cmd: 'notifyparticipartorjoin'
                },
                notifyparticipartorremove: {
                    cmd: 'notifyparticipartorremove'
                },
                notifyparticipartorexit: {
                    cmd: 'notifyparticipartorexit'
                },
                notifyapplyforstartspeak: {
                    cmd: 'notifyapplyforstartspeak'
                },
                notifyapplyforendspeak: {
                    cmd: 'notifyapplyforendspeak'
                },
                notifyinvitespeak: {
                    cmd: 'notifyinvitespeak'
                },
                notifyterminatespeak: {
                    cmd: 'notifyterminatespeak'
                },
                notifyparticipartorleave: {
                    cmd: 'notifyparticipartorleave'
                },
                notifyparticipartorreturn: {
                    cmd: 'notifyparticipartorreturn'
                },
                notifyparticipatormodify: {
                    cmd: 'notifyparticipatormodify'
                },
                notifyimmsg: {
                    cmd: 'notifyimmsg'
                },
                notifyaacoperate: {
                    cmd: 'notifyaacoperate'
                },
                notifybaseinfo: {
                    cmd: 'notifybaseinfo'
                }
            },
            pugroup: {
                cmd: "pugroup"
            },
            lossconnection: {
                cmd: "lossconnection"
            },
            pubkey: {
                cmd: 'pubkey'
            },
            login: {
                cmd: 'login'
            },
            logout: {
                cmd: 'logout'
            },
            keeplive: {
                cmd: 'keeplive'
            },
            pulist: {
                cmd: 'pulist'
            },
            openchanle: {
                cmd: 'openchanle'
            },
            altervideodir: {
                cmd: 'altervideodir'
            },
            keeplivechanle: {
                cmd: 'keeplivechanle'
            },
            openchanletsp: {
                cmd: 'openchanletsp'
            },
            writechanletsp: {
                cmd: 'writechanletsp'
            },
            closechanle: {
                cmd: 'closechanle'
            },
            pugpsdata: {
                cmd: 'pugpsdata'
            },
            searchlist: {
                cmd: 'searchlist'
            },
            recordfile_del: {
                cmd: 'recordfile_del'
            },
            vod: {
                cmd: 'vod'
            },
            snapshot: {
                cmd: 'snapshot'
            },
            getptzattr: {
                cmd: 'getptzattr'
            },
            setptzattr: {
                cmd: 'setptzattr'
            },
            ptzctrl: {
                cmd: 'ptzctrl'
            },
            getconflist: {
                cmd: 'getconflist'
            },
            getconfinfo: {
                cmd: 'getconfinfo'
            },
            confaudiorecord: {
                cmd: 'confaudiorecord'
            },
            confimsend: {
                cmd: 'confimsend'
            },
            getonuserlist: {
                cmd: 'getonuserlist'
            },
            createconf: {
                cmd: 'createconf'
            },
            deleteconf: {
                cmd: 'deleteconf'
            },
            particularadd: {
                cmd: 'particularadd'
            },
            particularremove: {
                cmd: 'particularremove'
            },
            applyforspeak: {
                cmd: 'applyforspeak'
            },
            applyforendspeak: {
                cmd: 'applyforendspeak'
            },
            confstart: {
                cmd: 'confstart'
            },
            confend: {
                cmd: 'confend'
            },
            invitespeak: {
                cmd: 'invitespeak'
            },
            terminalspeak: {
                cmd: 'terminalspeak'
            },
            confleave: {
                cmd: 'confleave'
            },
            confreturn: {
                cmd: 'confreturn'
            },
            pucfg: {
                getpudeviceinfo: {
                    cmd: "getpudeviceinfo"
                },
                getpuethernet: {
                    cmd: "getpuethernet"
                },
                getpuradionetwork: {
                    cmd: "getpuradionetwork"
                },
                getpuwifi: {
                    cmd: "getpuwifi"
                },
                getPuPower: {
                    cmd: "getPuPower"
                },
                getPuOnlineControl: {
                    cmd: "getPuOnlineControl"
                },
                getPuUpdateStatus: {
                    cmd: "getPuUpdateStatus"
                },
                getPuLinkactionList: {
                    cmd: "getPuLinkactionList"
                },
                getPuCarInfo: {
                    cmd: "getPuCarInfo"
                },
                getPuZfyInfo: {
                    cmd: "getPuZfyInfo"
                },
                setPuZfyInfo: {
                    cmd: "setPuZfyInfo"
                },
                getPuServers: {
                    cmd: "getPuServers"
                }
            },

            usercfg: {
                modgroupinfo: {
                    cmd: "modgroupinfo"
                },
                moduserinfo: {
                    cmd: "moduserinfo"
                },
                moduserpwd: {
                    cmd: "moduserpwd"
                },
                addgroup: {
                    cmd: "addgroup"
                },
                adduser: {
                    cmd: "adduser"
                },
                delgroup: {
                    cmd: "delgroup"
                },
                deluser: {
                    cmd: "deluser"
                },
                getgrouplist: {
                    cmd: "getgrouplist"
                }
            },

            recordplay: {
                play: {
                    cmd: 'recordplay_play'
                },
                pause: {
                    cmd: 'recordplay_pause'
                },
                teardown: {
                    cmd: 'recordplay_teardown'
                }
            },

            compare: function (src, des) {
                if (src.req == des.req && src.subreq == des.subreq)
                    return true;
                else
                    return false;
            }
        };

        /**打开视频使用的协议*/
        jSWProtocol.ProtoType = {
            HLS: 1,
            RTMP: 2,
            OCX: 4,
            HTTP_FLV: 6,
            OTHER: 5,
            /*串口等*/
        };

        jSWProtocol.RotateType = {
            RIGHT90: 90,
            LEFT90: -90
        };
        /**
         * dft_options = {
         *     session: null,
         *     msgtype: -1,
         *     target: "",
         *     targetIndex: -1,
         *     payload: null,
         *     cmd: "",
         *     callback: null,
         *     tag: null
         * }
         */
        jSWProtocol.SendRequest = function (options) {

            dft_options = {
                session: null,
                msgtype: -1,
                target: "",
                targetIndex: -1,
                payload: null,
                cmd: "",
                hdlg: -1,
                callback: null,
                callbackrelay: null,
                tag: null,
                _dhcb: null,
                _dhcbtag: null
            }

            options = jSWUtils.extend({}, dft_options, options || {});

            var param = this.JsonParamCommand(options.session._p_emms,
                options.msgtype,
                jSWProtocol.BVCU_Command(options.target, options.targetIndex, options.payload, options.hdlg));

            var attchdata = null;

            if (options.callbackrelay != null) {
                attchdata = {
                    callback: options.callbackrelay,
                    options: {
                        tag: options.tag
                    },
                    _dhcbtag: options._dhcbtag
                };
            }
            var rc = jSWProtocol._internalSend({
                cmd: options.cmd,
                session: options.session,
                request: param,
                callback: options.callback,
                attchdata: attchdata,
                tag: options.tag
            });

            return rc;
        }

        /**控制协议, 为用户登录、鉴权提供支持，其数据的输入输出都在EmmsHeader中。*/
        jSWProtocol.EmmsHeader = function (appkey, version) {
            this.key = appkey; /**GUID	分配给应用用于访问API的Key，请求消息中必需。	M/O*/
            this.ver = version; /**[1-9][0-9]?\.[0-9]	API协议版本，请求消息中¬必需。当前版本1.0.	M/O*/
            this.session = ''; /**GUID	会话ID，请求用户特定信息必需。	M/O*/
            this.id = 0; /**命令唯一ID*/
            this.code = 0; /**int	错误编码，响应消息中必需。	M/O*/
        }

        jSWProtocol.GetPuLanguageByIndex = function (iLanguageIndex) {
            if (iLanguageIndex == 0)
                return "INVALID";
            else if (iLanguageIndex == 1) {
                return 'ENGLISH';
            } else if (iLanguageIndex == 2) {
                return 'CHINESE_SIMPLIFIED';
            } else if (iLanguageIndex == 3) {
                return 'CHINESE_TRADITIONAL';
            }
        };

        jSWProtocol.BVCU_ServerParam = function (serverIP, serverPort, userName, password) {
            this.server = serverIP;
            this.port = serverPort;
            this.user = userName;
            this.password = password;
        }

        /**jSWProtocol.ProtoType*/
        jSWProtocol.BVCU_DialogParam = function (pId, pMajor, pMinor, pProto, pMedia, pHwnd, hDlg, bOverTcp) {
            var param = new proto.WEBBVCU.BVCU_DialogParam();
            param.setId(pId);
            param.setMajor(pMajor);
            param.setMinor(pMinor);
            param.setProto(pProto);
            param.setMedia(pMedia);
            param.setHdlg(hDlg);
            if (bOverTcp) {
                param.setBovertcp(1);
            }
            if (pHwnd != "undefined") {
                param.setHwnd(pHwnd);
            }
            return param;
        }

        jSWProtocol.BVCU_Command = function (pTargetId, pTargetIndex, pContent, hdlg) {
            var request = new proto.WEBBVCU.Request();
            request.setTargetid(pTargetId);
            request.setTargetindex(pTargetIndex);
            request.setHdlg(hdlg);
            if (null != pContent) {
                try {
                    request.setPayload(pContent.serializeBinary().buffer);
                } catch (e) {
                    request.setPayload(jSWUtils.string2Uint8Array(pContent));
                }
            }
            return request;
        };

        jSWProtocol.ParamPuList = function (pStatus, pPagesize, pPage, iIndex, iCount) {
            var getpulist = new proto.WEBBVCU.BVCU_GetPulist();

            getpulist.setStatus(pStatus);
            getpulist.setPagesize(pPagesize);
            getpulist.setPage(pPage);
            getpulist.setIndex(iIndex);
            getpulist.setIcount(iCount);

            return getpulist;
        }

        jSWProtocol.BVCU_SearchInfo = function (piType, piPostition, piCount) {
            this.iType = piType;
            this.iPostition = piPostition;
            this.iCount = piCount;
            this.iTotalCount = -1;
        }

        jSWProtocol.SearchType = {
            UNKNOWN: 0,
            FILE: 1, // 文件
            CU_LOGIN: 2,
            PU_LOGIN: 3,
            OPERATE: 4,
            DIALOG: 5,
            PULIST: 6,
            EVENT: 7,
            USERLIST: 8,
            IM_MSG: 9,
            UALIST: 10
        };

        jSWProtocol.BVCU_Search_FileFilter = function (pszPUID, piChannelIndex, piTimeBegin, piTimeEnd, piFileType,
            piRecordType, piFileSizeMin, piFileSizeMax) {
            this.szPUID = pszPUID;
            this.iChannelIndex = piChannelIndex;
            this.iTimeBegin = piTimeBegin;
            this.iTimeEnd = piTimeEnd;
            this.iFileType = piFileType;

            this.iRecordType = piRecordType;
            this.iFileSizeMin = piFileSizeMin;
            this.iFileSizeMax = piFileSizeMax;
        }

        jSWProtocol.CheckOptions = function (msgtype, srcoptions) {
            var def_options = null;
            var argsErrorDes = null;
            var createErrorfun = function (MSGType, errDes, desErr) {
                desErr += msgtype + errDes;
            }

            switch (msgtype) {
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_IM_MSG:
                    if (undefined == srcoptions.szTextMsg || null == srcoptions.szTextMsg) {
                        createErrorfun(msgtype, "szTextMsg: set error!", argsErrorDes);
                    }
                    if (undefined == srcoptions.callback || null == srcoptions.callback) {
                        createErrorfun(msgtype, "callback: set error!", argsErrorDes);
                    }
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_SET_PU_REBOOT_OR_SHUTDOWN:
                    if (undefined == srcoptions.ioption || null == srcoptions.ioption || srcoptions.ioption != 0 || srcoptions.ioption != 1) {
                        createErrorfun(msgtype, "ioption: set error!", argsErrorDes);
                    }
                    if (undefined == srcoptions.callback || null == srcoptions.callback) {
                        createErrorfun(msgtype, "callback: set error!", argsErrorDes);
                    }
                    break;
            }

            if (argsErrorDes == null) {
                return jSW.RcCode.RC_CODE_S_OK;
            } else {
                console.log(argsErrorDes);
                return jSW.RcCode.RC_CODE_E_INVALIDARG;
            }
        }

        jSWProtocol.uint8ArrayToHexArray = function (uint8array) {
            var szHexTemp = [];
            var temphex = "";
            for (var iindex = 0; iindex < uint8array.length; iindex += 2) {
                temphex = "";
                temphex += String.fromCharCode(uint8array[iindex]);
                temphex += String.fromCharCode(uint8array[iindex + 1]);
                szHexTemp.push(temphex);
            }
            return szHexTemp;
        }

        jSWProtocol.uint8ArrayToHex = function (uint8array) {
            var sSzHex = "";
            var iTemp = 0;
            for (var iIndex = 0; iIndex < uint8array.length; iIndex++) {
                sSzHex += (uint8array[iIndex] >> 4).toString(16);
                sSzHex += (uint8array[iIndex] & 0x0f).toString(16);
            }
            return sSzHex;
        }

        jSWProtocol.HexToString = function (uint8array) {
            return String.fromCharCode.apply(null, uint8array);
        }

        jSWProtocol.LargeHexToString = function (arr) {
            var out = "";
            for (var i = 0; i < arr.length / 2; i++) {
                var tmp = [arr[i * 2], arr[i * 2 + 1]];
                var charValue = String.fromCharCode.apply(null, tmp);
                out += charValue
            }
            return out;
        }

        jSWProtocol.EventSourcePb2_Json = function (eventSourcePb) {
            eventSourcePb = proto.BVCU.Event_Source.deserializeBinary(eventSourcePb);
            var szTargetId = eventSourcePb.getSzid();

            while (szTargetId.indexOf("PU_0") == 0) {
                szTargetId = szTargetId.replace("PU_0", "PU_");
            }

            var eventSourceJson = {
                iEventType: eventSourcePb.getIeventtype(),
                stTime: this.Walltime2Json(eventSourcePb.getSttime().toObject()),
                szID: szTargetId,
                iSubDevIdx: eventSourcePb.getIsubdevidx(),
                iValue: eventSourcePb.getIvalue(),
                bEnd: eventSourcePb.getBend() == 1,
                szDevID: eventSourcePb.getSzdevid(),
                szEventDesc: eventSourcePb.getSzeventdesc(),
            }

            return eventSourceJson;
        }

        jSWProtocol.ParseFileTranInfo = function (transInfoPb) {
            var data = {
                sEvent: null,
                sProgressInfo: null
            };

            data.sEvent = jSWProtocol.SWFileTransEventPb2_Json(transInfoPb);
            if (jSW.FileTransEvent.FILE_TRANS_PROCESS == data.sEvent.iEvent) {
                data.sProgressInfo = jSWProtocol.SWFileTransInfoPb2_Json(transInfoPb);
            }
            return data;
        }

        jSWProtocol.SWFileTransInfoPb2_Json = function (transInfoPb) {
            transInfoPb = proto.WEBBVCU.FileTransInfo.deserializeBinary(transInfoPb);
            var transInfo = {
                iCreateTime: transInfoPb.getIcreatetime(),
                iOnlineTime: transInfoPb.getIonlinetime(),
                iTransferBytes: transInfoPb.getItransferbytes(),
                iTotalBytes: transInfoPb.getItotalbytes(),
                iSpeedKBpsLongTerm: transInfoPb.getIspeedkbpslongterm(),
                iSpeedKBpsShortTerm: transInfoPb.getIspeedkbpsshortterm(),
            };
            return transInfo;
        }
        jSWProtocol.SWFileTransEventPb2_Json = function (transInfoPb) {
            transInfoPb = proto.WEBBVCU.FileTransInfo.deserializeBinary(transInfoPb);
            var downloadPath = transInfoPb.getDownfilefrompu();
            var szbase64 = downloadPath && downloadPath["getImgbase64"] ? downloadPath.getImgbase64() : "";
            var event = {
                iEvent: transInfoPb.getIevent(),
                iResult: transInfoPb.getIresult(),
                szRemoteFilePath: downloadPath ? downloadPath.getSzremotefilepathname() : "",
                szLocalFilePath: downloadPath ? downloadPath.getSzlocalfilepathname() : "",
                imgbase64: szbase64.length > 0 ? this.LargeHexToString(downloadPath.getImgbase64()) : "",
                nruid: transInfoPb.getSznruid()
            }
            return event;
        }

        jSWProtocol.SWOpenDialogPb2_Json = function (openDialog) {
            openDialog = proto.BVCU.PUConfig.OpenDialog.deserializeBinary(openDialog);

            var tempData = openDialog.toObject()

            var desdata = {
                szID: openDialog.getSzid(),
                iChannelIndex: openDialog.getIchannelindex(),
                iStreamIndex: openDialog.getIstreamindex(),
                iAVStreamDir: openDialog.getIavstreamdir(),
                bRecord: openDialog.getBrecord(),
                iApplierID: openDialog.getIapplierid(),
                iType: tempData.itype,
                szApplyeeUserId: tempData.szapplyeeuserid,
                szApplyerUserId: tempData.szapplyeruserid,
                szCallId: tempData.szcallid
            };
            return desdata;
        }

        jSWProtocol.IMFilePb2json = function (imfilepb) {
            var imfile = {
                szID: imfilepb.getSzid(),
                szFile: imfilepb.getSzfilepath(),
                iFileSize: imfilepb.getIfilesize(),
                iDuration: imfilepb.getIduration()
            }
            return imfile;
        }

        jSWProtocol.IMMsgPb2_Json = function (szmsgdata) {
            var szmsgs = [];
            szmsgdata = szmsgdata.getSzmsgsList();
            var szmsgdataitem = null;
            var tempmsgdata;
            var imsgtype = 0;
            for (var ii = 0; ii < szmsgdata.length; ii++) {
                szmsgdataitem = szmsgdata[ii];
                imsgtype = szmsgdataitem.getItype();
                tempmsgdata = {
                    imsgid: szmsgdataitem.getImsgid(),
                    iType: imsgtype,
                    szTextMsg: (imsgtype == this.IMMSGTypes.TEXT || imsgtype == this.IMMSGTypes.FACE) ? szmsgdataitem.getSztextmsg() : null,
                    stGpsData: imsgtype == this.IMMSGTypes.GPS ? this.GPSDatapb2Json(szmsgdataitem.getStgpsdata()) : null,
                    stFile: (imsgtype == this.IMMSGTypes.FILE || imsgtype == this.IMMSGTypes.PICTURE || imsgtype == this.IMMSGTypes.AUDIO || imsgtype == this.IMMSGTypes.CONF_AUDIO) ? this.IMFilePb2json(szmsgdataitem.getStfile()) : null
                };

                if (imsgtype == this.IMMSGTypes.FACE) {
                    var ImEmtion = jSW.DependencyMgr.GetEmotionSync();
                    tempmsgdata.szTextMsg = ImEmtion.GetGifEmotion(tempmsgdata.szTextMsg);
                    console.log(tempmsgdata.szTextMsg);
                }
                szmsgs.push(tempmsgdata);
            }
            return szmsgs;
        }

        jSWProtocol.AUDIO_OP_CODES = {
            CAPTURE_BEGIN: 0,
            CAPTURE_END: 1,
            RENDER_BEGIN: 2,
            RENDER_END: 3,
            RENDER_NOTIFY: 4
        };

        jSWProtocol.AUDIO_OP_PLAYSTATUS = {
            PLAY_EVENT_PLAY: 1,
            PLAY_EVENT_CLOSE: 2,
            PLAY_EVENT_EOF: 3
        };

        jSWProtocol.AACOperatePb2_Json = function (audioOperatePb) {
            var audioOperate = {
                iOpCode: audioOperatePb.getIoperatecode()
            };

            if (audioOperate.iOpCode == jSWProtocol.AUDIO_OP_CODES.CAPTURE_END) {
                audioOperate["audioFilePath"] = jSWProtocol.HexToString(audioOperatePb.getSzlocalpath());
                audioOperate["iDuration"] = audioOperatePb.getIduration();
            }

            if (audioOperate.iOpCode == jSWProtocol.AUDIO_OP_CODES.RENDER_NOTIFY) {
                audioOperate["iPlayStatu"] = audioOperatePb.getIplaystatu();
            }
            return audioOperate;
        }

        jSWProtocol.IMMsgPksPb2_Json = function (imMsgPks) {
            var immsg = {
                iSourceId: imMsgPks.getIsourceid(),
                szSourceId: imMsgPks.getSzsourceid(),
                iTargetId: imMsgPks.getItargetid(),
                szTargetId: imMsgPks.getSztargetid(),
                szmsg: [],
                isPic: imMsgPks.getBispic(),
                isDownload: imMsgPks.getBisdownload(),
                imsgtype: imMsgPks.getImsgtype()
            }

            var szmsgdatas = proto.BVCU.IM_Msgs.deserializeBinary(imMsgPks.getPayload());
            immsg.szmsg = jSWProtocol.IMMsgPb2_Json(szmsgdatas);
            return immsg;
        }

        jSWProtocol.IMMSGTypes = {
            UNKNOWN: 0,
            TEXT: 1,
            FACE: 2,
            GPS: 3,
            FILE: 4,
            PICTURE: 5,
            AUDIO: 6,
            VIDEO: 7,
            CONF_AUDIO: 8,
            CALL_AUDIO: 9,
            CALL_VIDEO: 10,
            CUSTOM: 31,
            onlyOcx: function (imMsgType) {
                return (imMsgType == this.FILE || imMsgType == this.AUDIO);
            }
        }

        jSWProtocol.EVENTACTIONS = {
            NONE: 0, //无效值
            DISKERROR: 0x0001, //磁盘错误。源：PU/NRU的Storage
            OUTOFDISKSPACE: 0x0002, //磁盘空间不足。源：PU/NRU的Storage
            VIDEOLOST: 0x1000, //视频丢失。源：PU 的 VideoIn
            VIDEOMD: 0x1001, //运动检测。源：PU 的 VideoIn
            VIDEOOCCLUSION: 0x1002, //视频遮挡。源：PU 的 VideoIn
            ALERTIN: 0x1003, //报警输入。源：PU 的 AlertIn
            PERIOD: 0x1004, //周期报警。源：PU 的 周期报警    
            PUONLINE: 0x1005, //PU上线。源：PU
            PUOFFLINE: 0x1006, //PU下线。源：PU
            LOWTEMPERATURE: 0x1007, //低温报警。源：PU 的 TemperatureIn
            HIGHTEMPERATURE: 0x1008, //高温报警。源：PU 的 TemperatureIn
            SLOWSPEED: 0x1009, //低速报警。源：PU 的 GPS
            OVERSPEED: 0x100a, //超速报警。源：PU 的 GPS
            INTOREGION: 0x100b, //进入区域报警。源：PU 的 GPS
            OUTREGION: 0x100c, //出区域报警。源：PU 的 GPS
            LOWVOLTAGE: 0x100d, //低电压报警。源：PU 的 VoltageIn
            HIGHVOLTAGE: 0x100e, //高电压报警。源：PU 的 VoltageIn

            //NRU相关
            NRUONLINE: 0x2000, //NRU上线
            NRUOFFLINE: 0x2001, //NRU下线

            //VTDU相关
            VTDUONLINE: 0x3000,
            VTDUOFFLINE: 0x3001,

            //CMS相关
            CMSONLINE: 0x4000,
            CMSOFFLINE: 0x4001,

            //用户(User)相关
            USERLOGIN: 0x5000, //用户登录
            USERLOGOUT: 0x5001, //用户注销

            //decorder(解码器）相关
            DECONLINE: 0x6000,
            DECOFFLINE: 0x6001,

            //该值及往后的值为自定义类型
            CUSTOM: 0x10000000,
        };

        jSWProtocol.BVCU_EventStoragePb2_Json = function (eventStoragePb) {
            eventStoragePb = proto.WEBBVCU.EventStorage.deserializeBinary(eventStoragePb);
            var eventStorage = {
                iresult: eventStoragePb.getIresult(),
                szfilename: this.HexToString(eventStoragePb.getSzfilename()),
                itimestamp: eventStoragePb.getItimestamp()
            };
            return eventStorage;
        }

        jSWProtocol.Int2Uint = function (int32) {
            var rcuint32 = Number(int32);
            var rc = new Uint32Array(1);
            rc[0] = rcuint32;
            return rc[0];
        }

        jSWProtocol._CmdParse = function (iMethod, iSubMethod) {
            var sResult = jSW.DependencyMgr.cmdParseInJect(function (cmdParse) {
                return cmdParse.parseCmd(iMethod, iSubMethod);
            });
            return sResult;
        };

        jSWProtocol.OperateSearchLogs2_Json = function (payload) {
            var searchResponse = proto.BVCU.Search.Search_Response.deserializeBinary(payload);
            var searchinfopb = searchResponse.getStsearchinfo();
            var operationLogs = searchResponse.getPoperatelogList();
            var searchResultJson = {
                info: null,
                result: []
            };
            var searchinfo = {
                ipostition: searchinfopb.getIpostition(),
                icount: searchinfopb.getIcount(),
                itotalcount: searchinfopb.getItotalcount()
            };

            searchResultJson.info = searchinfo;

            var oplog = null;
            var oplogjon = null;
            for (operationindex in operationLogs) {
                oplog = operationLogs[operationindex];
                //oplog = new proto.BVCU.Search.Search_OperateLog();
                oplogjon = {
                    szsourceid: oplog.getSzsourceid(),
                    szusername: oplog.getSzusername(),
                    sztargetid: oplog.getSztargetid(),
                    imethod: oplog.getImethod(),
                    isubmethod: oplog.getIsubmethod(),
                    iapplierid: oplog.getIapplierid(),
                    itargetindex: oplog.getItargetindex(), //目
                    ioperatetime: oplog.getIoperatetime(),
                    bresult: oplog.getIresult(),
                    szdescription: oplog.getSzdescription(),
                    smethod: null
                };
                oplogjon.smethod = jSWProtocol._CmdParse(oplogjon.imethod, oplogjon.isubmethod);
                searchResultJson.result.push(oplogjon);
            }

            return searchResultJson;
        }

        jSWProtocol._GetAllRescources = function (session, rescources) {
            var allRescources = [];
            var pu = null;
            var channel = null;
            var channelrec = null;
            var res = null;
            var restemp = null;
            try {
                for (puindex in session._arr_pu) {
                    pu = session._arr_pu[puindex];
                    res = null;
                    for (recindex in rescources) {
                        restemp = rescources[recindex];
                        if (pu._id_pu == restemp.puid) {
                            res = restemp;
                            break;
                        }
                    }
                    if (res == null) {
                        res = {
                            puid: pu._id_pu,
                            puname: pu._info_pu.name.length > 0 ? pu._info_pu.name : pu._id_pu,
                            config: false,
                            channels: []
                        };
                        for (channelindex in pu._arr_channel) {
                            channel = pu._arr_channel[channelindex];
                            channelrec = {
                                channelindex: channel._id_chanel,
                                iscansee: false
                            };
                            res.channels.push(channelrec);
                        }
                    }
                    allRescources.push(res);
                }
            } catch (e) {}
            return allRescources;
        }

        jSWProtocol._LocalResource_2Pb = function (localresources, payload) {
            var recsourcepb = null;
            var recsource = null;
            var puPerPb = null;
            var channelUint8Array = null;
            var channeltemp = null;
            for (recindex in localresources) {
                recsource = localresources[recindex];
                recsourcepb = new proto.BVCU.UserConfig.Resource();
                puPerPb = new proto.BVCU.UserConfig.PUPermissions();
                recsourcepb.setSpuid(recsource.puid);
                recsourcepb.setSzpermissions(puPerPb);
                puPerPb.setConfig((recsource.config ? jSW.SwUserManager.PERMISSIONS.ON : jSW.SwUserManager.PERMISSIONS.OFF));
                channelUint8Array = new Uint8Array(recsource.channels.length);

                for (channelindex in recsource.channels) {
                    channeltemp = recsource.channels[channelindex];
                    channelUint8Array[channeltemp.channelindex] = (channeltemp.iscansee ? jSW.SwUserManager.PERMISSIONS.ON : jSW.SwUserManager.PERMISSIONS.OFF);
                }
                puPerPb.setChannel(channelUint8Array);
                payload.addPresource(recsourcepb, recindex);
            }
        }

        jSWProtocol._BVCU_UCFG_Resource_Pb2Json = function (resource_pb, sessionhelper) {
            var puPermission = resource_pb.getSzpermissions();
            var channelPermission = [];
            var channelPermissionlistpb = puPermission.getChannel();
            var channelPermissionTemp = null;
            var channelPermissionpb = null;
            var pujson = null;
            for (channelIndex in channelPermissionlistpb) {
                channelPermissionpb = channelPermissionlistpb[channelIndex];
                channelPermissionTemp = {
                    channelindex: Number(channelIndex),
                    iscansee: channelPermissionlistpb[channelIndex] == jSW.SwUserManager.PERMISSIONS.ON
                };
                channelPermission.push(channelPermissionTemp);
            }

            pujson = sessionhelper._swGetPu(resource_pb.getSpuid());
            var json = {
                puid: resource_pb.getSpuid(),
                puname: (pujson != null && pujson._info_pu.name.length > 0) ? pujson._info_pu.name : resource_pb.getSpuid(),
                config: puPermission.getConfig() == 1,
                channels: channelPermission
            };
            return json;
        }

        jSWProtocol.Walltime2Json = function (walltime) {
            var sttime = {};
            for (var iKey in walltime) {
                sttime[iKey.substr(1)] = walltime[iKey];
            }
            return sttime;
        }

        jSWProtocol.SetGpsData = function (gpsData, options) {
            var paramName = null;
            var desData = 0;
            for (var iIndex in options) {
                if (iIndex) {
                    paramName = "setI";
                    if (iIndex == "lat") {
                        paramName += "latitude";
                    } else if (iIndex == "long") {
                        paramName += "longitude";
                    } else {
                        paramName += iIndex;
                    }
                    if (gpsData[paramName]) {
                        desData = jSWProtocol.TransNumberInt2Uint(options[iIndex]);
                        desData = parseInt(desData)
                        gpsData[paramName](desData)
                    }
                }
            }
        }

        var TransNumberInt2UintHelper = Math.pow(2, 32);
        jSWProtocol.TransNumberInt2Uint = function (uint32Data) {
            var result = uint32Data;
            if (uint32Data < 0) {
                result += TransNumberInt2UintHelper;
            }
            return result;
        }

        var ParseNumberUint2IntHelper = TransNumberInt2UintHelper / 2;
        jSWProtocol.ParseNumberUint2Int = function (int32Data) {
            var result = int32Data;
            if (int32Data > ParseNumberUint2IntHelper) {
                result -= TransNumberInt2UintHelper;
            }
            return result;
        }

        jSWProtocol.GetWalltime = function () {
            var wallTime = new proto.BVCU.PUConfig.WallTime();
            var myData = new Date();
            var info = {
                year: myData.getUTCFullYear(),
                month: myData.getUTCMonth(),
                day: myData.getUTCDay(),
                hour: myData.getUTCHours(),
                minute: myData.getUTCMinutes(),
                second: myData.getUTCSeconds()
            };
            var infoSetF = null;
            for (var iIndex in info) {
                infoSetF = wallTime["setI" + iIndex];
                infoSetF.bind(wallTime)(info[iIndex]);
            }
            return wallTime;
        }


        jSWProtocol.GPSDatapb2Json = function (gpsdatapb) {
            var gpsdatajson = {};
            jSWProtocol.BasePbToJson(gpsdatapb, gpsdatajson);
            gpsdatajson.long = gpsdatajson.longitude;
            gpsdatajson.lat = gpsdatajson.latitude;
            return gpsdatajson;
        }

        function GetPbGetName(mysrcpb, handle, bToInt) {
            var myvalue = null;
            var myPbProName = "";
            for (var pbproName in mysrcpb.__proto__) {
                if (pbproName.indexOf("get") == 0) {
                    if (pbproName.indexOf("JsPbMessageId") >= 0 || pbproName.indexOf("Extension") >= 0) {
                        continue;
                    }
                    myvalue = mysrcpb[pbproName]();
                    if (typeof myvalue == "number") {
                        if (bToInt) {
                            myvalue = jSWProtocol.ParseNumberUint2Int(myvalue);
                        } else {
                            myvalue = jSWProtocol.TransNumberInt2Uint(myvalue);
                        }
                    }
                    myPbProName = pbproName.slice(3);
                    myPbProName = myPbProName.indexOf("St") == 0 ? myPbProName.slice(2) : myPbProName.slice(1);
                    handle(myPbProName, myvalue);
                }
            }
        }

        jSWProtocol.BasePbToJson = function (srcpb, desData) {
            GetPbGetName(srcpb, function (pbProName, proValue) {
                desData[pbProName] = proValue;
                if (typeof proValue == "object") {
                    desData[pbProName] = {};
                    jSWProtocol.BasePbToJson(proValue, desData[pbProName]);
                }
            }, true);
        }


        jSWProtocol.FTPFileInfoGPSInfo2Json = function (fTPFileInfoGPSInfopb) {
            var ftpFileInfopb = fTPFileInfoGPSInfopb.getFileinfo();
            var fileInfoJson = this.FTPFileInfo2Json(ftpFileInfopb);
            var szpuid = this.HexToString(fTPFileInfoGPSInfopb.getSzpuid());
            var szpuname = this.HexToString(fTPFileInfoGPSInfopb.getSzpuname());
            var szgpsdata = fTPFileInfoGPSInfopb.getSzgpsdataList();
            var data = {
                fileinfo: fileInfoJson,
                gpsdata: {
                    szpuid: szpuid,
                    szpuname: szpuname,
                    szgpspoint: []
                }
            };

            var gpsdata = null;
            var gpsdatapb = null;
            for (gpsindex in szgpsdata) {
                gpsdatapb = szgpsdata[gpsindex];
                gpsdata = jSWProtocol.GPSDatapb2Json(gpsdatapb);
                data.gpsdata.szgpspoint.push(gpsdata);
            }

            return data;
        }

        jSWProtocol.FTPFileInfo2Json = function (fTPFileInfopb) {
            var fileinfo = {
                szFilePath: jSWProtocol.HexToString(fTPFileInfopb.getSzfilepath()),
                szFileName: jSWProtocol.HexToString(fTPFileInfopb.getSzfilename()),
                iTimeBegin: fTPFileInfopb.getItimebegin(),
                iTimeEnd: fTPFileInfopb.getItimeend(),
                iFileSize: fTPFileInfopb.getIfilesize()
            };
            return fileinfo;
        }

        jSWProtocol.CmsChannelInfo2Json = function (cmsChannelInfopb) {
            var json = {
                szID: null,
                iApplierID: 0,
                iMediaDir: 0,
                bOverTCP: 0
            };
            json.szID = cmsChannelInfopb.getSzid();
            json.iApplierID = cmsChannelInfopb.getIapplierid();
            json.iMediaDir = cmsChannelInfopb.getImediadir();
            json.bOverTCP = cmsChannelInfopb.getBovertcp();
            return json;
        }

        jSWProtocol.CmsDialogInfos2Json = function (CmsDialogInfospb) {
            var sz = [];
            var list = CmsDialogInfospb.getCmsdialoginfosList();
            list.forEach(function (item) {
                var cmsDialogInfo = {
                    stRequestor: null,
                    stTarget: null,
                    szVTDUID: null,
                    iChannelIndex: -1,
                    iPathWay: 0
                };
                cmsDialogInfo.stRequestor = jSWProtocol.CmsChannelInfo2Json(item.getStrequestor());
                cmsDialogInfo.stTarget = jSWProtocol.CmsChannelInfo2Json(item.getSttarget());
                cmsDialogInfo.szVTDUID = item.getSzvtduid();
                cmsDialogInfo.iChannelIndex = item.getIchannelindex();
                cmsDialogInfo.iPathWay = item.getIpathway();
                sz.push(cmsDialogInfo);
            });
            return sz;
        }


        /*
        json = {
            puid: puid,
            permission: {
                config: puPermission.getConfig() == 1,
                channels: [
                    {
                        channelindex:
                        iscansee:
                    }
                ]
            }
        };
        */

        jSWProtocol.GroupPb_2Json = function (group_pb) {
            var userGroupJson = {
                id: group_pb.getSid(),
                name: group_pb.getSname(),
                parentid: group_pb.getSparentid()
            };

            return userGroupJson;
        }

        jSWProtocol.GroupInfoPb_2Json = function (groupinfo_pb, sessionHelper) {
            var resourcelist = groupinfo_pb.getPresourceList();
            var resources = this.BVCU_UCFG_Resource_Pb2Json(resourcelist, sessionHelper);

            var data = {
                id: groupinfo_pb.getSid(),
                name: groupinfo_pb.getSname(),
                parentid: groupinfo_pb.getSparentid(),
                description: groupinfo_pb.getSdescription(),
                resources: resources,
                allresources: jSWProtocol._GetAllRescources(sessionHelper, [])
            };
            return data;
        }

        jSWProtocol.USER_USERPb_2Json = function (user_pb) {
            var user = {
                id: user_pb.getSid(),
                groupid: user_pb.getSgroupid(),
                name: user_pb.getSname()
            };
            return user;
        }

        jSWProtocol.USER_USERINFO_Pb2Json = function (userinfo_pb, sessionHelper) {
            var resources = userinfo_pb.getPresourceList();
            resources = this.BVCU_UCFG_Resource_Pb2Json(resources, sessionHelper);
            var iSysadmin = userinfo_pb.getIsysadmin();
            var data = null;
            data = {
                sId: userinfo_pb.getSid(),
                sPasswd: userinfo_pb.getSpasswd(),
                bSetPasswd: userinfo_pb.getSpasswd().length > 0,
                sysadmin: {
                    isGroup: ((iSysadmin & jSW.SwUserManager.SYSADMIN.GROUP) == jSW.SwUserManager.SYSADMIN.GROUP),
                    isUser: ((iSysadmin & jSW.SwUserManager.SYSADMIN.USER) == jSW.SwUserManager.SYSADMIN.USER),
                    isDev: ((iSysadmin & jSW.SwUserManager.SYSADMIN.DEV) == jSW.SwUserManager.SYSADMIN.DEV),
                    isDevAss: ((iSysadmin & jSW.SwUserManager.SYSADMIN.DEVASS) == jSW.SwUserManager.SYSADMIN.DEVASS),
                },
                iPtz: userinfo_pb.getIptz(),
                sServerId: userinfo_pb.getSserverid(),
                sGroupId: userinfo_pb.getSgroupid(),
                iMaxSession: userinfo_pb.getImaxsession(),
                sAllocateId: userinfo_pb.getSallocateid(),
                sName: userinfo_pb.getSname(),
                sPhone: userinfo_pb.getSphone(),
                sEmail: userinfo_pb.getSemail(),
                sDescription: userinfo_pb.getSdescription(),
                Resource: resources
            };
            return data;
        }

        jSWProtocol.JsIMMSG2Pb = function (msgitems, pcallback) {
            var pbPaylod = new proto.BVCU.IM_Msgs();
            pbPaylod.rcallback = null;
            var msgitem = null;
            var msgitempb = null;
            var ImEmtion = jSW.DependencyMgr.GetEmotionSync();
            for (var iindex = 0; iindex < msgitems.length; iindex++) {
                msgitem = msgitems[iindex];
                msgitempb = pbPaylod.addSzmsgs();
                msgitempb.setItype(msgitem.iType);
                pbPaylod.iitemmsgtype = msgitem.iType;
                switch (msgitem.iType) {
                    case jSWProtocol.IMMSGTypes.TEXT:
                        msgitempb.setSztextmsg(msgitem.data);
                        break;
                    case jSWProtocol.IMMSGTypes.FACE:
                        var eCId = ImEmtion.GetEmotionCode(msgitem.data);
                        msgitempb.setSztextmsg(eCId);
                        break;
                    case jSWProtocol.IMMSGTypes.PICTURE:
                    case jSWProtocol.IMMSGTypes.FILE:
                        var imfile = new proto.BVCU.IM_File();
                        imfile.setSzid(msgitem.nruid);
                        imfile.setSzfilepath(msgitem.data);
                        msgitempb.setStfile(imfile);
                        pbPaylod.rcallback = pcallback;
                        break;
                    case jSWProtocol.IMMSGTypes.AUDIO:
                        var imfile = new proto.BVCU.IM_File();
                        imfile.setSzid(msgitem.nruid);
                        imfile.setSzfilepath(msgitem.data);
                        imfile.setIduration(msgitem.iduration);
                        msgitempb.setStfile(imfile);
                        pbPaylod.rcallback = pcallback;
                        break;
                    case jSWProtocol.IMMSGTypes.GPS:
                        var gpsData = new proto.BVCU.PUConfig.GPSData();
                        var curWTime = jSWProtocol.GetWalltime();
                        gpsData.setSttime(curWTime);
                        jSWProtocol.SetGpsData(gpsData, msgitem.data);
                        msgitempb.setStgpsdata(gpsData);
                        break;
                    default:
                        break;
                }
            }
            return pbPaylod;
        }

        jSWProtocol.BVCU_UCFG_Resource_Pb2Json = function (resource_pbs, sessionhelper) {
            var resource_pb = null;
            var resources = [];
            var resource = null;

            for (resourceindex in resource_pbs) {
                resource_pb = resource_pbs[resourceindex];
                resource = this._BVCU_UCFG_Resource_Pb2Json(resource_pb, sessionhelper);
                resources.push(resource);
            }
            return resources;
        }

        jSWProtocol._BVCU_DayTimeSlice_Pb2Json = function (bvcu_daytimeslice_pb) {
            var json = {
                iHourBegin: bvcu_daytimeslice_pb.getChourbegin(),
                iMinuteBegin: bvcu_daytimeslice_pb.getCminutebegin(),
                iSecondBegin: bvcu_daytimeslice_pb.getCsecondbegin(),
                iHourEnd: bvcu_daytimeslice_pb.getChourend(),
                iMinuteEnd: bvcu_daytimeslice_pb.getCminuteend(),
                iSecondEnd: bvcu_daytimeslice_pb.getCsecondend()
            }
            return json;
        }

        jSWProtocol.BVCU_DayTimeSlice_Pb2Json = function (bvcu_daytimeslice_pb) {
            var stday = bvcu_daytimeslice_pb.getStdayList();
            var json = {
                period0: {
                    time: this._BVCU_DayTimeSlice_Pb2Json(stday[0].getSttime()),
                    rco: this._OnlineControlOne_Pb2Json(stday[0].getStrco())
                },
                period1: {
                    time: this._BVCU_DayTimeSlice_Pb2Json(stday[1].getSttime()),
                    rco: this._OnlineControlOne_Pb2Json(stday[1].getStrco())
                },
                period2: {
                    time: this._BVCU_DayTimeSlice_Pb2Json(stday[2].getSttime()),
                    rco: this._OnlineControlOne_Pb2Json(stday[2].getStrco())
                },
                period3: {
                    time: this._BVCU_DayTimeSlice_Pb2Json(stday[3].getSttime()),
                    rco: this._OnlineControlOne_Pb2Json(stday[3].getStrco())
                },
                period4: {
                    time: this._BVCU_DayTimeSlice_Pb2Json(stday[4].getSttime()),
                    rco: this._OnlineControlOne_Pb2Json(stday[4].getStrco())
                },
                period5: {
                    time: this._BVCU_DayTimeSlice_Pb2Json(stday[5].getSttime()),
                    rco: this._OnlineControlOne_Pb2Json(stday[5].getStrco())
                },
            }
            return json;
        }

        jSWProtocol.BVCU_DayTimeSlice_Pb2JsonOnly = function (bvcu_daytimeslice_pb) {
            var stday = bvcu_daytimeslice_pb.getStdayList();
            var json = {
                period0: this._BVCU_DayTimeSlice_Pb2Json(stday[0]),
                period1: this._BVCU_DayTimeSlice_Pb2Json(stday[1]),
                period2: this._BVCU_DayTimeSlice_Pb2Json(stday[2]),
                period3: this._BVCU_DayTimeSlice_Pb2Json(stday[3]),
                period4: this._BVCU_DayTimeSlice_Pb2Json(stday[4]),
                period5: this._BVCU_DayTimeSlice_Pb2Json(stday[5])
            }
            return json;
        }

        jSWProtocol._OnlineControlOne_Pb2Json = function (onlinecontrolone_pb) {
            var json = {
                iTrigger: onlinecontrolone_pb.getItrigger(),
                iEvent: onlinecontrolone_pb.getIevent(),
                iOnlineTime: onlinecontrolone_pb.getIonlinetime(),
                iThrough: onlinecontrolone_pb.getIthrough()
            };
            return json;
        }

        jSWProtocol.Drivers2Json = function (drivers) {
            var driverPb = null;
            var PersonInfopb = null;
            var driverJson = null;
            var driverJsonArray = [];
            for (driverindex in drivers) {
                driverPb = drivers[driverindex];
                PersonInfopb = driverPb.getStpersoninfo();
                driverJson = {
                    stPersonInfo: {
                        szName: PersonInfopb.getSzname(),
                        cGender: PersonInfopb.getCgender(),
                        sAge: PersonInfopb.getSage()
                    },
                    szCardID: driverPb.getSzcardid(),
                    szCertificateID: driverPb.getSzcertificateid(),
                    szIssuingAgency: driverPb.getSzissuingagency()
                };
                driverJsonArray.push(driverJson);
            }
            return driverJsonArray;
        }

        jSWProtocol.ParamSearchList = function (pstSearchInfo, pstFilter) {
            var search = new proto.BVCU.Search.Search_Request();
            var searchInfo = new proto.BVCU.Search.SearchInfo();
            var filefilter = new proto.BVCU.Search.Search_FileFilter();
            var ichannelindex = pstFilter.iChannelIndex;

            searchInfo.setIcount(pstSearchInfo.iCount);
            searchInfo.setIpostition(pstSearchInfo.iPostition);
            searchInfo.setItype(pstSearchInfo.iType);

            filefilter.setSzpuid(pstFilter.szPUID);
            if (pstFilter.iChannelIndex == -1) {
                ichannelindex = 0xffffffff;
            }
            filefilter.setIchannelindex(ichannelindex);
            filefilter.setIfiletype(pstFilter.iFileType);
            filefilter.setItimebegin(pstFilter.iTimeBegin);
            filefilter.setItimeend(pstFilter.iTimeEnd);
            filefilter.setIfilesizemin(pstFilter.iFileSizeMin);
            filefilter.setIfilesizemax(pstFilter.iFileSizeMax);
            filefilter.setIrecordtype(pstFilter.iRecordType);
            filefilter.setSzdesc1(pstFilter.szDesc1);
            filefilter.setSzdesc2(pstFilter.szDesc2);
            filefilter.setItimecondition(pstFilter.iTimeCondition);
            filefilter.setSzfilename(pstFilter.filename);
            filefilter.setSzuserid(pstFilter.userid);

            search.setStsearchinfo(searchInfo);
            search.setStfilefilter(filefilter);

            return search;
        }

        jSWProtocol.JsonParamLogin = function (EmmsHeader, RequestHeader, BVCU_ServerParam) {
            this.emms = EmmsHeader; /**EmmsHeader API协议头。	M*/
            this.request = RequestHeader;
            this.param = BVCU_ServerParam;
        }

        jSWProtocol.JsonParamNoAttach = function (EmmsHeader, MSGType) {
            var request = new proto.WEBBVCU.Request();
            request.setEmms(EmmsHeader);
            request.setMsgtype(MSGType);
            return request;
        }

        jSWProtocol.JsonParamDialog = function (EmmsHeader, MsgType, dialogParam) {
            var request = new proto.WEBBVCU.Request();
            request.setEmms(EmmsHeader);
            request.setMsgtype(MsgType);
            request.setPayload(dialogParam.serializeBinary().buffer);
            request.setTargetid(dialogParam.getId());
            request.setTargetindex(dialogParam.getMajor());
            request.setHdlg(dialogParam.getHdlg());
            return request;
        }

        jSWProtocol.JsonParamTSPDialog = function (EmmsHeader, MsgType, Param) {
            var request = new proto.WEBBVCU.Request();
            request.setEmms(EmmsHeader); /**EmmsHeader API协议头。	M*/
            request.setMsgtype(MsgType);
            request.setPayload(Param.serializeBinary().buffer);
            return request;
        }

        jSWProtocol.JsonParamCommand = function (EmmsHeader, MsgType, request) {
            request.setEmms(EmmsHeader);
            request.setMsgtype(MsgType);
            return request;
        }

        jSWProtocol.txRequest = new Array(); // 记录已经发送的命令, 响应时调用响应的回调函数通知
        jSWProtocol.txRequestHelp = new Array();
        jSWProtocol.txGetRequestId = function () {
            var cmdId = -1,
                i = 0;
            for (; i < jSWProtocol.txRequest.length; i++) {
                if (jSWProtocol.txRequest[i] == null) {
                    cmdId = i;
                }
            }
            if (-1 == cmdId) {
                cmdId = i;
                jSWProtocol.txRequest[i] = 1;
            }

            return cmdId;
        }

        jSWProtocol.txGetRequestRelayId = function () {
            var cmdId = -1,
                i = 0;
            for (; i < jSWProtocol.txRequestHelp.length; i++) {
                if (jSWProtocol.txRequestHelp[i] == null) {
                    cmdId = i;
                }
            }
            if (-1 == cmdId) {
                cmdId = i;
                jSWProtocol.txRequestHelp[i] = 1;
            }

            return cmdId;
        }

        jSWProtocol.txSetRequestOption = function (requestId, option) {
            jSWProtocol.txRequest[requestId] = option;
        }

        jSWProtocol.txSetRequestHelperOption = function (requestId, option) {
            jSWProtocol.txRequestHelp[requestId] = option;
        }

        jSWProtocol.txGetRequestOption = function (requestId) {
            if (requestId >= 0 && requestId < jSWProtocol.txRequest.length) {
                return jSWProtocol.txRequest[requestId];
            }
            return null;
        }

        jSWProtocol.txGetRequestHelperOption = function (requestId) {
            if (requestId >= 0 && requestId < jSWProtocol.txRequestHelp.length) {
                return jSWProtocol.txRequestHelp[requestId];
            }
            return null;
        }

        jSWProtocol.txOnResponseUint8Array = function (requestId, szUint8) {
            var cmdId = requestId;
            var response = null;

            try {
                response = proto.WEBBVCU.Response.deserializeBinary(szUint8);
            } catch (e) {
                var err = jSWProtocol.HexToString(szUint8);
                jSWUtils.debugLog("deserialize protobuf error:" + err);
                jSWUtils.debugLog(e);
            }

            if (response) {
                cmdId = response.getId();

                // 根据Id找到对应的option
                options = jSWProtocol.txGetRequestOption(cmdId);

                if (-2017 == cmdId) {
                    // -2017 是服务器通知Notify
                    var emmsHeader = response.getEmms();
                    var session = jSWUtils._getSessionById(emmsHeader.getSession());

                    if (session) {
                        if (options == null) {
                            options = {
                                cmd: null,
                                callback: null
                            }
                        }
                        session._internalNotify(options, response);
                    } else {
                        var msgType = response.getMsgtype();
                        console.warn(msgType + 'session not found ' + emmsHeader.getSession());
                    }
                } else {
                    if (options) {
                        jSWProtocol.txSetRequestOption(cmdId, null);

                        // 回调
                        if (options.success && typeof (options.success) == 'function') {
                            options.success(response, 0);
                        }
                    }
                }
            }
        }

        jSWProtocol.txOnResponse = function (requestId, data) {
            var cmdId = requestId;
            var options = null;

            try {
                if (jSWUtils._mMgr.bUseOcx()) {
                    jSWProtocol.txOnResponseUint8Array(cmdId, data);
                } else {
                    var reader = new FileReader();
                    reader.addEventListener("loadend", function () {
                        var result = reader.result;
                        var arr = new Uint8Array(result);
                        jSWProtocol.txOnResponseUint8Array(cmdId, arr);
                    });
                    reader.readAsArrayBuffer(data);
                }
            } catch (e) {
                console.error('error: ' + e);
                options = jSWProtocol.txGetRequestOption(cmdId);
                if (options) {
                    jSWProtocol.txSetRequestOption(cmdId, null);

                    // 回调
                    if (options.error && typeof (options.error) == 'function') {
                        options.error(options.option, data, 'get json object fail');
                    }
                }
            }
        }


        jSWProtocol.testSend = "";
        /**发送请求, ajax post, options {
                session: this,
                cmd: 'login',
                request: request,
                tag: null,
                enforce: boolean,
                attchdata: ,
                callback: function(options, response) { }
            }*/
        jSWProtocol._internalSend = function (options) {
            var session = options.session;
            var request = options.request;

            var requestId = jSWProtocol.txGetRequestId();
            request.setId(Number(requestId));
            request.setRelayid(-1);
            if (options.attchdata != null) {
                var requestRelayId = jSWProtocol.txGetRequestRelayId();
                request.setRelayid(requestRelayId);
            }

            var src_data = null;
            var crypto_data = null;


            if (request.getMsgtype() == proto.WEBBVCU.MSGType.WEB_BVCU_GET_PUBKEY) {
                /**获取密钥, 暂时不加密*/
                console.log('获取公钥');
                crypto_data = request.serializeBinary();
            } else if (request.getMsgtype() == proto.WEBBVCU.MSGType.WEB_BVCU_LOGIN) {
                /**登录使用rsa密钥加密*/
                src_data = request.getPayload();
                crypto_data = jnRSA.RSA_Encrypt_32UIntTo8Array(session.pubkey_d, session.pubkey_n, src_data);
                request.setPayload(crypto_data);
                crypto_data = request.serializeBinary();

                console.log('登录:公钥 d:' + session.pubkey_d + ', n:' + session.pubkey_n);
            } else {
                if (session._serverConfig || (options.enforce != null && options.enforce)) {
                    crypto_data = request.serializeBinary();
                } else {
                    session._internalOnResponseFail(options, '', 'not login');
                    return jSW.RcCode.RC_CODE_E_DISCONNECTED;
                }
            }

            if (crypto_data) {
                if (!jSWUtils._mMgr.bUseOcx()) {
                    crypto_data = crypto_data.buffer;
                }

                var ajax_options = {
                    data: crypto_data,
                    option: options,
                    requestID: requestId,
                    success: function (jsonObject, status) {
                        session._internalOnResponseSuccess(options, jsonObject, status);
                    },
                    error: function (opt, responseText, errMsg) {
                        session._internalOnResponseFail(options, responseText, errMsg);
                    }
                };

                jSWProtocol.txSetRequestOption(requestId, ajax_options);
                if (options.attchdata != null) {
                    jSWProtocol.txSetRequestHelperOption(requestRelayId, options.attchdata);
                }

                jSWProtocol.testSend = ajax_options.data;
                return session._send(ajax_options.data);
            }

            return jSW.RcCode.RC_CODE_E_FAIL;
        }



    })(jSWProtocol);

    //var HTTPNruFile = jSW.DependencyMgr.ClaimModule('HTTPNruFile')
    dMgr.RegClaimedModule("jSWProtocol");

    //var PuLoad = jSW.DependencyMgr.GetModule("PuLoad");
})(jSW.DependencyMgr);