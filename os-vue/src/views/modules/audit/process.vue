<template>
  <div class="app-container">
    <div v-if="!designVisible">
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
            <el-button type="primary" @click="handleDesign()">流程设计</el-button>
            <el-button type="success" @click="handleAdd()">上传流程</el-button>
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
          <el-form-item label="上传流程" prop="userId">
            <el-upload
              ref="upload"
              class="upload-demo"
              :headers="{'X-Token':token}"
              :action="uploadUrl"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :file-list="fileList"
              :auto-upload="false"
            >
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
              <div slot="tip" class="el-upload__tip">只能上传流程文件，且不超过20M</div>
            </el-upload>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </div>
</template>

<script>

import { getList, activate, suspend, del } from '@/api/audit/process'
import { getToken } from '@/utils/auth'

export default {
  data() {
    return {
      uploadUrl: `http://localhost:9090/dev-api/audit/process`,
      token: getToken(),
      dialogVisible: false,
      designVisible: false,
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
      del([row.deploymentId]).then((response) => {
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
    submitUpload() {
      this.$refs.upload.submit()
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    handlePreview(file) {
      console.log(file)
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
        return item.deploymentId
      }
      )
    },
    // 文件上传
    handleAvatarSuccess(response) {
      if (response.errorCode) {
        this.$message.error(response.userTips)
      } else {
        this.$message.success(response.userTips)
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
