<template>
  <div class="chart-content bgc">
    <header>粉丝年龄分布</header>
    <el-row>
      <el-col :span="12">
        <bar-chart :chartData="barChartData" />
      </el-col>
      <el-col :span="12">
        <pie-chart :chartData="pieChartData" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import BarChart from '@/components/BarChart'
import PieChart from '@/components/PieChart'

export default {
  data () {
    return {
      barChartData: {
        xAxisData: ['0-17岁', '18-23岁', '24-30岁', '31-40岁', '41-50岁', '50+岁'],
        seriesData: [250, 350, 260, 470, 170, 250]
      },
      pieChartData: [
        { value: 250, name: '0-17岁' },
        { value: 350, name: '18-23岁' },
        { value: 260, name: '24-30岁' },
        { value: 470, name: '31-40岁' },
        { value: 170, name: '41-50岁' },
        { value: 250, name: '50岁+' }
      ]
    }
  },
  components: {
    BarChart,
    PieChart
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
