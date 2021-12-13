(function (dMgr) {
    var jSWProtocol = dMgr.GetModule("jSWProtocol");
    var jSWUtils = dMgr.GetModule("jSWUtils");
    var jSWOptions = dMgr.GetModule("jSWOptions");

    // ----SWCONFERENCE---
    jSW.SwConfManager = function (parent) {
        this._parent = parent;
        this._clear();
    };

    jSW.SwConfManager.MODE_SPEAK = {
        DISCUSSIONGROUP: (0 << 0),//讨论组模式
        CHAIRMAN: (1 << 0),//演讲培训模式
    }//会议发言类型

    jSW.SwConfManager.MODE_JOIN = {
        INVITE: (0 << 4),//不能主动加入
        PASSWORD: (1 << 4),//主动加入，但需要输入密码
        FREE: (2 << 4),//主动加入，不提示密码
    }//会议加入类型

    jSW.SwConfManager.MODE_APPLY = {
        NEEDAGREE: (0 << 7),//需要管理员同意
        AUTOAGREE: (1 << 7),//服务器自动同意
    }//是否由服务器自动同意发言申请

    jSW.SwConfManager.MODE_START = {
        STOPADMIN: (0 << 8),//由会议管理员来开始
        FOREVER: (1 << 8),//会议自动开始，并不允许停止
    }//会议是否永远开始（自动开始，不准停止）

    jSW.SwConfManager.STATUS = {
        STOPPED: 0,
        STARTED: 1
    }

    jSW.SwConfManager.POWER = {
        ADMIN: 1,
        MODETATOR: 2
    }

    jSW.SwConfManager._UTILS = {
        participatorToLocalUser: function (parent, participator) {
            var filterPuId = participator.getSzid();
            var appendData = filterPuId;
            while(filterPuId.indexOf("PU_0") == 0){
                filterPuId = filterPuId.replace("PU_0", "PU_");
            }
            

            var partiName = participator.getSzusername();
            while(partiName.indexOf("PU_0") == 0){
                partiName = partiName.replace("PU_0", "PU_");
            }
            
            var particular = {
                id: filterPuId,
                name: partiName,
                addr: participator.getSzaddr(),
                applierid: participator.getIapplierid(),
                pid: participator.getIpid(),
                //allowedmediadir: participator.getIallowedmediadir(),
                aliasname: participator.getSzaliasname(),
                isonline: (participator.getIstatus() > 0),
                isleave: (participator.getIstatus() == 0x00000001 || participator.getIstatus() <= 0),
                isinseat: (participator.getIstatus() == 0x00000002 || participator.getIstatus()== 0x00000003),
                isSpeak: (participator.getIstatus()== 0x00000003),
                isadmin: (participator.getIpower() & jSW.SwConfManager.POWER.ADMIN) == jSW.SwConfManager.POWER.ADMIN,
                ismodetator: (participator.getIpower() & jSW.SwConfManager.POWER.MODETATOR) == jSW.SwConfManager.POWER.MODETATOR,
                volume: participator.getIvolume(),
                _appendData: appendData
            };
            return particular;
        },
        confBaseInfoToLocalInfo: function (confBaseInfo) {
            var baseinfo = {
                name: confBaseInfo.getSzname(),
                id: confBaseInfo.getSzid(),
                password: confBaseInfo.getSzpassword(),
                bIsDiscussiongroup: (confBaseInfo.getImode() & 0x0f) == 0,
                bIsChairman: (confBaseInfo.getImode() & 0x0f) == 1,
                bIsInvite: (confBaseInfo.getImode() & 0x38) == 0,
                bIsPassword: (confBaseInfo.getImode() & 0x38) == 0x10,
                bIsFree: (confBaseInfo.getImode() & 0x38) == 0x20,
                bAutoApply: (confBaseInfo.getImode() & 0x80) == 0x80,
                bAutoStart: (confBaseInfo.getImode() & 0x100) == 0x100,
                bIsStarted: confBaseInfo.getIconfstatus() == jSW.SwConfManager.STATUS.STARTED,
                bAudioRecord: confBaseInfo.getIrecordstatus() == 1,
                szRecordNRUID: confBaseInfo.getSzrecordnruid(),
                _imode: confBaseInfo.getImode(),
                _istatus: confBaseInfo.getIconfstatus(),
                iTimeout: confBaseInfo.getItimeout()
            };
            return baseinfo;
        },
        onlineUserToLocal: function (onlineUser) {
            var onlineuser = {
                userid: onlineUser.getSuserid(),
                devid: onlineUser.getSzdevid(),
                addr: onlineUser.getSzaddr(),
                isonline: onlineUser.getIstatus() == 1,
                iapplierid: onlineUser.getIapplierid(),
                year: onlineUser.getStlogintime().getIyear(),
                month: onlineUser.getStlogintime().getImonth(),
                day: onlineUser.getStlogintime().getIday(),
                hour: onlineUser.getStlogintime().getIhour(),
                minute: onlineUser.getStlogintime().getIminute(),
                second: onlineUser.getStlogintime().getIsecond()
            };

            return onlineuser;
        }
    };

    jSW.SwConfManager.AudioOperateTypes = {
        CAPTURE_BEGIN: 0,
        CAPTURE_END: 1,
        RENDER_BEGIN: 2,
        RENDER_END: 3
    };

    jSW.SwConfManager.prototype = {
        _parent: null,
        _conf_list: [],
        _conf_list_names: [],
        _user_online: [],
        _user_online_names: [],
        _ifocus: -1,
        _callbackManager: null,
        _callback: null,
        _watchCallBack: null,

        _clear: function () {
            this._conf_list = [];
            this._conf_list_names = [];
        },

        /**
         * options{
         *    iOperateCodeType: jSW.SwConfManager.AudioOperateTypes,
         *    szLocalFilePath: ""
         *    callback: function(options, resposne, data){},
         *    tag: {}
         * }
         */
        swLocalAudioRecord: function (options) {
            var payload = new proto.WEBBVCU.AudioOperate();
            payload.setIoperatecode(options.iOperateCodeType);
            if (options.iOperateCodeType == jSWProtocol.AUDIO_OP_CODES.RENDER_BEGIN) {
                payload.setSzlocalpath(jSWUtils.string2Uint8Array(options.szLocalFilePath));
            }
            var rc = jSWProtocol.SendRequest({
                session: this._parent,
                msgtype: proto.WEBBVCU.MSGType.WEB_UTILS_AAC_OPERATE,
                payload: payload,
                callback: options.callback,
                tag: options.tag
            });
            return rc;
        },

        /**
        * callback: function(){}
        * tag: object
        */
        swGetImEmotions: function (options) {
            var ImEmtion = jSW.DependencyMgr.GetEmotionSync();
            jSWUtils.ManualAsyncReply(options.callback, jSW.RcCode.RC_CODE_S_OK, ImEmtion.GetEmotimoUrls(), options.tag);
            return jSW.RcCode.RC_CODE_S_OK;
        },

        _swFocus: function (iIndex) {
            this._ifocus = iIndex;
        },

        _getOnlineUsers: function (options) {
            var rc = this._parent.swGetOnlineUsers({
                callback: function (sender, response, data) {
                    var tag = sender.tag;
                    var confManager = tag.confManager;
                    confManager._swFreshUserOnlielist(data);
                    sender.tag = tag.option.tag;
                    tag.option.callback(sender, response, data);
                },
                tag: {
                    confManager: this,
                    option: options
                }
            })
            return rc;

            var param = jSWProtocol.JsonParamCommand(this._parent._p_emms,
               proto.WEBBVCU.MSGType.WEB_BVCU_GET_USER_ONLINE,
               jSWProtocol.BVCU_Command('', -1, null));

            var rc = jSWProtocol._internalSend({
                cmd: jSWProtocol.RequestHeader.getonuserlist.cmd,
                session: this._parent,
                request: param,
                callback: function (sender, response, data) {
                    var tag = sender.tag;
                    var confManager = tag.confManager;
                    confManager._swFreshUserOnlielist(data);
                    sender.tag = tag.option.tag;
                    tag.option.callback(sender, response, data);
                },
                tag: {
                    confManager: this,
                    option: options
                }
            })

            return rc;
        },

        _onCmrRecvImMsg: function (imInfo) {
            var conf = this.swGetConfByConfId(imInfo.szTargetId);
            conf._onRecvImMsg(imInfo);
        },

        _onCMrNotify: function (msgType, cmd, response) {
            if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_CONF_CREATE ||
                msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_BASEINFO) {
                var szconfBaseInfo = [];
                var pbconfBaseInfo = proto.BVCU.ConfBaseInfo.deserializeBinary(response.getPayload());
                var confbaseinfo = jSW.SwConfManager._UTILS.confBaseInfoToLocalInfo(pbconfBaseInfo);
                szconfBaseInfo.push(confbaseinfo);
                this._swFreshConflist(szconfBaseInfo);
                var conf = this.swGetConfByConfId(confbaseinfo.id);
                this._dispatchCallback(conf, cmd, confbaseinfo.id);
            }
            else if (msgType == proto.WEBBVCU.MSGType.WEB_UTILS_AAC_OPERATE) {
                var pbAudioOperate = proto.WEBBVCU.AudioOperate.deserializeBinary(response.getPayload());
                var notiAudioOperate = jSWProtocol.AACOperatePb2_Json(pbAudioOperate);
                this._dispatchCallback(null, cmd, notiAudioOperate);
            } else {
                var targetid = response.getTargetid();
                if (targetid.indexOf("CONF_") == -1) {
                    targetid = targetid.replace('CONF', 'CONF_');
                }

                var conf = this.swGetConfByConfId(targetid);

                if (conf == null && cmd == jSWProtocol.RequestHeader.confnotify.notifyparticipartoradd.cmd) {
                    var confMgr = this
                    jSW.SWCONF.prototype._swGetConfInfo({
                        parent: this._parent,
                        targetId: targetid,
                        callback: function() {
                            let myTargetId = targetid;
                            var myConf = confMgr.swGetConfByConfId(myTargetId)
                            if (myConf != null) {
                                myConf._onNotify(msgType, cmd, response.getPayload());
                            }
                        }
                    })
                }

                if (conf != null) {
                    conf._onNotify(msgType, cmd, response.getPayload());
                }
            }
        },

        onSendMsgGetResult: function (targeid, msg) {
            var conf = this.swGetConfByConfId(targeid);
            //conf._onSendMsgGetResult(msg, true);
        },

        _onNotifyUserInfo: function (msgType, response) {

            if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_PU_ONOFFLINE) {
                for (var iIndex = 0; iIndex < this._conf_list_names.length; iIndex++) {
                    conf = this._conf_list[this._conf_list_names[iIndex]];

                    if (conf != null) {
                        conf._onNotify(msgType, "", response);
                    }
                }
                return;
            }
            else {

                var payload = response.getPayload();
                var sourceinfo = proto.BVCU.Event_Source.deserializeBinary(payload);
                console.log("notify: user " + sourceinfo.getSzid() +
                        (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_USERLOGIN ?
                        'login' : 'logout'));

                var conf = null;
                for (var iIndex = 0; iIndex < this._conf_list_names.length; iIndex++) {
                    conf = this._conf_list[this._conf_list_names[iIndex]];

                    if (conf != null) {
                        conf._onNotify(msgType, "", sourceinfo);
                    }
                }

            }
        },

        _swFreshConflist: function (conflist) {
            var conf = null;
            for (var i = 0; i < conflist.length; i++) {
                if (this._conf_list[conflist[i].id] == null) {
                    this._conf_list[conflist[i].id] = conf = new jSW.SWCONF(this._parent, conflist[i]);
                    this._conf_list_names.push(conflist[i].id)
                }
                else {
                    conf = this._conf_list[conflist[i].id];
                    conf._freshBaseInfo(conflist[i]);
                }
            }

            if (conflist.length > 0) {
                this._swFocus(0);
            }
        },

        _swFreshConfInfo: function (confInfo) {
            var isContain = false;
            for (var i = 0; i < this._conf_list_names.length; i++) {
                if (confInfo.baseinfo.id == this._conf_list_names[i]) {
                    isContain = true;
                    break;
                }
            }

            if (!isContain) {
                this._conf_list[confInfo.baseinfo.id] = new jSW.SWCONF(this._parent, confInfo.baseinfo);
                this._conf_list_names[this._conf_list_names.length] = confInfo.baseinfo.id;
            }
            this._conf_list[confInfo.baseinfo.id]._swFreshConfInfo(confInfo);
            return this._conf_list[confInfo.baseinfo.id];
        },

        _swFreshUserOnlielist: function (users) {
            this._user_online = [];
            this._user_online_names = [];

            for (var i = 0; i < users.length; i++) {
                this._user_online_names[i] = users[i].userid + users[i].iapplierid;
                this._user_online[this._user_online_names[i]] = users[i];
            }

            var confTemp = null;
            for (var i = 0; i < this._conf_list_names.length; i++) {
                confTemp = this._conf_list[this._conf_list_names[i]];
                if (confTemp != null) {
                    confTemp._swFreshFreeUsers(users, this._parent._arr_pu);
                }
            }
        },

        _swGetConfList: function (options) {

            var param = jSWProtocol.JsonParamCommand(this._parent._p_emms,
               proto.WEBBVCU.MSGType.WEB_BVCU_GET_CONF_LIST,
               jSWProtocol.BVCU_Command('', -1, null));

            var rc = jSWProtocol._internalSend({
                cmd: jSWProtocol.RequestHeader.getconflist.cmd,
                session: this._parent,
                request: param,
                callback: options.callback,
                tag: options.tag
            });

            return rc;
        },

        swInit: function (options) {
            var params = {
                callback: options.callback,
                session: this._parent,
                confMgr: this
            };
            var rc = this._swGetConfList({
                callback: function (sender, response, szConf) {
                    if (szConf.length == 0) {
                        var rc = response.emms.code;
                        sender.tag.callback(response.emms.code);
                    }

                    var conf;
                    var targetid;
                    var session = sender.tag.session;
                    var confMgr = sender.tag.confMgr;
                    var params = null;
                    var desSzConfID = [];
                    var infotemp = null;
                    for (iindex in szConf) {
                        infotemp = {};
                        infotemp.id = szConf[iindex].id;
                        infotemp.bGeted = false;
                        desSzConfID.push(infotemp);
                    }

                    var _innerGetConfInfo = function (params) {
                        if (params.iCurrentIndex >= params.szInfoTemp.length) {
                            if (jSWOptions.CheckNotOcx()){
                                var HandleConfSpeak = jSW.DependencyMgr.GetModule("HandleConfSpeak")
                                HandleConfSpeak.OnIStartConfConnectWsAudio()
                            }
                            params.initcallback(jSW.RcCode.RC_CODE_S_OK);
                            return;
                        }
                        var currentInfo = params.szInfoTemp[params.iCurrentIndex];
                        var conf = params.confMgr.swGetConfByConfId(currentInfo.id);

                        if (conf) {
                            var getparams = {
                                callback: params.onGetConfInfo,
                                parent: params.session,
                                targetId: currentInfo.id,
                                tag: params
                            };
                            var rc = conf._swGetConfInfo(getparams);
                            if (rc != jSW.RcCode.RC_CODE_S_OK) {
                                console.log("conf init, on get conf info error");
                                params.initcallback(jSW.RcCode.RC_CODE_E_NOTFOUND);
                            }
                        } else {
                            params.toNextIndex(true);
                            onGetConfInfo({ tag: params }, { emms: jSW.RcCode.RC_CODE_S_OK });
                        }
                    }

                    var onGetConfInfo = function (sender, response, confInfo) {
                        var tag = sender.tag;
                        var rc = response.emms.code;
                        if (rc != jSW.RcCode.RC_CODE_S_OK) {
                            tag.initcallback(rc);
                        } else {
                            var params = tag;
                            params.toNextIndex(true);
                            tag.getconfinfo(params);
                        }
                    }

                    var params = {
                        session: session,
                        szInfoTemp: desSzConfID,
                        iCurrentIndex: 0,
                        onGetConfInfo: onGetConfInfo,
                        confMgr: confMgr,
                        initcallback: sender.tag.callback,
                        getconfinfo: _innerGetConfInfo,
                        toNextIndex: function (bresult) {
                            this.szInfoTemp[this.iCurrentIndex].bGeted = bresult;
                            this.iCurrentIndex++;
                        }
                    };

                    _innerGetConfInfo(params);

                },
                tag: params
            });

            console.log("conference manager init :" + (rc == 0 ? "success" : "faild"));
            return rc;
        },

        swGetConfList: function () {
            var conflist = [];
            var conf = null;
            for (var iIndex = 0; iIndex < this._conf_list_names.length; iIndex++) {
                conf = this._conf_list[this._conf_list_names[iIndex]];
                conflist.push(conf);
            }
            return conflist;
        },

        swGetConfByConfId: function (id) {
            return this._conf_list[id];
        },

        /**var options = {
            confbaseinfo:{
                name:conf 名字。必须设置为非空
                speakmode:发言类型 jSW.SwConfManager.MODE_SPEAK.*
                joinmode:jSW.SwConfManager.MODE_JOIN.*
                applyformode:jSW.SwConfManager.MODE_APPLY.*
                startmode:jSW.SwConfManager.MODE_START.*
                password: 密码，仅对CONF_MODE_JOIN_PASSWORD有意义
                confstatus: 会议状态,jSW.SwConfManager.MODE_START.*,
                bautorecord: boolean
            },
            callback: function(options, response) {
                options = options;
                response = {
                    emms: emms,
                    request: request
                }
            },
            tag: tag
        };*/
        swCreateConf: function (poptions) {
            var dft_poptions = {
                confbaseinfo: {
                    name: 'conf',
                    speakmode: jSW.SwConfManager.MODE_SPEAK.CHAIRMAN,
                    joinmode: jSW.SwConfManager.MODE_JOIN.INVITE,
                    applyformode: jSW.SwConfManager.MODE_APPLY.AUTOAGREE,
                    startmode: jSW.SwConfManager.MODE_START.FOREVER,
                    password: '',
                    bautorecord: false,
                    itimeout: 0
                },
                callback: null
            };

            poptions = jSWUtils.extend({}, dft_poptions, poptions || {});

            var option = new proto.BVCU.ConfBaseInfo();

            //if (poptions.confbaseinfo.speakmode == jSW.SwConfManager.MODE_SPEAK.DISCUSSIONGROUP) {
            //    console.warn("the conference speak mode DISCUSSIONGROUP No longer support!")
            //    poptions.confbaseinfo.speakmode = jSW.SwConfManager.MODE_SPEAK.CHAIRMAN;
            //}

            var mode = poptions.confbaseinfo.speakmode +
                poptions.confbaseinfo.joinmode +
                poptions.confbaseinfo.applyformode +
                poptions.confbaseinfo.startmode + (poptions.confbaseinfo.bautorecord ? 1 << 9 : 0);
            option.setImode(mode);

            if (poptions.confbaseinfo.name.length == 0) {
                console.warn("create conference name is empty, server will named automatically");
                return jSW.RcCode.RC_CODE_E_BVCU_INVALIDARG;
            }

            option.setSzname(poptions.confbaseinfo.name);
            //option.setIconfstatus(poptions.confbaseinfo.confstatus);

            if (poptions.confbaseinfo.joinmode == jSW.SwConfManager.MODE_JOIN.PASSWORD) {
                option.setSzpassword(jSWUtils.string2Uint8Array(poptions.confbaseinfo.password));
            }
            option.setItimeout(poptions.confbaseinfo.itimeout);

            var param = jSWProtocol.JsonParamCommand(this._parent._p_emms,
               proto.WEBBVCU.MSGType.WEB_BVCU_SET_CONF_CREATE,
               jSWProtocol.BVCU_Command('', -1, option));

            var rc = jSWProtocol._internalSend({
                cmd: jSWProtocol.RequestHeader.createconf.cmd,
                session: this._parent,
                request: param,
                callback: poptions.callback,
                tag: poptions.tag
            });

            return rc;
        },

        swDeleteConf: function (poptions) {

            var dft_poptions = {
                confid: "",
                callback: null
            };

            poptions = jSWUtils.extend({}, dft_poptions, poptions || {});


            var param = jSWProtocol.JsonParamCommand(this._parent._p_emms,
               proto.WEBBVCU.MSGType.WEB_BVCU_SET_CONF_DELETE,
               jSWProtocol.BVCU_Command(poptions.confid, -1, null));

            var rc = jSWProtocol._internalSend({
                cmd: jSWProtocol.RequestHeader.deleteconf.cmd,
                session: this._parent,
                request: param,
                callback: poptions.callback,
                tag: poptions.tag
            });

            return rc;
        },

        //订阅会议状态改变事件
        swRegConfWatch: function (callback) {
            this._watchCallBack = callback;
            jSWUtils.debugLogInfo("Reg Conf Watch Ok!");
            return jSW.RcCode.RC_CODE_S_OK;
        },

        _dispatchCallback: function (sender, event, data) {
            if (this._watchCallBack) {
                this._watchCallBack(this, event, {
                    conf: sender,
                    data: data
                });
            }
            else {

            }
        },

        _onConfDeletedOk: function (targetid) {
            var isContain = false;
            var iIndex = 0;
            for (var i = 0; i < this._conf_list_names.length; i++) {
                if (this._conf_list_names[i] == targetid) {
                    iIndex = i;
                    isContain = true;
                    break;
                }
            }

            if (isContain) {
                this._conf_list_names.splice(iIndex, 1);
                this._conf_list.splice(targetid, 1);
            }
        },

        _onParticularAddResult: function (particularAddResult) {
            var targetid = particularAddResult.targetid;
            var conf = this.swGetConfByConfId(targetid);

            conf._swGetConfInfo({
                parent: this._parent,
                targetId: targetid
            })
        },

        _onConfOpertaion: function (data) {
            var conf = this.swGetConfByConfId(data.targetid);

            if (conf == null) {
                console.warn("con't find conf");
            }

            if (data.bIsStarted && jSWOptions.CheckNotOcx()){
                var HandleConfSpeak = jSW.DependencyMgr.GetModule("HandleConfSpeak")
                HandleConfSpeak.OnIStartConfConnectWsAudio()
            }

            conf._onConStatusChange(data.bIsStarted);
        },

        _onApplyForSpeak: function (data) {
            var conf = this.swGetConfByConfId(data.targetid);

            if (conf == null) {
                console.warn("con't find conf");
            }

            conf._onCurrentUserSpeakChange(data.bIsSpeak);
            var HandleConfSpeak = jSW.DependencyMgr.GetModule("HandleConfSpeak");
            HandleConfSpeak.OnCurrentUserApplySpeakChange(data.bIsSpeak);
        },

        _onConfLeaveOrReturn: function(data){
            var conf = this.swGetConfByConfId(data.targetid);
            conf._onCurrentUserSeatStatusChange(data.bIsSpeak)
        },

        _deleteConfByConfId: function (confid) {
            var conf = this.swGetConfByConfId(confid);
            if (conf == null) {
            }
            else {
                for (var iIndex = 0; iIndex < this._conf_list_names.length; iIndex++) {
                    if (this._conf_list_names[iIndex] == confid) {
                        this._conf_list_names.splice(iIndex, 1);
                        this._conf_list.splice(confid, 1);
                        this._conf_list[confid] = null;
                        console.log('conf:' + confid + 'delete success');
                        return;
                    }
                }
            }
        }

    };

    jSW.SWCONF_USER = function (parent, options) {
        this._parent = parent;
        this._user_base_info = options;
    };

    jSW.SWCONF_USER.prototype = {
        _parent: null,
        _user_base_info: null,
        swGetUserInfo: function () {
            return this._user_base_info;
        }
    };

    jSW.SWCONF = function (parent, options) {
        this._parent = parent;
        this._conf_base_info = options;
        this._msg_storage = [];
    };


    jSW.SWCONF.prototype = {
        _parent: null,
        _conf_base_info: null,
        _conf_particulars: [],
        _conf_creater: null,
        _free_users: [],
        _is_speak: false,
        _speak_user: null,
        _conf_current_index: -1,
        id: null,
        __is_freshed: false,
        _getConfInfoCbk: function (confInfo) {
            ;
        },

        _swFreshFreeUsers: function (users, pus) {
            this._free_users = [];
            var confparticular = null;
            var userTemp = null;
            var pusTemp = null;
            var isContain = false;

            for (var iUsersIndex = 0; iUsersIndex < users.length; iUsersIndex++) {
                isContain = false;
                userTemp = users[iUsersIndex];
                for (var iIndex = 0; iIndex < this._conf_particulars.length; iIndex++) {
                    confparticular = this._conf_particulars[iIndex];
                    if (!jSWProtocol.JudgeIdType(confparticular.id).bCu) {
                        continue;
                    }

                    if (confparticular.id == userTemp.devid &&
                        userTemp.iapplierid == confparticular.applierid) {
                        isContain = true;
                        break;
                    }
                }

                if (!isContain) {
                    this._free_users[this._free_users.length] = {
                        id: userTemp.devid,
                        name: userTemp.userid,
                        applierid: userTemp.iapplierid
                    };
                }
            }


            for (var iPusIndex = 0; iPusIndex < pus.length; iPusIndex++) {
                isContain = false;
                pusTemp = pus[iPusIndex];
                if (pusTemp == null || pusTemp._info_pu == null || pusTemp._info_pu.onlinestatus == 0) {
                    continue;
                }
                for (var iIndex = 0; iIndex < this._conf_particulars.length; iIndex++) {
                    confparticular = this._conf_particulars[iIndex];

                    if (!jSWProtocol.JudgeIdType(confparticular.id).bPu) {
                        continue;
                    }

                    if (confparticular.id == pusTemp._id_pu) {
                        isContain = true;
                        break;
                    }
                }

                if (!isContain) {
                    this._free_users[this._free_users.length] = {
                        id: pusTemp._info_pu.id,
                        name: pusTemp._info_pu.name
                    };
                }
            }
        },

        _swFreshConfInfo: function (confInfo) {
            this._conf_base_info = confInfo.baseinfo;
            this._conf_particulars = confInfo.particulars;
            this._conf_creater = this._conf_particulars[confInfo.icreater];
            this._conf_current_index = confInfo.icurrentuserindex;
            this.__is_freshed = true;
        },

        _getCurrentUser: function (options) {
            if (!this.__is_freshed) {
                console.log('please do init before call me');
                return null;
            }

            var currentUser = null;

            if (this._conf_current_index != -1) {
                currentUser = this._conf_particulars[this._conf_current_index];
            }

            return currentUser;
        },

        _particularOperation: function (options, cmd, msgType) {
            var dft_poptions = {
                users: [],
                callback: null
            };

            var particularAdd = new proto.WEBBVCU.Conf_Participator_ADD();

            var userlisttemp = null;
            var user = null;
            for (var iIndex = 0; iIndex < options.users.length; iIndex++) {
                var name = options.users[iIndex].id;
                user = options.users[iIndex];
                if (jSWProtocol.JudgeIdType(name).bCu) {
                    if (user != null) {
                        userlisttemp = particularAdd.addSzparticcipator();
                        userlisttemp.setSzid(name);
                        userlisttemp.setSzaddr(user.addr);
                        userlisttemp.setSzusername(user.name);
                        userlisttemp.setIapplierid(user.applierid);
                        userlisttemp.setIpid(user.pid);
                        userlisttemp.setSzaliasname(user.aliasname)
                    }
                } else if (jSWProtocol.JudgeIdType(name).bPu) {
                    userlisttemp = particularAdd.addSzparticcipator();
                    userlisttemp.setSzid(name);
                    userlisttemp.setIpid(user.pid);
                    userlisttemp.setSzusername(user.name);
                    userlisttemp.setSzaliasname(user.aliasname)
                } else if (jSWProtocol.JudgeIdType(name).bUa) {
                    userlisttemp = particularAdd.addSzparticcipator();
                    userlisttemp.setSzid(name);
                    userlisttemp.setIapplierid(user.applierid);
                    userlisttemp.setIpid(user.pid);
                    userlisttemp.setSzaddr(user.addr);
                    userlisttemp.setSzusername(user.name);
                    userlisttemp.setSzaliasname(user.aliasname)
                }
                else {
                    userlisttemp = particularAdd.addSzparticcipator();
                    userlisttemp.setSzid(name);
                    userlisttemp.setSzusername(user.name);
                    console.log("the participater to add is offline user");
                }

                if (this._conf_base_info.iMode == jSW.SwConfManager.MODE_SPEAK.DISCUSSIONGROUP) {
                    userlisttemp.setIallowedmediadir(jSW.MEDIADIR.AUDIOSEND + jSW.MEDIADIR.AUDIORECV);
                } else if (this._conf_base_info.iMode == jSW.SwConfManager.MODE_SPEAK.CHAIRMAN) {
                    userlisttemp.setIallowedmediadir(jSW.MEDIADIR.AUDIORECV);
                }
            }

            if (particularAdd.getSzparticcipatorList().length == 0) {
                console.log("添加用户列表为空");
                return;
            }

            var param = jSWProtocol.JsonParamCommand(this._parent._p_emms,
               msgType,
               jSWProtocol.BVCU_Command(this._conf_base_info.id, -1, particularAdd));

            var rc = jSWProtocol._internalSend({
                cmd: cmd,
                session: this._parent,
                request: param,
                callback: options.callback,
                tag: options.tag
            });

            return rc;
        },

        _confOpertion: function (options, cmd, msgType, data) {

            var param = jSWProtocol.JsonParamCommand(this._parent._p_emms,
                msgType,
                jSWProtocol.BVCU_Command(this._conf_base_info.id, -1, data));

            var rc = jSWProtocol._internalSend({
                cmd: cmd,
                session: this._parent,
                request: param,
                callback: options.callback,
                tag: options.tag
            });

            return rc;
        },

        _onConStatusChange: function (bIsStarted) {
            this._conf_base_info.bIsStarted = bIsStarted;
        },

        _onCurrentUserSpeakChange: function (bIsSpeak) {
            var user = this.swGetCurrentUser();
            user.isSpeak = bIsSpeak;
        },

        _onCurrentUserSeatStatusChange: function (bIsSeat) {
            var user = this.swGetCurrentUser();
            user.isinseat = bIsSeat
            user.isleave = !bIsSeat

            if(bIsSeat && jSWOptions.CheckNotOcx()){
                var HandleConfSpeak = jSW.DependencyMgr.GetModule("HandleConfSpeak")
                HandleConfSpeak.OnIStartConfConnectWsAudio()
            }
        },

        _freshBaseInfo: function (confbaseinfo) {
            this._conf_base_info = confbaseinfo;
        },

        /*
            options: {
                szTextMsg: String,
                callback: function(options, response, data){
    
                },
                tag: Object
            }
        */
        _dhcbConIMSend: function (hander, payload) {
        },

        _onSendMsgGetResult: function (szmsg, bsend) {
            var isdrop = szmsg[0].iType == jSWProtocol.IMMSGTypes.PICTURE && bsend;
            if (!isdrop) {
                var msg = {
                    confid: this._conf_base_info.id,
                    sender: this._getCurrentUser(),
                    szmsgs: szmsg,
                    bismine: true
                }
                this._onGetImMsg(msg);
            }
        },
        /*
         * 即时通讯
         * options = {
         *  msgitems: [
         *      {
         *          iType: jSWProtocol.IMMSGTypes,
         *          data: ,
         *          nruid: ,
         *          targetid: 
         *      }
         *  ],
         *  callback: function(options, response){
         *  },
         *  pcallback: function(options, response, data){
         *      
         *  }
         *  tag: object
         * }
         */
        swConfIMSend: function (options) {
            if (typeof options.msgitems == undefined || options.msgitems.length == 0) {
                jSWUtils.debugLogInfo("send im with INVALIDARG");
                return jSW.RcCode.RC_CODE_E_INVALIDARG;
            }
            var rc = jSW.RcCode.RC_CODE_S_OK;
            var immodule = this._parent._immodule;
            options._targetid = this._conf_base_info.id;

            if (options.msgitems[0].iType == jSWProtocol.IMMSGTypes.PICTURE ||
                options.msgitems[0].iType == jSWProtocol.IMMSGTypes.AUDIO ||
                options.msgitems[0].iType == jSWProtocol.IMMSGTypes.FILE) {
                if (typeof options.pcallback == undefined) {
                    options.pcallback = this.dft_pcallback;
                }
            }

            if (options.msgitems[0].iType == jSWProtocol.IMMSGTypes.AUDIO) {
                options.msgitems[0].iType = jSWProtocol.IMMSGTypes.FILE; 
                //上传没有单独的接口，和消息类型完全耦合了，这样是为了复用，之后上传下载接口独立出来
                rc = immodule._swConfIMSend(options, proto.WEBBVCU.MSGType.WEB_BVCU_CONF_IM_MSG, this._dhcbConIMSend, jSWProtocol.IMMSGTypes.AUDIO);
            } else {
                rc = immodule._swConfIMSend(options, proto.WEBBVCU.MSGType.WEB_BVCU_CONF_IM_MSG, this._dhcbConIMSend);
            }
            return rc;
        },

        dft_pcallback: function () {

        },

        /*
         * 即时通讯
         * options = {
         *  msgitems: [
         *      {
         *          iType: jSWProtocol.IMMSGTypes,
         *          data: 
         *          nruid: ""
         *      }
         *  ],
         *  callback: function(options, response){
         *  },
         *  pcallback: function(options, response, data){
         *      
         *  }
         *  tag: object,
         *  targetid: 
         * }
         */
        SwConfFileRecv: function (options) {
            var immodule = this._parent._immodule;
            var rc = immodule._SwConfFileRecv(options);
            return rc;
        },

        /**
           var options = {
                callback:function(emms, users){
                }
           }
        **/
        swGetOnlineUsers: function (options) {
            var rc = this._parent._confManager._getOnlineUsers({
                callback: function (sender, response, json) {
                    var tag = sender.tag;
                    console.log("可翻牌的用户或设备:" + jSWUtils.getJsonString(tag.tag._free_users));
                    var eventOptions = jSWProtocol.BuildEventOptions(null, null, null, tag.optionstag);
                    tag.callback(eventOptions, response, tag.tag._free_users);
                },
                tag: {
                    callback: options.callback,
                    tag: this,
                    optionstag: options.tag
                }
            });
            return rc;
        },

        swGetOnlineUsersClassified: function(options) {
            if (typeof options == 'undefined' || typeof options.callback != 'function') {
                return jSW.RcCode.RC_CODE_E_INVALIDARG
            }

            var rc = this.swGetOnlineUsers({
                callback: function(opts, response, data) {
                    var UserOptions = opts.tag
                    var result = null

                    if (response.emms.code == jSW.RcCode.RC_CODE_S_OK) {
                        result = {
                            userlist: null,
                            pulist: null
                        }

                        result.userlist = data.filter(function(item){ return item.id.indexOf("UA_") == 0;})

                        result.pulist = data.filter(function(item) { 
                            return item.id.indexOf("PU_") == 0 && (false == result.userlist.some(function(uaItem){
                                var isHas = false

                                try{
                                    isHas = item.id.split("_")[1] == uaItem.id.split("_")[1]
                                }catch(e){
                                    console.error(e)
                                }

                                return isHas
                            }))
                        })

                        result.userlist = data.filter(function(item){ return item.id.indexOf("UA_") == 0 || item.id.indexOf("CU_") == 0;})
                    }

                    var eventOptions = jSWProtocol.BuildEventOptions(null, null, null, UserOptions.optionstag);
                    UserOptions.callback(eventOptions, response, result);
                },
                tag: options
            })

            return rc;
        },

        swSetConfInfo: function(options){
            if (jSWOptions.CheckOcx()) {
                console.error("ocx mode not support")
                return jSW.RcCode.RC_CODE_E_UNSUPPORTED;
            }

            if (typeof options == 'undefined' || typeof options.callback == 'undefined' || typeof options.info == 'undefined') {
                return jSW.RcCode.RC_CODE_E_INVALIDARG;
            }

            var payload = new proto.BVCU.ConfBaseInfo();

            payload.setImode(this._conf_base_info._imode);

            payload.setSzid(this._conf_base_info.id);
            
            
            var iMode = 0
            var desInfo = options.info
            payload.setSzname(desInfo.name);
            
            iMode = (desInfo.bIsChairman ? 1 : 0)

            if(desInfo.bIsInvite){
                iMode += (0)
            }else if(desInfo.bIsPassword){
                iMode += (1 << 4)

                if(typeof desInfo.password == 'undefined' || desInfo.password.length == 0){
                    return jSW.RcCode.RC_CODE_E_INVALIDARG
                }

                payload.setSzpassword(jSWUtils.string2Uint8Array(desInfo.password))

            }else if(desInfo.bIsFree){
                iMode += (2 << 4)
            }else{
                return jSW.RcCode.RC_CODE_E_INVALIDARG;
            }

            if (desInfo.bAutoApply) {
                iMode += (1 << 7)
            }

            if (desInfo.bAutoStart){
                iMode += (1 << 8)
            }

            payload.setImode(iMode)
            payload.setItimeout(this._conf_base_info.iTimeout)

            var rc = jSWProtocol.SendRequest({
                session: this._parent,
                msgtype: proto.WEBBVCU.MSGType.WEB_BVCU_SET_CONF_INFO,
                target: this._conf_base_info.id,
                targetIndex: -1,
                payload: payload,
                cmd: "WEB_BVCU_SET_CONF_INFO",
                callback: options.callback,
                tag: options.tag
            });
            return rc;
        },

        swGetCurrentUser: function () {

            if (!this.__is_freshed) {
                console.warn('please get conf details info first');
                return null;
            }

            if (this._conf_current_index < 0) {
                return null;
            } else {
                return this._conf_particulars[this._conf_current_index];
            }
        },

        _swGetConfInfo: function (options) {
            if (options == null) {
                options = {};
            }

            var parent = options.parent == null ? this._parent : options.parent;
            var targetId = options.targetId == null ? this._conf_base_info.id : options.targetId;
            options.callback = options.callback == null ? function () { } : options.callback;

            var rc = jSWProtocol.SendRequest({
                session: parent,
                msgtype: proto.WEBBVCU.MSGType.WEB_BVCU_GET_CONF_INFO,
                target: targetId,
                targetIndex: -1,
                cmd: jSWProtocol.RequestHeader.getconfinfo.cmd,
                callback: options.callback,
                tag: options.tag
            });

            return rc;
        },

        /*
        * options: {
        *       bRecord: boolean,
        *       szNruId: string,
        *       callback: function(){},
        *       tag: object
        * }
        * */
        swSetConfAudioRecord: function (options) {
            if (this._conf_base_info.bAudioRecord == options.bRecord &&
                this._conf_base_info.szRecordNRUID == options.szNruId) {
                setTimeout(function (options) {
                    options.callback({ tag: options.tag }, {
                        emms: {
                            code: jSW.RcCode.RC_CODE_S_OK
                        }
                    });
                }, 50, options);
                return jSW.RcCode.RC_CODE_S_OK;
            }
            var payload = new proto.BVCU.ConfBaseInfo();
            payload.setSzid(this._conf_base_info.id);
            payload.setImode(this._conf_base_info._imode);
            payload.setSzname(this._conf_base_info.name);

            if (options.bRecord) {
                payload.setSzrecordnruid(options.szNruId);
            } else {
                payload.setSzrecordnruid("");
            }

            var szTargetId = this._conf_base_info.id;
            var rc = jSWProtocol.SendRequest({
                session: this._parent,
                msgtype: proto.WEBBVCU.MSGType.WEB_BVCU_CONF_ALTER_AUDIO_RECORD_STATUS,
                target: szTargetId,
                targetIndex: -1,
                payload: payload,
                cmd: jSWProtocol.RequestHeader.confaudiorecord.cmd,
                callback: options.callback,
                tag: options.tag
            });
            return rc;
        },

        _swGetParticipatorByNecessaryInfo: function (user) {
            var userResult = null;
            for (var iIndex = 0; iIndex < this._conf_particulars.length; iIndex++) {
                userResult = this._conf_particulars[iIndex];
                if (userResult.id == user.id &&
                    userResult.applierid == user.applierid &&
                    userResult.pid == user.pid) {
                    return userResult;
                }
            }

            return null;
        },

        swGetConfInfo: function () {
            return this._conf_base_info;
        },

        //var dft_poptions = {
        //    uses: [], 
        //    callback: null，
        //    tag:tag
        //};
        swParticipatorRemove: function (options) {
            var user;
            var iIndex;
            for (iIndex = 0; iIndex < options.users.length; iIndex++) {
                user = this._swGetParticipatorByNecessaryInfo(options.users[iIndex]);
                if (user == null) {
                    console.warn(user.id + "not found");
                    continue;
                }
                if(user._appendData.length > 0){
                    options.users[iIndex].id = user._appendData;
                }
                
                if (jSWProtocol.JudgeIdType(user.id).bCu) {
                    if (user.id == this._conf_creater.id &&
                        user.iapplierid == this._conf_creater.applierid) {
                        options.users.splice(iIndex, 1);
                        console.warn("id:" + user.id + ",name:" + user.name + "是创建者 移除该用户操作取消");
                    }
                } else if (jSWProtocol.JudgeIdType(user.id).bPu) {
                    if (user.id == this._conf_creater.id) {
                        options.users.splice(iIndex, 1);
                        console.warn("id:" + user.id + ",name:" + user.name + "是创建者 移除该用户操作取消");
                    }
                } else if (jSWProtocol.JudgeIdType(user.id).bUa) {
                    if (user.id == this._conf_creater.id) {
                        options.users.splice(iIndex, 1);
                        console.warn("id:" + user.id + ",name:" + user.name + "是创建者 移除该用户操作取消");
                    }
                }
            }

            if (options.users.length <= 0) {
                console.warn("删除会议成员为空");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            var rc = this._particularOperation(options,
                jSWProtocol.RequestHeader.particularremove.cmd,
                proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_REMOVE);

            return rc;
        },

        //var dft_poptions = {
        //    uses: [], //this.swFreshFreeUsers回复的集合子集
        //    callback: null
        //    tag:tag
        //};
        swParticipatorAdd: function (options) {
            return this._particularOperation(options,
                jSWProtocol.RequestHeader.particularadd.cmd,
                proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_ADD);
        },

        /*
            options = {
                callback: function(Options, Response, jSW.SWCONF_USER[]){},
                tag: Object
            }
        */
        swGetParticularList: function (options) {
            if (!this.__is_freshed) {
                console.warn('please get conf info first');
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            var eventOptions = jSWProtocol.BuildEventOptions(null, null, this._parent._parent, options.tag);
            var response = jSWProtocol.BuildResponse(jSW.RcCode.RC_CODE_S_OK, null);
            setTimeout(options.callback, 50, eventOptions, response, this._conf_particulars);
            return jSW.RcCode.RC_CODE_S_OK;
        },

        swConfStart: function (options) {
            if (this._conf_base_info.bIsStarted) {
                console.warn("conf is started");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }
            return this._confOpertion(options,
                jSWProtocol.RequestHeader.confstart.cmd,
                proto.WEBBVCU.MSGType.WEB_BVCU_CONF_START);
        },

        swConfStop: function (options) {
            if (!this._conf_base_info.bIsStarted) {
                console.warn("conf is not started");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }
            return this._confOpertion(options,
                jSWProtocol.RequestHeader.confend.cmd,
                proto.WEBBVCU.MSGType.WEB_BVCU_CONF_STOP);
        },

        swApplyForSpeak: function (options) {
            if (!this._conf_base_info.bIsChairman) {
                console.warn("current conf mode not support apply for speak");
                return jSW.RcCode.RC_CODE_E_FAIL;
            } else if (this._is_speak) {
                console.warn("you are speaking, so you need not apply for one again");
                return jSW.RcCode.RC_CODE_E_FAIL;
            } if (!this._conf_base_info.bIsStarted) {
                console.warn("the conference you apply for speak is not started !");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            return this._confOpertion(options,
                jSWProtocol.RequestHeader.applyforspeak.cmd,
                proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_APPLYFOR_STARTSPEAK);
        },

        swApplyForEndSpeak: function (options) {
            if (!this._conf_base_info.bIsChairman) {
                console.warn("current conf mode not support apply for speak");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            var user = this._getCurrentUser();

            if (user == null || !user.isSpeak) {
                console.warn("no speak shoud be end that you are not speaking");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            return this._confOpertion(options,
                jSWProtocol.RequestHeader.applyforendspeak.cmd,
                proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_APPLYFOR_ENDSPEAK);
        },

        //var dft_poptions = {
        //    user: [],this._conf_particulars的元素
        //    callback: null
        //    tag:tag
        //};
        swInviteSpeak: function (options) {
            if (!this._conf_base_info.bIsChairman) {
                console.warn("current conf mode not support invite speak");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            if (options.user == null) {
                console.warn("invite args error");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            var user = null;
            var name = options.user.id;
            user = this._swGetParticipatorByNecessaryInfo(options.user);

            if (user == null) {
                console.warn("Participator you invited not exist!");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            if (!user.isonline) {
                console.warn("Participator you invited not in conference!");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            if (user.isSpeak) {
                console.warn("Participator you invited is speaking!");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            var particular = new proto.BVCU.ConfParticipatorInfo();

            var endPointType = jSWProtocol.JudgeIdType(name)

            if (endPointType.bCu || endPointType.bUa) {
                if (user != null) {
                    particular.setSzid(name);
                    particular.setSzusername(user.name);
                    particular.setIapplierid(user.applierid);
                    particular.setIpid(user.pid);
                }
            } else if (endPointType.bPu) {
                particular.setSzid(name);
                particular.setIapplierid(user.applierid);
                particular.setIpid(user.pid);
            }
            else {
                console.error("语音会议添加用户 用户信息有问题");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            //particular.setIstatus(-1);

            var param = jSWProtocol.JsonParamCommand(this._parent._p_emms,
               proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_INVITE_SPEAK,
               jSWProtocol.BVCU_Command(this._conf_base_info.id, -1, particular));

            var rc = jSWProtocol._internalSend({
                cmd: jSWProtocol.RequestHeader.invitespeak.cmd,
                session: this._parent,
                request: param,
                callback: options.callback,
                tag: options.tag
            });

            return rc;
        },

        //var dft_poptions = {
        //    user: [],this._conf_particulars的元素
        //    callback: null
        //    tag:tag
        //};
        swTerminalSpeak: function (options) {
            if (!this._conf_base_info.bIsChairman) {
                console.warn("current conf mode not support invite speak");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            if (options.user == null) {
                console.warn("terminate args error");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            var user = null;
            var name = options.user.id;

            user = this._swGetParticipatorByNecessaryInfo(options.user);

            if (user == null) {
                console.warn("Participator you invited not exist!");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            if (!user.isonline || !user.isinseat) {
                console.warn("Participator you invited not in conference!");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            if (!user.isSpeak) {
                console.warn("Participator you invited is not speaking!");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            var particular = new proto.BVCU.ConfParticipatorInfo();
            var endPointType = jSWProtocol.JudgeIdType(name);

            if (endPointType.bCu || endPointType.bUa) {
                if (user != null) {
                    particular.setSzid(name);
                    particular.setSzusername(user.name);
                    particular.setIapplierid(user.applierid);
                    particular.setIpid(user.pid);
                }
            } else if (endPointType.bPu) {
                particular.setSzid(name);
                particular.setIapplierid(user.applierid);
                particular.setIpid(user.pid);
            }
            else {
                console.error("语音会议添加用户 用户信息有问题");
                return jSW.RcCode.RC_CODE_E_FAIL;
            }

            //particular.setIstatus(-1);


            var param = jSWProtocol.JsonParamCommand(this._parent._p_emms,
               proto.WEBBVCU.MSGType.WEB_BVCU_PARTICIPATOR_TERMINATE_SPEAK,
               jSWProtocol.BVCU_Command(this._conf_base_info.id, -1, particular));

            var rc = jSWProtocol._internalSend({
                cmd: jSWProtocol.RequestHeader.terminalspeak.cmd,
                session: this._parent,
                request: param,
                callback: options.callback,
                tag: options.tag
            });

            return rc;
        },

        swParticipatorLeave: function (options) {

            if (this._conf_base_info.bIsStarted) {

                var currentUser = this.swGetCurrentUser();

                if (currentUser == null) {
                    return jSW.RcCode.RC_CODE_E_FAIL;
                }

                if (!currentUser.isinseat) {
                    console.warn('currentUser never inseat :' + this._conf_base_info.id + ', need not do leave');
                    return jSW.RcCode.RC_CODE_S_OK;
                }

                return this._confOpertion(options,
                    jSWProtocol.RequestHeader.confleave.cmd,
                    proto.WEBBVCU.MSGType.WEB_BVCU_CONF_LEAVE);
                console.log('user leave' + this._conf_base_info.id + 'conf');
            }
            else {
                console.log('conf:' + this._conf_base_info.id + ' not started , so can not do return');
                return jSW.RcCode.RC_CODE_E_FAIL;
            }
        },

        swParticipatorReturn: function (options) {
            if (this._conf_base_info.bIsStarted) {

                var currentUser = this.swGetCurrentUser();

                if (currentUser == null) {
                    return jSW.RcCode.RC_CODE_E_FAIL;
                }

                if (!currentUser.isleave) {
                    console.warn('currentUser never leave :' + this._conf_base_info.id + ', need not do return');
                    return jSW.RcCode.RC_CODE_E_FAIL;
                }

                var data = new proto.BVCU.ConfParticipatorInfo();

                data.setIapplierid(currentUser.applierid);
                data.setIpid(currentUser.pid);
                data.setSzid(currentUser.id);
                data.setSzusername(currentUser.name);
                data.setSzaddr(currentUser.addr);
                data.setIallowedmediadir(currentUser.allowedmediadir);

                return this._confOpertion(options,
                    jSWProtocol.RequestHeader.confreturn.cmd,
                    proto.WEBBVCU.MSGType.WEB_BVCU_CONF_RETURN, data);
                console.log('user return' + this._conf_base_info.id + 'conf');
            }
            else {
                console.log('conf:' + this._conf_base_info.id + ' not started , so can not do return');
                return jSW.RcCode.RC_CODE_E_FAIL;
            }
        },

        swParticipatorInviteJoin: function(options){
            if (this._conf_base_info.bIsStarted) {

                var currentUser = options.user;

                if (currentUser == null) {
                    return jSW.RcCode.RC_CODE_E_FAIL;
                }

                if (!currentUser.isleave) {
                    console.warn('currentUser never leave :' + this._conf_base_info.id + ', need not do return');
                    return jSW.RcCode.RC_CODE_E_FAIL;
                }

                var data = new proto.BVCU.ConfParticipatorInvite();

                data.setIpid(currentUser.pid);
                data.setSzid(currentUser.id);
                data.setSzusername(currentUser.name);

                return this._confOpertion(options, "PARTICIPATOR_INVITE_JOIN",
                    proto.WEBBVCU.MSGType.WEB_BVCU_SUBMETHOD_CONF_PARTICIPATOR_INVITE_JOIN, data);
                console.log('user return' + this._conf_base_info.id + 'conf');
            }
            else {
                console.log('conf:' + this._conf_base_info.id + ' not started , so can not do return');
                return jSW.RcCode.RC_CODE_E_FAIL;
            }
        },

        swParticipatorKickout: function(options){
            if (this._conf_base_info.bIsStarted) {

                var currentUser = options.user;

                if (currentUser == null) {
                    return jSW.RcCode.RC_CODE_E_FAIL;
                }

                if (currentUser.isleave) {
                    console.warn('currentUser never inseat :' + this._conf_base_info.id + ', need not do leave');
                    return jSW.RcCode.RC_CODE_E_FAIL;
                }

                var data = new proto.BVCU.ConfParticipatorInvite();

                data.setIpid(currentUser.pid);
                data.setSzid(currentUser.id);
                data.setSzusername(currentUser.name);

                return this._confOpertion(options, "PARTICIPATOR_KICKOUT",
                    proto.WEBBVCU.MSGType.WEB_BVCU_SUBMETHOD_CONF_PARTICIPATOR_KICKOUT, data);
                console.log('user return' + this._conf_base_info.id + 'conf');
            }
            else {
                console.log('conf:' + this._conf_base_info.id + ' not started , so can not do return');
                return jSW.RcCode.RC_CODE_E_FAIL;
            }
        },

        _addParticipators: function (participators) {
            var confPartList = this._conf_particulars
            
            for (var iIndex = 0; iIndex < participators.length; iIndex++) {
                if(jSWProtocol.JudgeIdType(participators[iIndex].id).bUa ||
                    false == this._conf_particulars.some(function(item, index) {
                    
                    if (item.applierid == participators[iIndex].applierid) {
                        confPartList[index] = participators[iIndex]
                    }

                    return item.applierid == participators[iIndex].applierid
                }))
                {
                    this._conf_particulars[this._conf_particulars.length] = participators[iIndex]
                }
            }
        },

        _removeParticipator: function (participators) {
            var participatorTemp;
            console.log("当前会议xxxxxxxxxxxxxxxxxxxxx", this, "当前用户", this._getCurrentUser())
            for (var ii = 0; ii < participators.length; ii++) {
                participator = participators[ii];



                for (var iIndex = 0; iIndex < this._conf_particulars.length; iIndex++) {
                    participatorTemp = this._conf_particulars[iIndex];
                    if (jSWProtocol.JudgeIdType(participator.id).bPu) {
                        if (participator.id == participatorTemp.id) {
                            this._conf_particulars.splice(iIndex, 1);
                            console.log('delete conf user success');
                            return;
                        }
                    }
                    else if (jSWProtocol.JudgeIdType(participator.id).bCu) {

                        if(participator.id == this._getCurrentUser().id &&
                            participator.applierid == this._getCurrentUser().applierid) {
                                this._parent._confManager._deleteConfByConfId(this._conf_base_info.id);
                                return
                            }

                        if (participator.id == participatorTemp.id &&
                            participator.applierid == participatorTemp.applierid &&
                            participator.name == participatorTemp.name) {

                            this._conf_particulars.splice(iIndex, 1);
                            console.log('delete conf user success');
                            return;
                        }
                    } else if (jSWProtocol.JudgeIdType(participator.id).bUa) {
                        if (participator.id == participatorTemp.id &&
                            participator.name == participatorTemp.name) {
                            this._conf_particulars.splice(iIndex, 1);
                            console.log('delete conf user success');
                            return;
                        }
                    }
                }
            }
            console.log('delete conf user faild');
        },

        _updateParticipator: function (participator) {
            if (!this.__is_freshed) {
                this._swGetConfInfo({
                    callback: function () {
                        console.log('update conf user success');
                    }
                });
                return;
            }

            var participatorTemp;
            for (var iIndex = 0; iIndex < this._conf_particulars.length; iIndex++) {
                participatorTemp = this._conf_particulars[iIndex];
                if (jSWProtocol.JudgeIdType(participator.id).bPu) {
                    if (participator.id == participatorTemp.id) {
                        if (participator.volume > 128) {
                            participator.volume = this._conf_particulars[iIndex].volume;
                        }
                        this._conf_particulars[iIndex] = participator;
                        console.log('update conf user success');
                        return;
                    }
                }
                else if (jSWProtocol.JudgeIdType(participator.id).bCu) {
                    if (participator.id == participatorTemp.id &&
                        participator.applierid == participatorTemp.applierid &&
                        participator.name == participatorTemp.name) {

                        if (participator.volume > 128) {
                            participator.volume = this._conf_particulars[iIndex].volume;
                        }

                        let lastSpeak = this._conf_particulars[iIndex].isSpeak

                        this._conf_particulars[iIndex] = participator;
                        console.log('update conf user success');

                        if (this._conf_base_info.bIsDiscussiongroup) {
                            var currentUser = this.swGetCurrentUser();
                            if (currentUser.id == participator.id && participator.pid == currentUser.pid && lastSpeak != currentUser.isSpeak ) {
                                this._parent.swGetConfManager()._onApplyForSpeak({
                                    bIsSpeak:currentUser.isSpeak,
                                    targetid: this._conf_base_info.id
                                })
                            }
                        }

                        return;
                    }
                }
                else if (jSWProtocol.JudgeIdType(participator.id).bUa) {
                    if (participator.id == participatorTemp.id &&
                        participator.name == participatorTemp.name) {

                        if (participator.volume > 128) {
                            participator.volume = this._conf_particulars[iIndex].volume;
                        }

                        this._conf_particulars[iIndex] = participator;
                        console.log('update conf user success');
                        return;
                    }
                }
            }

            this._conf_particulars.push(participator);
        },

        _onUserLoginout: function (cuid, iApplierID, isLogin) {
            var particular = null;
            for (var iIndex = 0; iIndex < this._conf_particulars.length; iIndex++) {
                particular = this._conf_particulars[iIndex];
                if (particular.name == cuid) {
                    if (particular.applierid == iApplierID || iApplierID == -1) {
                        particular.isleave = true
                        particular.isSpeak = false
                        particular.isinseat = false
                        particular.isonline = isLogin;
                        return particular;
                    }
                }
            }
            return null;
        },

        swGetAllMsg: function () {
            return this._msg_storage;
        },

        _onGetImMsg: function (msg) {
            //this._msg_storage.push(msg);
        },

        _getPartByToken: function (szid, iappiredid) {
            var part = null;
            for (var iindex = 0; iindex < this._conf_particulars.length; iindex++) {
                part = this._conf_particulars[iindex];
                if (part.applierid == iappiredid) {
                    return part;
                }
            }
        },

        _onNotify: function (msgType, cmd, payload) {

            var data = null;

            switch (msgType) {
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_CONF_START:
                    this._conf_base_info.bIsStarted = true;
                    data = this._conf_base_info.id;
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyconfstart.cmd;
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_CONF_DELETE:
                    data = this._conf_base_info.id;
                    this._parent._confManager._deleteConfByConfId(this._conf_base_info.id);
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyconfdelete.cmd;
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_CONF_STOP:
                    this._conf_base_info.bIsStarted = false;
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyconfstop.cmd;
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_ADD:
                    var response = proto.BVCU.ConfParticipators.deserializeBinary(payload);
                    var portslist = response.getConfportsList();
                    var portsLocal = [];
                    var participator = null;
                    for (var iIndex = 0; iIndex < portslist.length; iIndex++) {
                        participator =
                            jSW.SwConfManager._UTILS.participatorToLocalUser(this._parent, portslist[iIndex]);
                        portsLocal[iIndex] = participator;
                        if (participator != null) {
                            jSWUtils.debugLogInfo("WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_JOIN:" + participator.name);
                        }
                    }
                    this._addParticipators(portsLocal);
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipartoradd.cmd;
                    data = portsLocal;
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_JOIN:
                    var participator = proto.BVCU.ConfParticipatorInfo.deserializeBinary(payload);
                    participator = jSW.SwConfManager._UTILS.participatorToLocalUser(this._parent, participator);
                    var portsLocal = [];
                    portsLocal.push(participator);
                    this._addParticipators(portsLocal);
                    data = portsLocal;
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipartorjoin.cmd;
                    console.log("WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_JOIN:" + participator.name);
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_REMOVE:
                    var response = proto.BVCU.ConfParticipators.deserializeBinary(payload);
                    var portslist = response.getConfportsList();
                    var portsLocal = [];
                    var participator = null;
                    for (var iIndex = 0; iIndex < portslist.length; iIndex++) {
                        participator =
                            jSW.SwConfManager._UTILS.participatorToLocalUser(this._parent, portslist[iIndex]);
                        portsLocal[iIndex] = participator;
                        if (participator != null) {
                            jSWUtils.debugLogInfo("WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_REMOVE:" + participator.name);
                        }
                    }
                    this._removeParticipator(portsLocal);
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipartorremove.cmd;
                    data = portsLocal;
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_EXIT:
                    var participator = proto.BVCU.ConfParticipatorInfo.deserializeBinary(payload);
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipartorexit.cmd;
                    participator = jSW.SwConfManager._UTILS.participatorToLocalUser(this._parent, participator);
                    console.log("WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_EXIT:" + participator.name);
                    var parts = [];
                    parts.push(participator);
                    this._removeParticipator(parts);
                    data = parts;
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_APPLYFOR_ENDSPEAK:
                    data = proto.BVCU.ConfParticipatorInfo.deserializeBinary(payload);
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyapplyforendspeak.cmd;
                    data = jSW.SwConfManager._UTILS.participatorToLocalUser(this._parent, data);
                    console.log("WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_APPLYFOR_ENDSPEAK:" + data.name);
                    this._updateParticipator(data);
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_TERMINATE_SPEAK:
                    var data = proto.BVCU.ConfParticipatorInfo.deserializeBinary(payload);
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyterminatespeak.cmd;
                    data = jSW.SwConfManager._UTILS.participatorToLocalUser(this._parent, data);
                    console.log("WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_TERMINATE_SPEAK:" + data.name);
                    this._updateParticipator(data);
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_APPLYFOR_STARTSPEAK:
                    var data = proto.BVCU.ConfParticipatorInfo.deserializeBinary(payload);
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyapplyforstartspeak.cmd;
                    data = jSW.SwConfManager._UTILS.participatorToLocalUser(this._parent, data);
                    console.log("WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_APPLYFOR_STARTSPEAK:" + data.name);
                    this._updateParticipator(data);
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_INVITE_SPEAK:
                    var data = proto.BVCU.ConfParticipatorInfo.deserializeBinary(payload);
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyinvitespeak.cmd;
                    data = jSW.SwConfManager._UTILS.participatorToLocalUser(this._parent, data);
                    console.log("WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_INVITE_SPEAK:" + data.name);
                    this._updateParticipator(data);
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_LEAVE:
                    var data = proto.BVCU.ConfParticipatorInfo.deserializeBinary(payload);
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipartorleave.cmd;
                    data = jSW.SwConfManager._UTILS.participatorToLocalUser(this._parent, data);
                    console.log("WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_LEAVE:" + data.name);
                    this._updateParticipator(data);
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_RETURN:
                    var data = proto.BVCU.ConfParticipatorInfo.deserializeBinary(payload);
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipartorreturn.cmd;
                    data = jSW.SwConfManager._UTILS.participatorToLocalUser(this._parent, data);
                    console.log("WEB_BVCU_CONF_NOTIFY_PARTICIPATOR_RETURN:" + data.name);
                    this._updateParticipator(data);
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_PARTICIPATOR_MODIFY:
                    var data = proto.BVCU.ConfParticipatorInfo.deserializeBinary(payload);
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipatormodify.cmd;
                    data = jSW.SwConfManager._UTILS.participatorToLocalUser(this._parent, data);
                    console.log("notifyparticipatormodify:" + data.name);
                    this._updateParticipator(data);
                    if (data.isinseat) {
                        this._conf_base_info.bIsStarted = true;
                    }
                    var arrayData = [];
                    arrayData.push(data);
                    data = arrayData;
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_USERLOGIN:
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipatormodify.cmd;
                    var eventsource = payload;
                    data = [];
                    var part = this._onUserLoginout(eventsource.getSzid(), eventsource.getIvalue(), true);
                    if (part) {
                        data.push(part);
                    } else {
                        return;
                    }
                    break;

                case proto.WEBBVCU.MSGType.WEB_BVCU_CONF_NOTIFY_USERLOGOUT:
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipatormodify.cmd;
                    var eventsource = payload;
                    data = [];
                    var part = this._onUserLoginout(eventsource.getSzid(), eventsource.getIvalue(), false);
                    if (part) {
                        data.push(part);
                    } else {
                        return;
                    }
                    break;
                case proto.WEBBVCU.MSGType.WEB_BVCU_NOTIFY_PU_ONOFFLINE:
                    cmd = jSWProtocol.RequestHeader.confnotify.notifyparticipatormodify.cmd;
                    var pulistNotify = payload;
                    var pulistData = [];
                    for (var iIndex = 0; iIndex < pulistNotify.length; iIndex++) {
                        var part = this._onUserLoginout(pulistNotify[iIndex]._id_pu, -1,
                            pulistNotify[iIndex]._info_pu.onlinestatus == 1);
                        if (part) {
                            pulistData.push(part);
                        } else {
                            return;
                        }
                    }
                    data = pulistData;
                    break;
                default: break;
            }
            this._parent._confManager._dispatchCallback(this, cmd, data);
        },

        _onRecvImMsg: function (imInfo) {
            var cmd = jSWProtocol.RequestHeader.confnotify.notifyimmsg.cmd;
            var sender = this._getPartByToken(imInfo.szSourceId, imInfo.iSourceId);
            var bismine = (sender == this._getCurrentUser());
            var data = {
                confid: imInfo.szTargetId,
                sender: sender,
                szmsgs: imInfo.szmsg,
                bismine: bismine
            }
            this._parent._confManager._dispatchCallback(this, cmd, data);
        }
    };


    dMgr.RegModule("Conference", new (function () { }));
})(jSW.DependencyMgr);