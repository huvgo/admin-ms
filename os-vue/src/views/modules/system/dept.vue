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
            ref="queryForm"
            :inline="true"
            :model="queryParam"
            @keyup.enter.native="fetchData()"
          >
            <el-form-item prop="name">
              <el-input v-model="queryParam.name" placeholder="部门名称" clearable />
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
            <el-table-column label="部门名称" prop="name" width="150" />
            <el-table-column label="上级部门" prop="parentIds">
              <template slot-scope="scope">{{ scope.row.parentIds |arrayFilter(list) }}</template>
            </el-table-column>
            <el-table-column label="排序" prop="sort" width="150" />
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
      </el-col>
    </el-row>
    <el-dialog :visible.sync="dialogVisible" :title="'新增'">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" label-width="80px">
        <el-form-item v-show="false" label="ID" prop="id" />
        <el-form-item v-show="false" label="上级部门ID" prop="parentId" />
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="dataForm.name" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="上级部门" prop="parentIds">
          <el-cascader
            ref="cascader"
            v-model="dataForm.parentIds"
            :options="treeData"
            :props="{ checkStrictly: true,label:'name',value:'id',expandTrigger:'hover' }"
            clearable
            :show-all-levels="true"
            style="width:100%"
            @change="handleCascaderChange"
          />
        </el-form-item>

        <el-form-item label="排序" prop="sort">
          <el-input v-model="dataForm.sort" placeholder="请输入排序" />
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
import { add, del, update, getList } from '@/api/system/dept'
import { getTree } from '@/api/system/dept'
import TreeCard from './components/TreeCard'
import { mapGetters } from 'vuex'
export default {
  components: { TreeCard },
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
        deptId: '',
        name: '',
        currentPage: 1,
        pageSize: 10
      },
      dataForm: {
        id: '',
        parentId: 0,
        parentIds: [],
        name: '',
        sort: '',
        mobile: '',
        enabled: ''
      },
      rules: {
        name: [
          { required: true, message: '部门名称不能为空', trigger: 'blur' }
        ]
      },
      treeData: [],
      ids: [],
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
      // 获取DeptTreeData
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
      if (parentId === this.dataForm.id) {
        this.$message({ message: '上级部门不能为自己', type: 'error' })
        return
      }
      this.dataForm.parentId = parentId
      this.dataForm.parentIds = val
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
