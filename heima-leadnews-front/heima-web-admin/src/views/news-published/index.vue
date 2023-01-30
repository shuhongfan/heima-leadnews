<template>
  <div>
    <section class="filter">
      <div class="filter-container">
        <el-form ref="form" :inline="true" size="medium">
          <el-form-item label="搜索内容：">
            <el-input
              v-model="listQuery.title"
              placeholder="请输入内容标题"
              class="filter-item"
              clearable
              @input="resetPage"
            />
          </el-form-item>
          <el-form-item label="发布状态：">
            <el-select v-model="listQuery.enable" placeholder="请选择状态" clearable @change="resetPage">
              <el-option
                v-for="(item, index) in enableList"
                :key="index"
                :label="item.label"
                :value="item.value">
              </el-option>
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
        </el-form>
      </div>
    </section>

    <section class="result">
      <el-table
        v-if="list.length"
        :data="list"
        :header-cell-style="{textAlign: 'center', fontWeight: '600'}"
        :cell-style="{textAlign: 'center'}"
        highlight-current-row
      >
        <el-table-column label="序号" type="index" width="50"></el-table-column>
        <el-table-column label="标题">
          <template slot-scope="scope">
            <span>{{ scope.row.title }}</span>
          </template>
        </el-table-column>
        <el-table-column label="作者">
          <template slot-scope="scope">
            <span>{{ scope.row.authorName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="类型">
          <template slot-scope="scope">
            <span>{{ typeList.find((item) => { return item.value === scope.row.type }).label }}</span>
          </template>
        </el-table-column>
        <el-table-column label="标签">
          <template slot-scope="scope">
            <span>{{ scope.row.labels }}</span>
          </template>
        </el-table-column>
        <el-table-column label="发布时间">
          <template slot-scope="scope">
            <span v-html="dateTimeFormatWithBr(scope.row.publishTime)"></span>
          </template>
        </el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            <span>{{ enableList.find((item) => { return item.value === scope.row.enable }).label }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              type="text"
              class="el-button--success-text"
              @click="goToDetailView(scope.row)"
            >查看</el-button>
            <el-button
              type="text"
              class="el-button--primary-text"
              :disabled="scope.row.enable === 1"
              @click="operateForDownOrUp(scope.row.id, 1)"
            >上架</el-button>
            <el-button
              type="text"
              class="el-button--danger-text"
              :disabled="scope.row.enable === 0"
              @click="operateForDownOrUp(scope.row.id, 0)"
            >下架</el-button>
          </template>
        </el-table-column>
      </el-table>
      <empty-data v-else />
    </section>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getList" />
  </div>
</template>

<script>
import Pagination from '@/components/pagination'
import EmptyData from '@/components/empty-data'
import { findPublished, downOrUp } from '@/api/news'
import myMixin from '@/utils/mixins'

export default {
  name: 'NewsPublishedList',
  mixins: [myMixin],
  components: {
    Pagination,
    EmptyData
  },
  data () {
    return {
      enableList: [
        { label: '全部', value: undefined },
        { label: '已上架', value: 1 },
        { label: '已下架', value: 0 }
      ],
      typeList: [
        {
          value: 0,
          label: '无图文章'
        },
        {
          value: 1,
          label: '单图文章'
        },
        {
          value: 3,
          label: '多图文章'
        }
      ],
      date: null,
      // 列表相关
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        title: undefined,
        enable: undefined,
        page: 1,
        size: 10,
        beginDate: undefined,
        endDate: undefined
      }
    }
  },
  created () {
    this.getList()
  },
  methods: {
    async getList () {
      // el-input和el-select执行clear后值为''，主动转换为undefined
      if (this.listQuery.title === '') {
        this.listQuery.title = undefined
      }
      if (this.listQuery.enable === '') {
        this.listQuery.enable = undefined
      }

      const res = await findPublished(this.listQuery)
      if (res.code === 200) {
        this.list = res.data
        this.total = res.total
      } else {
        this.$message({ type: 'error', message: res.errorMessage })
      }
    },
    // 重置分页page
    resetPage () {
      this.listQuery.page = 1
      this.getList()
    },
    handelDatePickerChange () {
      this.listQuery.beginDate = this.date ? this.date[0] : undefined
      this.listQuery.endDate = this.date ? this.date[1] : undefined
      this.resetPage()
    },
    // 查看详情
    goToDetailView (row) {
      this.$router.push({
        path: '/news-published/detail',
        query: {
          id: row.id
        }
      })
    },
    // 上架/下架
    async operateForDownOrUp (id, enable) {
      const { code, errorMessage } = await downOrUp({ id: id, enable: enable })

      if (code === 0) {
        this.getList()
        this.$message({ type: 'success', message: '操作成功！' })
      } else {
        this.$message({ type: 'error', message: errorMessage })
      }
    }
  }
}
</script>
