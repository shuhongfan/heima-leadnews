<template>
    <div class="cell-body">
        <img class="logo" src="/static/images/buttons/search_1.png"/>
        <text class="title" @click="onClickText">{{title}}</text>
        <img class="img" @click="onDeleteHistory" src="/static/images/buttons/delete_0.png"></img>
    </div>
</template>

<script>
    export default {
        name: "search_0",
        props:{
            id:{
                type:String,
                default:''
            },
            title:{
                type:String,
                default:'显示字'
            },
            icon:{
                type:String,
                default:'\uf017'
            }
        },
        methods : {
            onDeleteHistory : function(){
                this.$emit("onDeleteHistory",this.id)
            },
            onClickText :function(){
                this.$emit("onClickText",this.title)
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .cell-body{
        flex-direction: row;
        font-size: 36px;
        align-items: center;
        border-bottom-color: #e8e8e8;
        border-bottom-width: 1px;
        border-bottom-style: solid;
        padding: 17px 20px;
        background-color: #ffffff;
        height: 86px;
    }
    .title{
        flex: 1;
        color: #333333;
        font-family: "微软雅黑";
        font-size: 32px;
    }
    .icon{
        font-size: 40px;
        color: #ebebeb;
        width: 45px;
        margin-right: 10px;
    }
    .img{
        width: 33px;
        height: 26px;
    }
    .logo{
        width: 22px;
        height: 22px;
        margin-right: 25px;
    }
</style>
