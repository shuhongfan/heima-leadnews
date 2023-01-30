<template>
  <div>
    <section class="result">
      <el-table
        :data="list"
        :header-cell-style="setHeaderCellStyle"
        :cell-style="setCellStyle"
        highlight-current-row
      >
        <el-table-column label="序号" type="index" width="50"></el-table-column>
        <el-table-column label="文章名称" prop="articleName" min-width="150" show-overflow-tooltip></el-table-column>
        <el-table-column label="阅读" prop="readNum"></el-table-column>
        <el-table-column label="评论量" prop="commentNum"></el-table-column>
        <el-table-column label="收藏量" prop="collectNum"></el-table-column>
        <el-table-column label="转发量" prop="forwardNum"></el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button
              type="text"
              class="el-button--success-text"
              @click="goToDetailAnalysis(scope.row)"
            >详细分析</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getList" />
  </div>
</template>

<script>
import Pagination from '@/components/pagination'

export default {
  name: 'ArticlePagination',
  components: {
    Pagination
  },
  data () {
    return {
      total: 6,
      listQuery: {
        page: 1,
        size: 10
      },
      list: [
        {
          id: 1,
          articleName: '项目管理感悟',
          readNum: 150,
          commentNum: 150,
          collectNum: 10,
          forwardNum: 150
        },
        {
          id: 2,
          articleName: '北大创业人：“理想”的喧哗与骚动',
          readNum: 150,
          commentNum: 150,
          collectNum: 10,
          forwardNum: 150
        },
        {
          id: 3,
          articleName: '没想到，拼多多竟然想用AI种草莓给我吃',
          readNum: 150,
          commentNum: 150,
          collectNum: 10,
          forwardNum: 150
        },
        {
          id: 4,
          articleName: '英特尔是如何丢掉苹果生意的？',
          readNum: 150,
          commentNum: 150,
          collectNum: 10,
          forwardNum: 150
        },
        {
          id: 5,
          articleName: '“鹰翼门”关不上，“幽灵刹车”事件不断，特斯拉…',
          readNum: 150,
          commentNum: 150,
          collectNum: 10,
          forwardNum: 150
        },
        {
          id: 6,
          articleName: '微软及时选择“站队”，或拯救了折叠屏手机的未…',
          readNum: 234,
          commentNum: 150,
          collectNum: 10,
          forwardNum: 150
        }
      ]
    }
  },
  methods: {
    setHeaderCellStyle ({ row, column, rowIndex, columnIndex }) {
      return columnIndex !== 1 ? 'text-align: center;font-weight: 600;' : 'text-align: left;font-weight: 600;'
    },
    setCellStyle ({ row, column, rowIndex, columnIndex }) {
      return columnIndex !== 1 ? 'text-align: center;' : 'text-align: left;'
    },
    goToDetailAnalysis (row) {
      this.$router.push({ path: '/statistics/detail/' + row.id })
    },
    getList () {}
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.result {
  margin: 40px 0;
}

.el-pagination {
  border-top: 1px solid #ebeef5;
}
</style>
