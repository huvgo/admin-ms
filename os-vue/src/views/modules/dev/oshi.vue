<template>
  <div class="app-container">
    <el-row :gutter="15">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>CPU信息</span>
          </div>
          <div class="component-item">
            <table v-loading="listLoading" class="table table-striped table-bordered">
              <tr>
                <td class="column">属性</td>
                <td class="column">值</td>
              </tr>
              <tr>
                <td class="value">核心数</td>
                <td class="value">{{ data.cpu.cpuNum }}</td>
              </tr>
              <tr>
                <td class="value">用户使用率</td>
                <td class="value">{{ data.cpu.used }}</td>
              </tr>
              <tr>
                <td class="value">系统使用率</td>
                <td class="value">{{ data.cpu.sys }}</td>
              </tr>
              <tr>
                <td class="value">当前空闲率</td>
                <td class="value">{{ data.cpu.free }}</td>
              </tr>
            </table>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>内存信息</span>
          </div>
          <div class="component-item">
            <table v-loading="listLoading" class="table table-striped table-bordered">
              <tr>
                <td class="column">属性</td>
                <td class="column">内存</td>
                <td class="column">JVM</td>
              </tr>
              <tr>
                <td class="value">总内存</td>
                <td class="value">{{ data.mem.total }}</td>
                <td class="value">{{ data.jvm.total }}</td>
              </tr>
              <tr>
                <td class="value">已用内存</td>
                <td class="value">{{ data.mem.used }}</td>
                <td class="value">{{ data.jvm.used }}</td>
              </tr>
              <tr>
                <td class="value">剩余内存</td>
                <td class="value">{{ data.mem.free }}</td>
                <td class="value">{{ data.jvm.free }}</td>
              </tr>
              <tr>
                <td class="value">使用率</td>
                <td class="value">{{ data.mem.usage }}</td>
                <td class="value">{{ data.jvm.usage }}</td>
              </tr>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="15">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>JAVA虚拟机信息</span>
          </div>
          <div class="component-item">
            <table v-loading="listLoading" class="table table-striped table-bordered">
              <tr>
                <td class="column">Jvm名称</td>
                <td class="value">{{ data.jvm.name }}</td>
                <td class="column">Java版本</td>
                <td class="value">{{ data.jvm.version }}</td>
              </tr>
              <tr>
                <td class="column">启动时间</td>
                <td class="value">{{ data.jvm.startTime }}</td>
                <td class="column">运行时长</td>
                <td class="value">{{ data.jvm.runTime }}</td>
              </tr>
              <tr>
                <td class="column">安装路径</td>
                <td class="value" colspan="3">{{ data.jvm.home }}</td>
              </tr>
              <tr>
                <td class="column">项目路径</td>
                <td class="value" colspan="3">{{ data.sys.userDir }}</td>
              </tr>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="15">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>服务器信息</span>
          </div>
          <div class="component-item">
            <table v-loading="listLoading" class="table table-striped table-bordered">
              <tr>
                <td class="column">服务器名称</td>
                <td class="value">{{ data.sys.computerName }}</td>
                <td class="column">操作系统</td>
                <td class="value">{{ data.sys.osName }}</td>
              </tr>
              <tr>
                <td class="column">服务器IP</td>
                <td class="value">{{ data.sys.computerIp }}</td>
                <td class="column">系统架构</td>
                <td class="value">{{ data.sys.osArch }}</td>
              </tr>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="15">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>磁盘信息</span>
          </div>
          <div class="component-item">
            <table v-loading="listLoading" class="table table-striped table-bordered">
              <tr>
                <td class="column">盘符路径</td>
                <td class="column">文件系统</td>
                <td class="column">盘符类型</td>
                <td class="column">总大小</td>
                <td class="column">可用大小</td>
                <td class="column">已用大小</td>
                <td class="column">已用百分比</td>
              </tr>
              <tr v-for="(item,index) in data.sysFiles" :key="index">
                <td class="value">{{ item.dirName }}</td>
                <td class="value">{{ item.sysTypeName }}</td>
                <td class="value">{{ item.typeName }}</td>
                <td class="value">{{ item.total }}</td>
                <td class="value">{{ item.free }}</td>
                <td class="value">{{ item.used }}</td>
                <td class="value">{{ item.usage }}</td>
              </tr>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getList } from '@/api/device'

export default {
  data() {
    return {
      data: {
        cpu: {},
        mem: {},
        jvm: {},
        sys: {},
        sysFiles: []
      },
      list: [],
      listLoading: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getList().then(response => {
        this.data = response.data
        this.listLoading = false
      })
    }
  }
}
</script>

<style scoped>
.line {
  text-align: center;
}
.el-row {
  margin-bottom: 20px;
}
.table {
  border-collapse: collapse;
  border-spacing: 0;
  background-color: transparent;
  display: table;
  width: 100%;
  max-width: 100%;
  margin: 0 auto;
}
.table td {
  text-align: left;
  vertical-align: middle;
  min-height: 20px;
  line-height: 20px;
  font-family: 'Arial Normal', 'Arial';
  position: relative;
  padding: 9px 15px;
  font-size: 14px;
}
.table-bordered {
  border: 1px solid #e6e6e6;
}
.column {
  width: 30px;
  height: 30px;
  border: 1px solid #e6e6e6;
  background: #f2f2f2;
}
.value {
  width: 30px;
  height: 30px;
  border: 1px solid #e6e6e6;
}
* {
  color: #666;
}
</style>

