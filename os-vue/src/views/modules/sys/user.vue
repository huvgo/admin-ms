<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="4">
        <menu-tree :data="treeData" @tree-click="handleTreeClick" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
            <el-form-item>
              <el-input v-model="queryParam.username" placeholder="用户名" clearable />
            </el-form-item>
            <el-form-item>
              <el-input v-model="queryParam.mobile" placeholder="手机号" clearable />
            </el-form-item>
            <el-form-item>
              <el-button @click="fetchData()">查询</el-button>
              <el-button type="primary" @click="handleAdd()">新增</el-button>
              <el-button
                plain
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
            fit
            highlight-current-row
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" header-align="center" align="center" width="50" />
            <el-table-column label="账号" prop="username" />
            <el-table-column label="角色" prop="roleIds">
              <template slot-scope="scope">{{ scope.row.roleIds |arrayFilter(roleOptions) }}</template>
            </el-table-column>
            <el-table-column label="部门" prop="deptId">
              <template slot-scope="scope">{{ deptMap[scope.row.deptId] }}</template>
            </el-table-column>
            <el-table-column label="姓名" prop="name" />
            <el-table-column label="手机号" prop="mobile" />
            <el-table-column label="头像" align="center" prop="avatar" width="120">
              <template slot-scope="{row}">
                <el-image style="width: 60px; height: 60px" :src="row.avatar">
                  <div slot="error"></div>
                </el-image>
              </template>
            </el-table-column>
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
            <el-table-column align="center" label="操作" width="250">
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
      </el-col>
    </el-row>

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
                <el-form-item label="账号" prop="username">
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
                <el-form-item label="状态" size="mini" prop="status">
                  <el-switch v-model="dataForm.status" />
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
                      :label="item.name"
                      :value="item.id"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="所属部门" prop="deptIds">
                  <el-cascader
                    ref="cascader"
                    v-model="dataForm.deptIds"
                    :options="treeData"
                    :props="{ checkStrictly: true,label:'name',value:'id',expandTrigger:'hover' }"
                    clearable
                    :show-all-levels="true"
                    style="width:100%"
                    @change="handleCascaderChange"
                  />
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
import { getTree, getMap } from '@/api/sys/dept'
import MenuTree from './components/MenuTree'
export default {
  components: { MenuTree },
  filters: {
    statusFilter(status) {
      const statusMap = {
        true: 'success',
        draft: 'info',
        false: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      dialogVisible: false,
      queryParam: {
        username: '',
        deptId: '',
        mobile: '',
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
      treeData: [],
      multipleSelection: [],
      deptMap: [],
      roleOptions: [],
      list: null,
      listLoading: true,
      total: 0
    }
  },
  created() {
    this.fetchDeptTreeData()
    this.fetchOptionData()
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
    fetchDeptTreeData() {
      getTree().then((response) => {
        this.treeData = response.data
      })
    },
    fetchOptionData() {
      getMap().then((response) => {
        this.deptMap = response.data
      })
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
    },
    handleTreeClick(val) {
      this.queryParam.deptId = val
      this.fetchData()
    },
    handleCascaderChange(val) {
      const parentId = val[val.length - 1]
      this.dataForm.deptId = parentId
      this.dataForm.deptIds = val
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
.padd >>> .el-dialog__body {
  padding-top: 15px;
}
</style>
