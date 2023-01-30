<template>
  <div class="filter-container bgc">
    <div class="filter">
      <el-page-header @back="goBack" content="文章数据详情"></el-page-header>
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

    <!-- 饼图分析 -->
    <pie-analysis />
  </div>
</template>

<script>
import DayWeekMonth from '@/components/day-week-month'
import DimensionStatist from './components/DimensionStatist'
import PieAnalysis from './components/PieAnalysis'
import myMixin from '@/utils/mixins'

export default {
  name: 'ContentDetail',
  mixins: [myMixin],
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
          icon: 'iconicon_tiaochulv_pjydjd',
          number: '61',
          unit: '%',
          text: '平均阅读进度'
        },
        {
          icon: 'iconicon_data_tiaochulv',
          number: '13.2',
          unit: '%',
          text: '跳出率'
        },
        {
          icon: 'iconicon_data_pjydsj',
          number: '107',
          text: '平均阅读时间（秒）'
        },
        {
          icon: 'iconicon_data_tjzfl',
          number: '2,567',
          text: '推荐转发量'
        },
        {
          icon: 'iconicon_data_pll',
          number: '231',
          text: '评论量'
        },
        {
          icon: 'iconicon_data_zydl',
          number: '1,247',
          text: '总阅读量'
        },
        {
          icon: 'iconicon_data_zydl',
          number: '987',
          text: '粉丝阅读量'
        }
      ],
      colorPlate1: ['#E5F6FF', '#E2F6E9', '#FFF0E9', '#F6F3FF', '#E2F6E9', '#FFF0E9', '#E5F6FF'],
      colorPlate2: ['#B7E6FF', '#C3E8D1', '#FFDAC9', '#D9D3FF', '#C3E8D1', '#FFDAC9', '#B7E6FF'],
      colorPlate3: ['#2C71FF', '#3BD396', '#FF562D', '#5A3ED4', '#3BD396', '#FF562D', '#2C71FF']
    }
  },
  components: {
    DayWeekMonth,
    DimensionStatist,
    PieAnalysis
  },
  methods: {
    goBack () {
      this.goBackPage()
    },
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
