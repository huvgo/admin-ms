<template>
  <el-popover placement="bottom" width="350" trigger="click">
    <el-tabs :stretch="true">
      <el-tab-pane label="通告">
        <div style="height: 420px;">
          <el-table
            v-show="list.length>0"
            :data="list"
            fit
            highlight-current-row
            :show-header="false"
          >
            <el-table-column width="60">
              <template slot-scope="scope">
                <el-avatar :src="scope.row.senderAvatar" />
              </template>
            </el-table-column>
            <el-table-column prop="content">
              <template slot-scope="scope">
                <span style="font-size:14px;font-width:500">{{ scope.row.content }}</span>
                <br />
                <span style="font-size:10px">{{ scope.row.pushTime }}</span>
              </template>
            </el-table-column>
          </el-table>
          <a v-show="list.length>0">
            <div class="admin-notice-clear" @click="clearNotice()">清空通告</div>
          </a>
        </div>
      </el-tab-pane>
    </el-tabs>

    <div slot="reference">
      <el-badge v-if="list.length>0" :value="list.length" class="bell-badge">
        <i class="el-icon-bell"></i>
      </el-badge>
      <i v-else class="el-icon-bell"></i>
    </div>
  </el-popover>
</template>

<script>
import { cleearNotice } from '@/api/system/user'
import { getToken } from '@/utils/auth'

export default {
  name: 'MessageBell',
  data() {
    return {
      list: [],
      isFullscreen: false
    }
  },
  mounted() {
    if ('WebSocket' in window) {
      this.websocket = new WebSocket('ws://' + process.env.VUE_APP_SOCKET_API + '/user/notice/' + getToken())
      this.initWebSocket()
    } else {
      alert('当前浏览器 Not support websocket')
    }
  },
  beforeDestroy() {
    this.onbeforeunload()
  },
  methods: {
    clearNotice() {
      cleearNotice().then((response) => {
        this.list = []
      })
    },
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
      this.list = JSON.parse(event.data).data
    },
    setOncloseMessage() {
      console.log('WebSocket连接关闭    状态码：' + this.websocket.readyState)
    },
    onbeforeunload() {
      this.closeWebSocket()
    },
    closeWebSocket() {
      this.websocket.close()
    }
  }
}
</script>

<style scoped>
.bell-badge >>> .el-badge__content.is-fixed {
  top: 10px;
}

.el-popover {
  height: 500px;
  overflow: auto;
}

.admin-notice-clear{
  background: #fff;
  color:#586cb1;
  font-size: 14px;
  font-weight: 500;
  overflow: hidden;
  z-index: 9999;
  position: absolute;
  padding:5px;
  text-align:center;
  width: 100%;
  height: 28px;
  border-top: 1px solid #f0f0f0;
  bottom: 0;
  line-height: 28px;
}
</style>
