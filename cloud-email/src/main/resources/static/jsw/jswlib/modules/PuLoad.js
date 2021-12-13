/**
 * auto load pu list, after login ok,
 */
(function (dMgr) {
    var jSWProtocol = dMgr.GetModule("jSWProtocol");

    function PuLoad() {

    }

    var mSession = null;
    /**
     * 初始化，注入session
     */
    PuLoad.prototype.PuLoadInit = function (tConfig) {
        this.config = {};
        this.config.nEGP = tConfig.bNEGP;
        this.config.bManualLP = tConfig.bManualLP;
        this.config.bAutoLoadOnlyOnlinePu = tConfig.bAutoLoadOnlyOnlinePu
        _InnerInit();
        return this;
    }


    PuLoad.prototype.OnSearchPuListResponse = function (msgType, payload) {
        var stRc = null;
        if (msgType == proto.WEBBVCU.MSGType.WEB_BVCU_SEARCH_GET_PULIST) {
            var searchRsp = {
                puList: null,
                info: null
            }
            var searchResponse = proto.BVCU.Search.Search_Response.deserializeBinary(payload).toObject();
            searchRsp.puList = mSession._internalOnResponsePulist(searchResponse.ppulistList);
            searchRsp.info = searchResponse.stsearchinfo;
            console.log('on get pu list');
            if (this.config.bManualLP) {
                stRc = searchRsp; // 返回检索信息
                console.log('Manual Search Get Pu List: ')
                console.log(stRc.info);
            } else {
                OnAutoLoadPuList.bind(this)(searchRsp);
                stRc = searchRsp.puList; // 为兼容老版本，自动获取，返回设备列表
            }
        } else {
            console.error('we no longer support WEB_BVCU_GET_PULIST');
        }
        return stRc;
    }

    var _innerTag = {
        iStart: 0,
        iCount: 0
    };
    var iTotalCount = 0;
    var iEveryCount = 64;
    var bLoadOver = false;

    function _InnerInit() {
        _innerTag.iStart = 0;
        _innerTag.iCount = iEveryCount;
        bLoadOver = false;
    }


    function OnAutoLoadPuList(puList) {
        if (!this.config.bManualLP) {
            var tag = _innerTag;
            if (!this.config.bManualLP) {
                iTotalCount += puList.info.icount;
                console.log(puList);
                if (iTotalCount < puList.info.itotalcount) {
                    tag.iStart += iEveryCount;
                    _SearchGetPuList.bind(this)(null, tag.iStart, iEveryCount, null, tag);
                } else {
                    bLoadOver = true;
                }
            }
        }
    }

    PuLoad.prototype.CheckPuLoadShouldNotNotify = function (msgType) {
        if (msgType != proto.WEBBVCU.MSGType.WEB_BVCU_SEARCH_GET_PULIST) {
            return false;
        }
        return !this.config.bManualLP && !this.config.nEGP && !bLoadOver;
    }


    PuLoad.prototype.OnLoginOk = function (session) {
        mSession = session;
        iTotalCount = 0;
        _InnerInit()
        if (!this.config.bManualLP) {
            _SearchGetPuList.bind(this)({}, 0, iEveryCount, null, _innerTag);
        }
    }

    function _SearchGetPuList(filter, iPosition, iCount, cb, tag) {
        var payload = new proto.BVCU.Search.Search_Request();
        var searchInfo = new proto.BVCU.Search.SearchInfo();

        payload.setStsearchinfo(searchInfo);
        searchInfo.setItype(jSWProtocol.SearchType.PULIST);
        searchInfo.setIcount(Number(iCount));
        searchInfo.setIpostition(Number(iPosition));

        if (filter) {
            var puFilter = new proto.BVCU.Search.Search_PUListFilter();

            puFilter.setIonlinestatus(filter.iOnlineStatus ? filter.iOnlineStatus : 0 || (this.config.bAutoLoadOnlyOnlinePu && this.config.bManualLP));
            puFilter.setItimebegin(filter.iTimeBegin ? filter.iTimeBegin : 0)
            puFilter.setItimeend(filter.iTimeEnd ? filter.iTimeEnd : 0)
            puFilter.setSzidorname(filter.szIDOrName ? filter.szIDOrName : "")
            puFilter.setSzgroupid(filter.szGroupID ? filter.szGroupID : "")

            payload.setStpulistfilter(puFilter);
        }

        var command = this.config.bManualLP ? '' : jSWProtocol.RequestHeader.pulist.cmd;
        var rc = jSWProtocol.AutoReqSendProxy(mSession, null, null, function () {
            return {
                msgtype: proto.WEBBVCU.MSGType.WEB_BVCU_SEARCH_GET_PULIST,
                payload: payload,
                callback: cb,
                tag: tag,
                cmd: command
            }
        });
        return rc;
    }

    /**
     * 
     */
    PuLoad.prototype.SearchGetPulist = function (options, cb, tag) {
        var position = Number(options.iPosition);
        var count = Number(options.iCount);
        var filter = options.stFilter;
        if (count <= 0) {
            count == 128;
        }
        return _SearchGetPuList.bind(this)(filter, position, count, cb, tag);
    }

    //jSW.DependencyMgr.ClaimModule('PuLoad')
    dMgr.RegClaimedModule("PuLoad", PuLoad);

    //var PuLoad = jSW.DependencyMgr.GetModule("PuLoad");
})(jSW.DependencyMgr);