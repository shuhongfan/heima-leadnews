<template>
  <div ref="chart" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'

export default {
  props: {
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    chartData: {
      type: Array
    }
  },
  data () {
    return {
      chart: null
    }
  },
  watch: {
    data: function (val) {
      this.initChart()
    }
  },
  mounted () {
    this.initChart()
  },
  beforeDestroy () {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart () {
      this.chart = echarts.init(this.$refs.chart)
      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b} {c}<br/>占比：{d}%',
          backgroundColor: 'rgba(0, 0, 0, 0.67)'
        },
        legend: {
          orient: 'vertical',
          top: 'middle',
          left: 400,
          itemGap: 25,
          itemWidth: 12,
          itemHeight: 12,
          textStyle: {
            color: '#58637D',
            padding: [0, 0, 0, 14]
          }
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '60%'],
            avoidLabelOverlap: true,
            label: {
              show: false
            },
            labelLine: {
              show: false
            },
            data: this.chartData,
            center: [250, '50%']
          }
        ],
        color: ['#5B8FF9', '#5AD8A6', '#5D7092', '#F6BD16', '#E8684A', '#6DC8EC']
      })
    }
  }
}
</script>
