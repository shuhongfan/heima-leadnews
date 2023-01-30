<template>
    <div class="login-wapper">
        <div class="log-top"><TopBar/></div>
        <image class="img" src="/static/images/login.png"/>
        <div class="bg-wapper">
            <div class="input-wapper">
                <text class="icon">{{userIcon}}</text>
                <input v-model="params.phone" return-key-type="defalut"
                       autocomplete="off"
                       placeholder="请输入手机号"
                       class="input"
                />
            </div>
            <div class="input-wapper">
                <text class="icon">{{passIcon}}</text>
                <input v-model="params.password" return-key-type="go"
                       autocomplete="off"
                       type="password"
                       placeholder="请输密码"
                       class="input"
                />
            </div>
            <text class="button" @click="login"> 登  录 </text>
            <div class="more">
                <text class="go-register" @click="tip">没有账号，去注册</text>
            <!--<router-link to="/home">-->
                <!--<text class="button"> 登  录 </text>-->
            <!--</router-link>-->
            </div>
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
                    phone:'admin',
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
                        if(d.code==200){
                            this.$store.setToken(d.data.token)
                            this.$router.push("/home")
                        }else{
                            modal.toast({ message:'用户或密码错误',duration:3})
                        }
                    }).catch(e=>{
                        console.log(e)
                    })
                }
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
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
    .log-top{
        width: 750px;
        height: 90px;
    }
    .empty{
        flex: 1;
        background-color: #ffffff;
    }
    .title{
        font-size: 52px;
        color: @title-color;
        margin: 55px 0px;
    }
    .icon{
        color: @icon-color;
        font-size: 32px;
    }
    .input-wapper {
        flex-direction: row;
        width: 700px;
        border-bottom-width: 1px;
        border-bottom-color: #eeeeee;
        padding: 15px 0px;
        align-items: center;
        margin: 15px 0px;
    }
    .more{
        margin-top: 35px;
        flex-direction: row;
    }
    .go-register{
        font-size: 24px;
        color: @placeholder-color;
        text-decoration: underline;
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
        margin-left: 20px;
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
        border-radius: 10px;
        color: @bg-white;
        font-size: 32px;
        text-align: center;
        line-height: 70px;
    }
    .img{
        width: 750px;
        height: 298px;
        margin-top: -2px;
        margin-bottom: 30px;
    }
</style>
