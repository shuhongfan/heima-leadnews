<template>
  <div class="container">
    <div class="header">
      <el-page-header @back="goBack" content="文章详情"></el-page-header>
      <div v-if="detail.status === 3">
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
      <div v-else-if="detail.status === 8" class="operate">
        <span class="status">状态：</span>
        <span class="pass">审核通过</span>
      </div>
      <div v-else-if="detail.status === 2" class="operate">
        <span class="status">状态：</span>
        <span class="fail">已驳回</span>
        <br />
        <span class="reason">理由：{{ detail.reason }}</span>
      </div>
    </div>
    <div class="main">
      <el-row :gutter="30">
        <el-col :span="5">
          <span class="name">文章标题</span>
        </el-col>
        <el-col :span="5">
          <span class="name">作者</span>
        </el-col>
        <el-col :span="5">
          <span class="name">更新时间</span>
        </el-col>
      </el-row>
      <el-row :gutter="30" style="margin-top:20px;">
        <el-col :span="5">
          <span class="value">{{ detail.title }}</span>
        </el-col>
        <el-col :span="5">
          <span class="value">{{ detail.authorName }}</span>
        </el-col>
        <el-col :span="5">
          <span class="value">{{ detail.submitedTime | dateTimeFormat }}</span>
        </el-col>
      </el-row>
      <el-row :gutter="30" style="margin-top:50px;">
        <el-col :span="5">
          <span class="name">正文信息</span>
        </el-col>
      </el-row>
      <p v-for="(item, index) in JSON.parse(detail.content)" :key="index">
        <span v-if="item.type === 'text'" style="color: #17233e;">{{ item.value }}</span>
        <img v-else :src="item.value" />
      </p>
    </div>
  </div>
</template>

<script>
import { authOne, authPass, authFail } from '@/api/news'
import myMixin from '@/utils/mixins'

export default {
  name: 'NewsDetail',
  mixins: [myMixin],
  data () {
    return {
      id: '',
      detail: {
        content: '[]'
      }
    }
  },
  created () {
    this.id = this.$route.query.id
    this.loadData()
  },
  methods: {
    async loadData () {
      const { data } = await authOne(this.id)
      this.detail = data
    },
    async operateForPass () {
      const { code, errorMessage } = await authPass({ id: this.detail.id, status: 4 })
      if (code === 0) {
        this.loadData()
        this.$message({ type: 'success', message: '操作成功！' })
      } else {
        this.$message({ type: 'error', message: errorMessage })
      }
    },
    async operateForFail () {
      try {
        const result = await this.showPrompt()

        const { code, errorMessage } = await authFail({ id: this.detail.id, status: 2, msg: result.value })
        if (code === 0) {
          this.loadData()
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
      // margin-right: 20px;
      color: $--color-text-secondary;
    }

    // .triangle {
    //   color: $--color-primary;
    // }
  }
}
</style>
