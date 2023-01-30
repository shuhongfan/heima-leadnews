<template>
  <div>
    <section class="filter">
    <div class="filter-container">
      <el-form ref="form" :inline="true" size="medium">
        <el-form-item label="敏感词：">
          <el-input
            v-model="listQuery.name"
            placeholder="请输入敏感词"
            class="filter-item"
            clearable
            @input="resetPage"
          />
        </el-form-item>
      </el-form>
    </div>
    <el-button type="success" icon="el-icon-circle-plus-outline" class="el-button--has-icon" @click="createSensitive">新建</el-button>
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
        <el-table-column label="敏感词">
          <template slot-scope="scope">
            <span>{{ scope.row.sensitives }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间">
          <template slot-scope="scope">
            <span v-html="dateTimeFormatWithBr(scope.row.createdTime)"></span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              type="text"
              class="el-button--success-text"
              @click="editSensitive(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              class="el-button--danger-text"
              @click="delSensitive(scope.row.id)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <empty-data v-else />
    </section>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getList" />

    <sensitive-create-dialog :dialogVisible.sync="createDialogVisible" @refreshList="getList" />

    <sensitive-edit-dialog :dialogVisible.sync="editDialogVisible" :sensitive="sensitive" @refreshList="getList" />
  </div>
</template>

<script>
import myMixin from '@/utils/mixins'
import Pagination from '@/components/pagination'
import EmptyData from '@/components/empty-data'
import SensitiveCreateDialog from './components/sensitive-create-dialog'
import SensitiveEditDialog from './components/sensitive-edit-dialog'
import { loadList, deleteData } from '@/api/sensitive'

export default {
  name: 'SensitiveList',
  mixins: [myMixin],
  components: {
    Pagination,
    EmptyData,
    SensitiveCreateDialog,
    SensitiveEditDialog
  },
  data () {
    return {
      // 列表相关
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        name: undefined,
        page: 1,
        size: 10
      },
      sensitive: {},
      // 对话框相关
      createDialogVisible: false,
      editDialogVisible: false
    }
  },
  created () {
    this.getList()
  },
  methods: {
    async getList () {
      // el-input和el-select执行clear后值为''，主动转换为undefined
      if (this.listQuery.name === '') {
        this.listQuery.name = undefined
      }

      const res = await loadList(this.listQuery)
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
    // 新增数据
    createSensitive: function () {
      this.createDialogVisible = true
    },
    // 编辑数据
    editSensitive: function (sensitive) {
      this.editDialogVisible = true
      this.sensitive = sensitive
    },
    async delSensitive (id) {
      try {
        await this.showDeleteConfirm('敏感词')
        this.doDelete(id)
      } catch (err) {
        this.$message({ type: 'info', message: '已取消删除' })
      }
    },
    async doDelete (id) {
      const res = await deleteData(id)
      if (res.code === 0) {
        this.getList()
        this.$message({ type: 'success', message: '操作成功！' })
      } else {
        this.$message({ type: 'error', message: res.errorMessage })
      }
    }

  }
}
</script>
