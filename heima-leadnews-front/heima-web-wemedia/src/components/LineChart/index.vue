<template>
  <div class="chart-content">
    <!-- TODO: 样式细化 -->
    <header>{{ chartData.title }}</header>
    <div ref="chart" :style="{height:height,width:width}" />
  </div>
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
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: false
    }
  },
  data () {
    return {
      chart: null,
      sidebarElm: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler (val) {
        this.setOptions(val)
      }
    }
  },
  mounted () {
    this.initChart()
    // 监听侧边栏的变化
    this.sidebarElm = document.getElementsByClassName('sidebar-container')[0]
    this.sidebarElm && this.sidebarElm.addEventListener('transitionend', this.sidebarResizeHandler)
  },
  beforeDestroy () {
    if (!this.chart) {
      return
    }
    if (this.autoResize) {
      window.removeEventListener('resize', this.__resizeHandler)
    }

    this.sidebarElm && this.sidebarElm.removeEventListener('transitionend', this.sidebarResizeHandler)

    this.chart.dispose()
    this.chart = null
  },
  methods: {
    sidebarResizeHandler (e) {
      if (e.propertyName === 'width') {
        this.__resizeHandler()
      }
    },
    setOptions (lineOption) {
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'line',
            z: 1,
            lineStyle: {
              type: 'dashed'
            }
          },
          // TODO: 传参进来
          formatter: '{b0}<br />累计阅读量&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{c0}次',
          backgroundColor: 'rgba(0, 0, 0, 0.67)'
        },
        grid: {
          left: '0',
          right: '20',
          top: '30',
          bottom: '0',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: lineOption.xAxisData,
          axisLine: {
            lineStyle: {
              color: '#D8DDE3'
            }
          },
          axisLabel: {
            interval: 1,
            formatter: function (value, index) {
              return value.split(' ')[1]
            },
            color: '#58637D',
            fontWeight: 600
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#D8DDE3'
            }
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#58637D',
            fontWeight: 600
          },
          splitLine: {
            lineStyle: {
              color: '#D8DDE3',
              type: 'dashed'
            }
          }
        },
        series: [{
          data: lineOption.seriesData,
          type: 'line',
          symbol: 'circle',
          symbolSize: 8,
          itemStyle: {
            color: '#3BD396'
          },
          lineStyle: {
            color: '#3BD396'
          }
        }]
      })
    },
    initChart () {
      this.chart = echarts.init(this.$refs.chart)
      this.setOptions(this.chartData)
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

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

.chart-content {
  margin-top: 50px;
}
</style>
