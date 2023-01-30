<template>
  <div class="chart-content bgc">
    <header>你的受众喜欢哪些分类的内容</header>
    <bar-chart :chartData="barChartData" />
  </div>
</template>

<script>
import BarChart from '@/components/BarChart'

export default {
  data () {
    return {
      barChartData: {
        xAxisData: ['大数据', '人工智能', '智能硬件', '新品发布', '智慧成熟', '智慧交通', '物联网', '爬虫技术', '游戏技术', '计算机语言'],
        seriesData: [250, 350, 260, 470, 170, 250, 250, 350, 260, 470]
      }
    }
  },
  components: {
    BarChart
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.chart-content {
  margin-top: 30px;
  padding: 30px;

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
