<template>
  <div class="bgc">
    <div class="filter">
      <div class="filter-container">
        <el-switch
          v-model="listQuery.isCollection"
          :width="138"
          :active-value="1"
          :inactive-value="0"
          active-text="收藏"
          inactive-text="全部"
          active-color="#F3F4F7"
          inactive-color="#F3F4F7"
          @change="handleSwitchChange"
        ></el-switch>
      </div>
      <div>
        <span class="total">已上传{{ total }}张图片</span>
        <el-button type="success" class="el-button--has-icon" @click="showPicDialog = true">
          <svg class="icon svg-icon" aria-hidden="true">
            <use xlink:href="#iconicon_btn_tjsh" />
          </svg>
          上传图片
        </el-button>
      </div>
    </div>
    <div v-if="list.length" class="content-card">
      <el-card v-for="img in list" :key="img.id" :body-style="{ padding: '0px' }" shadow="hover">
        <img class="image" :class="{'collection': listQuery.isCollection == 1}" :src="img.url" />
        <div v-if="listQuery.isCollection == 0" class="operate">
          <div class="item" @click="collectOrCancel(img)">
            <svg class="icon svg-icon" aria-hidden="true">
              <use :xlink:href="img.isCollection ? '#iconbtn_collect_sel' : '#iconbtn_collect'" />
            </svg>
            {{ img.isCollection ? '已收藏' : '收藏' }}
          </div>
          <div class="item" @click="delImg(img)">
            <svg class="icon svg-icon" aria-hidden="true">
              <use xlink:href="#iconbtn_del" />
            </svg>删除
          </div>
        </div>
      </el-card>
    </div>
    <empty-data v-else />

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getList" />

    <el-dialog
      width="849px"
      center
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :visible.sync="showPicDialog"
      :show-close="false"
      :before-close="closeModal"
    >
      <upload :dialogVisible="showPicDialog" @uploadSuccess="uploadSuccess" />
      <div slot="footer" class="dialog-footer">
        <el-button type="warning" class="el-button--no-icon" @click="closeModal">关闭</el-button>
        <el-button type="success" class="el-button--no-icon" @click="closeModal">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import myMixin from '@/utils/mixins'
import Pagination from '@/components/pagination'
import EmptyData from '@/components/empty-data'
import Upload from '@/components/Upload/upload.vue'
import { getAllImgData, delImg, collectOrCancel } from '@/api/publish'

export default {
  name: 'MaterialList',
  mixins: [myMixin],
  components: {
    Pagination,
    Upload,
    EmptyData
  },
  data () {
    return {
      total: 0,
      listQuery: {
        page: 1,
        size: 20,
        isCollection: 0
      },
      imgChange: false, // 是否上传过图片导致图片数据变化 此状态用来控制是否在关闭后要进行重新加载
      showPicDialog: false,
      list: []// 存储图片的数据 同时作为收藏数据和全部数据的引用
    }
  },
  created () {
    this.getList()
  },
  methods: {
    async getList () {
      const result = await getAllImgData(this.listQuery)
      this.list = result.data
      this.total = result.total
    },
    // 取消或者收藏图片
    async collectOrCancel (img) {
      let isCollected = img.isCollection
      if (isCollected === 1) { isCollected = 0 } else { isCollected = 1 }
      // 取相反状态
      const result = await collectOrCancel(img.id, { isCollected: isCollected })
      if (result.code === 0) {
        img.isCollection = isCollected // 取相反状态
        this.$forceUpdate() // 强制更新
        this.$message({ type: 'success', message: '操作成功' })
      } else {
        this.$message({ type: 'error', message: result.errorMessage })
      }
    },
    // 删除图片
    async delImg (img) {
      try {
        await this.showDeleteConfirm('素材')

        const { code, errorMessage } = await delImg(img.id)
        if (code === 0) {
          this.$message({ type: 'success', message: '删除成功' })
          this.getList()
        } else {
          this.$message({ type: 'error', message: errorMessage })
        }
      } catch (err) {
        this.$message({ type: 'info', message: '已取消删除' })
      }
    },
    uploadSuccess () {
      this.imgChange = true
    },
    closeModal () {
      if (this.imgChange) {
        this.getList()
        this.imgChange = false
      }
      this.showPicDialog = false
    },
    handleSwitchChange () {
      this.listQuery.page = 1
      this.getList()
    }
  }
}
</script>
<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.filter {
  height: 96px;
}

/deep/ .el-switch {
  height: auto;
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
  margin-left: 22px;
  z-index: 9999;
}

/deep/ .el-switch__label--right {
  right: 0;
  margin-right: 23px;
}

/deep/ .el-switch__core {
  height: 40px;
  border-radius: 4px;
}

/deep/ .el-switch__core:after {
  width: 63px;
  height: 32px;
  top: 3px;
  left: 3px;
  box-shadow: 0px 1px 4px 0px rgba(0, 0, 0, 0.06);
  border-radius: 4px;
}

/deep/ .el-switch.is-checked .el-switch__core::after {
  margin-left: 65px;
  left: 3px;
}

.total {
  margin-right: 16px;
  color: $--color-text-secondary;
}

.content-card {
  display: flex;
  padding-left: 6px;
  padding-bottom: 30px;
  flex-wrap: wrap;

  .el-card {
    position: relative;
    width: 186px;
    height: 183px;
    margin-top: 30px;
    margin-left: 24px;
    border-radius: 8px;
    border: 1px solid $--background-color-base;

    .image {
      display: block;
      width: 100%;
      height: 124px;
      object-fit: cover;
    }

    .collection {
      height: 183px;
    }

    .operate {
      display: flex;
      height: 57px;
      justify-content: space-around;
      align-items: center;

      .item {
        display: flex;
        align-items: center;
        font-size: 12px;
        cursor: pointer;

        .icon {
          width: 30px;
          height: 30px;
        }
      }
    }
  }

  .el-card.is-always-shadow,
  .el-card.is-hover-shadow:focus,
  .el-card.is-hover-shadow:hover {
    -webkit-box-shadow: 0px 2px 8px 4px rgba(51, 53, 58, 0.07);
    box-shadow: 0px 2px 8px 4px rgba(51, 53, 58, 0.07);
  }
}

.el-pagination {
  border-top: 2px solid #EBEEF5;
}

/deep/ .el-dialog__footer {
  border-top: 2px solid #EBEEF5;
}
</style>
