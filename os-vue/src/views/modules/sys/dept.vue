<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
      <el-form-item>
        <el-input v-model="queryParam.id" placeholder="ID" clearable />
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
      <el-table-column label="部门ID" prop="id" />
      <el-table-column label="上级部门ID" prop="parent_id" />
      <el-table-column label="所有上级部门ID" prop="parent_ids" />
      <el-table-column label="部门名称" prop="name" />
      <el-table-column label="机构类型" prop="type" />
      <el-table-column label="排序" prop="sort" />
      <el-table-column label="联系电话" prop="mobile" />
      <el-table-column label="逻辑删除" prop="status" />
      <el-table-column label="创建者ID" prop="create_user_id" />
      <el-table-column label="创建时间" prop="create_time" />
      <el-table-column label="修改者ID" prop="modify_user_id" />
      <el-table-column label="修改时间" prop="modify_time" />
      <el-table-column label="是否删除  1：已删除  0：正常" prop="deleted" />
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

    <el-dialog :visible.sync="dialogVisible" :title="'新增'">
      <el-form ref="dataForm" :model="dataForm" label-width="80px" label-position="left">
        <el-form-item v-show="false" label="ID" prop="id" />
        <el-form-item label="上级部门ID" prop="parent_id">
          <el-input v-model="dataForm.parent_id" placeholder="请输入上级部门ID" />
        </el-form-item>
        <el-form-item label="所有上级部门ID" prop="parent_ids">
          <el-input v-model="dataForm.parent_ids" placeholder="请输入所有上级部门ID" />
        </el-form-item>
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="dataForm.name" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="机构类型" prop="type">
          <el-input v-model="dataForm.type" placeholder="请输入机构类型" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="dataForm.sort" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="联系电话" prop="mobile">
          <el-input v-model="dataForm.mobile" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="逻辑删除" prop="status">
          <el-input v-model="dataForm.status" placeholder="请输入逻辑删除" />
        </el-form-item>
        <el-form-item label="创建者ID" prop="create_user_id">
          <el-input v-model="dataForm.create_user_id" placeholder="请输入创建者ID" />
        </el-form-item>
        <el-form-item label="创建时间" prop="create_time">
          <el-input v-model="dataForm.create_time" placeholder="请输入创建时间" />
        </el-form-item>
        <el-form-item label="修改者ID" prop="modify_user_id">
          <el-input v-model="dataForm.modify_user_id" placeholder="请输入修改者ID" />
        </el-form-item>
        <el-form-item label="修改时间" prop="modify_time">
          <el-input v-model="dataForm.modify_time" placeholder="请输入修改时间" />
        </el-form-item>
        <el-form-item label="是否删除  1：已删除  0：正常" prop="deleted">
          <el-input v-model="dataForm.deleted" placeholder="请输入是否删除  1：已删除  0：正常" />
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
import { add, del, update, getList } from '@/api/sys/dept'

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
        currentPage: 1,
        pageSize: 10
      },
      dataForm: {
        id: '',
        parent_id: '',
        parent_ids: '',
        name: '',
        type: '',
        sort: '',
        mobile: '',
        status: '',
        create_user_id: '',
        create_time: '',
        modify_user_id: '',
        modify_time: '',
        deleted: ''
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
