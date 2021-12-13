(function (dMgr) {
    function HttpFlv() {
        var szVideoCollect = [];
        var iIndexVideo = 0;
        var iIndexVideoFlagName = "_mySwIndexFlag";
        var iCheckIntervalHandle = -1;

        function GetPlayerThroughDlgparam(dlgparam) {
            return dlgparam['httpFlvPlayer'];
        }

        function SetPlayerThroughDlgparam(dlgparam, player) {
            dlgparam['httpFlvPlayer'] = player;
            SavePlayerToAnArray(player);
        }

        function SavePlayerToAnArray(player) {
            player[iIndexVideoFlagName] = (iIndexVideo += 1);
            szVideoCollect[player[iIndexVideoFlagName]] = player;
        }

        function DeletePlayerFromAnArray(player) {
            if (player) {
                szVideoCollect[player[iIndexVideoFlagName]] = null;
            }
        }

        function CheckPlayerVideoPlayTooSlow() {
            var player = null;
            for (var iIndex in szVideoCollect) {
                player = szVideoCollect[iIndex];
                if (player) {
                    var buffered = player.buffered;
                    if (buffered.length > 0) {
                        var end = buffered.end(0)
                        console.log("interval is : " + (end - player.currentTime));
                        if (end - player.currentTime > 3) {
                            player.currentTime = end - 0.1;
                        }
                    }
                }
            }
        }

        function TryStartCheckLoop() {
            if (iCheckIntervalHandle < 0) {
                iCheckIntervalHandle = setInterval(CheckPlayerVideoPlayTooSlow, 3000);
            }
        }

        this.CreatePlayer = function (videoEleParent, DesUrl, dlgparam) {
            var media = dlgparam['_media'];
            var hasAudio = (media & jSW.MEDIADIR.AUDIORECV) == jSW.MEDIADIR.AUDIORECV;
            var hasVideo = (media & jSW.MEDIADIR.VIDEORECV) == jSW.MEDIADIR.VIDEORECV;

            var videoEle = videoEleParent.getElementsByTagName("video")[0];
            var mediaDataSource = {
                hasAudio: hasAudio,
                hasVideo: hasVideo,
                type: 'flv',
                url: DesUrl,
                isLive: true,
                bh265: dlgparam.bh265
            };
            var player = flvjs.createPlayer(mediaDataSource, {
                enableStashBuffer: false
            });

            player.attachMediaElement(videoEle);
            player.load();

            videoEle.addEventListener("play", function () {
                var buffered = this.buffered;
                if (buffered.length > 0) {
                    var end = buffered.end(0)
                    if (end - this.currentTime > 0.15) {
                        this.currentTime = end - 0.1
                    }
                }
            });

            player.play();
            if (GetPlayerThroughDlgparam(dlgparam)) {
                ClosePlayer(GetPlayerThroughDlgparam(dlgparam));
            }
            SetPlayerThroughDlgparam(dlgparam, player);
            TryStartCheckLoop();

            return player
        }

        function ClosePlayer(player) {
            DeletePlayerFromAnArray(player);
            player.pause();
            player.unload();
            player.detachMediaElement();
            player.destroy();
        }

        this.ClosePlayer = function (dlgparam) {
            if (GetPlayerThroughDlgparam(dlgparam)) {
                ClosePlayer(GetPlayerThroughDlgparam(dlgparam));
            }
        }

        this.buildUrlFromRtmpUrl = function (host, url, bUseWs) {
            var params = url.split(':')[1].split('/');

            if (bUseWs) {
                if (host.indexOf("https://") == 0) {
                    host = "wss" + host.slice(5) + "/jswapi/media"
                } else if (host.indexOf("http://") == 0) {
                    host = "ws" + host.slice(4) + "/jswapi/media"
                }
            }

            var rcUrl = host + '/live?port=' + params[0] + '&app=' + params[1] + '&stream=' + params[2];

            return rcUrl;
        }
    }

    dMgr.RegModule("HttpFlv", new HttpFlv());
    //var httpflv = jSW.DependencyMgr.GetModule("HttpFlv");
})(jSW.DependencyMgr);