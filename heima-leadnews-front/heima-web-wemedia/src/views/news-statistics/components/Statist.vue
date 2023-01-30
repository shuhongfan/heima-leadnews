<template>
  <div class="content">
    <el-row :gutter="40">
      <el-col :span="8">
        <div class="grid-content">
          <i></i>
          <div>
            <div>{{article}} 个</div>
            <span>图文发布量</span>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content">
          <i></i>
          <div>
            <div>{{likes}} 个</div>
            <span>点赞数量</span>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content">
          <i></i>
          <div>
            <div>{{collection}}</div>
            <span>收藏量</span>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  props: ['collection', 'likes', 'article'],
  components: {
  },
  computed: {

  },
  methods: {

  }
}
</script>

<style lang="scss" scoped>
.content {
  padding: 0 20px 20px;
}
.el-row {
  .el-col {
    .grid-content {
      background-color: #f8f8f8;
      padding: 30px 0;
      display: flex;
      justify-content: center;
      i {
        width: 50px;
        height: 50px;
        background-color: red;
        border-radius: 50%;
      }
      & > div {
        margin: 0px 0 0 10px;
        span {
          color: #444444;
          font-size: 14px;
        }
        div {
          font-size: 24px;
        }
      }
    }
  }
}

</style>
