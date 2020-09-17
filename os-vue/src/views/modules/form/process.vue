<template>
  <div class="app-container">
    <el-card class="box-card">
      <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
        <el-form-item>
          <el-input v-model="queryParam.userId" placeholder="用户ID" clearable />
        </el-form-item>
        <el-form-item>
          <el-input v-model="queryParam.remark" placeholder="备注" clearable />
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
        fit
        highlight-current-row
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" header-align="center" align="center" width="50" />
        <el-table-column label="名称" prop="name" />
        <el-table-column label="流程文件" prop="resourceName" />
        <el-table-column label="分组" prop="tenantId" />
        <el-table-column label="激活/挂起" align="center" prop="suspensionState" width="150">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.suspensionState"
              :active-icon-class="row.suspensionState?'el-icon-success':'el-icon-error'"
              @change="handleSwitchChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
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
        <el-form-item label="上传" prop="userId">
          <el-upload
            :headers="{'X-Token':token}"
            :action="uploadUrl"
            :show-file-list="false"
            multiple
            :on-success="handleAvatarSuccess"
          >
            <i class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
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

import { getList, activate, suspend, del } from '@/api/form/process'
import { getToken } from '@/utils/auth'

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
      uploadUrl: `http://localhost:9090/dev-api/process/definition`,
      token: getToken(),
      dialogVisible: false,
      queryParam: {
        userId: '',
        remark: '',
        currentPage: 1,
        pageSize:
            10
      },
      dataForm: {
        id: '',
        userId: '',
        remark: '',
        createTime: '',
        updateTime: '' },
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
        this.list = response.data
        this.total = response.data.total
        this.listLoading = false
      }
      )
    },
    handleSwitchChange(row) {
      let request
      if (!row.suspensionState) {
        request = suspend(row.id)
      } else {
        request = activate(row.id)
      }
      request.then((response) => {
        this.dialogVisible = false
        this.fetchData()
        this.$message({ message: response.userTips, type: 'success' })
      }, (err) => {
        console.log(err)
        row.suspensionState = !row.suspensionState
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
      }
      )
    },
    handleDelete({ $index, row }) {
      del([row.id]).then((response) => {
        this.fetchData()
        this.$message({ message: response.message, type: 'success' })
      }
      )
    },
    handleBatchDelete() {
      del(this.ids).then((response) => {
        this.fetchData()
        this.$message({ message: response.message, type: 'success' })
      }
      )
    },
    handleEdit(scope) {
      this.dialogVisible = true
      this.$nextTick(() => {
        this.dataForm = JSON.parse(JSON.stringify(scope.row))
      }
      )
    },
    // 多选
    handleSelectionChange(ids) {
      this.ids = ids.map(item => {
        return item.id
      }
      )
    },
    // 文件上传
    handleAvatarSuccess(response) {
      if (response && response.code === 20000) {
        this.$message.success(response.userTips)
      } else {
        this.$message.error(response.userTips)
      }
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
