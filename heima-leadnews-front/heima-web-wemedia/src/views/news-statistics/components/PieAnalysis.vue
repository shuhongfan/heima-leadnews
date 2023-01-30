<template>
  <el-row class="pie-analysis">
    <el-col :span="12">
      <header>阅读来源分析</header>
      <pie-chart :chartData="readingSources" />
    </el-col>
    <el-col :span="12">
      <header>阅读完成度分析</header>
      <pie-chart :chartData="readingCompletion" />
    </el-col>
  </el-row>
</template>

<script>
import PieChart from '@/components/PieChart'

export default {
  name: 'PieAnalysis',
  data () {
    return {
      readingSources: [
        { value: 201, name: '推荐' },
        { value: 280, name: '频道' },
        { value: 70, name: '相关阅读' },
        { value: 70, name: '应用外阅读' },
        { value: 60, name: '其他（搜索、推送等）' }
      ],
      readingCompletion: [
        { value: 201, name: '20%以下' },
        { value: 280, name: '20%-80%' },
        { value: 70, name: '80%以上' }
      ]
    }
  },
  components: {
    PieChart
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.pie-analysis {
  margin-top: 80px;

  header {
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
}
</style>
