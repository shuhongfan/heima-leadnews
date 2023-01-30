<template>
    <div class="body" @click="onClick" :style="getBodyStyle">
        <div class="bg-ops" :style="getBodyStyle"></div>
        <image v-if="img!=''" class="img" :src="img"></image>
        <text v-else class="icon" :style="getIconStyle">{{icon}}</text>
        <text class="text">热搜：十三届全国人大二次会议</text>
    </div>
</template>

<script>
    export default {
        props:{
            flex:{
                type:Number,
                default:1
            },
            height:{
                type:Number,
                default:70
            },
            icon:{
              type:String,
              default:'\uf044'
            },
            img:{
                type:String,
                default:''
            }
        },
        computed:{
            getBodyStyle:function(){
                return {
                    borderColor:this.color,
                    'flex':this.flex,
                    'height':this.height+'px'
                }
            },
            getIconStyle:function(){
                return {
                    width:(this.height-30)+"px",
                    height:(this.height-10)+"px",
                    lineHeight:(this.height-10)+"px",
                    fontSize:(this.height/70)*32+"px"
                }
            }
        },
        methods:{
            onClick : function(){
                if(this.inpValue!=''){
                    this.$emit('onClick', {});
                }
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .body{
        flex-direction: row;
        justify-content: center;
        align-items: center;
        margin-left: 20px;
        margin-right: 20px;
    }
    .img{
        width: 32px;
        height: 32px;
        margin-right: 10px;
    }
    .bg-ops{
        flex: 1;
        position: absolute;
        top: 0px;
        left: 0px;
        background-color: #f1f2f7;
        width: 570px;
        height: 30px;
        border-radius: @top-height - 30;
    }
    .icon{
        font-size: 28px;
        margin-right: 10px;
    }
    .text{
        font-family: MicrosoftYaHei;
        color: #b3b8bc;
        font-size: 28px;
    }
</style>
