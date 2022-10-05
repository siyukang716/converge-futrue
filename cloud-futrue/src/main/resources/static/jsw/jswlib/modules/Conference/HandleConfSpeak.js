(function (dMgr) {
    function HandleConfSpeak() {
        var AudioMgr = dMgr.ClaimModule("AudioMgr");

        this.OnCurrentUserApplySpeakChange = function (bSpeak) {
            if (bSpeak) {
                AudioMgr.StartAudio();
            } else {
                AudioMgr.StopAudio();
            }
        }

        this.OnIStartConfConnectWsAudio = function(){
            AudioMgr.ConnectWsAudio()
        }
    }

    dMgr.RegModule("HandleConfSpeak", new HandleConfSpeak());
    //var HandleConfSpeak = jSW.DependencyMgr.GetModule("HandleConfSpeak");
})(jSW.DependencyMgr);