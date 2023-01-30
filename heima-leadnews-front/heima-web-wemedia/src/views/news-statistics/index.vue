<template>
  <div class="filter-container bgc">
    <div class="filter">
      <el-form ref="form" :inline="true" size="medium">
        <el-form-item label="数据时间：" style="margin-right: 30px;">
          <el-date-picker
            v-model="parms.time"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            placeholder="选择日期"
            style="width: 400px;"
            :picker-options="pickerOptions"
          />
        </el-form-item>
        <el-form-item>
          <day-week-month @handleChange="handleRadioGroupSelChange" />
        </el-form-item>
      </el-form>
    </div>

    <!-- 指标统计 -->
    <dimension-statist
      :statistList="statistList"
      :colorPlate1="colorPlate1"
      :colorPlate2="colorPlate2"
      :colorPlate3="colorPlate3"
    />

    <!-- 文章列表 -->
    <article-pagination />
  </div>
</template>

<script>
import DayWeekMonth from '@/components/day-week-month'
import DimensionStatist from './components/DimensionStatist'
import ArticlePagination from './components/ArticlePagination'

export default {
  name: 'ContentStatist',
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
      statistList: [
        {
          icon: 'iconicon_data_twfbl',
          number: '513',
          text: '图文发布量'
        },
        {
          icon: 'iconicon_data_dianzan',
          number: '23,456',
          text: '点赞数量'
        },
        {
          icon: 'iconicon_data_scsl',
          number: '1,235',
          text: '收藏数量'
        },
        {
          icon: 'iconicon_data_tjzfl',
          number: '2,567',
          text: '转发数量'
        }
      ],
      colorPlate1: ['#E5F6FF', '#E2F6E9', '#FFF0E9', '#F6F3FF'],
      colorPlate2: ['#B7E6FF', '#C3E8D1', '#FFDAC9', '#D9D3FF'],
      colorPlate3: ['#2C71FF', '#3BD396', '#FF562D', '#5A3ED4']
    }
  },
  components: {
    DayWeekMonth,
    DimensionStatist,
    ArticlePagination
  },
  methods: {
    handleRadioGroupSelChange (radioGroupSel) {
      this.parms.type = radioGroupSel
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.filter-container {
  padding: 0 30px 30px;

  .filter {
    height: 78px;
    padding: 0;
    border-bottom: none;

    .el-form-item {
      margin-bottom: 0;
    }

    /deep/ .el-form-item__content {
      line-height: 1;
    }
  }
}
</style>
