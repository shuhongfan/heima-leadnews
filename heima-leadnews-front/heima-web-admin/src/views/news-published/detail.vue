<template>
  <div class="container">
    <div class="header">
      <el-page-header @back="goBack" content="文章详情"></el-page-header>
      <div class="operate">
        <span class="status">状态：</span>
        <span :class="detail.enable ? 'pass' : 'fail'">{{ detail.enable ? '已上架': '已下架' }}</span>
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
import { authOne } from '@/api/news'
import myMixin from '@/utils/mixins'

export default {
  name: 'NewsPublishedDetail',
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
      color: $--color-text-secondary;
    }
  }
}
</style>
