<template>
  <el-popover placement="bottom" trigger="click">
    <el-tabs>
      <el-tab-pane label="通告">
        <el-table
          v-loading="listLoading"
          :data="list"
          element-loading-text="Loading"
          border
          fit
          stripe="true"
          highlight-current-row
          :show-header="false"
        >
          <el-table-column prop="sender" width="60" />
          <el-table-column prop="content" />
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="配置管理">消息</el-tab-pane>
      <el-tab-pane label="角色管理">代办清单</el-tab-pane>
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
import { notice } from '@/api/sys/user'

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
      notice().then((response) => {
        this.list = response.data
        console.log(response)
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
