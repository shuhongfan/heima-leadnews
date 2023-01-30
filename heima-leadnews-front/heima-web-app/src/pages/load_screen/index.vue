<template>
    <div class="wapper" ref="bg">
        <image class="img" src="/static/images/load_screen.png"/>
    </div>
</template>

<script>
    export default {
        name: "load_screen",
        created() {
            let _this = this
            setTimeout(function(){
               _this.goLogin()
            },2000)
        },
        methods : {
            goLogin : function () {
                let _this = this
                let animation = weex.requireModule('animation')
                animation.transition(this.$refs.bg, {
                    styles: {
                        opacity: '0',
                    },
                    duration: 100, //ms
                    timingFunction: 'ease',
                    needLayout:false,
                    delay: 0 //ms
                },function(){
                    _this.$router.push("/login")
                })
            }
        }
    }
</script>

<style scoped>
    .wapper{
        flex: 1;
        width: 750px;
    }
    .img{
        flex: 1;
        width: 750px;
    }
</style>
