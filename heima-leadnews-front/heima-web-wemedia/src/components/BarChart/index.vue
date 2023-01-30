<template>
  <div ref="chart" :style="{ height: height, width: width }" />
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
    // TODO: 浏览器窗口 resize
    sidebarResizeHandler (e) {
      if (e.propertyName === 'width') {
        this.__resizeHandler()
      }
    },
    setOptions () {
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'none'
          },
          backgroundColor: 'rgba(0, 0, 0, 0.67)'
        },
        grid: {
          left: 0,
          right: 0,
          bottom: 0,
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.chartData.xAxisData,
          axisLine: {
            lineStyle: {
              color: '#D8DDE3',
              width: 2
            }
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#58637D',
            fontWeight: 600,
            fontSize: 12
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#1E1E32',
            fontWeight: 600,
            fontSize: 14
          },
          splitLine: {
            lineStyle: {
              color: '#D8DDE3',
              type: 'dashed'
            }
          }
        },
        series: [
          {
            type: 'bar',
            barWidth: 20,
            itemStyle: {
              color: '#D4DBE8',
              barBorderRadius: [4, 4, 0, 0]
            },
            emphasis: {
              itemStyle: {
                color: '#3175FB'
              }
            },
            data: this.chartData.seriesData
          }
        ]
      })
    },
    initChart () {
      this.chart = echarts.init(this.$refs.chart)
      this.setOptions(this.chartData)
    }
  }
}
</script>
