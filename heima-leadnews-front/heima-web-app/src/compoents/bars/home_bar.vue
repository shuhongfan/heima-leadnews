<template>
    <div class="bar_bg">
        <image class="img" src="/static/images/buttons/xiaoxi.png"/>
        <Search type="search" @onClick="onClick" img="/static/images/buttons/search_0.png" left-width="40" rightWidth="20" placeholder="请输入搜索关键字..."/>
        <image class="img" src="/static/images/buttons/jia.png"/>
    </div>
</template>

<script>
    import Search from '@/compoents/inputs/search_buttion';
    export default {
        name: "index",
        components: { Search},
        data:()=>{
            return {
                icon:'\uF002'
            }
        },
        mounted(){
            this.backgroundColor = this.$config.style.main_bg;
        },
        methods: {
            onClick : function(){
                this.$router.push('/search')
            },
            minibarLeftButtonClick () {
            },
            minibarRightButtonClick () {
            }
        }
    };
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .bar_bg{
        width: @screen-width;
        flex-direction: row;
        background-color:#ffffff;
        border-style: solid;
        height: @top-height;
        padding: 9px 15px 9px;
        align-items: center;
    }
    .icon{
        width: 25px;
        color: #ffffff;
        font-family: fontawesome;
        font-size: 32px;
    }
    .img{
        width: 37px;
        height: 40px;
        margin-left: 10px;
        margin-right: 10px;
    }
</style>
