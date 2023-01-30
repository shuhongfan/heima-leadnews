<template>
    <wxc-minibar title=""
                 :background-color="backgroundColor"
                 text-color="#FFFFFF"
                 :useDefaultReturn="useDefaultReturn"
                 @wxcMinibarLeftButtonClicked="minibarLeftButtonClick"
                 @wxcMinibarRightButtonClicked="minibarRightButtonClick">
        <text class="icon" slot="left" style="text-align: left">&#xf104;</text>
        <text class="icon" slot="right" style="text-align: left" @click="noAction">···</text>
    </wxc-minibar>
</template>

<script>
    import { WxcMinibar } from 'weex-ui';

    export default {
        name: "article_top_bar",
        components: { WxcMinibar},
        props:{
          text:{
              type:String,
              default:'文章标题'
          }
        },
        data:()=>{
            return {
                backgroundColor:'#ffffff',
                useDefaultReturn:false
            }
        },
        mounted(){
            //this.backgroundColor = this.$config.style.main_bg;
        },
        computed:{
            getText:function(){
                let temp = this.text;
                if(this.text.length>12){
                    temp = this.text.substring(0,11)+'...';
                }
                return temp;
            }
        },
        methods: {
            noAction:function () {
                this.$config.noAction()
            },
            minibarLeftButtonClick () {
                this.$router.back()
            },
            minibarRightButtonClick () {
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .icon{
        font-size: 38px;
        color: #000000;
    }
</style>
