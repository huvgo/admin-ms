<template>
  <div class="app-container">
    <el-card class="box-card">
      <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
        <el-form-item>
          <el-input v-model="queryParam.name" placeholder="角色名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button @click="fetchData()">查询</el-button>
          <el-button type="primary" @click="handleAdd()">新增</el-button>
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
      <el-form ref="dataForm" :model="dataForm" label-width="80px" label-position="left">
        <el-form-item v-show="false" label="ID" prop="id" />
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="dataForm.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="数据权限" prop="dataScope">
          <el-input v-model="dataForm.dataScope" placeholder="请输入数据权限范围" />
        </el-form-item>
        <el-form-item label="菜单权限" prop="menuIds">
          <el-tree
            ref="menuTree"
            :data="menuList"
            :props="{
              children: 'children',
              label: 'name'
            }"
            show-checkbox
            node-key="id"
            class="permission-tree"
            :expand-on-click-node="false"
            :check-strictly="true"
            :check-on-click-node="true"
            :default-expand-all="true"
            @check="handleNodeClick"
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
import { add, del, update, getList } from '@/api/sys/role'
import { getList as getMenuList } from '@/api/sys/menu'
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
        dataScope: '',
        remark: '',
        deptId: '',
        createUserId: '',
        createTime: ''
      },
      ids: [],
      list: null,
      menuList: null,
      listLoading: true,
      total: 0
    }
  },
  created() {
    this.fetchData()
    this.fetchMenuData()
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
        this.$refs.menuTree.setCheckedKeys(this.dataForm.menuIds)
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
        this.$refs.menuTree.setCheckedKeys(this.dataForm.menuIds)
      })
    },
    // 多选
    handleSelectionChange(ids) {
      this.ids = ids.map((item) => {
        return item.id
      })
    },
    handleNodeClick(data, checked) {
      this.dataForm.menuIds = checked.checkedKeys.concat(checked.halfCheckedKeys)
      // this.dataForm.menuIds = checked.checkedKeys
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
