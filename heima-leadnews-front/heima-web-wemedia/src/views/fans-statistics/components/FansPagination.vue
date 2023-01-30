<template>
  <div class="bgc">
    <section class="result">
      <!-- TODO: 调整样式细节 -->
      <div style="margin-bottom:50px;">
        <header>数据列表</header>
        <el-date-picker
          v-model="parms.time"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd"
          placeholder="选择日期"
          style="width: 400px;margin-left: 27px;"
          :picker-options="pickerOptions"
          size="medium"
        />
      </div>
      <el-table
        :data="list"
        :header-cell-style="{textAlign: 'center', fontWeight: '600'}"
        :cell-style="{textAlign: 'center'}"
        highlight-current-row
      >
        <el-table-column label="时间" prop="dateTime" width="150"></el-table-column>
        <el-table-column label="粉丝数量" prop="fansNum"></el-table-column>
        <el-table-column label="粉丝阅读量" prop="readNum"></el-table-column>
        <el-table-column label="粉丝收益（元）" prop="income"></el-table-column>
        <el-table-column label="取消关注量" prop="cancelNum"></el-table-column>
      </el-table>
    </section>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getList" />
  </div>
</template>

<script>
import Pagination from '@/components/pagination'

export default {
  name: 'FansPagination',
  components: {
    Pagination
  },
  data () {
    return {
      parms: {
        type: '30',
        stime: '',
        etime: '',
        time: []
      },
      pickerOptions: {
        disabledDate (time) {
          return time.getTime() > Date.now()
        }
      },
      total: 6,
      listQuery: {
        page: 1,
        size: 10
      },
      list: [
        {
          dateTime: '2020-08-02',
          fansNum: 50,
          readNum: 0,
          income: 0,
          cancelNum: '3,365'
        },
        {
          dateTime: '2020-08-01',
          fansNum: 150,
          readNum: 2,
          income: 2,
          cancelNum: '3.300'
        },
        {
          dateTime: '2020-07-31',
          fansNum: 50,
          readNum: 6,
          income: 6,
          cancelNum: '3,223'
        },
        {
          dateTime: '2020-07-30',
          fansNum: 150,
          readNum: 8,
          income: 8,
          cancelNum: '3.210'
        },
        {
          dateTime: '2020-07-29',
          fansNum: 125,
          readNum: 9,
          income: 9,
          cancelNum: '3,108'
        },
        {
          dateTime: '2020-07-28',
          fansNum: 150,
          readNum: 29,
          income: 29,
          cancelNum: '3.034'
        }
      ]
    }
  },
  methods: {
    goToDetailAnalysis (row) {
      this.$router.push({ path: '/statistics/detail?newsId=' + row.id })
    },
    getList () {}
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

header {
  display: inline-block;
  color: $--color-text-primary;
  font-size: 18px;
  font-weight: 600;
}

header::before {
  content: '\00A0';
  width: 4px;
  height: 18px;
  margin-right: 10px;
  border-radius: 3px;
  background: $--color-primary;
}

/deep/ .el-form-item__label {
  font-size: 18px;
  font-weight: 600;
}

.result {
  margin-top: 30px;
  padding: 30px 30px 0 30px;
}
</style>
