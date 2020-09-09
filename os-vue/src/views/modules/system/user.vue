<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :xs="24" :sm="8" :md="7" :lg="4" :xl="4">
        <tree-card :data="treeData" @tree-click="handleTreeClick" />
      </el-col>
      <el-col
        :class="device ==='mobile'?'mobile-el-col':''"
        :xs="24"
        :sm="16"
        :md="15"
        :lg="20"
        :xl="20"
      >
        <el-card class="box-card">
          <el-form
            ref="queryParam"
            :inline="true"
            :model="queryParam"
            @keyup.enter.native="fetchData()"
          >
            <el-form-item>
              <el-input v-model="queryParam.username" prop="username" placeholder="用户名" clearable />
            </el-form-item>
            <el-form-item>
              <el-input v-model="queryParam.mobile" prop="mobile" placeholder="手机号" clearable />
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
            <el-table-column label="状态" align="center" prop="enabled" width="150">
              <template slot-scope="{row}">
                <el-switch
                  v-model="row.enabled"
                  :active-icon-class="row.enabled?'el-icon-success':'el-icon-error'"
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
              <el-form ref="dataForm" :rules="rules" :model="dataForm" label-width="80px">
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
                <el-form-item label="状态" size="mini" prop="enabled">
                  <el-switch v-model="dataForm.enabled" />
                </el-form-item>
                <el-form-item label="角色配置" prop="roleIds">
                  <el-select
                    v-model="dataForm.roleIds"
                    multiple
                    placeholder="请选择"
                    style="width: 100%;"
                    @change="handleSelectChange"
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
import { add, del, update, getList } from '@/api/system/user'
import { option } from '@/api/system/role'
import { getTree, getMap } from '@/api/system/dept'
import TreeCard from './components/TreeCard'
import { mapGetters } from 'vuex'

export default {
  components: { TreeCard },
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
    var validateComfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.dataForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
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
        enabled: true,
        roleIds: [],
        deptIds: [],
        name: '',
        idNumber: '',
        email: ''
      },
      // 表单校验
      rules: {
        username: [
          { required: true, message: '用户名称不能为空', trigger: 'blur' }
        ],
        deptIds: [
          { required: true, message: '所属部门不能为空', trigger: 'blur' }
        ],
        roleIds: [
          { required: true, message: '所属角色不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '用户密码不能为空', trigger: 'blur' }
        ],
        comfirmPassword: [
          { required: true, validator: validateComfirmPassword, trigger: 'blur' }
        ],
        email: [
          { message: '邮箱地址不能为空', trigger: 'blur' },
          {
            type: 'email',
            message: "'请输入正确的邮箱地址",
            trigger: ['blur', 'change']
          }
        ],
        mobile: [
          { required: true, message: '手机号码不能为空', trigger: 'blur' },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: '请输入正确的手机号码',
            trigger: 'blur'
          }
        ]
      },
      treeData: [],
      ids: [],
      deptMap: [],
      roleOptions: [],
      list: null,
      listLoading: true,
      total: 0
    }
  },
  computed: {
    ...mapGetters([
      'device'
    ])
  },
  watch: {
    'dataForm.roleIds': {
      handler: function(newV, oldV) {

      },
      deep: true
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
      this.rules.password[0].required = true
      this.rules.comfirmPassword[0].required = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        console.log(this.dataForm)
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
      this.rules.password[0].required = false
      this.rules.comfirmPassword[0].required = false
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        this.dataForm = JSON.parse(JSON.stringify(scope.row))
      })
    },
    // 多选
    handleSelectionChange(ids) {
      this.ids = ids.map(item => {
        return item.id
      })
    },
    handleTreeClick(val) {
      this.queryParam.deptId = val
      this.fetchData()
    },
    handleCascaderChange(val) {
      const parentId = val[val.length - 1]
      this.dataForm.deptId = parentId
      this.dataForm.deptIds = val
      this.$refs.dataForm.validateField('deptIds')
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
    },
    handleSelectChange(val) {
      this.$refs.dataForm.validateField('roleIds')
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
