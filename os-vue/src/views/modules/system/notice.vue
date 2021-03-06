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
          <el-input v-model="queryParam.enabled" placeholder="状态" clearable />
        </el-form-item>
        <el-form-item>
          <el-button @click="fetchData()">查询</el-button>
          <el-button type="info" @click="resetQueryFields()">重置</el-button>
          <el-button type="primary" @click="handleAdd()">新增</el-button>
        </el-form-item>
        <el-form-item>
          <el-button
            plain
            type="danger"
            :disabled="ids.length <= 0"
            @click="handleBatchDelete()"
          >批量删除</el-button>
        </el-form-item>
      </el-form>

      <el-table
        v-loading="listLoading"
        :data="list"
        element-loading-text="Loading"
        fit
        highlight-current-row
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" header-align="center" align="center" width="50" />
        <el-table-column label="ID" prop="id" />
        <el-table-column label="发送人" prop="sender" />
        <el-table-column label="消息内容" prop="content" />
        <el-table-column label="推送时间" prop="pushTime" />
        <el-table-column label="类型" prop="type" />
        <el-table-column label="状态" align="center" prop="enabled" width="150">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.enabled"
              :active-icon-class="row.enabled?'el-icon-success':'el-icon-error'"
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
      <el-form ref="dataForm" :rules="rules" :model="dataForm" label-width="80px">
        <el-form-item v-show="false" label="ID" prop="id" />
        <el-form-item label="消息内容" prop="content">
          <el-input v-model="dataForm.content" placeholder="请输入消息内容" />
        </el-form-item>
        <el-form-item label="推送时间" prop="pushTime">
          <el-date-picker
            v-model="dataForm.pushTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择推送时间"
            style="width:100%"
          />
        </el-form-item>
        <el-form-item label="状态" prop="enabled">
          <el-switch v-model="dataForm.enabled" />
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
import { add, del, update, getList } from '@/api/system/notice'

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
        enabled: '',
        currentPage: 1,
        pageSize: 10
      },
      dataForm: {
        id: '',
        senderId: '',
        content: '',
        pushTime: '',
        type: '1',
        enabled: true
      },
      rules: {
        content: [
          { required: true, message: '消息内容不能为空', trigger: 'blur' }
        ]
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
    resetQueryFields() {
      this.queryParam = {}
      this.fetchData()
    },
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          let request
          if (this.dataForm.id) {
            request = update(this.dataForm)
          } else {
            request = add(this.dataForm)
          }
          request.then((response) => {
            this.dialogVisible = false
            this.fetchData()
            this.$message({ message: response.userTips, type: 'success' })
          })
        }
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
        this.$message({ message: response.userTips, type: 'success' })
      })
    },
    handleBatchDelete() {
      del(this.ids).then((response) => {
        this.fetchData()
        this.$message({ message: response.userTips, type: 'success' })
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
        this.$message({ message: response.userTips, type: 'success' })
      }, (err) => {
        console.log(err)
        row.enabled = true
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
