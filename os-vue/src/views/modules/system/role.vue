<template>
  <div class="app-container">
    <el-card class="box-card">
      <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
        <el-form-item>
          <el-input v-model="queryParam.name" placeholder="角色名称" clearable />
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
        <el-table-column label="角色名称">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="数据权限范围">
          <template slot-scope="scope">{{ scope.row.dataScope }}</template>
        </el-table-column>
        <el-table-column label="备注">
          <template slot-scope="scope">{{ scope.row.remark }}</template>
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
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="dataForm.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="权限范围" prop="dataScope">
          <el-select
            v-model="dataForm.dataScope "
            placeholder="请选择权限范围"
            style="width:100%"
            @change="handleSelectChange"
          >
            <el-option
              v-for="item in dataScopeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-show="dataForm.dataScope == 2" label="数据权限" prop="deptIds">
          <el-tree
            ref="deptTree"
            :data="treeData"
            :props="defaultProps"
            show-checkbox
            node-key="id"
            :expand-on-click-node="false"
            :check-strictly="true"
            :check-on-click-node="true"
            :default-expand-all="true"
            @check="handleDeptNodeClick"
          />
        </el-form-item>
        <el-form-item label="菜单权限" prop="menuIds">
          <el-tree
            ref="menuTree"
            :data="menuList"
            :props="defaultProps"
            show-checkbox
            node-key="id"
            class="permission-tree"
            :expand-on-click-node="false"
            :check-on-click-node="true"
            :default-expand-all="true"
            @check="handleMenuNodeClick"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dataForm.remark" placeholder="请输入备注" />
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
import { add, del, update, getList } from '@/api/system/role'
import { getList as getMenuList } from '@/api/system/menu'
import { getTree } from '@/api/system/dept'
import { treeDataTranslate } from '@/utils'

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
        name: '',
        menuIds: [],
        deptIds: [],
        dataScope: '',
        remark: '',
        deptId: '',
        createUserId: '',
        createTime: ''
      },
      rules: {
        name: [
          { required: true, message: '角色名不能为空', trigger: 'blur' }
        ],
        dataScope: [
          { required: true, message: '权限范围不能为空', trigger: 'blur' }
        ],
        menuIds: [
          { required: true, message: '菜单权限不能为空', trigger: 'blur' }
        ]
      },
      // 数据范围选项
      dataScopeOptions: [
        {
          value: 1,
          label: '全部数据权限'
        },
        {
          value: 2,
          label: '自定数据权限'
        },
        {
          value: 3,
          label: '本部门数据权限'
        },
        {
          value: 4,
          label: '本部门及以下数据权限'
        },
        {
          value: 5,
          label: '仅本人数据权限'
        }
      ],
      ids: [],
      treeData: [],
      list: null,
      menuList: null,
      listLoading: true,
      total: 0,
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  created() {
    this.fetchData()
    this.fetchMenuData()
    this.fetchDeptTreeData()
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
    fetchMenuData() {
      getMenuList(this.queryParam).then((response) => {
        this.menuList = treeDataTranslate(response.data, 'id')
      })
    },
    fetchDeptTreeData() {
      getTree().then((response) => {
        this.treeData = response.data
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
        this.$refs.menuTree.setCheckedKeys(this.dataForm.menuIds)
        this.$refs.deptTree.setCheckedKeys(this.dataForm.deptIds)
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
        this.$refs.menuTree.setCheckedKeys(this.dataForm.menuIds)
        if (!this.dataForm.deptIds) {
          this.dataForm.deptIds = []
        }
        this.$refs.deptTree.setCheckedKeys(this.dataForm.deptIds)
      })
    },
    // 多选
    handleSelectionChange(ids) {
      this.ids = ids.map((item) => {
        return item.id
      })
    },
    handleMenuNodeClick(data, checked) {
      this.dataForm.menuIds = checked.checkedKeys
      this.$refs.dataForm.validateField('menuIds')
    },
    handleDeptNodeClick(data, checked) {
      this.dataForm.deptIds = checked.checkedKeys
    },
    handleSelectChange(val) {
      this.$refs.dataForm.validateField('dataScope')
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
