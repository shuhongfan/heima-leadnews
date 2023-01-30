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
          <el-form-item label="审核状态：">
            <el-select v-model="listQuery.status" placeholder="请选择状态" clearable @change="resetPage">
              <el-option
                v-for="(item, index) in statusList"
                :key="index"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
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
            <span>{{ statusList.find((item) => { return item.value === scope.row.status }).label }}</span>
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
              :disabled="scope.row.status !== 3"
              @click="operateForPass(scope.row.id)"
            >通过</el-button>
            <el-button
              type="text"
              class="el-button--danger-text"
              :disabled="scope.row.status !== 3"
              @click="operateForFail(scope.row.id)"
            >驳回</el-button>
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
import { authList, authPass, authFail } from '@/api/news'
import myMixin from '@/utils/mixins'

export default {
  name: 'NewsList',
  mixins: [myMixin],
  components: {
    Pagination,
    EmptyData
  },
  data () {
    return {
      statusList: [
        {
          label: '全部',
          value: undefined
        },
        // {
        //   label: '草稿',
        //   value: 0
        // },
        {
          label: '提交（待审核）',
          value: 1
        },
        {
          label: '审核失败',
          value: 2
        },
        {
          label: '待人工审核',
          value: 3
        },
        {
          label: '人工审核通过',
          value: 4
        },
        {
          label: '审核通过（待发布）',
          value: 8
        },
        {
          label: '已发布',
          value: 9
        }
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
      // 列表相关
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        title: undefined,
        status: undefined,
        page: 1,
        size: 10
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
      if (this.listQuery.status === '') {
        this.listQuery.status = undefined
      }

      const res = await authList(this.listQuery)
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
    // 查看详情
    goToDetailView (row) {
      this.$router.push({
        path: '/news/detail',
        query: {
          id: row.id
        }
      })
    },
    // 通过/驳回
    async operateForPass (id) {
      const { code, errorMessage } = await authPass({ id: id, status: 4 })
      if (code === 0) {
        this.getList()
        this.$message({ type: 'success', message: '操作成功！' })
      } else {
        this.$message({ type: 'error', message: errorMessage })
      }
    },
    async operateForFail (id) {
      try {
        const result = await this.showPrompt()

        const { code, errorMessage } = await authFail({ id: id, status: 2, msg: result.value })
        if (code === 0) {
          this.getList()
          this.$message({ type: 'success', message: '操作成功！' })
        } else {
          this.$message({ type: 'error', message: errorMessage })
        }
      } catch (err) {
        this.$message({ type: 'info', message: '已取消驳回' })
      }
    }
  }
}
</script>
