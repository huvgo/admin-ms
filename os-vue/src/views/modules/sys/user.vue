<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
      <el-form-item>
        <el-input v-model="queryParam.username" placeholder="用户名" clearable />
      </el-form-item>
      <el-form-item>
        <el-button @click="fetchData()">查询</el-button>
        <el-button type="primary" @click="handleAdd()">新增</el-button>
        <el-button
          type="danger"
          :disabled="multipleSelection.length <= 0"
          @click="handleBatchDelete()"
        >批量删除</el-button>
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
      <el-table-column label="用户名" prop="username" />
      <el-table-column label="姓名" prop="name" />
      <el-table-column label="手机号" prop="mobile" />
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

    <el-dialog class="padd" :visible.sync="dialogVisible" :title="'新增'">
      <el-row :gutter="15">
        <el-col :span="24">
          <el-card class="box-card" shadow="never">
            <div slot="header" class="clearfix">
              <span>账号信息</span>
            </div>
            <div class="component-item">
              <el-form ref="dataForm" :model="dataForm" label-width="80px" label-position="left">
                <el-form-item v-show="false" label="ID" prop="id" />
                <el-form-item label="昵称" prop="username">
                  <el-input v-model="dataForm.username" placeholder="请输入昵称" />
                </el-form-item>
                <el-form-item label="手机号" prop="mobile">
                  <el-input v-model="dataForm.mobile" placeholder="请输入手机号" />
                </el-form-item>
                <el-form-item label="密码" prop="password">
                  <el-input v-model.trim="dataForm.password" type="password" placeholder="请输入密码" />
                </el-form-item>
                <el-form-item label="确认密码" prop="comfirmPassword">
                  <el-input
                    v-model.trim="dataForm.comfirmPassword"
                    type="password"
                    placeholder="确认密码"
                  />
                </el-form-item>
                <el-form-item label="允许登陆" size="mini" prop="status">
                  <el-switch
                    v-model="dataForm.status"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                  />
                </el-form-item>
                <el-form-item label="角色配置" prop="roleIds">
                  <el-select
                    v-model="dataForm.roleIds"
                    multiple
                    placeholder="请选择"
                    style="width: 100%;"
                  >
                    <el-option
                      v-for="item in roleOptions"
                      :key="item.id"
                      :label="item.roleName"
                      :value="item.id"
                    />
                  </el-select>
                </el-form-item>
              </el-form>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="24">
          <el-card class="box-card" shadow="never">
            <div slot="header" class="clearfix">
              <span>基本信息</span>
            </div>
            <div class="component-item">
              <el-form ref="dataForm2" :model="dataForm" label-width="80px" label-position="left">
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="dataForm.name" placeholder="请输入姓名" />
                </el-form-item>
                <el-form-item label="身份证号" prop="idNumber">
                  <el-input v-model.trim="dataForm.idNumber" placeholder="身份证号" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model.trim="dataForm.email" placeholder="邮箱" />
                </el-form-item>
              </el-form>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { add, del, update, getList } from '@/api/sys/user'
import { option } from '@/api/sys/role'

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
      options: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
        label: '蚵仔煎'
      }, {
        value: '选项4',
        label: '龙须面'
      }, {
        value: '选项5',
        label: '北京烤鸭'
      }],
      dialogVisible: false,
      queryParam: {
        currentPage: 1,
        pageSize: 10
      },
      dataForm: {
        id: '',
        username: '',
        mobile: '',
        password: '',
        comfirmPassword: '',
        status: true,
        roleIds: [],
        name: '',
        idNumber: '',
        email: ''
      },
      multipleSelection: [],
      roleOptions: [],
      list: null,
      listLoading: true,
      total: 0
    }
  },
  created() {
    this.fetchData()
    this.fetchRoleData()
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
    fetchRoleData() {
      option().then((response) => {
        this.roleOptions = response.data
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
        this.$refs['dataForm2'].resetFields()
        console.log(this.dataForm)
      })
    },
    handleDelete({ $index, row }) {
      del([row.id]).then((response) => {
        this.fetchData()
        this.$message({ message: response.message, type: 'success' })
      })
    },
    handleBatchDelete() {
      const ids = this.multipleSelection.map(item => { return item.id })
      del(ids).then((response) => {
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
    handleSelectionChange(val) {
      this.multipleSelection = val
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
.padd >>> .el-dialog__body {
  padding-top: 15px;
}
</style>
