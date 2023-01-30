<template>
    <div class="t-body">
        <text class="icon" style="color: red">{{icon}}</text>
        <text class="title">{{title}}</text>
        <text class="icon">&#xf105;</text>
    </div>
</template>

<script>
    export default {
        name: "title",
        props:{
            title:{
                type:String,
                default:'显示字'
            },
            icon:{
                type:String,
                default:'\uf06d'
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .t-body{
        flex-direction: row;
        font-size: 36px;
        align-items: center;
        border-bottom-color: #ebebeb;
        border-bottom-width: 1px;
        border-bottom-style: solid;
        padding: 18px 20px;
        background-color: #ffffff;
        margin-top: 15px;
    }
    .title{
        flex: 1;
        color: #222222;
        font-weight: bold;
    }
    .icon{
        color: #ebebeb;
        font-size: 40px;
        margin-right: 10px;
        width: 38px;
    }
</style>
