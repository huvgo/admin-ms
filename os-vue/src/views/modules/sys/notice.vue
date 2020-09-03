<template>
  <div class="app-container">
    <el-card class="box-card">
      <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
        <el-form-item>
          <el-input v-model="queryParam.sender" placeholder="发送人" clearable />
        </el-form-item>
        <el-form-item>
          <el-input v-model="queryParam.type" placeholder="类型" clearable />
        </el-form-item>
        <el-form-item>
          <el-input v-model="queryParam.status" placeholder="状态" clearable />
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
        <el-table-column label="ID" prop="id" />
        <el-table-column label="发送人" prop="sender" />
        <el-table-column label="消息内容" prop="content" />
        <el-table-column label="创建时间" prop="createDate" />
        <el-table-column label="类型" prop="type" />
        <el-table-column label="状态" align="center" prop="status" width="150">
          <template slot-scope="{row}">
            <!-- <el-tag :type="row.status | statusFilter">{{ row.status?'正常':'冻结' }}</el-tag> -->
            <el-switch
              v-model="row.status"
              :active-icon-class="row.status?'el-icon-success':'el-icon-error'"
              @change="handleSwitchChange(row)"
            />
          </template>
        </el-table-column>
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
        <el-form-item label="消息内容" prop="content">
          <el-input v-model="dataForm.content" placeholder="请输入消息内容" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-input v-model="dataForm.type" placeholder="请输入类型" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="dataForm.status" />
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
import { add, del, update, getList } from '@/api/sys/notice'

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
        sender: '',
        type: '',
        status: '',
        currentPage: 1,
        pageSize: 10
      },
      dataForm: {
        id: '',
        senderId: '',
        content: '',
        createDate: '',
        type: '',
        status: true
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
    },
    handleSwitchChange(row) {
      update(row).then((response) => {
        this.dialogVisible = false
        this.fetchData()
        this.$message({ message: response.message, type: 'success' })
      }, (err) => {
        console.log(err)
        row.status = true
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
