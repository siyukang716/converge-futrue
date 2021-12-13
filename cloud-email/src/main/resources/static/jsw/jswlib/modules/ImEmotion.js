(function (dMgr) {
    dMgr.RegModule("ImEmotion", new (function () {
        var eMotionsUrls = [];
        var EmotionDir = "jswlib/res/emotion/f";
        var jswHost = dMgr.jSWGetHost();
        var tempGifIndex = "";
        for (var ii = 0; ii < 104; ii++) {
            if(ii < 10){
                tempGifIndex = "00" + ii;
            } else if (ii < 100) {
                tempGifIndex = "0" + ii;
            } else {
                tempGifIndex = ii;
            }
            eMotionsUrls[ii] = {
                url: jswHost + EmotionDir + tempGifIndex + ".gif",
                id: tempGifIndex
            };
        }

        this.GetEmotionCode = function (Id) {
            return '/base/f' + Id;
        }

        this.GetGifEmotion = function (Id) {
            Id = Id.replace('/base/f', "");
            var iEIndex = Number(Id);
            return eMotionsUrls[iEIndex];
        }

        this.GetEmotimoUrls = function () {
            return eMotionsUrls;
        }
    }));
})(jSW.DependencyMgr);