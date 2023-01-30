<template>
  <div class="bgc">
    <section class="filter">
      <div class="filter-container">
        <el-form ref="form" :inline="true" size="medium">
          <el-col>
            <el-form-item label="文章状态：">
              <el-radio-group v-model="listQuery.status" @change="resetPage">
                <el-radio v-for="(item, index) in statusList" :key="index" :label="item.label">{{ item.value }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col>
            <el-form-item label="关键字：">
              <el-input v-model="listQuery.keyword" placeholder="请输入关键字" style="width: 179px;" clearable @input="resetPage" />
            </el-form-item>
            <el-form-item label="频道列表：">
              <el-select v-model="listQuery.channelId" style="width: 179px;" clearable @change="resetPage">
                <el-option
                  v-for="item in channelList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="发布日期：">
              <el-date-picker
                type="daterange"
                v-model="date"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                style="width: 239px;"
                @change="handelDatePickerChange"
              />
            </el-form-item>
          </el-col>
        </el-form>
      </div>
    </section>

    <section class="result">
      <div v-if="list.length" class="content-card">
        <!-- TODO: hover时，按钮渐变动画 -->
        <div v-for="(item, index) in list" :key="index" class="item-card">
          <el-image :src="item.images ? getImage(item) : require('@/assets/news/pic_nopic.png')">
            <div slot="placeholder" class="image-slot">
              <img src="@/assets/news/pic_loading@2x.png" />
            </div>
            <div slot="error" class="image-slot">
              <img src="@/assets/news/pic_fail.png" />
            </div>
          </el-image>
          <!-- TODO: 抽出组件，样式细化 -->
          <div class="top">
            <div v-show="item.status == '9'&&item.enable=='0'" class="item" @click="operateBtn(item.id, 'up')">
              <el-tooltip effect="dark" content="上架" placement="top" visible-arrow="false">
                <svg class="icon svg-icon" aria-hidden="true">
                  <use xlink:href="#iconbtn_down" />
                </svg>
              </el-tooltip>
            </div>
            <div v-if="item.status == '9'&&item.enable=='1'" class="item" @click="operateBtn(item.id, 'down')">
              <el-tooltip effect="dark" content="下架" placement="top" visible-arrow="false">
                <svg class="icon svg-icon" aria-hidden="true">
                  <use xlink:href="#iconbtn_up" />
                </svg>
              </el-tooltip>
            </div>
            <div v-if="item.status != '9'||(item.status == '9'&&item.enable=='0')" class="item" @click="operateBtn(item.id, 'modify')">
              <el-tooltip effect="dark" content="编辑" placement="top" visible-arrow="false">
                <svg class="icon svg-icon" aria-hidden="true">
                  <use xlink:href="#iconbtn_edit" />
                </svg>
              </el-tooltip>
            </div>
            <div v-if="item.status != '9'||(item.status == '9'&&item.enable=='0')" class="item" @click="operateBtn(item.id, 'del')">
              <el-tooltip effect="dark" content="删除" placement="top" visible-arrow="false">
                <svg class="icon svg-icon" aria-hidden="true">
                  <use xlink:href="#iconbtn_del" />
                </svg>
              </el-tooltip>
            </div>
          </div>
          <div class="content">
            <div class="center">{{item.title}}</div>
            <div class="bottom">
              <time class="time">{{item.createdTime | dateFormat}}</time>
              <!-- TODO: 不同状态显示不同颜色 -->
              <span class="draft" v-if="item.status == '0'">草稿</span>
              <span class="audit" v-if="item.status == '1'">待审核</span>
              <span class="audit" v-if="item.status == '3'">待人工审核</span>
              <span class="audit" v-if="item.status == '4'">待发布</span>
              <span class="publish" v-if="item.status == '8'">待发布</span>
              <span class="publish" v-if="item.status == '9'">已发表</span>
              <span class="unaudit" v-if="item.status == '2'">未通过审核</span>
              <span class="delete" v-if="item.status == '100'">已删除</span>
              <template v-if="item.status == '9'">
                <span class="draft" v-if="item.enable == '0'">已下架</span>
                <span class="audit" v-if="item.enable == '1'">已上架</span>
              </template>
            </div>
          </div>
        </div>
      </div>
      <empty-data v-else />
    </section>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getList" />
  </div>
</template>

<script>
import myMixin from '@/utils/mixins'
import Pagination from '@/components/pagination'
import EmptyData from '@/components/empty-data'
import { getChannels } from '@/api/publish'
import { searchArticle, deleteArticles, upDownArticle } from '@/api/content'

export default {
  name: 'NewsList',
  mixins: [myMixin],
  components: {
    Pagination,
    EmptyData
  },
  data () {
    return {
      // 当前状态
      // 0 草稿
      // 1 提交（待审核）
      // 2 审核失败
      // 3 人工审核
      // 4 人工审核通过
      // 8 审核通过（待发布）
      // 9 已发布
      statusList: [
        { label: undefined, value: '全部' },
        { label: 0, value: '草稿' },
        { label: 1, value: '待审核' },
        { label: 9, value: '审核通过' },
        { label: 2, value: '审核失败' }
      ],
      date: null,
      channelList: [
        { id: undefined, name: '全部' }
      ],
      host: '',
      // 列表相关
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        status: undefined,
        keyword: undefined,
        channelId: undefined,
        beginPubDate: undefined,
        endPubDate: undefined,
        page: 1,
        size: 10
      }
    }
  },
  created () {
    this.getChannels()
    this.getList()
  },
  methods: {
    async getChannels () {
      const result = await getChannels()
      this.channelList = [...this.channelList, ...result.data]
    },
    async getList () {
      // el-input和el-select执行clear后值为''，主动转换为undefined
      if (this.listQuery.keyword === '') {
        this.listQuery.keyword = undefined
      }
      // el-input和el-select执行clear后值为''，主动转换为undefined
      if (this.listQuery.channelId === '') {
        this.listQuery.channelId = undefined
      }

      const result = await searchArticle(this.listQuery)
      this.host = result.host
      this.total = result.total
      this.list = result.data
    },
    // 重置分页page
    resetPage () {
      this.listQuery.page = 1
      this.getList()
    },
    handelDatePickerChange () {
      this.listQuery.beginPubDate = this.date ? this.date[0] : undefined
      this.listQuery.endPubDate = this.date ? this.date[1] : undefined
      this.resetPage()
    },
    getImage: function (item) {
      const temp = item.images.split(',')
      if (temp.length > 0) {
        return this.host + temp[0]
      }
    },
    // 操作
    operateBtn (id, type) {
      switch (type) {
        case 'modify':
          this.$router.push({ path: '/news/publish', query: { newsId: id } })
          break
        case 'down':
          this.upOrDown(id, 0)
          break
        case 'up':
          this.upOrDown(id, 1)
          break
        case 'del':
          this.deleteArticlesById(id)
          break
        default:
      }
    },
    // 根据Id删除文章
    async deleteArticlesById (id) {
      try {
        await this.showDeleteConfirm('文章')
        const temp = await deleteArticles(id)
        if (temp.code === 0) {
          this.$message({ type: 'success', message: '删除成功!' })
          this.getList()
        } else {
          this.$message({ type: 'error', message: temp.error_message })
        }
      } catch (err) {
        this.$message({ type: 'info', message: '已取消删除' })
      }
    },
    // 上下架
    async upOrDown (id, enable) {
      const temp = await upDownArticle({ id: id, enable: enable })
      if (temp.code === 0) {
        this.$message({ type: 'success', message: '操作成功!' })
        this.getList()
      } else {
        this.$message({ type: 'error', message: temp.error_message })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.filter {
  height: 158px;
}

.content-card {
  display: flex;
  padding-left: 6px;
  padding-bottom: 30px;
  flex-wrap: wrap;

  .item-card {
    position: relative;
    width: 233px;
    height: 315px;
    margin-top: 30px;
    margin-left: 30px;
    border-radius: 8px;
    border: 1px solid $--background-color-base;

    /deep/ .el-image__inner,
    .image-slot img {
      display: block;
      width: 100%;
      height: 155px;
      object-fit: cover;
      border-top-left-radius: 8px;
      border-top-right-radius: 8px;
    }

    .top {
      position: absolute;
      top: 21px;
      right: 17px;
      display: none;
      -webkit-transition: .3s;
      transition: .3s;

      .item {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 40px;
        height: 40px;
        margin-left: 16px;
        background: $--color-white;
        border-radius: 50%;
        cursor: pointer;

        .icon {
          width: 30px;
          height: 30px;
        }
      }
    }

    .content {
      padding: 22px 16px 0 17px;

      .center {
        height: 71px;
        color: #20232A;
        font-weight: 600;
      }

      .bottom {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 13px;

        .time {
          font-size: 14px;
          color: $--color-text-secondary;
          line-height: 19px;
        }

        .draft {
          padding: 3px 7px;
          background: #F0F3F9;
          border-radius: 4px;
          color: $--color-text-secondary;
        }

        .audit {
          padding: 3px 7px;
          background: #EEF4FF;
          border-radius: 4px;
          color: $--color-primary;
        }

        .publish {
          padding: 3px 7px;
          background: #EBF7F2;
          border-radius: 4px;
          color: $--color-success;
        }

        .unaudit {
          padding: 3px 7px;
          background: #FFEFEF;
          border-radius: 4px;
          color: $--color-danger;
        }
      }
    }
  }

  .item-card:hover .top {
    display: flex;
  }
}

.el-pagination {
  border-top: 1px solid #ebeef5;
}
</style>
