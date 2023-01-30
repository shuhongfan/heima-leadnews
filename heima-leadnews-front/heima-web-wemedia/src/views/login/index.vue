<template>
  <div class="wrapper">
    <div class="login-box clearfix">
      <div class="inline-block welcome-box fl">
        <div class="welcome">欢迎使用</div>
        <div class="project">黑马头条自媒体人管理系统</div>
        <img class="block img" src="@/assets/login/welcome@2x.png" />
      </div>
      <div class="inline-block form-box fr">
        <img class="block heima-img" src="@/assets/login/logo_index@2x.png" />
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="login-form">
          <el-form-item class="input-label name-input-label">用户名 / user name</el-form-item>
          <el-form-item prop="name" class="name-input-box">
            <el-input
              type="text"
              v-model="ruleForm.name"
              autocomplete="off"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
          <el-form-item class="input-label password-input-label">密码 / password</el-form-item>
          <el-form-item prop="password" class="password-input-box">
            <el-input
              type="password"
              v-model="ruleForm.password"
              autocomplete="off"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
          <el-form-item prop="signAgreement" class="signagreement-input-box">
            <el-checkbox v-model="ruleForm.signAgreement">我已阅读并同意用户协议和隐私条款</el-checkbox>
          </el-form-item>
          <el-form-item class="loginBtn" :class="validateState ? 'enabled' : ''">
            <el-button :disabled="!validateState" @click="submitForm('ruleForm')">登 录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { loginByUsername } from '@/api/login'
export default {
  data () {
    var validateSignAgreement = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请阅读并同意协议'))
      } else {
        callback()
      }
    }
    return {
      ruleForm: {
        name: 'itheima',
        password: '123456',
        signAgreement: true
      },
      rules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'change' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'change' }
        ],
        signAgreement: [
          { validator: validateSignAgreement, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    validateState () {
      return this.ruleForm.name !== '' && this.ruleForm.password !== '' && this.ruleForm.signAgreement
    }
  },
  methods: {
    async submitForm () {
      try {
        await this.$refs.ruleForm.validate()
        const { password, name } = this.ruleForm

        const res = await loginByUsername(name, password)
        if (res.code === 0) {
          this.$router.replace({ path: '/statistics/index' })
        } else {
          this.$message({
            message: res.errorMessage,
            type: 'error'
          })
        }
      } catch (err) {
        console.log('err: ' + err)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
// TODO: 颜色按照规范调整
.wrapper {
  background-image: url('~@/assets/login/login_bg@2x.png');
  background-size: cover;
  background-repeat: no-repeat;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;

  .login-box {
    width: 1070px;
    height: 594px;
    background: #FFFFFF;
    box-shadow: 0px 0px 30px 15px rgba(25, 25, 99, 0.06);
    border-radius: 18px;

    .welcome-box {
      width: 642px;
      height: 594px;
      padding-left: 121px;
      box-sizing: border-box;
      background: linear-gradient(180deg, #597EF7 0%, #3175FB 100%);
      border-radius: 18px 0px 0px 18px;

      .welcome {
        height: 56px;
        margin-top: 69px;
        font-size: 40px;
        font-family: PingFangSC-Thin, PingFang SC;
        font-weight: 100;
        color: #FFFFFF;
        line-height: 56px;
      }

      .project {
        height: 45px;
        font-size: 32px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #FFFFFF;
        line-height: 45px;
      }

      .img {
        width: 331px;
        height: 239px;
        margin-top: 86px;
      }
    }

    .form-box {
      width: 428px;
      height: 594px;
      background: #FFFFFF;
      border-radius: 0px 18px 18px 0px;

      .heima-img {
        width: 172px;
        height: 48px;
        margin: 69px auto 0;
      }

      .login-form {
        width: 300px;
        margin: 0 auto;
        text-align: center;

        .input-label {
          /deep/ .el-form-item__content {
            font-size: 16px;
            font-weight: 400;
            color: #BAC0CD;
            line-height: 22px;
          }
        }

        .name-input-label {
           margin-top: 44px;
        }

        .name-input-box {
          margin-top: 13px;
        }

        .password-input-label {
           margin-top: 24px;
        }

        .password-input-box {
          margin-top: 13px;
        }

        .signagreement-input-box {
          margin-top: 40px;

          /deep/ .el-form-item__content {
            line-height: 20px;
          }

          /deep/ .el-checkbox__label {
            height: 20px;
            font-size: 14px;
            font-weight: 400;
            color: #BAC0CD;
            line-height: 20px;
          }
        }

        /deep/ .el-input__inner {
          width: 300px;
          height: 50px;
          background: #F3F4F7;
          border-radius: 30px;
          border: 1px solid #D5E6FF;
          text-align: center;
          line-height: 48px;
        }

        /deep/ .el-form-item .el-input__inner:hover,
        /deep/ .el-form-item .el-input__inner:focus {
          border-color: #3175FB;
        }

        /deep/ .el-form-item.is-error .el-input__inner {
          border-color: #FF5C5C;
        }

        .loginBtn {
          /deep/ .el-button {
            width: 300px;
            height: 60px;
            margin-top: 18px;
            background: #BFD0F2;
            border-radius: 30px;
            font-size: 18px;
            font-weight: 400;
            color: #FFFFFF;
          }
        }

        .enabled {
          /deep/ .el-button {
            background: #3175FB;
          }
        }

        /deep/ .el-form-item__error {
          width: 100%;
        }
      }
    }
  }
}
</style>
