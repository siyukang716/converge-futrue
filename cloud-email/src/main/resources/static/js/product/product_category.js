
var baseUrl = '/product/category/',actiontype='';
$(function () {
    layui.use(['form'],function(){
        var form = layui.form;
        form.on('submit(addSubmit)', function (data) {
           clearForm('categoryForm');
           var zTreeObj = $.fn.zTree.getZTreeObj("categoryZTree");
           var srcNode = zTreeObj.getSelectedNodes();
           actiontype = 'add';
           var level = srcNode[0].level;
           formAssignment('categoryForm',{
               'categoryId' : '',
               'parentId' : srcNode[0].categoryId,
               'categoryName' : '',
               'categoryCode' : '',
               'categoryStatus' : '',
               'categoryLevel' : level+1

           })
            return false;
        });
        form.on('submit(saveSubmit)', function (data) {
            var zTreeObj = $.fn.zTree.getZTreeObj("categoryZTree");
            var srcNode = zTreeObj.getSelectedNodes();
            var operating = srcNode[0].operating;
            if (operating != false ||  actiontype != 'up')
                submitForm(baseUrl+'aOrU','','categoryForm',$('#categoryForm').serialize(),'GET',saveCallback);
            return false;
        });
        form.on('submit(deleteSubmit)', function (data) {
            // var zTreeObj = $.fn.zTree.getZTreeObj("categoryZTree")
            // var srcNode = zTreeObj.getSelectedNodes();
            // if (srcNode[0].isParent)
            //     layer.msg('不能删除有子分类数据!!!!', {icon: 5});
            // else
                submitForm(baseUrl+'del','','categoryForm',{"categoryId":getFormVal('categoryForm').categoryId},'GET',delCallback);
            return false;
        });

    });
    var setting = {
        data: {
            simpleData: {
                enable: true,
                idKey: 'categoryId',
                pIdKey: 'parentId',
                rootPId: 0
            }
        }, view: {
            showLine: false,
            showIcon: true,
        }, async: {
            enable: true,
            type: "post",
            dataType: 'json',
            url: baseUrl+'ztree',
            autoParam: ['categoryId', 'parentId']
        }, callback: {
            onClick: zTreeOnClick
        }
    };
    $.fn.zTree.init($("#categoryZTree"), setting, {
        'categoryId' : '0',
        'isParent' : true,
        'name' : '商品分类',
        'parentId' : 'aaa',
        'categoryLevel' : '1',
        'categoryStatus' : '1',
        'operating' : false
    });
});

function zTreeOnClick(event, treeId, treeNode) {
    actiontype = 'up';
    var pNode = treeNode.getParentNode();
    if (pNode != null)
        echoForm(baseUrl+'getById',{'categoryId':treeNode.categoryId},'categoryForm','GET');
    else
        formAssignment('categoryForm',{
            'categoryId' : '0',
            'parentId' : '000',
            'categoryName' : '商品分类',
            'categoryCode' : '0000',
            'categoryStatus' : '',
            'categoryLevel' : ''

        });

};
function echoFormCallback(data,formId){
    formAssignment(formId,data.data);
}
function saveCallback(data) {
    var newNode = [{categoryId: data.data.categoryId, parentId: data.data.parentId, name: data.data.categoryName}];
    if (actiontype == 'add') {
        clearForm('categoryForm')
        addNode(newNode);
    } else {
        renameTreeNode(data.data.categoryName);
    }
}
function delCallback() {
    clearForm('categoryForm')
    removeTreeNode();
}
//添加节点
function addNode(node) {
    var zTreeObj = $.fn.zTree.getZTreeObj("categoryZTree");
    var selectedNodes = zTreeObj.getSelectedNodes();
    zTreeObj.addNodes(selectedNodes[0], node);
    zTreeObj.selectNode(node);
}

//重命名节点
function renameTreeNode(txtObj) {
    var zTreeObj = $.fn.zTree.getZTreeObj("categoryZTree");
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
    var zTreeObj = $.fn.zTree.getZTreeObj("categoryZTree")
    var srcNode = zTreeObj.getSelectedNodes();
    zTreeObj.removeNode(srcNode[0]);
}