<template>
  <div>
    <section class="filter">
      <div class="filter-container">
        <el-form ref="form" :inline="true" size="medium">
          <el-form-item label="频道名称：">
            <el-input
              v-model="listQuery.name"
              placeholder="请输入频道名称"
              class="filter-item"
              clearable
              @input="resetPage"
            />
          </el-form-item>
          <el-form-item label="账号状态：">
            <!-- TODO: clearable按钮还原，下拉列表宽度，输入框宽度 -->
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
      <el-button type="success" icon="el-icon-circle-plus-outline" class="el-button--has-icon" @click="createChannel">新建</el-button>
    </section>

    <section class="result">
      <el-table
        v-if="list.length"
        :data="list"
        :header-cell-style="{textAlign: 'center', fontWeight: '600'}"
        :cell-style="{textAlign: 'center'}"
        highlight-current-row
      >
        <el-table-column
          width="50"
          label="序号"
        >
          <template slot-scope="scope">
            <span>{{ (listQuery.page - 1) * listQuery.size + scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="频道名称">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="描述">
          <template slot-scope="scope">
            <span>{{ scope.row.description }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="是否默认频道">
          <template slot-scope="scope">
            {{ scope.row.isDefault ? '是' : '否'}}
          </template>
        </el-table-column>
        <!-- TODO: 添加小圆圈 -->
        <el-table-column label="状态">
          <template slot-scope="scope">{{ scope.row.status ? '启动' : '禁用' }}</template>
        </el-table-column>
        <el-table-column
          label="排序">
          <template slot-scope="scope">
            <span>{{ scope.row.ord }}</span>
          </template>
        </el-table-column>-->
        <el-table-column
          label="创建时间">
          <template slot-scope="scope">
            <span v-html="dateTimeFormatWithBr(scope.row.createdTime)"></span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240">
          <template slot-scope="scope">
            <el-button
              type="text"
              class="el-button--success-text"
              @click="editChannel(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              class="el-button--primary-text"
              :disabled="scope.row.status"
              @click="updateChannel(scope.row.id, scope.row.name, true)"
            >启用</el-button>
            <el-button
              type="text"
              class="el-button--danger-text"
              :disabled="!scope.row.status"
              @click="updateChannel(scope.row.id, scope.row.name, false)"
            >禁用</el-button>
            <el-button
              type="text"
              class="el-button--danger-text"
              @click="delChannel(scope.row.id)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <empty-data v-else />
    </section>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getList" />

    <channel-create-dialog :dialogVisible.sync="createDialogVisible" @refreshList="getList" />

    <channel-edit-dialog :dialogVisible.sync="editDialogVisible" :channel="channel" @refreshList="getList" />
  </div>
</template>

<script>
import myMixin from '@/utils/mixins'
import Pagination from '@/components/pagination'
import EmptyData from '@/components/empty-data'
import ChannelCreateDialog from './components/channel-create-dialog'
import ChannelEditDialog from './components/channel-edit-dialog'
import { loadList, updateData, delData } from '@/api/channel'

export default {
  name: 'ChannelList',
  mixins: [myMixin],
  components: {
    Pagination,
    EmptyData,
    ChannelCreateDialog,
    ChannelEditDialog
  },
  data () {
    return {
      // 账号状态
      statusList: [
        { label: '全部', value: undefined },
        { label: '启动', value: 1 },
        { label: '禁用', value: 0 }
      ],
      // 列表相关
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        name: undefined,
        status: undefined,
        page: 1,
        size: 10
      },
      channel: {},
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
      if (this.listQuery.status === '') {
        this.listQuery.status = undefined
      }

      const res = await loadList(this.listQuery)
      if (res.code === 200) {
        this.list = res.data
        this.total = res.total
        this.listLoading = false
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
    createChannel: function () {
      this.createDialogVisible = true
    },
    // 编辑数据
    editChannel: function (channel) {
      this.editDialogVisible = true
      this.channel = channel
    },
    // TODO: 禁用提示用户
    async updateChannel (id, name, status) {
      const res = await updateData({ id: id, name: name, status: status })
      if (res.code === 0) {
        this.dialogFormVisible = false
        this.getList()
        this.$message({ type: 'success', message: '操作成功！' })
      } else {
        this.$message({ type: 'error', message: res.errorMessage })
      }
    },
    async delChannel (id) {
      try {
        await this.showDeleteConfirm('频道')
        this.doDelete(id)
      } catch (err) {
        this.$message({ type: 'info', message: '已取消删除' })
      }
    },
    async doDelete (id) {
      const res = await delData(id)
      if (res.code === 0) {
        this.dialogFormVisible = false
        this.getList()
        this.$message({ type: 'success', message: '删除成功！' })
      } else {
        this.$message({ type: 'error', message: res.errorMessage })
      }
    }
  }
}
</script>
