(function (dMgr) {
    dMgr.RegModule("CmdParse", new (function () {

        //var _BVCU_SUBMETHOD_TYPE = {
        //    BVCU_SUBMETHOD_UNKNOWN : 0, //未知命令
        //    BVCU_SUBMETHOD_CUSTOM : 0x00001, //自定义命令，服务器透明转发该命令给目标。输入类型：自定义；输出类型: 自定义。
        //    //注意，BVCU_CmdMsgContent.pData被认为是字节类型，BVCU_CmdMsgContent.iDataCount表示字节长度

        //    //=============query====================    
        //    //PU部分-----------------------------
        //    BVCU_SUBMETHOD_PU_LIST : 0x00010,//CU从Server获取设备列表。输入类型：无；输出类型: BVCU_PUCFG_PUChannelInfo数组
        //    BVCU_SUBMETHOD_PU_BROADCASTSTATUS,//获取广播的设备列表状态(一个Session只能同时有一个广播)。输入类型：无；输出类型：BVCU_BroadcastStatus数组
        //    BVCU_SUBMETHOD_PU_GPSDATA,//获取PU的GPS通道数据。输入类型：无；输出类型: BVCU_PUCFG_GPSData
        //    BVCU_SUBMETHOD_PU_STORAGE_MEDIA, //获取某个存储器的分区信息。输入类型无; 输出类型：BVCU_PUCFG_Storage_Media数组    
        //    BVCU_SUBMETHOD_PU_GROUPLIST, // 获取设备分组列表。输入类型：无；输出类型：BVCU_PUCFG_GroupItem数组
        //    BVCU_SUBMETHOD_PU_GROUPINFO, // 获取设备分组信息。输入类型：BVCU_PUCFG_GroupItem；输出类型：BVCU_PUCFG_GroupInfo
        //    BVCU_SUBMETHOD_PU_UPDATESTATUS, //升级固件的状态。输入类型：无；输出类型：BVCU_PUCFG_UpdateStatus
        //    BVCU_SUBMETHOD_PU_POSITION, //基站、wifi定位信息。输入类型：无；输出类型：BVCU_PUCFG_Position
        //    BVCU_SUBMETHOD_PU_UserList, //获取平台设备使用者列表。输入类型：无；输出类型：BVCU_PUCFG_User数组
        //    BVCU_SUBMETHOD_PU_UserInfo, //获取平台设备使用者信息。输入类型：BVCU_PUCFG_User；输出类型：BVCU_PUCFG_UserInfo
        //    BVCU_SUBMETHOD_PU_UserGroupList, //获取平台设备使用者组列表。输入类型：无；输出类型：BVCU_PUCFG_UserGroup数组
        //    BVCU_SUBMETHOD_PU_UserGroupInfo, //获取平台设备使用者组信息。输入类型：BVCU_PUCFG_UserGroup；输出类型：BVCU_PUCFG_UserGroupInfo

        //    //User部分---------------------------
        //    BVCU_SUBMETHOD_USER_GROUPLIST = 0x01000,//获取用户组列表。输入类型：无；输出类型：BVCU_UCFG_UserGroup数组
        //    BVCU_SUBMETHOD_USER_GROUPINFO,//获取用户组信息。输入类型：BVCU_UCFG_UserGroup；输出类型：BVCU_UCFG_UserGroupInfo
        //    BVCU_SUBMETHOD_USER_USERLIST, //获取用户列表。输入类型：无；输出类型：BVCU_UCFG_User数组
        //    BVCU_SUBMETHOD_USER_USERINFO, //获取用户信息。输入类型：BVCU_UCFG_User； 输出类型：BVCU_UCFG_UserInfo
        //    BVCU_SUBMETHOD_USER_ONLINE, //获取在线用户列表。输入类型：无； 输出类型：BVCU_UCFG_User_Online数组
        //    BVCU_SUBMETHOD_USER_ONLINEINFO, //获取在线用户信息。输入类型：BVCU_UCFG_User_Online； 输出类型：BVCU_UCFG_User_OnlineInfo

        //    //VTDU部分----------------------------
        //    BVCU_SUBMETHOD_VTDU_LIST = 0x01300,//获取VTDU列表。输入类型：无；输出类型：BVCU_VTDUCFG_VTDUItem数组

        //    //NRU部分----------------------------
        //    BVCU_SUBMETHOD_NRU_LIST = 0x01400,//获取NRU列表。输入类型：无；输出类型：BVCU_NRUCFG_NRUItem数组
        //    BVCU_SUBMETHOD_NRU_SCHEDULE_LIST, //某个NRU存储计划列表。输入类型：无；输出类型：BVCU_NRUCFG_Storage_Schedule_ListItem数组
        //    BVCU_SUBMETHOD_NRU_SCHEDULE_GET,//NRU的一条存储计划。输入类型：BVCU_NRUCFG_Storage_Schedule_ListItem, 输出类型：BVCU_NRUCFG_Storage_Schedule

        //    //Decoder部分------------------------
        //    BVCU_SUBMETHOD_DEC_LIST = 0x01500,//获取解码器列表。输入类型：无；输出类型：BVCU_DECCFG_BaseInfo数组

        //    //CONF部分---------------------------
        //    BVCU_SUBMETHOD_CONF_LIST = 0x01600,//获取所有会议列表，BVCU_Command.szTargetID设置为CMS ID。输入类型：无；输出类型：BVCU_Conf_BaseInfo数组
        //    //BVCU_Command.iTargetIndex：0-查询所有会议列表；1-查询自己所在会议列表；2-查询自己不在的会议列表；3-查询有管理权限的会议列表。
        //    BVCU_SUBMETHOD_CONF_INFO,//获取某个会议详细信息，BVCU_Command.szTargetID设置为CONF_ID@CMS(@CMS可以省略，表示目前登录的CMS）。输入类型：无；输出类型：BVCU_Conf_Info

        //    //ALARM部分--------------------------
        //    BVCU_SUBMETHOD_LINKACTION_LIST = 0x01700,//获取报警联动列表。输入类型：无；输出类型：BVCU_Event_LinkAction_Base数组
        //    BVCU_SUBMETHOD_LINKACTION_GET,//获取某个报警联动详细信息。输入类型：BVCU_Event_LinkAction_Base;输出类型：BVCU_Event_LinkAction

        //    //Search部分---------------------------
        //    // BVCU_Command.szTargetID设置为CMS/NRU/VTDU ID。
        //    BVCU_SUBMETHOD_SEARCH_LIST = 0x01800, // 索引文件（记录等）。输入类型：BVCU_Search_Request 输出类型:BVCU_Search_Response 
        //    BVCU_SUBMETHOD_SYSTEM_INFO, // 系统及状态信息。输入类型：无  输出类型:BVCU_ENCFG_SystemInfo

        //    //Region部分-------------------------
        //    BVCU_SUBMETHOD_REGION_LIST = 0x1900, //获取CMS区域列表。输入类型：无；输出类型：BVCU_Region_Base数组
        //    BVCU_SUBMETHOD_REGION_GET, //获取某个CMS区域详细信息。输入类型：BVCU_Region_Base;输出类型：BVCU_Region_Info

        //    //UKey部分---------------------------
        //    BVCU_SUBMETHOD_UKEY_LIST = 0x2000, //获取授权UKey列表。输出类型：BVCU_UCFG_UKeyBase数组
        //    BVCU_SUBMETHOD_UKEY_GET, //获取某个授权UKey详细信息。输入类型：BVCU_UCFG_UKeyBase;输出类型：BVCU_UCFG_UKeyInfo

        //    //CMS部分----------------------------
        //    BVCU_SUBMETHOD_CMS_BASEINFO = 0x2100, //获取登录CMS基本信息。输入类型：无;输出类型：BVCU_CMSCFG_BaseInfo
        //    BVCU_SUBMETHOD_CMS_DIALOGINFO, //获取登录CMS通道信息。输入类型：BVCU_CMSCFG_DialogInfo;输出类型：BVCU_CMSCFG_DialogInfo数组
        //    BVCU_SUBMETHOD_CMS_TIME, //获取登录CMS时间。输入类型：BVCU_CMSCFG_WallTime; 输出类型：BVCU_CMSCFG_WallTime

        //    //=============query/control=============    
        //    /*注意：query/control类型的命令，通常query和control的输入类型和输出类型是对称的，例如
        //    BVCU_SUBMETHOD_PTZATTR命令做Query时，输入类型无，输出类型是BVCU_PUCFG_PTZAttr，做Control
        //    时，输入类型BVCU_PUCFG_PTZAttr，输出类型无。以下注释中，只表明Control的参数，如果没有特别说明，     
        //    Query的参数输入/输出类型就是Control的输出/输入类型
        //    */
        //    //PU部分
        //    BVCU_SUBMETHOD_PU_DEVICEINFO = 0x10000, //设备信息。输入类型：BVCU_PUCFG_DeviceInfo；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_PU_CHANNELINFO,//某个PU的通道信息。输入类型：BVCU_PUCFG_PUChannelInfo；输出类型: 无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_PU_DEVICETIME,//设备时间。输入类型：BVCU_WallTime；输出类型：无
        //    BVCU_SUBMETHOD_PU_SERVERS, //注册服务器。输入类型：BVCU_PUCFG_Servers；输出类型：无
        //    BVCU_SUBMETHOD_PU_ETHERNET, //以太网。输入类型：BVCU_PUCFG_Ethernet；输出类型：无
        //    BVCU_SUBMETHOD_PU_WIFI, //WIFI。输入类型：BVCU_PUCFG_Wifi；输出类型：无
        //    BVCU_SUBMETHOD_PU_RADIONETWORK, //无线网络。输入类型：BVCU_PUCFG_RadioNetwork数组；输出类型：无
        //    BVCU_SUBMETHOD_PU_PTZATTR,  //云台属性。输入类型：BVCU_PUCFG_PTZAttr；输出类型：无；
        //    BVCU_SUBMETHOD_PU_ENCODERCHANNEL,//编码通道属性。输入类型：BVCU_PUCFG_EncoderChannel；输出类型：无；
        //    BVCU_SUBMETHOD_PU_VIDEOIN,//视频输入属性。输入类型：BVCU_PUCFG_VideoIn；输出类型：无
        //    BVCU_SUBMETHOD_PU_AUDIOIN,//音频输入属性。输入类型：BVCU_PUCFG_AudioIn；输出类型：无
        //    BVCU_SUBMETHOD_PU_AUDIOOUT,//音频输出属性。输入类型：BVCU_PUCFG_AudioOut；输出类型：无
        //    BVCU_SUBMETHOD_PU_ALERTIN,//报警输入属性。输入类型：BVCU_PUCFG_AlertIn；输出类型：无
        //    BVCU_SUBMETHOD_PU_ALERTOUT,//报警输出属性。输入类型：BVCU_PUCFG_AlertOut；输出类型：无
        //    BVCU_SUBMETHOD_PU_SERIALPORT,//串口属性。输入类型：BVCU_PUCFG_SerialPort;输出类型：无
        //    BVCU_SUBMETHOD_PU_GPS,//GPS属性。输入类型：BVCU_PUCFG_GPSParam;输出类型：无
        //    BVCU_SUBMETHOD_PU_STORAGE_SCHEDULE, //存储计划。输入类型：BVCU_PUCFG_Storage_Schedule;输出类型：无
        //    BVCU_SUBMETHOD_PU_STORAGE_RULE, //存储属性。输入类型：BVCU_PUCFG_Storage_Rule;输出类型：无    
        //    BVCU_SUBMETHOD_PU_STORAGE_FORMAT,//初始化某个存储器。输入类型：BVCU_PUCFG_Storage_Format;输出类型：无  
        //    BVCU_SUBMETHOD_PU_ONLINECONTROL,//上下线控制。输入类型：BVCU_PUCFG_OnlineControl；输出类型：无
        //    BVCU_SUBMETHOD_PU_SNAPSHOTPARAM,//配置自动抓拍参数。输入类型：BVCU_PUCFG_SnapshotParam;输出类型：无
        //    BVCU_SUBMETHOD_PU_POWER,//配置电源参数。输入类型：BVCU_PUCFG_Power;输出类型：无
        //    BVCU_SUBMETHOD_PU_GPSSPEEDLIMIT,//配置设备全局限速。输入类型：BVCU_PUCFG_GPSSpeedLimit;输出类型：无
        //    BVCU_SUBMETHOD_PU_CARINFO,//设备相关的车辆信息（如：车载设备）。输入类型：BVCU_PUCFG_CarInfo;输出类型：无
        //    BVCU_SUBMETHOD_PU_BLUETOOTH,//设备蓝牙信息。 输入类型：BVCU_PUCFG_Bluetooth;输出类型：无
        //    BVCU_SUBMETHOD_PU_VIDEOIN_CMOS,//视频输入CMOS属性。输入类型：BVCU_PUCFG_ImageExposure；输出类型：无
        //    BVCU_SUBMETHOD_PU_ZFYINFO,//设备相关的执法记录仪信息。输入类型：BVCU_PUCFG_ZFYInfo;输出类型：无

        //    //VTDU部分
        //    BVCU_SUBMETHOD_VTDU_INFO = 0x10F00,//VTDU信息。输入类型：BVCU_VTDUCFG_VTDUInfo；输出类型：无

        //    //NRU部分
        //    BVCU_SUBMETHOD_NRU_INFO = 0x11000,//NRU信息。输入类型：BVCU_NRUCFG_NRUInfo；输出类型：无

        //    //Decoder部分------------------------
        //    //BVCU_Command.szTargetID设置为DEC_ID@CMS(@CMS可以省略，表示目前登录的CMS）
        //    BVCU_SUBMETHOD_DEC_DEVICEINFO = 0x11100, // 解码器详细信息。输入类型：BVCU_DECCFG_DeviceInfo；输出类型：无
        //    BVCU_SUBMETHOD_DEC_OPTIONS, //解码器设置选项。输入类型：BVCU_DECCFG_Options；输出类型：无
        //    BVCU_SUBMETHOD_DEC_DECODER, // 解码器解码通道配置。输入类型：BVCU_DECCFG_DecoderParam；输出类型：无
        //    BVCU_SUBMETHOD_DEC_DISPLAY, // 解码器显示通道配置。输入类型：BVCU_DECCFG_DisplayParam；输出类型：无

        //    //CONF部分
        //    //BVCU_Command.szTargetID设置为CONF_ID@CMS(@CMS可以省略，表示目前登录的CMS）
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_VOLUME = 0x11200,//调整某个参与者的音量。注意只影响本地听到的音量。
        //    //输入类型：BVCU_Conf_Participator_Info，输出类型：无
        //    BVCU_SUBMETHOD_CONF_BASEINFO,//获取/设置会议的信息，BVCU_Command.szTargetID设置为CONF_ID@CMS(@CMS可以省略，表示目前登录的CMS）。
        //    //输入类型：BVCU_Conf_BaseInfo；输出类型：无；触发类型：同名Notify

        //    //=============control=============    
        //    //PU部分---------------------------
        //    BVCU_SUBMETHOD_PU_REBOOT = 0x20000,   //重启设备。输入类型：无；输出类型：无
        //    BVCU_SUBMETHOD_PU_DELETE,       //从数据库中删除PU。输入类型：无；输出类型: 无
        //    BVCU_SUBMETHOD_PU_SHUTDOWN, //关闭设备。输入类型：无；输出类型：无
        //    BVCU_SUBMETHOD_PU_SAVECONFIG, //通知设备保存配置。输入类型：无；输出类型：无
        //    BVCU_SUBMETHOD_PU_PTZCONTROL,//操作云台。输入类型：BVCU_PUCFG_PTZControl；输出类型：无    
        //    BVCU_SUBMETHOD_PU_PUTOFFLINE,//手工使PU下线。输入类型：无；输出类型：无
        //    BVCU_SUBMETHOD_PU_MANUALRECORD,//手工启动/停止PU录像。输入类型：BVCU_PUCFG_ManualRecord；输出类型：无
        //    BVCU_SUBMETHOD_PU_SNAPSHOT, //手工远程抓拍。输入类型：BVCU_PUCFG_Snapshot；输出类型：无
        //    BVCU_SUBMETHOD_PU_UPGRADE, //升级固件。输入类型：BVCU_PUCFG_Upgrade；输出类型：无
        //    BVCU_SUBMETHOD_PU_ADDGROUP, //添加设备分组。输入类型：BVCU_PUCFG_GroupInfo；输出类型：BVCU_PUCFG_GroupItem；触发类型：同名Notify
        //    BVCU_SUBMETHOD_PU_MODGROUP, //修改设备分组。输入类型：BVCU_PUCFG_GroupInfo；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_PU_DELGROUP, //删除设备分组。输入类型：BVCU_PUCFG_GroupItem；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_PU_OPENDIALOG, //请求创建会话。输入类型：BVCU_PUCFG_OpenDialog； 输出类型：无
        //    BVCU_SUBMETHOD_PU_SYNCHRONIZATION, //同步图片（上传未上传的抓图，下载CU上传的图片）。输入类型：BVCU_PUCFG_Picture
        //    BVCU_SUBMETHOD_PU_USER_ADDGROUP, //添加设备使用者组。输入类型：BVCU_PUCFG_UserGroupInfo；输出类型：BVCU_PUCFG_UserGroup；触发类型：同名Notify
        //    BVCU_SUBMETHOD_PU_USER_MODGROUP, //修改设备使用者组。输入类型：BVCU_PUCFG_UserGroupInfo；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_PU_USER_DELGROUP, //删除设备使用者组。输入类型：BVCU_PUCFG_UserGroup；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_PU_USER_ADDUSER,  //添加设备使用者。输入类型：BVCU_PUCFG_UserInfo；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_PU_USER_MODUSER,  //修改设备使用者。输入类型：BVCU_PUCFG_UserInfo；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_PU_USER_DELUSER,  //删除设备使用者。输入类型：BVCU_PUCFG_User；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_PU_USER_OPERATE,  //绑定或解绑设备。输入类型：BVCU_PUCFG_UserOperate；输出类型：无；触发类型：同名Notify

        //    //User部分---------------------------
        //    BVCU_SUBMETHOD_USER_ADDGROUP = 0x22000, //添加用户组。输入类型：BVCU_UCFG_UserGroupInfo；输出类型：BVCU_UCFG_UserGroup；触发类型：同名Notify
        //    BVCU_SUBMETHOD_USER_MODGROUP, //修改用户组。输入类型：BVCU_UCFG_UserGroupInfo；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_USER_DELGROUP, //删除用户组。输入类型：BVCU_UCFG_UserGroup；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_USER_ADDUSER,  //添加用户。输入类型：BVCU_UCFG_UserInfo；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_USER_MODUSER,  //修改用户。输入类型：BVCU_UCFG_UserInfo；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_USER_DELUSER,  //删除用户。输入类型：BVCU_UCFG_User；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_USER_MODPASSWD,//修改用户密码。输入类型：BVCU_UCFG_ModPasswd；输出类型：无
        //    BVCU_SUBMETHOD_USER_KICKOUT,  //踢出在线用户。输入类型：BVCU_UCFG_Kickout；输出类型：无

        //    //NRU部分---------------------------
        //    BVCU_SUBMETHOD_NRU_DELETE = 0x22400, //从数据库中删除NRU。输入类型：无；输出类型: 无
        //    BVCU_SUBMETHOD_NRU_SCHEDULE_SET,//配置NRU的一条存储计划。输入类型：BVCU_NRUCFG_Storage_Schedule, 输出类型:无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_NRU_SCHEDULE_DEL,//删除NRU的一条存储计划。输入类型：BVCU_NRUCFG_Storage_Schedule_ListItem, 输出类型:无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_NRU_MANUALRECORD,//手工启动/停止某个PU录像到NRU。输入类型：BVCU_NRUCFG_ManualRecord；输出类型：无
        //    BVCU_SUBMETHOD_NRU_SNAPSHOT,    //手工抓拍到NRU。输入类型：BVCU_NRUCFG_Snapshot；输出类型：无
        //    BVCU_SUBMETHOD_NRU_BACKUPDB,    //手工备份数据库。BVCU_Command.iTargetIndex：备份表信息，见BVCU_DBTABLE_TYPE。需要是超级管理员权限。
        //    // 输入类型：无；输出类型：BVCU_NRUCFG_BackupDB

        //    //CONF部分---------------------------
        //    //以下由具有BVCU_CONF_PARTICIPATOR_POWER_ADMIN权限者调用
        //    BVCU_SUBMETHOD_CONF_CREATE = 0x22600,//创建会议，BVCU_Command.szTargetID设置为CMS ID。
        //    // 输入类型：BVCU_Conf_BaseInfo，
        //    // 输出类型：BVCU_Conf_BaseInfo + BVCU_Conf_Participator_Info 同名notify

        //    //以下所有BVCU_SUBMETHOD_CONF_*命令除非特别说明，否则BVCU_Command.szTargetID设置为CONF_ID@CMS(@CMS可以省略，表示目前登录的CMS）
        //    BVCU_SUBMETHOD_CONF_DELETE,//删除会议。输入类型：无;输出类型:无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_CONF_START,//开始会议。BVCU_Command.iTargetIndex==1表示多组同时监听。输入类型：无;输出类型:无；触发类型：BVCU_SUBMETHOD_CONF_START_CMD
        //    BVCU_SUBMETHOD_CONF_STOP,//停止会议。输入类型：无;输出类型:无；触发类型：同名Notify

        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_ADD, //添加会议参与者。
        //    //输入类型：BVCU_Conf_Participator_Info数组，一次调用最多允许BVCU_CONF_MAX_PARTICIPATOR_ONETIME个。
        //    //输出类型：BVCU_Conf_Participator_AddResult
        //    //触发类型：BVCU_SUBMETHOD_CONF_PARTICIPATOR_ADD_CMD（被加入者收到）和BVCU_SUBMETHOD_CONF_PARTICIPATOR_ADD_NOTIFY（所有参与者收到）

        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_REMOVE,//删除会议参与者。输入类型：BVCU_Conf_Participator_Info数组；输出类型：无；触发类型：同名Notify
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_MODIFY,//修改会议参与者。输入类型：BVCU_Conf_Participator_Info数组；输出类型：无；触发类型：同名Notify

        //    //以下由BVCU_CONF_PARTICIPATOR_POWER_MODETATOR权限者调用
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_INVITE_SPEAK,//点名发言。仅对BVCU_CONF_MODE_SPEAK_CHAIRMAN模式的会议有意义。
        //    //输入类型：BVCU_Conf_Participator_Info；输出类型：无
        //    //触发类型：同名Notify(所有参与者收到)
        //    //  BVCU_SUBMETHOD_CONF_PARTICIPATOR_INVITE_SPEAK_CMD(仅被点名者收到)
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_TERMINATE_SPEAK,//结束发言。输入类型：BVCU_Conf_Participator_Info；输出类型：无;触发类型：同名Notify
        //    //以下由participator调用
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_JOIN,//请求加入会议。输入类型：BVCU_Conf_Participator_Join；输出类型：无；
        //    //触发类型：BVCU_SUBMETHOD_CONF_PARTICIPATOR_JOIN_NOTIFY（所有参与者收到）
        //    //            BVCU_SUBMETHOD_CONF_PARTICIPATOR_JOIN_CMD(仅会议ADMIN收到）
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_EXIT,//请求退出会议。输入类型：无；输出类型：无；触发类型：BVCU_SUBMETHOD_CONF_PARTICIPATOR_EXIT_NOTIFY    
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_STARTSPEAK,//申请发言，仅对BVCU_CONF_MODE_SPEAK_CHAIRMAN模式的会议有意义。输入类型：无;输出类型：无；
        //    //触发类型：BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_STARTSPEAK_NOTIFY    （所有参与者收到）
        //    //          BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_STARTSPEAK_CMD(仅会议ADMIN收到）
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_ENDSPEAK,//结束发言，仅对BVCU_CONF_MODE_SPEAK_CHAIRMAN模式的会议有意义。输入类型：无;输出类型：无；
        //    //触发类型：BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_ENDSPEAK_NOTIFY    
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_LEAVE,//暂时离开会议。输入类型：无；输出类型：无；
        //    //触发类型：BVCU_SUBMETHOD_CONF_PARTICIPATOR_LEAVE_NOTIFY
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_RETURN,//与LEAVE相对，回到会议。BVCU_Command.iTargetIndex==1表示多组同时监听。输入类型：BVCU_Conf_Participator_Info;输出类型：无：
        //    //触发类型：BVCU_SUBMETHOD_CONF_PARTICIPATOR_RETURN_NOTIFY
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_INFO,//更改了自己的本地信息(目前仅szAliasName有意义)，通知CMS。输入类型：BVCU_Conf_Participator_Info；输出类型：无;触发类型：同名Notify
        //    BVCU_SUBMETHOD_CONF_VISITOR_JOIN,   //请求旁听会议。输入类型：无；输出类型：BVCU_Conf_Participator_Info
        //    /*BVCU_Command.szTargetID设置为CONF_ID@CMS(@CMS可以省略，表示目前登录的CMS）\CU_ID\PU_ID\PU_Group
        //      BVCU_Command.iTargetIndex设置为目标的ApplierID（PU\PU_Group\CONF填0）*/
        //    BVCU_SUBMETHOD_IM_MSG = 0x22620, // 发送即时消息。输入类型：BVCU_IM_Msg数组 输出类型：BVCU_IM_Msg数组，用于获取各消息ID。消息接收方收到同名Notify。

        //    //存储文件部分---------------------------
        //    /*BVCU_Search_FileFilter 中szDesc1，szDesc2作为修改字段，其余都作为过滤条件*/
        //    BVCU_SUBMETHOD_RECORDFILE_DESC_SET = 0x22630,  //设置文件记录的描述。输入类型：BVCU_Search_FileFilter，输出类型:无
        //    BVCU_SUBMETHOD_RECORDFILE_DELETE,          //删除指定条件的文件记录以及文件。输入类型：BVCU_NRUCFG_DeleteFile，输出类型:BVCU_NRUCFG_DeleteFileResult

        //    //事件联动部分---------------------------
        //    BVCU_SUBMETHOD_LINKACTION_ADD  = 0x22700,//添加报警联动。输入类型：BVCU_Event_LinkAction;输出类型：无;
        //    BVCU_SUBMETHOD_LINKACTION_SET,//修改报警联动。输入类型：BVCU_Event_LinkAction;输出类型：无;
        //    BVCU_SUBMETHOD_LINKACTION_DEL,//删除报警联动。输入类型：BVCU_Event_LinkAction_Base;输出类型：无;触发类型：同名Notify
        //    BVCU_SUBMETHOD_EVENT_PROCESS,//处警。输入类型：BVCU_Event_SourceSaved;输出类型：无

        //    //0x22800部分-------------------------------
        //    BVCU_SUBMETHOD_RTSP = 0x22800,//回放会话RTSP控制命令。输入类型：BVCU_CTRL_RTSP;输出类型：BVCU_CTRL_RTSP;接收者先回命令，再发新数据。

        //    //电子围栏&线路规划部分------------------
        //    BVCU_SUBMETHOD_REGION_ADD = 0x22900, //添加CMS区域。输入类型：BVCU_Region_Info;输出类型：无;
        //    BVCU_SUBMETHOD_REGION_SET, //修改CMS区域。输入类型：BVCU_Region_Info;输出类型：无;
        //    BVCU_SUBMETHOD_REGION_DEL, //删除CMS区域。输入类型：BVCU_Region_Base;输出类型：无;触发类型：同名Notify

        //    //UKey管理------------------
        //    BVCU_SUBMETHOD_UKEY_ADD = 0x23000, //添加授权UKey。输入类型：BVCU_UCFG_UKeyInfo;输出类型：无;
        //    BVCU_SUBMETHOD_UKEY_SET, //修改授权Ukey。输入类型：BVCU_UCFG_UKeyInfo;输出类型：无;
        //    BVCU_SUBMETHOD_UKEY_DEL, //删除授权UKey。输入类型：BVCU_UCFG_UKeyBase;输出类型：无;

        //    //=============subscribe/notify=============
        //    //调用者应检查BVCU_NotifyMsgContent.szSourceID，确定信息的发出者
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_ADD_NOTIFY = BVCU_SUBMETHOD_CONF_PARTICIPATOR_ADD,//负载BVCU_Conf_Participator_Info
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_JOIN_NOTIFY = BVCU_SUBMETHOD_CONF_PARTICIPATOR_JOIN,//负载BVCU_Conf_Participator_Info
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_EXIT_NOTIFY = BVCU_SUBMETHOD_CONF_PARTICIPATOR_EXIT,//负载BVCU_Conf_Participator_Info
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_STARTSPEAK_NOTIFY = BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_STARTSPEAK,//负载BVCU_Conf_Participator_Info
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_ENDSPEAK_NOTIFY = BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_ENDSPEAK,//负载BVCU_Conf_Participator_Info
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_LEAVE_NOTIFY = BVCU_SUBMETHOD_CONF_PARTICIPATOR_LEAVE,//负载BVCU_Conf_Participator_Info
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_RETURN_NOTIFY = BVCU_SUBMETHOD_CONF_PARTICIPATOR_RETURN,//负载BVCU_Conf_Participator_Info        

        //    BVCU_SUBMETHOD_SYNCHRONIZATION_NOTIFY = BVCU_SUBMETHOD_PU_SYNCHRONIZATION, //同步图片（下载PU上传的图片）。负载：BVCU_PUCFG_Picture

        //    BVCU_SUBMETHOD_LINKACTION_NOTIFY = 0x30000,//负载BVCU_Event_LinkAction_Notify
        //    BVCU_SUBMETHOD_EVENT_NOTIFY,//负载BVCU_Event_Source

        //    //=============收到的command=======================
        //    //应用程序在Session的OnCmmand回调中收到命令，处理后配置BVCU_Event_SessionCmd参数并执行命令的OnEvent回调，BVCU_Event_SessionCmd.iResult表示命令执行结果。
        //    //注意：不可以在OnCommand回调中执行OnEvent回调。
        //    //注释说明，输入负载表示接收的命令负载，回响负载表示OnEvent的BVCU_Event_SessionCmd.stContent的负载类型
        //    BVCU_SUBMETHOD_CONF_START_CMD = BVCU_SUBMETHOD_CONF_START, //会议开始，询问是否参加。一个Pariticipator一次只能参加一个Start会议
        //    //输入负载：BVCU_Conf_BaseInfo；回响负载：无
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_ADD_CMD = BVCU_SUBMETHOD_CONF_PARTICIPATOR_ADD,//被邀请加入会议。
        //    //输入负载：BVCU_Conf_BaseInfo+本参与者的BVCU_Conf_Participator_Info
        //    //回响负载：如果同意加入，负载BVCU_Conf_Participator_Info（应用程序应填写szAliasName），不同意为空
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_JOIN_CMD = BVCU_SUBMETHOD_CONF_PARTICIPATOR_JOIN,//用户请求加入，仅会议Admin收到。
        //    //输入负载：BVCU_Conf_Participator_Info；回响负载：无
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_STARTSPEAK_CMD = BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_STARTSPEAK,//用户请求发言，，会议Admin通过返回成功与否决定是否同意。负载BVCU_Conf_Participator_Info
        //    BVCU_SUBMETHOD_CONF_PARTICIPATOR_INVITE_SPEAK_CMD = BVCU_SUBMETHOD_CONF_PARTICIPATOR_INVITE_SPEAK,//被点名发言。输入负载：BVCU_Conf_Participator_Info；回响负载：无

        //    BVCU_SUBMETHOD_PU_OPENDIALOG_CMD = BVCU_SUBMETHOD_PU_OPENDIALOG, // PU请求建立会话。应用层回同意后，由应用层调用BVCU_Dialog_Open()创建会话。
        //    // 输入负载：BVCU_PUCFG_OpenDialog； 回响负载：无

        //    BVCU_SUBMETHOD_USER_KICKOUT_CMD = BVCU_SUBMETHOD_USER_KICKOUT,  // 当前用户被踢出。应用层只需提示用户被踢出，回复该命令后，库内部做下线处理。
        //    // 输入负载：BVCU_UCFG_Kickout； 回响负载：无

        //    //=============reserved=============
        //    BVCU_SUBMETHOD_RESERVED = 0x80000//之后的值内部使用

        //    };


        /*CU发出/接收的命令。在BVCU_SendCmd中用到*/
        //typedef enum _BVCU_METHOD_TYPE{
        //    BVCU_METHOD_UNKNOWN  = 0,  //未知
        //    BVCU_METHOD_QUERY,         //查询   
        //    BVCU_METHOD_CONTROL,       //控制
        //    BVCU_METHOD_SUBSCRIBE,     //订阅

        //    BVCU_METHOD_RESERVED = 0x80000,//保留供内部使用
        //    }BVCU_METHOD_TYPE;


        var szMethod = {
            0: ["BVCU_METHOD_UNKNOWN","未知命令"],
            1: ["BVCU_METHOD_QUERY", "查询命令"],
            2: ["BVCU_METHOD_CONTROL", "控制命令"],
            3: ["BVCU_METHOD_SUBSCRIBE", "订阅命令"],
            0x80000: [ "BVCU_METHOD_RESERVED", "保留供内部使用" ]
        },
            szSubMethod = {
                0: ["BVCU_METHOD_UNKNOWN", "未知命令"],
                0x00001: ["BVCU_SUBMETHOD_CUSTOM", "自定义命令，服务器透明转发该命令给目标。"],
                //    //注意，BVCU_CmdMsgContent.pData被认为是字节类型，BVCU_CmdMsgContent.iDataCount表示字节长度

                //    //=============query====================    
                //    //PU部分-----------------------------
                0x0000a: ["BVCU_SUBMETHOD_PU_LIST", "CU从Server获取设备列表。"],
                0x0000b: ["BVCU_SUBMETHOD_PU_BROADCASTSTATUS", "获取广播的设备列表状态(一个Session只能同时有一个广播)。"],
                0x0000c: ["BVCU_SUBMETHOD_PU_GPSDATA", "获取PU的GPS通道数据。"],
                0x0000d: ["BVCU_SUBMETHOD_PU_STORAGE_MEDIA", "获取某个存储器的分区信息。"],
                0x0000e: ["BVCU_SUBMETHOD_PU_GROUPLIST", "获取设备分组列表。"],
                0x0000f: ["BVCU_SUBMETHOD_PU_GROUPINFO", "获取设备分组信息。"],
                0x00010: ["BVCU_SUBMETHOD_PU_UPDATESTATUS", "升级固件的状态。"],
                0x00012: ["BVCU_SUBMETHOD_PU_POSITION", "基站、wifi定位信息。n"],
                0x00013: ["BVCU_SUBMETHOD_PU_UserList", "获取平台设备使用者列表。"],
                0x00014: ["BVCU_SUBMETHOD_PU_UserInfo", "获取平台设备使用者信息。"],
                0x00015: ["BVCU_SUBMETHOD_PU_UserGroupList", "获取平台设备使用者组列表。"],
                0x00016: ["BVCU_SUBMETHOD_PU_UserGroupInfo", "获取平台设备使用者组信息。"],

                //    //User部分---------------------------
                0x01000: ["BVCU_SUBMETHOD_USER_GROUPLIST", "获取用户组列表。"],
                0x01001: ["BVCU_SUBMETHOD_USER_GROUPINFO", "获取用户组信息。"],
                0x01002: ["BVCU_SUBMETHOD_USER_USERLIST", "获取用户列表。"],
                0x01003: ["BVCU_SUBMETHOD_USER_USERINFO", "获取用户信息。"],
                0x01004: ["BVCU_SUBMETHOD_USER_ONLINE", "获取在线用户列表。"],
                0x01005: ["BVCU_SUBMETHOD_USER_ONLINEINFO", "获取在线用户信息。"],

                //    //VTDU部分----------------------------
                0x01300: ["BVCU_SUBMETHOD_VTDU_LIST", "获取VTDU列表。"],

                //    //NRU部分----------------------------
                0x01400: ["BVCU_SUBMETHOD_NRU_LIST", "获取NRU列表。"],
                0x01401: ["BVCU_SUBMETHOD_NRU_SCHEDULE_LIST", "某个NRU存储计划列表。"],
                0x01402: ["BVCU_SUBMETHOD_NRU_SCHEDULE_GET", "获NRU的一条存储计划。"],

                //    //Decoder部分------------------------
                0x01500: ["BVCU_SUBMETHOD_DEC_LIST", "获取解码器列表。"],
                //    BVCU_SUBMETHOD_DEC_LIST = 0x01500,//获取解码器列表。输入类型：无；输出类型：BVCU_DECCFG_BaseInfo数组

                //    //CONF部分---------------------------
                0x01600: ["BVCU_SUBMETHOD_CONF_LIST", "获取所有会议列表，BVCU_Command.szTargetID设置为CMS ID。"],
                0x01601: ["BVCU_SUBMETHOD_CONF_INFO", "获取某个会议详细信息，BVCU_Command.szTargetID设置为CONF_ID@CMS(@CMS可以省略，表示目前登录的CMS）。"],
                //    //BVCU_Command.iTargetIndex：0-查询所有会议列表；1-查询自己所在会议列表；2-查询自己不在的会议列表；3-查询有管理权限的会议列表。

                //    //ALARM部分--------------------------
                0x01700: ["BVCU_SUBMETHOD_LINKACTION_LIST", "获取报警联动列表。"],
                0x01701: ["BVCU_SUBMETHOD_LINKACTION_GET", "获取某个报警联动详细信息。"],

                //    //Search部分---------------------------
                //    // BVCU_Command.szTargetID设置为CMS/NRU/VTDU ID。
                0x01800: ["BVCU_SUBMETHOD_SEARCH_LIST", "索引文件（记录等）。"],
                0x01801: ["BVCU_SUBMETHOD_SYSTEM_INFO", "统及状态信息。"],

                //    //Region部分-------------------------
                0x1900: ["BVCU_SUBMETHOD_REGION_LIST", "获取CMS区域列表。"],
                0x1901: ["BVCU_SUBMETHOD_REGION_GET", "获取某个CMS区域详细信息。"],

                //    //UKey部分---------------------------
                0x2000: ["BVCU_SUBMETHOD_UKEY_LIST", "获取授权UKey列表。组"],
                0x2001: ["BVCU_SUBMETHOD_UKEY_GET", "获取某个授权UKey详细信息。"],

                //    //CMS部分----------------------------
                0x2100: ["BVCU_SUBMETHOD_CMS_BASEINFO", "获取登录CMS基本信息。"],
                0x2101: ["BVCU_SUBMETHOD_CMS_DIALOGINFO", "获取登录CMS通道信息。"],
                0x2102: ["BVCU_SUBMETHOD_CMS_TIME", "获取登录CMS时间。"],

                //    //=============query/control=============    
                //    /*注意：query/control类型的命令，通常query和control的输入类型和输出类型是对称的，例如
                //    BVCU_SUBMETHOD_PTZATTR命令做Query时，输入类型无，输出类型是BVCU_PUCFG_PTZAttr，做Control
                //    时，输入类型BVCU_PUCFG_PTZAttr，输出类型无。以下注释中，只表明Control的参数，如果没有特别说明，     
                //    Query的参数输入/输出类型就是Control的输出/输入类型
                //    */
                //    //PU部分
                0x10000: ["BVCU_SUBMETHOD_PU_DEVICEINFO", "设备信息。"],
                0x10001: ["BVCU_SUBMETHOD_PU_CHANNELINFO", "某个PU的通道信息。"],
                0x10002: ["BVCU_SUBMETHOD_CMS_BASEINFO", "设备时间。"],
                0x10003: ["BVCU_SUBMETHOD_PU_SERVERS", "注册服务器。"],
                0x10004: ["BVCU_SUBMETHOD_PU_ETHERNET", "以太网。"],
                0x10005: ["BVCU_SUBMETHOD_PU_WIFI", "WIFI。"],
                0x10006: ["BVCU_SUBMETHOD_PU_RADIONETWORK", "无线网络。"],
                0x10007: ["BVCU_SUBMETHOD_PU_PTZATTR", "云台属性。"],
                0x10008: ["BVCU_SUBMETHOD_PU_ENCODERCHANNEL", "编码通道属性。；"],
                0x10009: ["BVCU_SUBMETHOD_PU_VIDEOIN", "视频输入属性。"],
                0x1000a: ["BVCU_SUBMETHOD_PU_AUDIOIN", "音频输入属性。"],
                0x1000b: ["BVCU_SUBMETHOD_PU_AUDIOOUT", "音频输出属性"],
                0x1000c: ["BVCU_SUBMETHOD_PU_ALERTIN", "报警输入属性。"],
                0x1000d: ["BVCU_SUBMETHOD_PU_ALERTOUT", "报警输出属性。无；"],
                0x1000e: ["BVCU_SUBMETHOD_PU_SERIALPORT", "编串口属性。"],
                0x1000f: ["BVCU_SUBMETHOD_PU_GPS", "GPS属性。输入类型："],
                0x10010: ["BVCU_SUBMETHOD_PU_STORAGE_SCHEDULE", "存储计划。"],
                0x10011: ["BVCU_SUBMETHOD_PU_STORAGE_RULE", "存储属性。 "],
                0x10012: ["BVCU_SUBMETHOD_PU_STORAGE_FORMAT", "初始化某个存储器。 "],
                0x10013: ["BVCU_SUBMETHOD_PU_ONLINECONTROL", "上下线控制。"],
                0x10014: ["BVCU_SUBMETHOD_PU_SNAPSHOTPARAM", "配置自动抓拍参数。"],
                0x10015: ["BVCU_SUBMETHOD_PU_POWER", "配置电源参数。"],
                0x10016: ["BVCU_SUBMETHOD_PU_GPSSPEEDLIMIT", "配置设备全局限速。无"],
                0x10017: ["BVCU_SUBMETHOD_PU_CARINFO", "设备相关的车辆信息（如：车载设备）。"],
                0x10018: ["BVCU_SUBMETHOD_PU_BLUETOOTH", "设备蓝牙信息。 "],
                0x10019: ["BVCU_SUBMETHOD_PU_VIDEOIN_CMOS", "视频输入CMOS属性。"],
                0x1001a: ["BVCU_SUBMETHOD_PU_ZFYINFO", "设备相关的执法记录仪信息。"],

                //    //VTDU部分
                0x10F00: ["BVCU_SUBMETHOD_VTDU_INFO", "VTDU信息。"],

                //    //NRU部分
                0x11000: ["BVCU_SUBMETHOD_NRU_INFO", "NRU信息。"],

                //    //Decoder部分------------------------
                //    //BVCU_Command.szTargetID设置为DEC_ID@CMS(@CMS可以省略，表示目前登录的CMS）
                0x11100: ["BVCU_SUBMETHOD_DEC_DEVICEINFO", "解码器详细信息。"],
                0x11100: ["BVCU_SUBMETHOD_DEC_OPTIONS", "解码器设置选项。"],
                0x11100: ["BVCU_SUBMETHOD_DEC_DECODER", "解码器解码通道配置。"],
                0x11100: ["BVCU_SUBMETHOD_DEC_DISPLAY", "解码器显示通道配置。"],

                //    //CONF部分
                //    //BVCU_Command.szTargetID设置为CONF_ID@CMS(@CMS可以省略，表示目前登录的CMS）
                0x11200: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_VOLUME", "调整某个参与者的音量。注意只影响本地听到的音量。"],
                0x11201: ["BVCU_SUBMETHOD_CONF_BASEINFO", "获取/设置会议的信息，BVCU_Command.szTargetID设置为CONF_ID@CMS(@CMS可以省略，表示目前登录的CMS）。"],

                //    //=============control=============    
                //    //PU部分---------------------------
                0x20000: ["BVCU_SUBMETHOD_PU_REBOOT", "重启设备。"],

                0x20001: ["BVCU_SUBMETHOD_PU_DELETE", "从数据库中删除PU。输"],
                0x20002: ["BVCU_SUBMETHOD_PU_SHUTDOWN", "关闭设备。"],
                0x20003: ["BVCU_SUBMETHOD_PU_SAVECONFIG", "通知设备保存配置。"],
                0x20004: ["BVCU_SUBMETHOD_PU_PTZCONTROL", "操作云台。 "],

                0x20005: ["BVCU_SUBMETHOD_PU_PUTOFFLINE", "手工使PU下线。"],
                0x20006: ["BVCU_SUBMETHOD_PU_MANUALRECORD", "手工启动/停止PU录像。"],
                0x20007: ["BVCU_SUBMETHOD_PU_SNAPSHOT", "手工远程抓拍。"],
                0x20008: ["BVCU_SUBMETHOD_PU_UPGRADE", "升级固件。"],

                0x20009: ["BVCU_SUBMETHOD_PU_ADDGROUP", "添加设备分组。"],
                0x2000a: ["BVCU_SUBMETHOD_PU_MODGROUP", "修改设备分组。"],
                0x2000b: ["BVCU_SUBMETHOD_PU_DELGROUP", "删除设备分组。"],
                0x2000c: ["BVCU_SUBMETHOD_PU_OPENDIALOG", "请求创建会话。"],

                0x2000d: ["BVCU_SUBMETHOD_PU_SYNCHRONIZATION", "同步图片（上传未上传的抓图，下载CU上传的图片）。"],
                0x2000e: ["BVCU_SUBMETHOD_PU_USER_ADDGROUP", "添加设备使用者组。"],
                0x2000f: ["BVCU_SUBMETHOD_PU_USER_MODGROUP", "修改设备使用者组。"],
                0x20010: ["BVCU_SUBMETHOD_PU_USER_DELGROUP", "删除设备使用者组。"],

                0x20011: ["BVCU_SUBMETHOD_PU_USER_ADDUSER", "添加设备使用者。"],
                0x20012: ["BVCU_SUBMETHOD_PU_USER_MODUSER", "修改设备使用者。"],
                0x20013: ["BVCU_SUBMETHOD_PU_USER_DELUSER", "删除设备使用者。"],
                0x20014: ["BVCU_SUBMETHOD_PU_USER_OPERATE", "绑定或解绑设备。"],

                //    //User部分---------------------------
                0x22001: ["BVCU_SUBMETHOD_USER_ADDGROUP", "添加用户组。"],
                0x22002: ["BVCU_SUBMETHOD_USER_MODGROUP", "修改用户组。"],
                0x22003: ["BVCU_SUBMETHOD_USER_DELGROUP", "删除用户组。"],
                0x22004: ["BVCU_SUBMETHOD_USER_ADDUSER", "添加用户。"],
                0x22005: ["BVCU_SUBMETHOD_USER_MODUSER", "修改用户。"],
                0x22006: ["BVCU_SUBMETHOD_USER_DELUSER", "删除用户。"],
                0x22007: ["BVCU_SUBMETHOD_USER_MODPASSWD", "修改用户密码。"],
                0x22008: ["BVCU_SUBMETHOD_USER_KICKOUT", "踢出在线用户。"],

                //    //NRU部分---------------------------
                0x22401: ["BVCU_SUBMETHOD_NRU_DELETE", "从数据库中删除NRU。"],
                0x22402: ["BVCU_SUBMETHOD_NRU_SCHEDULE_SET", "配置NRU的一条存储计划。"],
                0x22403: ["BVCU_SUBMETHOD_NRU_SCHEDULE_DEL", "删除NRU的一条存储计划。"],
                0x22404: ["BVCU_SUBMETHOD_NRU_MANUALRECORD", "手工启动/停止某个PU录像到NRU。"],
                0x22405: ["BVCU_SUBMETHOD_NRU_SNAPSHOT", "手工抓拍到NRU。"],
                0x22406: ["BVCU_SUBMETHOD_NRU_BACKUPDB", "手工备份数据库。"],

                //    //CONF部分---------------------------
                //    //以下由具有BVCU_CONF_PARTICIPATOR_POWER_ADMIN权限者调用
                0x22600: ["BVCU_SUBMETHOD_CONF_CREATE", "创建会议，BVCU_Command.szTargetID设置为CMS ID。"],

                //    //以下所有BVCU_SUBMETHOD_CONF_*命令除非特别说明，否则BVCU_Command.szTargetID设置为CONF_ID@CMS(@CMS可以省略，表示目前登录的CMS）
                0x22601: ["BVCU_SUBMETHOD_CONF_DELETE", "删除会议。"],
                0x22602: ["BVCU_SUBMETHOD_CONF_START", "开始会议。"],
                0x22603: ["BVCU_SUBMETHOD_CONF_STOP", "停止会议。"],
                0x22604: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_ADD", "添加会议参与者。"],
                0x22605: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_REMOVE", "删除会议参与者。"],
                0x22606: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_MODIFY", "修改会议参与者。"],

                //    //以下由BVCU_CONF_PARTICIPATOR_POWER_MODETATOR权限者调用
                0x22607: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_INVITE_SPEAK", "点名发言。"],
                0x22608: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_TERMINATE_SPEAK", "结束发言。"],

                //    //以下由participator调用
                0x22609: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_JOIN", "请求加入会议。"],
                0x2260a: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_EXIT", "请求退出会议。"],

                0x2260b: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_STARTSPEAK", "申请发言。"],
                0x2260c: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_APPLYFOR_ENDSPEAK", "结束发言。"],

                0x2260d: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_LEAVE", "暂时离开会议。"],
                0x2260e: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_RETURN", "返回会议。"],

                0x2260f: ["BVCU_SUBMETHOD_CONF_PARTICIPATOR_INFO", "更改了自己的本地信息(目前仅szAliasName有意义)，通知CMS。。"],
                0x22610: ["BVCU_SUBMETHOD_CONF_VISITOR_JOIN", "请求旁听会议。"],

                0x22620: ["BVCU_SUBMETHOD_IM_MSG", "发送即时消息。"],

                //    //存储文件部分---------------------------
                //    /*BVCU_Search_FileFilter 中szDesc1，szDesc2作为修改字段，其余都作为过滤条件*/
                0x22630: ["BVCU_SUBMETHOD_RECORDFILE_DESC_SET", "设置文件记录的描述。"],
                0x22631: ["BVCU_SUBMETHOD_RECORDFILE_DELETE", "删除指定条件的文件记录以及文件。"],

                //    //事件联动部分---------------------------
                0x22700: ["BVCU_SUBMETHOD_LINKACTION_ADD", "添加报警联动。"],
                0x22701: ["BVCU_SUBMETHOD_LINKACTION_SET", "修改报警联动。"],
                0x22702: ["BVCU_SUBMETHOD_LINKACTION_DEL", "删除报警联动。"],
                0x22703: ["BVCU_SUBMETHOD_EVENT_PROCESS", "处警。"],

                //    //0x22800部分-------------------------------
                0x22800: ["BVCU_SUBMETHOD_RTSP", "回放会话RTSP控制命令。"],

                //    //电子围栏&线路规划部分------------------
                0x22901: ["BVCU_SUBMETHOD_REGION_ADD", "添加CMS区域。"],
                0x22902: ["BVCU_SUBMETHOD_REGION_SET", "修改CMS区域。"],
                0x22903: ["BVCU_SUBMETHOD_REGION_DEL", "删除CMS区域。"],

                //    //UKey管理------------------
                0x23001: ["BVCU_SUBMETHOD_UKEY_ADD", "添加授权UKey。"],
                0x23002: ["BVCU_SUBMETHOD_UKEY_SET", "修改授权Ukey。"],
                0x23003: ["BVCU_SUBMETHOD_UKEY_DEL", "删除授权UKey。"],

                0x30000: ["BVCU_SUBMETHOD_LINKACTION_NOTIFY", "负载BVCU_Event_LinkAction_Notify。"],
                0x30001: ["BVCU_SUBMETHOD_EVENT_NOTIFY", "载BVCU_Event_Sourc。"],

                //    //=============reserved=============
                0x80000: ["BVCU_SUBMETHOD_RESERVED", "之后的值内部使用。"],
            };

        this.parseCmd = function (iMethod, iSubMethod) {
            var sResult = {
                method: szMethod[iMethod] ? szMethod[iMethod][1] : "unknow" + iMethod,
                subMethod: szSubMethod[iSubMethod] ? szSubMethod[iSubMethod][1] : "unknow" + iSubMethod
            }
            return sResult;
        }
    }));
})(jSW.DependencyMgr);