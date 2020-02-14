<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0,user-scalable=no,minimal-ui">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../img/asset-favico.ico">
    <title>预约</title>
    <link rel="stylesheet" href="../../css/page-health-order.css" />
    <link rel="stylesheet" href="../../plugins/elementui/index.css">
    <link rel="stylesheet" href="../../plugins/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="../../css/style.css">
</head>
<body data-spy="scroll" data-target="#myNavbar" data-offset="150">
<div class="app" id="app">
    <!-- 页面头部 -->
    <div class="top-header">
        <span class="f-left"><i class="icon-back" onclick="history.go(-1)"></i></span>
        <span class="center">传智健康</span>
        <span class="f-right"><i class="icon-more"></i></span>
    </div>
    <!-- 页面内容 -->
    <div class="contentBox">
        <div class="list-column1">
            <ul class="list">
                <#list setmealList as setmeal>
                <li class="list-item" >
                    <!--重点:页面跳转-->
                    <a class="link-page" href="setmeal_detail_${setmeal.id}.html">
                        <img class="img-object f-left" src="http://q5biab01l.bkt.clouddn.com/${setmeal.img}" alt="">
                        <div class="item-body">
                            <h4 class="ellipsis item-title">${setmeal.name?default("")}</h4><#--?default("")解决null的问题if标签里面要这样写?default("")?trim?length gt 1-->
                            <p class="ellipsis-more item-desc">${setmeal.remark?default("")}</p>
                            <p class="item-keywords">
                                <span><#--视频老师另一种写法-->
                                    <#if setmeal.sex=='0'>
                                        性别不限
                                    </#if>
                                    <#if setmeal.sex=='1'>
                                        男
                                    </#if>
                                    <#if setmeal.sex=='2'>
                                        女
                                    </#if>
                                </span>
                                <span>${setmeal.age?default("")}</span>
                            </p>
                        </div>
                    </a>
                </li>
                </#list>
            </ul>
            <div class="pagination-container" style="float: right;background: white;width: 100%;">
                <el-pagination
                        small
                        class="pagiantion"
                        @current-change="handleCurrentChange"
                        :current-page="pagination.currentPage"
                        :page-size="pagination.pageSize"
                        layout="total, prev, pager, next, jumper"
                        :total="pagination.total">
                </el-pagination>
            </div>
        </div>

    </div>

</div>
<!-- 页面 css js -->
<script src="../../plugins/vue/vue.js"></script>
<script src="../../plugins/vue/axios-0.18.0.js"></script>
<script src="../../plugins/elementui/index.js"></script>
<!--<script src="../js/page-health-order.js"></script>-->
<script>
    var vue = new Vue({
        el:'#app',
        data:{
            pagination: {//分页相关模型数据
                currentPage: ${currentPage},//当前页码
                pageSize:4,//每页显示的记录数
                total:${total},//总记录数
                queryString:null//查询条件
            },
            setmealList:[]
        },
        methods:{
            handleCurrentChange(currentPage) {
                window.location.href="m_setmeal_"+currentPage+".html"
            }
        },
    });
</script>
</body>