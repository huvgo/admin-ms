<template>
  <div class="app-container">
    <el-card class="box-card">
      <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
        <el-form-item>
          <el-button type="primary" @click="handleAdd()">新增</el-button>
        </el-form-item>
      </el-form>

      <el-table
        v-loading="listLoading"
        :data="list"
        element-loading-text="Loading"
        border
        fit
        highlight-current-row
        row-key="id"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        @selection-change="handleSelectionChange"
      >
        <el-table-column label="ID" prop="id" width="80" align="center" />
        <el-table-column label="菜单名称" prop="name" />
        <el-table-column label="菜单URL" prop="path" />
        <el-table-column label="授权标识" prop="perms">
          <template slot-scope="scope">{{ scope.row.perms }}</template>
        </el-table-column>
        <el-table-column label="类型" prop="type">
          <template slot-scope="scope">{{ scope.row.type }}</template>
        </el-table-column>
        <el-table-column label="菜单图标">
          <template slot-scope="scope">
            <i v-if="scope.row.meta.icon.indexOf('el-icon')===0" :class="scope.row.meta.icon" />
            <svg-icon v-else :icon-class="scope.row.meta.icon" />
          </template>
        </el-table-column>
        <el-table-column label="排序" prop="sort" />
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
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="dataForm.name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="上级菜单" prop="parentId">
          <el-input v-model="dataForm.parentId" placeholder="请输入父菜单ID，一级菜单为0" />
        </el-form-item>
        <el-form-item label="菜单URL" prop="path">
          <el-input v-model="dataForm.path" placeholder="请输入菜单URL" />
        </el-form-item>
        <el-form-item label="授权标识" prop="perms">
          <el-input v-model="dataForm.perms" placeholder="请输入授权标识" />
        </el-form-item>
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="dataForm.type" size="small">
            <el-radio-button label="0">目录</el-radio-button>
            <el-radio-button label="1">菜单</el-radio-button>
            <el-radio-button label="2">按钮</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单图标" prop="meta.icon">
          <el-input v-model="dataForm.meta.icon" placeholder="请输入菜单图标" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input v-model="dataForm.orderNum" placeholder="请输入排序" />
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
import { add, del, update, getList } from '@/api/sys/menu'
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
        currentPage: 1,
        pageSize: 10
      },
      dataForm: {
        parentId: '',
        name: '',
        path: '',
        meta: {
          title: this.name,
          icon: ''
        },
        perms: '',
        type: '1',
        orderNum: ''
      },
      ids: [],
      list: [],
      listLoading: true,
      total: 0
    }
  },
  watch: {
    'dataForm.name': {
      handler(newName, oldName) {
        this.dataForm.meta.title = newName
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getList(this.queryParam).then((response) => {
        this.list = treeDataTranslate(response.data, 'id')
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
      this.ids = ids.map((item) => {
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
