<template>
  <div class="app-container">
    <el-card class="box-card">
      <el-table
        v-loading="listLoading"
        :data="list"
        element-loading-text="Loading"
        fit
        highlight-current-row
      >
        <el-table-column label="ID" prop="apply.id" />
        <el-table-column label="申请人" prop="apply.other.applicantName" />
        <el-table-column label="备注" prop="apply.ext.content" />
        <el-table-column label="申请时间" prop="apply.ext.startTime" />
        <el-table-column label="类型" prop="apply.ext.leaveType" />
        <el-table-column label="创建时间" prop="apply.createTime" />
        <el-table-column label="更新时间" prop="apply.updateTime" />
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleAdd(scope)">审批</el-button>
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
        <el-form-item label="处理意见" prop="id">
          <el-input v-model="dataForm.id" placeholder="请输入处理意见" />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button @click="dialogVisible=false">返回</el-button>
        <el-button type="warning" @click="dataFormSubmit()">驳回</el-button>
        <el-button type="success" @click="dataFormSubmit()">通过</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getMyToDo, apply, approve } from '@/api/process/instance'

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
        currentPage:
            1,
        pageSize:
            10
      },
      dataForm: {
        id: '',
        processDefinitionId: '',
        apply: {
          ext: {
            content: ''
          },
          type: ''
        },
        approve: {

        },
        status: '',
        endTime: ''
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
      getMyToDo(this.queryParam).then((response) => {
        this.list = response.data
        this.listLoading = false
      }
      )
    },
    dataFormSubmit() {
      let request
      if (this.dataForm.id) {
        request = approve(this.dataForm)
      } else {
        request = apply(this.dataForm)
      }
      request.then((response) => {
        this.dialogVisible = false
        this.fetchData()
        this.$message({ message: response.message, type: 'success' })
      }
      )
    },
    handleSizeChange(pageSize) {
      this.queryParam.pageSize = pageSize
      this.fetchData()
    },
    handleCurrentChange(currentPage) {
      this.queryParam.currentPage = currentPage
      this.fetchData()
    },
    handleAdd(scope) {
      this.dataForm = scope.row
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
      })
    },
    handleCommit(item) {
      console.log(item.row)
      this.dataForm2.instanceId = item.row.id
      this.dataForm2.processId = item.row.processId
      approve(this.dataForm2).then((response) => {
        this.dialogVisible = false
        this.fetchData()
        this.$message({ message: response.message, type: 'success' })
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
