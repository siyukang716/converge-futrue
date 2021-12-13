// JavaScript Document

(function($) {

    jQuery.TigerPlayer = function () {

    };

    jQuery.TigerPlayer.prototype = {
        _isAction: true,
        _isPlay: true,
        _width: 0,
        _thewidth: 0,
        _CurrTime: 0,

        _addHour: 0,
        _addMinute: 0,
        _addSecond: 0,
        _TheHour: 0,
        _TheMinute: 0,
        _TheSecond: 0,

        _flag: 0,
        _alltime: 0,
        _addwidth: 0,
        _offsetW: 0,
        _times: 0,
        _rwidth: 0,

        _down: false,
        _BarMove: false,
        _lastX: 0,
        _NewX: 0,

        _userData: null,
        _event_play: null,
        _event_pause: null,
        _event_close: null,
        _event_jump: null,

        id_MyPlayerButtonPlayPause: null,
        id_MyPlayerButtonClose: null,
        id_BarControl: null,
        id_TheBar: null,
        id_BarBeginTime: null,
        id_BarFinishTime: null,
        id_TheColorBar: null,
        id_TimeBall: null,

        addBar: function(DOM, allTime, onPlay, onPause, onClose, onJump) {
            this.CleanAll();
            this._alltime = allTime;
            this._event_play = onPlay;
            this._event_pause = onPause;
            this._event_close = onClose;
            this._event_jump = onJump;

            now = new Date().getTime()
            now = now + '_' + Math.floor(Math.random()*1000+1);

            this.id_MyPlayerButtonPlayPause = 'MyPlayerButtonPlayPause_' + now;
            this.id_MyPlayerButtonClose = 'MyPlayerButtonClose_' + now;
            this.id_BarControl = 'BarControl_' + now;
            this.id_BarButton = 'BarButton_' + now;
            this.id_TheBar = 'TheBar_' + now;
            this.id_BarBeginTime = 'BarBeginTime_' + now;
            this.id_BarFinishTime = 'BarFinishTime_' + now;
            this.id_TheColorBar = 'TheColorBar_' + now;
            this.id_TimeBall = 'TimeBall_' + now;

            DOM.empty();
            DOM.append(
                '<div class="MyPlayerControl">' +
                    '<div class="MyPlayerButton">'  +
                        '<div class="MyPlayerButtonPlayPause record_play_glyphicon-pause" id="'+ this.id_MyPlayerButtonPlayPause +'"></div>' +
                    '</div>' +
                    '<div class="BarControl" id="' + this.id_BarControl + '">' +
                        '<div class="BarBeginTime" id="' + this.id_BarBeginTime + '">00:00</div>' +
                    '</div>' +
                    '<span class="MyPlayerButtonClose record_play_glyphicon-stop" id="'+this.id_MyPlayerButtonClose+'">X</span>' +
                '</div>');
            $('#' + this.id_BarControl).append(
                '<div class="TheBar" id="' + this.id_TheBar +'">' +
                    '<div class="TimeBall" id="' + this.id_TimeBall + '"></div>' +
                    '<div class="TheColorBar" id="'+this.id_TheColorBar+'"></div>' +
                '</div>' +
                '<div class="BarFinishTime" id="' + this.id_BarFinishTime + '">10:35</div>');

            DOM.player = this;

            document.getElementById(this.id_MyPlayerButtonPlayPause).player = this;
            document.getElementById(this.id_MyPlayerButtonClose).player = this;
            document.getElementById(this.id_BarControl).player = this;
            document.getElementById(this.id_TheBar).player = this;
            document.getElementById(this.id_BarBeginTime).player = this;
            document.getElementById(this.id_BarFinishTime).player = this;
            document.getElementById(this.id_TheColorBar).player = this;
            document.getElementById(this.id_TimeBall).player = this;

            this._width = $('#'+this.id_TheBar).width();
            this._times = allTime / 1000;
            this._rwidth = this._width - 8;
            this._addwidth = (this._width - 10) / this._times;
            var t = this.TransitionTime(allTime);
            $('#' + this.id_BarFinishTime).html(t.StringTime);

            $(document).on("mousedown", '#' + this.id_TimeBall, function(event) {
                window.player = event.target.player
                var player = window.player;
                if (!player) {
                    return true;
                }

                player._lastX = event.clientX;
                event.preventDefault();
                player._down = true;
                player._BarMove = true;
                if (player._isAction) {
                    player.StopBar();
                    document.getElementById(player.id_MyPlayerButtonPlayPause).className = "MyPlayerButtonPlayPause record_play_glyphicon-play"
                }
            });

            $(document).mousemove(function(event) {
                event.preventDefault();

                var player = window.player;
                if (!player) {
                    return true;
                }

                player._NewX = event.clientX;
                if (player._BarMove) {
                    var mcs = player._NewX - player._lastX;
                    player._lastX = player._NewX;
                    if (mcs < 0) {
                        if (player._thewidth - (-mcs) > 0) {
                            player._thewidth = player._thewidth - (-mcs)
                        }
                    } else {
                        if (player._thewidth + mcs < player._rwidth) {
                            player._thewidth = player._thewidth + mcs
                        } else {
                            player._thewidth = player._rwidth
                        }
                    }
                    player.timechange();

                    $('#'+player.id_TheColorBar).css("width", player.GetLeftPercentage());
                    $('#'+player.id_TimeBall).css("left", player.GetBarLeftPercentage());
                    //console.log(player);
                }
            });
            $(document).mouseup(function() {
                var player = window.player;
                window.player = null;
                if (!player) {
                    return true;
                }

                if (player._BarMove) {
                    player._BarMove = false;
                    player._down = false;
                    player._NewX = 0;
                    var xo = parseInt(player._CurrTime / 1000);
                    player._offsetW = player._thewidth - xo * player._addwidth;
                    if (player._isAction) {
                        player.OpenBar();
                        player._event_jump(player._userData, player._CurrTime)
                        document.getElementById(player.id_MyPlayerButtonPlayPause).className = "MyPlayerButtonPlayPause record_play_glyphicon-pause"
                    }
                }
            });
            $(document).on("click", '#' + this.id_MyPlayerButtonPlayPause, function(event) {
                var player = event.target.player;
                if (!player) {
                    return true;
                }

                if (player._isPlay) {
                    player.StopBar();
                    document.getElementById(player.id_MyPlayerButtonPlayPause).className = "MyPlayerButtonPlayPause record_play_glyphicon-play"
                } else {
                    player.OpenBar();
                    document.getElementById(player.id_MyPlayerButtonPlayPause).className = "MyPlayerButtonPlayPause record_play_glyphicon-pause"
                }
            });

            $(document).on("click", '#' + this.id_MyPlayerButtonClose, function(event) {
                var player = event.target.player;
                if (!player) {
                    return true;
                }

                player.restTime(0);
                //player.StopBar();

                player._event_close(player._userData);
            });


            this._isPlay = true;
            //this.OpenBar()
        },

        setUserData: function(userData) {
            this._userData = userData;

            console.log('setUserData: ' + this._userData)
        },

        restTime: function(allTime) {
            this.CleanAll();
            //this.StopBar();
            this._alltime = allTime;
            this._width = $('#'+this.id_TheBar).width();
            this._times = allTime / 1000;
            this._rwidth = this._width - 8;
            this._addwidth = (this._width - 10) / this._times;
            var t = this.TransitionTime(allTime);
            $('#' + this.id_BarFinishTime).html(t.StringTime);
            $('#' + this.id_TheColorBar).css("width", 0);
            $('#' + this.id_TimeBall).css("left", 0);
            //this.OpenBar()
        },
        Stop: function() {
            this.StopBar()
        },
        Begin: function() {
            this.OpenBar()
        },
        changeBarColor: function(COLOR) {
            var color = typeof(COLOR) != "undefined" ? COLOR : '#3498DB';
            $('#' + this.id_BarControl).css("background", color);
            $('#' + this.id_TimeBall).css("background", color)
        },
        changeFontColor: function(COLOR) {
            var color = typeof(COLOR) != "undefined" ? COLOR : '#3498DB';
            $('#'+this.id_BarBeginTime+',#'+this.id_BarFinishTime).css("color", color)
        },
        getTheTime: function() {
            return this._CurrTime;
        },

        CleanAll: function() {
            this._isAction = true;
            this._isPlay = true;
            this._thewidth = 0;
            this._CurrTime = 0;
            this._addHour = 0;
            this._addMinute = 0;
            this._addSecond = 0;
            this._TheHour = 0;
            this._TheMinute = 0;
            this._TheSecond = 0;
            this._offsetW = 0;
            this._thewidth = 0;
            this._flag = 0;
        },
    
        timechange: function() {
            this._CurrTime = parseInt(this._thewidth / this._rwidth * this._alltime);
            var ltx = this.TransitionTime(this._CurrTime);
            if (this._TheHour > 0) {
                if (ltx.hHour) {
                    $('#'+this.id_BarBeginTime).html(ltx.StringTime)
                    document.getElementById(this.id_BarBeginTime).innerText = ltx.StringTime;
                } else {
                    $('#'+this.id_BarBeginTime).html("00:" + ltx.StringTime)
                    document.getElementById(this.id_BarBeginTime).innerText = "00:" + ltx.StringTime;
                }
            } else {
                $('#'+this.id_BarBeginTime).html(ltx.StringTime)
                document.getElementById(this.id_BarBeginTime).innerText = ltx.StringTime;
            }

            this._addSecond = ltx.Tsec;
            this._addMinute = ltx.Tmin;
            this._addHour = ltx.Thour;
        },

        changeBar: function(currTime) {
            if ('undefined' === typeof currTime) {
                currTime = -1;
            }

            if (this._isPlay == false) {
                console.log('changeBar() _isPlay is false')
                return;
            }


            if (currTime != -1) {
                this._CurrTime = currTime;
                mss = currTime;
                var hours = parseInt((mss / (1000 * 60 * 60)));
                var minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60));
                var seconds = (mss % (1000 * 60)) / 1000;

                this._addHour = hours;
                this._addMinute = minutes;
                this._addSecond = seconds;
            }


            var second, minute, hour;
            this._thewidth = this._thewidth * 1 + this._addwidth - this._offsetW;
            if (this._offsetW > 0) {
                this._offsetW = 0
            }

            this._thewidth = this._width * (this._CurrTime / this._alltime)

            if (this._thewidth < this._rwidth && this._CurrTime < this._alltime) {

            } else {
                this._thewidth = this._rwidth;
                //this.StopBar();
            }

            {
                if (this._addSecond > 9) {
                    second = "" + this._addSecond
                } else {
                    second = "0" + this._addSecond
                }

                if (this._addMinute > 9) {
                    minute = "" + this._addMinute
                } else {
                    minute = "0" + this._addMinute
                }

                if (this._addHour > 9) {
                    hour = "" + this._addHour
                } else {
                    hour = "0" + this._addHour
                }

                if (this._addHour > 0) {
                    this._flag = 1
                }
            }

            if (this._flag == 0) {
                $('#'+this.id_BarBeginTime).html(minute + ":" + second)
            } else {
                $('#'+this.id_BarBeginTime).html(hour + ":" + minute + ".:" + second)
            }
            //this._alltime
            $('#'+this.id_TheColorBar).css("width", this.GetLeftPercentage());
            $('#'+this.id_TimeBall).css("left", this.GetBarLeftPercentage());

            //console.log('changeBar(): ' + minute + ":" + second)
        },

        GetLeftPercentage: function(){
            return this.GetCurrentPercentage() + "%";
        },

        GetBarLeftPercentage: function(){
            return "calc(" + this.GetCurrentPercentage() + "% - 5px)";
        },

        GetCurrentPercentage: function(){
            var iPercentage = this._CurrTime / this._alltime * 100;
            if(iPercentage > 100){
                iPercentage = 100;
            }
            return iPercentage;
        },

        TransitionTime: function(str) {
            var m = parseFloat(str) / 1000;
            var time = "";
            var second, minute, hour;
            var haveHour = false;
            var ch = 0,
                csx = 0,
                cm = 0;
            if (m >= 60 && m < 60 * 60) {
                if (parseInt(m / 60.0) < 10) {
                    minute = "0" + parseInt(m / 60.0)
                } else {
                    minute = parseInt(m / 60.0)
                }
                var cs = parseInt(m - parseInt(m / 60.0) * 60);
                if (cs < 10) {
                    second = "0" + cs
                } else {
                    second = "" + cs
                }
                this._TheMinute = parseInt(m / 60.0);
                this._TheSecond = cs;
                cm = this._TheMinute;
                this._TheHour = 0;
                csx = cs;
                time = minute + ":" + second;
                $('#'+this.id_BarBeginTime).html("00:00")
            } else if (m >= 60 * 60) {
                flag = 1;
                haveHour = true;
                ch = parseInt(m / 3600.0);
                cm = parseInt((parseFloat(m / 3600.0) - parseInt(m / 3600.0)) * 60);
                csx = parseInt((parseFloat((parseFloat(m / 3600.0) - parseInt(m / 3600.0)) * 60) - parseInt((parseFloat(m / 3600.0) - parseInt(m / 3600.0)) * 60)) * 60);
                if (ch < 10) {
                    hour = "0" + ch
                } else {
                    hour = "" + ch
                }

                if (cm < 10) {
                    minute = "0" + cm
                } else {
                    minute = "" + cm
                }

                if (csx < 10) {
                    second = "0" + csx
                } else {
                    second = "" + csx
                }
                this._TheHour = ch;
                this._TheMinute = cm;
                this._TheSecond = csx;
                time = hour + ":" + minute + ":" + second;
                $('#'+this.id_BarBeginTime).html("00:00:00")
            } else {
                $('#'+this.id_BarBeginTime).html("00:00");
                csx = parseInt(m);
                if (parseInt(m) > 9) {
                    second = "" + parseInt(m)
                } else {
                    second = "0" + parseInt(m)
                }

                this._TheMinute = 0;
                this._TheSecond = parseInt(m);
                this._TheHour = 0;
                time = "00:" + second
            }

            var tt = {
                hHour: haveHour,
                Thour: ch,
                Tmin: cm,
                Tsec: csx,
                StringTime: time
            };

            return tt
        },

        StopBar: function() {
            if (!this._down) {
                this._isAction = false
            }
            this._isPlay = false;
            //clearInterval(this.t)

            this._event_pause(this._userData);
        },

        OpenBar: function() {
            this._isAction = true;
            this._isPlay = true;
            //this.t = setInterval(this.changeBar, 1000)

            this._event_play(this._userData);
        },
    }
})(jQuery);
