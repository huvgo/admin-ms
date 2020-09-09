<template>
  <div class="app-container">
    <el-card>
      <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
        <el-form-item>
          <el-input v-model="queryParam.name" placeholder="名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button @click="fetchData()">查询</el-button>
          <el-button type="info" @click="resetQueryFields()">重置</el-button>
          <el-button type="primary" @click="handleAdd()">新增</el-button>
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
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-table class="deep" :data="scope.row.options">
              <el-table-column align="center" prop="label" label="选项名" />
              <el-table-column align="center" prop="value" label="值" />
              <el-table-column align="center" prop="code" label="编码">
                <template slot-scope="{row}">{{ row.code?row.code:'未设置' }}</template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column label="名称" prop="name" width="150" />
        <el-table-column label="编码" prop="code" width="150" />

        <el-table-column label="备注" prop="remarks" />
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
        <el-form-item label="名称" prop="name">
          <el-input v-model="dataForm.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="dataForm.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="dataForm.remarks"
            type="textarea"
            maxlength="140"
            placeholder="请输入备注"
            show-word-limit
          />
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-plus" @click="handleAddOption()">添加新选项</el-button>
        </el-form-item>
        <el-form-item :label="'选项'" prop="options" :inline="true">
          <div v-for="(item,index) in dataForm.options" :key="index">
            <el-input
              v-model="item.label"
              placeholder="请输入选项名称"
              style="width:20%;margin-right:5px;margin-top:10px"
            />
            <el-input
              v-model="item.value"
              placeholder="请输入值"
              style="width:20%;margin-right:5px;margin-top:10px"
            />
            <el-input
              v-model="item.code"
              placeholder="请输入编码"
              style="width:20%;margin-right:5px;margin-top:10px"
            />
            <el-button type="warn" @click="handleDelOption(index)">删除</el-button>
          </div>
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
import { add, del, update, getList } from '@/api/system/dictionary'

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
        name: '',
        currentPage: 1,
        pageSize: 10
      },
      dataForm: {
        id: '',
        parentId: '',
        code: '',
        name: '',
        sort: '',
        remarks: '',
        options: [
          {
            label: '',
            value: '',
            code: ''
          }
        ]
      },
      rules: {
        name: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '编码不能为空', trigger: 'blur' }
        ],
        options: [
          { required: true, message: '选项不能为空', trigger: 'blur' }
        ]
      },
      option: {
        label: '',
        value: '',
        code: ''
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
    handleAddOption() {
      this.dataForm.options.push(JSON.parse(JSON.stringify(this.option)))
      this.$refs.dataForm.validateField('options')
    },
    handleDelOption(index) {
      this.dataForm.options.splice(index, 1)
      this.$refs.dataForm.validateField('options')
    }
  }

}
</script>

<style scoped>
.deep >>> .el-table__body th,
.deep >>> .el-table__body td,
.deep >>> .el-table__header th,
.deep >>> .el-table__header td {
  border: 0 solid #ffffff !important;
}
</style>
