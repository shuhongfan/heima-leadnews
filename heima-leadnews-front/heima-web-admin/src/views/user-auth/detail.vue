<template>
  <div class="container">
    <div class="header">
      <el-page-header @back="goBack" content="实名认证"></el-page-header>
      <div v-if="realname.status.toString() === '1'">
        <el-button type="primary" class="el-button--has-icon" @click="operateForPass">
          <svg class="icon svg-icon" aria-hidden="true">
            <use xlink:href="#iconicon_btn_pass" />
          </svg>
          通过
        </el-button>
        <el-button type="danger" class="el-button--has-icon" @click="operateForFail">
          <svg class="icon svg-icon" aria-hidden="true">
            <use xlink:href="#iconicon_btn_reject" />
          </svg>
          驳回
        </el-button>
      </div>
      <div v-else-if="realname.status.toString() === '9'" class="operate">
        <span class="status">状态：</span>
        <span class="pass">审核通过</span>
      </div>
      <div v-else-if="realname.status.toString() === '2'" class="operate">
        <span class="status">状态：</span>
        <span class="fail">已驳回</span>
        <br />
        <span class="reason">理由：{{ realname.reason }}</span>
      </div>
    </div>
    <div class="main">
      <el-row :gutter="30">
        <el-col :span="4">
          <span class="name">姓名</span>
          <span class="value">{{ realname.name }}</span>
        </el-col>
        <el-col :span="20">
          <span class="name">身份证号</span>
          <span class="value">{{ realname.idno }}</span>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="9">
          <user-auth-detail-image :imgSrc="realname.fontImage" />
        </el-col>
        <el-col :span="9">
          <user-auth-detail-image :imgSrc="realname.backImage" />
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="9">
          <i class="triangle">▲</i>身份证正面
        </el-col>
        <el-col :span="9">
          <i class="triangle">▲</i>身份证背面
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="9">
          <user-auth-detail-image :imgSrc="realname.holdImage" />
        </el-col>
        <el-col :span="9">
          <user-auth-detail-image :imgSrc="realname.liveImage" />
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="9">
          <i class="triangle">▲</i>手持身份证
        </el-col>
        <el-col :span="9">
          <i class="triangle">▲</i>人脸识别信息
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { authPass, authFail } from '@/api/user-auth'
import UserAuthDetailImage from './components/detail-image'
import myMixin from '@/utils/mixins'

export default {
  name: 'RealnameAuth',
  mixins: [myMixin],
  components: {
    UserAuthDetailImage
  },
  data () {
    return {
      realname: {},
      id: ''
    }
  },
  created () {
    this.realname = this.$route.query
    this.id = this.realname.id
  },
  methods: {
    async operateForPass () {
      const { code, data, errorMessage } = await authPass({ id: this.id })

      if (code === 0) {
        this.realname.status = data.status
        this.$message({ type: 'success', message: '操作成功' })
      } else {
        this.$message({ type: 'error', message: errorMessage })
      }
    },
    async operateForFail () {
      try {
        const result = await this.showPrompt()

        const { code, data, errorMessage } = await authFail({ id: this.id, msg: result.value })
        if (code === 0) {
          this.realname.status = data.status
          this.realname.reason = data.reason
          this.$message({ type: 'success', message: '操作成功！' })
        } else {
          this.$message({ type: 'error', message: errorMessage })
        }
      } catch (err) {
        this.$message({ type: 'info', message: '已取消驳回' })
      }
    },
    goBack () {
      this.goBackPage()
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.container {
  height: 100%;
  padding: 30px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 22px;
    border-bottom: 1px solid #ebeef5;

    .operate {
      text-align: right;

      .status {
        height: 20px;
        color: $--color-text-secondary;
        line-height: 20px;
      }

      .pass {
        height: 26px;
        padding: 3px 7px;
        border-radius: 4px;
        background: #ebf7f2;
        color: $--color-success;
        line-height: 20px;
      }

      .fail {
        height: 26px;
        padding: 3px 8px;
        border-radius: 4px;
        background: #ffefef;
        color: $--color-danger;
        line-height: 20px;
      }

      .reason {
        display: inline-block;
        height: 17px;
        margin-top: 6px;
        color: $--color-danger;
        line-height: 17px;
      }
    }
  }

  .main {
    min-height: calc(100vh - 80px - 30px - 89px);
    padding-top: 30px;
    text-align: left;

    .name {
      margin-right: 20px;
      color: $--color-text-secondary;
    }

    .triangle {
      color: $--color-primary;
      // TODO: 线上样式和本地不一致，可能是webpack配置导致
      font-family: 'Avenir', Helvetica, Arial, sans-serif;
    }
  }
}
</style>
