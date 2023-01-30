<template>
    <div class="login-wapper">
        <image class="img" src="/static/images/login/login2.png"/>
        <div class="title">手机号登录</div>
        <div class="bg-wapper">
            <div class="input-wapper">
                <input v-model="params.phone" return-key-type="defalut"
                       autocomplete="off"
                       placeholder="手机号"
                       class="input"
                />
                <text class="getcode">获取验证码</text>
            </div>
            <div class="input-wapper">
                <input v-model="params.password" return-key-type="go"
                       autocomplete="off"
                       type="password"
                       placeholder="验证码"
                       class="input"
                />
            </div>
            <text class="button" @click="login"> 开始使用 </text>
            <div class="more">
                <!--<text class="go-register" @click="tip">密码登录</text>-->
                <text class="go-register" @click="tip">手机号注册</text>
                <text class="empty"></text>
                <text class="go-register" @click="noLogin">不登录，先看看</text>
               <!-- <router-link to="/home">
                    <text class="go-register">不登录，先看看</text>
                </router-link>-->
            </div>
            <div class="other">
                <image class="omg" src="/static/images/login/sina.png"/>
                <image class="omg" src="/static/images/login/wechat.png"/>
                <image class="omg" src="/static/images/login/qq.png"/>
            </div>
            <text class="concat">隐私条款</text>
        </div>
        <div class="empty"></div>
    </div>
</template>

<script>
    import Api from '@/apis/login/api'
    import TopBar from '@/compoents/bars/login_top_bar'
    const modal = weex.requireModule('modal')
    export default {
        name: "login",
        components:{TopBar},
        data(){
            return{
                userIcon : '\uf007',
                passIcon : '\uf023',
                params:{
                    phone:'13511223456',
                    password:'admin'
                }
            }
        },
        created(){
            Api.setVue(this);
        },
        methods:{
            tip : function(){
                modal.toast({ message:'该功能暂未实现！',duration:3})
            },
            login:function(){
                if(this.params.phone==''||this.params.password==''){
                    modal.toast({
                        message:'请输入用户名或密码',
                        duration:3
                    })
                }else{
                    Api.login(this.params).then(d=>{
                        if(d.code==0){
                            this.$store.setToken(d.data.token)
                            this.$router.push("/home")
                        }else{
                            modal.toast({ message:'用户或密码错误',duration:3})
                        }
                    }).catch(e=>{
                        console.log(e)
                    })
                }
            },
            noLogin:function () {
                this.params.phone ='';
                this.params.password ='';
                Api.login(this.params).then(d=>{
                    if(d.code==0){
                        this.$store.setToken(d.data.token)
                        this.$router.push("/home")
                    }
                }).catch(e=>{
                    console.log(e)
                })
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .getcode{
        color: @placeholder-color;
        font-size: 20px;
        border-bottom-width: 1px;
        border-bottom-color: @placeholder-color;
        border-top-width: 1px;
        border-top-color: @placeholder-color;
        border-left-width: 1px;
        border-left-color: @placeholder-color;
        border-right-width: 1px;
        border-right-color: @placeholder-color;
        border-top-left-radius: 20px;
        border-top-right-radius: 20px;
        border-bottom-left-radius: 20px;
        border-bottom-right-radius: 20px;
        padding: 10px;
    }
    .title{
        color: #666666;
        font-size: 36px;
        font-weight: bold;
        padding-left: 30px;
    }
    .login-wapper {
        flex: 1;
        width: 750px;
        flex-direction: column;
        background-color: #ffffff;
    }
    .bg-wapper{
        margin-top: 35px;
        margin-bottom: 35px;
        width: 750px;
        justify-content: center;
        align-items: center;
    }
    .empty{
        flex: 1;
        background-color: #ffffff;
    }
    .icon{
        color: @icon-color;
        font-size: 32px;
    }
    .input-wapper {
        flex-direction: row;
        width: 700px;
        border-bottom-width: 1px;
        border-bottom-color: #cccccc;
        padding: 15px 0px;
        align-items: center;
        margin: 15px 0px;
    }
    .more{
        width: 720px;
        margin-top: 35px;
        flex-direction: row;
        align-items: stretch;
        margin-left: 25px;
    }
    .go-register{
        font-size: 24px;
        color: #666666;
        margin-right: 35px;
    }
    .go-home{
        font-size: 24px;
        color: @placeholder-color;
        text-decoration: underline;
    }
    .input{
        border: none;
        flex: 1;
        line-height: 30px;
        font-size: 28px;
        color: @title-color;
        background-color: transparent;
        placeholder-color:@placeholder-color;
    }
    .input :active,.input :hover{
        background-color: transparent;
    }
    .button{
        margin-top:60px;
        background-color: #6db4fb;
        width: 690px;
        height: 70px;
        border-radius: 50px;
        color: @bg-white;
        font-size: 32px;
        text-align: center;
        line-height: 70px;
    }
    .img{
        width: 750px;
        height: 390px;
        margin-top: -2px;
    }
    .other{
        flex-direction: row;
        margin-top: 120px;
    }
    .omg{
        width: 88px;
        height: 88px;
        margin: 10px;
    }
    .concat{
        font-size: 24px;
        color: @placeholder-color;
    }
</style>
