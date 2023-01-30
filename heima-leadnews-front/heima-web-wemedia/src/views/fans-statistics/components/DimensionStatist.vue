<template>
  <div class="filter-container bgc">
    <div class="filter">
      <el-form ref="form" :inline="true" size="medium">
        <el-form-item label="统计日期：" style="margin-right: 30px;">
          <el-date-picker
            v-model="parms.time"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions"
            format="yyyy-MM-dd"
            placeholder="选择日期"
          />
        </el-form-item>
        <el-form-item>
          <day-week-month @handleChange="handleRadioGroupSelChange" />
        </el-form-item>
      </el-form>
    </div>

    <!-- 累计粉丝数量/粉丝累计阅读量/粉丝收益（元）/取消关注量 -->
    <dimension-statist
      :statistList="statistList"
      :colorPlate1="colorPlate1"
      :colorPlate2="colorPlate2"
      :colorPlate3="colorPlate3"
    />

    <!-- 阅读量趋势图 -->
    <line-chart :chartData="chartData" />
  </div>
</template>

<script>
import DayWeekMonth from '@/components/day-week-month'
import DimensionStatist from '../../news-statistics/components/DimensionStatist'
import LineChart from '@/components/LineChart'

export default {
  name: 'FansStatist',
  data () {
    return {
      parms: {
        type: '30',
        stime: '',
        etime: ''
      },
      pickerOptions: {
        disabledDate (time) {
          return time.getTime() > Date.now()
        }
      },
      statistList: [
        {
          icon: 'iconicon_data_ljfssl',
          number: '3,356',
          text: '累计粉丝数量'
        },
        {
          icon: 'iconicon_data_zydl',
          number: '3,235',
          text: '粉丝累计阅读量'
        },
        {
          icon: 'iconicon_data_fssy',
          number: '2,567',
          text: '粉丝收益（元）'
        },
        {
          icon: 'iconicon_data_qxgzl',
          number: '834',
          text: '取消关注量'
        }
      ],
      colorPlate1: ['#E2F6E9', '#FFF0E9', '#F6F3FF', '#E5F6FF'],
      colorPlate2: ['#C3E8D1', '#FFDAC9', '#D9D3FF', '#B7E6FF'],
      colorPlate3: ['#3BD396', '#FF562D', '#5A3ED4', '#2C71FF'],
      chartData: {
        title: '阅读量趋势图',
        xAxisData: [
          '2020-08-02 00:00',
          '2020-08-02 01:00',
          '2020-08-02 02:00',
          '2020-08-02 03:00',
          '2020-08-02 04:00',
          '2020-08-02 05:00',
          '2020-08-02 06:00',
          '2020-08-02 07:00',
          '2020-08-02 08:00',
          '2020-08-02 09:00',
          '2020-08-02 10:00',
          '2020-08-02 11:00',
          '2020-08-02 12:00',
          '2020-08-02 13:00',
          '2020-08-02 14:00',
          '2020-08-02 15:00',
          '2020-08-02 16:00',
          '2020-08-02 17:00',
          '2020-08-02 18:00',
          '2020-08-02 19:00',
          '2020-08-02 20:00',
          '2020-08-02 21:00',
          '2020-08-02 22:00',
          '2020-08-02 23:00',
          '2020-08-02 24:00'
        ],
        seriesData: [820, 932, 901, 934, 1290, 1330, 1320, 820, 932, 901, 934, 1290, 1330, 1320, 820, 932, 901, 934, 1290, 1330, 1320, 820, 932, 901, 934]
      },
      lineInfo: [
        { name: '发文量', type: 'article' },
        { name: '阅读量', type: 'read_count' },
        { name: '点赞量', type: 'likes' },
        { name: '评论量', type: 'comment' },
        { name: '收藏量', type: 'collection' },
        { name: '转发量', type: 'follow' },
        { name: '不喜欢', type: 'unlikes' }
      ]
    }
  },
  components: {
    DayWeekMonth,
    DimensionStatist,
    LineChart
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
  }
  .chart {
    padding: 0 20px;
    overflow: hidden;
    margin-bottom: 30px;
  }

  /deep/ .el-form-item__content {
    line-height: 1;
  }
}
</style>
