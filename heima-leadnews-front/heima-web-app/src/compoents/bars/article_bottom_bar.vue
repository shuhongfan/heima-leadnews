<template>
    <div class="bar_bg">
        <Search icon="" value="" returnType="send" @onSubmit="onSubmit" rightWidth=25 placeholder="写评论" />
        <image class="img" src="/static/images/buttons/commint.png" @click="goToComment"/>{{comment > 0 ? comment : ''}}
        <image v-if="collection" class="img collection" @click="clickCollection" src="/static/images/buttons/conlloction_active.png"/>
        <image v-else class="img collection" @click="clickCollection" src="/static/images/buttons/conlloction.png"/>
        <image v-if="forward" class="img forward" @click="clickForward" src="/static/images/buttons/share.png"/>
        <image v-else class="img forward" @click="clickForward" src="/static/images/buttons/share.png"/>
    </div>
</template>

<script>
    import Search from '@/compoents/inputs/search';

    const dom = weex.requireModule('dom')

    export default {
        name: "article_bottom_bar",
        components: {Search},
        props:{
            collection:{
                type:Boolean,
                default:false
            },
            forward:{
                type:Boolean,
                default:false
            },
            comment:{
                type:Number,
                default:0
            }
        },
        methods:{
            clickCollection : function(){
                this.$emit("clickCollection",{})
            },
            clickForward : function(){
                this.$emit("clickForward",{})
            },
            onSubmit : function(val){
                this.$emit("onSubmit", val)
            },
            // 让页面滚动到评论区
            goToComment: function() {
                dom.scrollToElement(this.$parent.$refs.comment, {})
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .bar_bg{
        width: @screen-width;
        flex-direction: row;
        align-items:center;
        border-width:1px;
        border-color: @border-color;
        background-color: @bg-white;
        border-style: solid;
        height: @top-height;
        padding-top: 4px;
        padding-left: 5px;
    }
    .icon{
        color: #a5a5a5;
        font-size: 48px;
        width: 80px;
    }
    .img{
        width: 42px;
        height: 40px;
        margin-top: 4px;
    }
    .collection{
        margin-left: 23px;
        margin-top: 0px;
    }
    .forward{
        margin-left: 20px;
        margin-right: 20px;
    }
</style>
