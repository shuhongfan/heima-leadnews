<template>
  <div>
    <section class="filter">
      <div class="filter-container">
        <el-form ref="form" :inline="true" size="medium">
          <el-form-item label="审核状态：">
            <el-radio-group v-model="listQuery.status" @change="resetPage">
              <el-radio v-for="(item, index) in statusList" :key="index" :label="item.label">{{ item.value }}</el-radio>
            </el-radio-group>
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
        <el-table-column label="姓名">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="身份证号">
          <template slot-scope="scope">
            <span>{{ scope.row.idno }}</span>
          </template>
        </el-table-column>
        <el-table-column label="认证类型">实名认证</el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            <span>{{ statusList.find(item => { return scope.row.status === item.label }).value }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              type="text"
              class="el-button--success-text"
              @click="operateForView(scope.row)"
            >查看</el-button>
            <el-button
              type="text"
              class="el-button--primary-text"
              :disabled="scope.row.status !== 1 ? true : false"
              @click="operateForPass(scope.row.id)"
            >通过</el-button>
            <el-button
              type="text"
              class="el-button--danger-text"
              :disabled="scope.row.status !== 1 ? true : false"
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
import { findAuthList, authPass, authFail } from '@/api/user-auth'
import myMixin from '@/utils/mixins'

export default {
  name: 'AuthManage',
  mixins: [myMixin],
  components: {
    Pagination,
    EmptyData
  },
  data () {
    return {
      listQuery: {
        status: undefined,
        page: 1,
        size: 10
      },
      total: 0,
      host: '',
      statusList: [
        { label: undefined, value: '全部' },
        { label: 0, value: '创建中' },
        { label: 1, value: '待审核' },
        { label: 2, value: '审核失败' },
        { label: 9, value: '审核通过' }
      ],
      list: []
    }
  },
  created () {
    this.getList()
  },
  methods: {
    async getList () {
      const res = await findAuthList(this.listQuery)
      if (res.code === 200) {
        this.list = res.data
        this.host = res.host
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
    operateForView (row) {
      this.$router.push({
        path: '/auth/detail',
        query: {
          id: row.id,
          name: row.name,
          idno: row.idno,
          fontImage: row.fontImage,
          backImage: row.backImage,
          holdImage: row.holdImage,
          liveImage: row.liveImage,
          status: row.status,
          reason: row.reason,
          host: this.host
        }
      })
    },
    async operateForPass (id) {
      const { code, errorMessage } = await authPass({ id: id })
      if (code === 0) {
        this.getList()
        this.$message({ type: 'success', message: '操作成功' })
      } else {
        this.$message({ type: 'error', message: errorMessage })
      }
    },
    async operateForFail (id) {
      try {
        const result = await this.showPrompt()

        const { code, errorMessage } = await authFail({ id: id, msg: result.value })
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
