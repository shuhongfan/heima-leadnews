<template>
    <el-dialog
      width="849px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :visible.sync="syncShowPicDialog"
      :show-close="false"
      :center="true"
      @open="open"
    >
      <!-- TODO: UI提出宽度需要调整 -->
      <el-switch
        v-model="activeName"
        :width="151"
        active-text="本地上传"
        inactive-text="素材库"
        active-color="#F3F4F7"
        inactive-color="#F3F4F7"
      ></el-switch>
      <!-- 素材库 -->
      <!-- TODO: 1.样式调整，全部和收藏数量 2.数据为空的占位图 -->
      <div v-show="!activeName">
        <div style="display: flex;">
          <el-tabs tab-position="left" v-model="activeName2" @tab-click="handleTabClick">
            <el-tab-pane label="全部" name="all"></el-tab-pane>
            <el-tab-pane label="收藏" name="collect"></el-tab-pane>
          </el-tabs>
          <div class="img_list_con">
            <div
              class="img_list"
              v-for="item in imgData"
              :key="item.id"
              @click="selectPic(item.id, item.url)"
            >
              <img :src="item.url" />
              <img
                :src="item.id === selectedImg.id ? selectedImgUrl : unselectedImgUrl"
                class="checkbox_img"
              />
            </div>
          </div>
        </div>

        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getList" />
      </div>
      <!-- 本地上传 -->
      <div v-show="activeName">
        <upload :dialogVisible="syncShowPicDialog" @uploadSuccess="uploadSuccess" />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="warning" class="el-button--no-icon" @click="cancleImg">取 消</el-button>
        <el-button type="success" class="el-button--no-icon" @click="btnOKImg">确 定</el-button>
      </span>
    </el-dialog>
</template>

<script>
import Pagination from '@/components/pagination'
import Upload from '@/components/Upload/upload.vue'
import { getAllImgData } from '@/api/publish'

export default {
  name: 'MaterialSelectDialog',
  components: { Pagination, Upload },
  props: ['showPicDialog'],
  data () {
    return {
      activeName: false,
      activeName2: 'all',
      imgData: [],
      total: 0,
      listQuery: {
        page: 1,
        size: 10
      },
      selectedImg: {},
      selectedImgUrl: require('@/assets/news/checkbox_sel@2x.png'),
      unselectedImgUrl: require('@/assets/news/checkbox_nor@2x.png')
    }
  },
  computed: {
    syncShowPicDialog: {
      get () {
        return this.showPicDialog
      },
      set (val) {
        this.$emit('update:showPicDialog', val)
      }
    }
  },
  methods: {
    open () {
      this.listQuery.page = 1
      this.getList()
    },
    handleTabClick () {
      this.listQuery.page = 1
      this.getList()
    },
    // 拉取所有的图片数据
    async getList (page) {
      const isCollect = this.activeName2 === 'collect'
      const result = await getAllImgData({
        page: this.listQuery.page,
        size: this.listQuery.size,
        isCollection: isCollect ? 1 : 0
      })
      this.imgData = result.data
      this.total = result.total
    },
    // 选择一张图片
    selectPic (id, url) {
      this.selectedImg = { id, url }
    },
    // 上传成功后
    uploadSuccess (url) {
      this.selectedImg = { url }
    },
    // 插入图片 或者替换封面图片
    btnOKImg () {
      this.$emit('btnOKImg', this.selectedImg.url)
      this.syncShowPicDialog = false
    },
    // 取消插入
    cancleImg () {
      this.syncShowPicDialog = false
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.tinymce-container {
  padding: 30px;
  text-align: left;

  .el-form {
    .title {
      /deep/ .el-input__inner {
        padding: 0;
        padding-bottom: 19px;
        border: none;
        border-bottom: 2px solid #ebeef5;
        border-radius: 0;
        line-height: 26px;
        font-size: 20px;
        color: $--color-text-placeholder;
      }
    }

    // .el-form-item {
    //   margin: 20px 0;
    // }

    .btn {
      border-top: 2px solid #ebeef5;
      // margin: 0 15px;
      // padding: 30px 0;
      padding-top: 31px;
      margin-bottom: 0;
    }
  }
}

.img_list {
  width: 155px;
  height: 103px;
  float: left;
  margin: 0px auto;
  border: 1px solid #eee;
  overflow: hidden;
  border-radius: 4px;
  margin: 0px 18px 16px 0;
  position: relative;
}

.img_list_con {
  flex: 1;
  overflow: hidden;
  margin-left: 30px;
  max-height: 340px;
}

.checkbox_img {
  position: absolute;
  top: 6px;
  right: 6px;
  width: 32px !important;
  height: 33px !important;
}

.img_list img {
  width: 155px;
  height: 103px;
  display: block;
  cursor: pointer;
  object-fit: cover;
}

.pagination {
  text-align: center;
}

// TODO: el-switch样式和内容封装到组件里
/deep/ .el-switch {
  height: auto;
  margin-bottom: 30px;
}

/deep/ .el-switch__label {
  position: absolute;
  color: $--color-text-secondary;
}

/deep/ .el-switch__label.is-active {
  color: $--color-text-primary;
  font-weight: 600;
}

/deep/ .el-switch__label--left {
  left: 0;
  margin-left: 16px;
  z-index: 9999;
}

/deep/ .el-switch__label--right {
  right: 0;
  margin-right: 10px;
}

/deep/ .el-switch__core {
  height: 40px;
  border-radius: 4px;
}

/deep/ .el-switch__core:after {
  width: 66px;
  height: 32px;
  top: 3px;
  left: 3px;
  box-shadow: 0px 1px 4px 0px rgba(0, 0, 0, 0.06);
  border-radius: 4px;
}

/deep/ .el-switch.is-checked .el-switch__core::after {
  margin-left: 76px;
  left: 3px;
}

/deep/ .el-dialog__footer {
  border-top: 2px solid #EBEEF5;
}
</style>
