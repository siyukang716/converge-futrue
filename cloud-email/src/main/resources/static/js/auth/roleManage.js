/**
 * 角色管理
 */



$(function () {
    layui.use(['form'], function () {
        var form = layui.form;
        form.on('submit(saveSubmit)', function (data) {
            $.ajax({
                url: '/role/saveOrUpdate',
                data: $("#roleForm").serialize(),
                cache: false,
                dataType: 'json',
                success: function (data) {
                    if (data.status == 200) {
                        layer.alert(data.message);
                        var newNode = [{id: data.data.id, pid: data.data.pid, name: data.data.roleName}];
                        if (actiontype == 'add') {
                            $("#roleForm")[0].reset();
                            addNode(newNode);
                        } else {
                            //$("#roleForm")[0].reset();
                            renameTreeNode(data.data.roleName);
                        }
                    } else {
                        layer.alert(data.message);
                    }
                },
                error: function (data) {

                }
            })
            return false;
        });
        form.on('submit(deleteSubmit)', function (data) {
            $.ajax({
                url: '/role/deleteById',
                data: {"id": $("#roleForm").find("#id").val()},
                cache: false,
                dataType: 'json',
                success: function (data) {
                    if (data.status == 200) {
                        $("#roleForm")[0].reset();
                        removeTreeNode();
                    } else {
                        layer.alert(data.message);
                    }
                },
                error: function (data) {

                }
            })
            return false;
        });


    });
    var setting = {
        data: {
            simpleData: {
                enable: true,
                idKey: 'id',
                pIdKey: 'pId',
                rootPId: 0
            }
        }, view: {
            showLine: false,
            showIcon: true,
        }, async: {
            enable: true,
            type: "post",
            dataType: 'json',
            url: '/ztree/getRole',
            autoParam: ['id', 'pId']
        }, callback: {
            onClick: zTreeOnClick
        }
    };
    $.ajax({
        url: '/ztree/getRole',
        data: {},
        cache: false,
        dataType: 'json',
        success: function (data) {
            $.fn.zTree.init($("#roleZTree"), setting, data);
        },
        error: function (data) {

        }
    })
});
var actiontype;

function zTreeOnClick(event, treeId, treeNode) {
    actiontype = 'update';
    var pNode = treeNode.getParentNode();
    $("#roleForm").find("#proleName").val(pNode == null?'':pNode.name);
    $.ajax({
        url: '/role/getObjById',
        data: {"id": treeNode.id},
        cache: false,
        dataType: 'json',
        success: function (data) {
            layui.use(['form'], function () {
                var form = layui.form;
                //给表单赋值
                form.val("roleForm", { //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
                    "roleName": data.data.roleName // "name": "value"
                    , "code": data.data.code
                    , "descpt": data.data.descpt
                    , "pid": data.data.pid
                    , "id": data.data.id
                });
            })
        },
        error: function (data) {

        }
    })
};

function insertSubmit() {
    actiontype = 'add';
    $("#roleForm")[0].reset();
    var zTreeObj = $.fn.zTree.getZTreeObj("roleZTree");
    var selectedNodes = zTreeObj.getSelectedNodes();
    $("#roleForm").find("#pid").val(selectedNodes[0].id);
    $("#roleForm").find("#proleName").val(selectedNodes[0].name);
}

//添加节点
function addNode(node) {
    var zTreeObj = $.fn.zTree.getZTreeObj("roleZTree");
    var selectedNodes = zTreeObj.getSelectedNodes();
    zTreeObj.addNodes(selectedNodes[0], node);
    zTreeObj.selectNode(node);
}

//重命名节点
function renameTreeNode(txtObj) {
    var zTreeObj = $.fn.zTree.getZTreeObj("roleZTree");
    var srcNode = zTreeObj.getSelectedNodes();
    if (!srcNode) {
        return;
    }
    if (srcNode[0].name != txtObj) {
        srcNode[0].name = txtObj;
        zTreeObj.updateNode(srcNode[0]);
        zTreeObj.selectNode(srcNode[0]);
    }
}

//删除节点
function removeTreeNode() {
    var zTreeObj = $.fn.zTree.getZTreeObj("roleZTree")
    var srcNode = zTreeObj.getSelectedNodes();
    zTreeObj.removeNode(srcNode[0]);
}