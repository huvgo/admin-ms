<template>
  <div class="app-container">
    <el-card class="box-card">
      <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
        <el-form-item>
          <el-input v-model="queryParam.id" placeholder="ID" clearable />
        </el-form-item>
        <el-form-item>
          <el-button @click="fetchData()">查询</el-button>
          <el-button type="primary" @click="dataFormSubmit()">立即生成</el-button>
          <el-button type="info" @click="handleEdit">其他设置</el-button>
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
        <el-table-column label="表名" prop="tableName" />
        <el-table-column label="注释" prop="tableComment" />
        <el-table-column label="修改时间" prop="createTime" />
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="info" @click="handleEdit(scope)">字段配置</el-button>
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
        <el-form-item label="模块">
          <el-input v-model="form.moduleName" placeholder="模块名称，例如：sys" />
        </el-form-item>
        <el-form-item label="移除表前缀">
          <el-input v-model="form.tablePrefix" placeholder="移除前缀，例如：sys_" />
        </el-form-item>

        <el-form-item label="作者">
          <el-input v-model="form.author" placeholder="注释中的作者信息" />
        </el-form-item>
        <el-form-item label="项目路径">
          <el-input v-model="form.projectPath" placeholder="项目路径，例如：com.company.project.modules" />
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
import { getList, generator } from '@/api/dev/code'

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
      form: {
        moduleName: '',
        tablePrefix: '',
        author: '',
        projectPath: ''
      },
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
      total: 0,
      otherSetting: false
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
      generator(this.dataForm).then((response) => {
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
</style>

