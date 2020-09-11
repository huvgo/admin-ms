<template>
  <div :style="{height:'400px',width:'100%'}"></div>
</template>

<script>
import echarts from 'echarts'
import resize from './mixins/resize'
import { getToken } from '@/utils/auth'

export default {
  mixins: [resize],
  data() {
    return {
      chart: null,
      data: []
    }
  },
  created() {
    if ('WebSocket' in window) {
      this.websocket = new WebSocket('ws://' + process.env.VUE_APP_SOCKET_API + '/socket/engine/oshi/' + getToken())
      this.initWebSocket()
    } else {
      alert('当前浏览器 Not support websocket')
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
    this.onbeforeunload()
  },
  methods: {
    initWebSocket() {
      // 连接错误
      this.websocket.onerror = this.setErrorMessage

      // 连接成功
      this.websocket.onopen = this.setOnopenMessage

      // 收到消息的回调
      this.websocket.onmessage = this.setOnmessageMessage

      // 连接关闭的回调
      this.websocket.onclose = this.setOncloseMessage

      // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      window.onbeforeunload = this.onbeforeunload
    },
    setErrorMessage() {
      console.log('WebSocket连接发生错误   状态码：' + this.websocket.readyState)
    },
    setOnopenMessage() {
      console.log('WebSocket连接成功    状态码：' + this.websocket.readyState)
    },
    setOnmessageMessage(event) {
      // 根据服务器推送的消息做自己的业务处理
      console.log('服务端返回：' + event.data)
      if (this.data.length >= 20) {
        this.data.shift()
      }
      this.data.push(JSON.parse(event.data).data)
      const xAxis = this.data.map(function(item) {
        return item[0]
      })
      const jvmData = this.data.map(function(item) {
        return item[1]
      })
      const memoryData = this.data.map(function(item) {
        return item[2]
      })
      const cpuUseData = this.data.map(function(item) {
        return item[3]
      })

      this.setOptions(xAxis, jvmData, memoryData, cpuUseData)
    },
    setOncloseMessage() {
      console.log('WebSocket连接关闭    状态码：' + this.websocket.readyState)
    },
    onbeforeunload() {
      this.closeWebSocket()
    },
    closeWebSocket() {
      this.websocket.close()
    },
    initChart() {
      this.chart = echarts.init(this.$el)
    },
    setOptions(xAxis, jvmData, memoryData, cpuUseData) {
      this.chart.setOption(
        {
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['JVM使用率', '内存使用率', 'CPU当前使用率']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxis
          },
          yAxis: {
            type: 'value',
            splitLine: { show: false }
          },
          series: [
            {
              name: 'JVM使用率',
              type: 'line',
              data: jvmData
            },
            {
              name: '内存使用率',
              type: 'line',
              data: memoryData
            },
            {
              name: 'CPU当前使用率',
              type: 'line',
              data: cpuUseData
            }
          ]
        }
      )
    }
  }
}
</script>
