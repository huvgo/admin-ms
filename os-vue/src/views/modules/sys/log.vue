<template>
  <div class="app-container">
    <el-card class="box-card">
      <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
        <el-form-item>
          <el-input v-model="queryParam.operation" placeholder="用户操作" clearable />
        </el-form-item>
        <el-form-item>
          <el-input v-model="queryParam.method" placeholder="请求方法" clearable />
        </el-form-item>
        <el-form-item>
          <el-input v-model="queryParam.params" placeholder="请求参数" clearable />
        </el-form-item>
        <el-form-item>
          <el-button @click="fetchData()">查询</el-button>
          <el-button type="primary" @click="handleAdd()">新增</el-button>
          <el-button type="danger" :disabled="ids.length <= 0" @click="handleBatchDelete()">批量删除</el-button>
        </el-form-item>
      </el-form>

      <el-table
        v-loading="listLoading"
        :data="list"
        element-loading-text="Loading"
        border
        fit
        highlight-current-row
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" header-align="center" align="center" width="50" />
        <el-table-column label="操作用户" prop="operationUserId" />
        <el-table-column label="用户操作" prop="operation" />
        <el-table-column label="请求方法" prop="method" />
        <el-table-column label="请求参数" prop="params" />
        <el-table-column label="执行时长(毫秒)" prop="time" />
        <el-table-column label="IP地址" prop="ip" />
        <el-table-column label="创建时间" prop="createDate" />
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope)">修改</el-button>
            <el-button type="danger" size="mini" @click="handleDelete(scope)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page="queryParam.currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryParam.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
    <el-dialog :visible.sync="dialogVisible" :title="'新增'">
      <el-form ref="dataForm" :model="dataForm" label-width="80px" label-position="left">
        <el-form-item v-show="false" label="ID" prop="id" />
        <el-form-item v-show="false" label="ID" prop="id" />
        <el-form-item label="操作用户ID" prop="operationUserId">
          <el-input v-model="dataForm.operationUserId" placeholder="请输入操作用户ID" />
        </el-form-item>
        <el-form-item label="用户操作" prop="operation">
          <el-input v-model="dataForm.operation" placeholder="请输入用户操作" />
        </el-form-item>
        <el-form-item label="请求方法" prop="method">
          <el-input v-model="dataForm.method" placeholder="请输入请求方法" />
        </el-form-item>
        <el-form-item label="请求参数" prop="params">
          <el-input v-model="dataForm.params" placeholder="请输入请求参数" />
        </el-form-item>
        <el-form-item label="执行时长(毫秒)" prop="time">
          <el-input v-model="dataForm.time" placeholder="请输入执行时长(毫秒)" />
        </el-form-item>
        <el-form-item label="IP地址" prop="ip">
          <el-input v-model="dataForm.ip" placeholder="请输入IP地址" />
        </el-form-item>
        <el-form-item label="创建时间" prop="createDate">
          <el-date-picker
            v-model="dataForm.createDate"
            style="width:100%"
            type="date"
            format="yyyy-MM-dd"
            placeholder="选择用户操作日期"
          />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { add, del, update, getList } from '@/api/sys/log'

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      dialogVisible: false,
      queryParam: {
        operation: '',
        method: '',
        params: '',
        currentPage: 1,
        pageSize: 10
      },
      dataForm: {
        id: '',
        operationUserId: '',
        operation: '',
        method: '',
        params: '',
        time: '',
        ip: '',
        createDate: ''
      },
      ids: [],
      list: null,
      listLoading: true,
      total: 0
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getList(this.queryParam).then((response) => {
        this.list = response.data.records
        this.total = response.data.total
        this.listLoading = false
      })
    },
    dataFormSubmit() {
      let request
      if (this.dataForm.id) {
        request = update(this.dataForm)
      } else {
        request = add(this.dataForm)
      }
      request.then((response) => {
        this.dialogVisible = false
        this.fetchData()
        this.$message({ message: response.message, type: 'success' })
      })
    },
    handleSizeChange(pageSize) {
      this.queryParam.pageSize = pageSize
      this.fetchData()
    },
    handleCurrentChange(currentPage) {
      this.queryParam.currentPage = currentPage
      this.fetchData()
    },
    handleAdd() {
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
      })
    },
    handleDelete({ $index, row }) {
      del([row.id]).then((response) => {
        this.fetchData()
        this.$message({ message: response.message, type: 'success' })
      })
    },
    handleBatchDelete() {
      del(this.ids).then((response) => {
        this.fetchData()
        this.$message({ message: response.message, type: 'success' })
      })
    },
    handleEdit(scope) {
      this.dialogVisible = true
      this.$nextTick(() => {
        this.dataForm = JSON.parse(JSON.stringify(scope.row))
      })
    },
    // 多选
    handleSelectionChange(ids) {
      this.ids = ids.map(item => {
        return item.id
      })
    }
  }
}
</script>

<style scoped>
.el-dialog {
  padding: 10px 20px;
}
.el-card {
  border: 0 solid #ffffff;
}
</style>
