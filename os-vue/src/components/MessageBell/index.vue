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
import screenfull from 'screenfull'
import { getNotice, cleearNotice } from '@/api/sys/user'

export default {
  name: 'Screenfull',
  data() {
    return {
      list: [],
      isFullscreen: false
    }
  },
  mounted() {
    this.init()
  },
  beforeDestroy() {
    this.destroy()
  },
  methods: {
    click() {
      if (!screenfull.enabled) {
        this.$message({
          message: 'you browser can not work',
          type: 'warning'
        })
        return false
      }
      screenfull.toggle()
    },
    change() {
      this.isFullscreen = screenfull.isFullscreen
    },
    init() {
      if (screenfull.enabled) {
        screenfull.on('change', this.change)
      }
      this.getNotice()
      setInterval(() => {
        this.getNotice()
      }, 30000)
    },
    getNotice() {
      getNotice().then((response) => {
        this.list = response.data
      })
    },
    clearNotice() {
      cleearNotice().then((response) => {
        if (response.code === 20000) {
          this.list = []
        }
      })
    },
    destroy() {
      if (screenfull.enabled) {
        screenfull.off('change', this.change)
      }
    }
  }
}
</script>

<style scoped>
.screenfull-svg {
  display: inline-block;
  cursor: pointer;
  fill: #5a5e66;
  width: 20px;
  height: 20px;
  vertical-align: 10px;
}
.bell-badge >>> .el-badge__content.is-fixed {
  top: 10px;
}
</style>
<style>
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
