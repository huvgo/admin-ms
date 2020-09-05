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
        </el-form-item>
      </el-form>

      <el-table
        v-loading="listLoading"
        :data="list"
        element-loading-text="Loading"
        border
        fit
        highlight-current-row
        @selection-change="handleSelectionChange($event)"
      >
        <el-table-column type="selection" header-align="center" align="center" width="50" />
        <el-table-column label="表名" prop="name" />
        <el-table-column label="注释" prop="comment" />
        <el-table-column label="修改时间" prop="createTime" />
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope)">配置</el-button>
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
        <el-form-item label="上级菜单" prop="menuParentId">
          <tree-Popover :id="dataForm.parentMenuId" @tree-popover-click="handleTreePopoverClick" />
        </el-form-item>
        <el-table :data="dataForm.columns" border fit highlight-current-row>
          <el-table-column label="字段名" prop="name" />
          <el-table-column label="注释" prop="comment" />
          <el-table-column label="条件查询" prop="condition" align="center" width="100">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.condition" />
            </template>
          </el-table-column>
          <el-table-column label="页面样式" prop="createTime">
            <template slot-scope="scope">
              <el-select v-model="scope.row.element" placeholder="请选择">
                <el-option
                  v-for="item in ymysOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="dialogVisible=false">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getList, generator } from '@/api/dev/code'
import { option } from '@/api/sys/dictionary'
import TreePopover from './../sys/components/TreePopover'
export default {
  components: { TreePopover },
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
      dataForm: {
        name: '',
        parentMenuId: 0,
        comment: '',
        createTime: '',
        engine: '',
        generator: false
      },
      dialogVisible: false,
      queryParam: {
        currentPage: 1,
        pageSize: 10
      },
      ymysOptions: [],
      list: null,
      listLoading: true,
      total: 0,
      otherSetting: false,
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  created() {
    this.fetchData()
    this.fetchOptionData()
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
    fetchOptionData() {
      option({ codes: 'pageStyle' }).then((response) => {
        this.ymysOptions = response.data.ymysOptions
      })
    },
    dataFormSubmit() {
      generator(this.list).then((response) => {
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
        this.dataForm = scope.row
        console.log(this.dataForm)
      })
    },
    // 多选
    handleSelectionChange(ids) {
      this.list.map(obj => { obj.generator = false })
      this.ids = ids.map(item => {
        this.list.map(obj => {
          if (item.name === obj.name) {
            obj.generator = true
          }
        })
      })
      console.log(this.list)
    },
    handleTreePopoverClick(val) {
      this.dataForm.parentMenuId = val
    }
  }
}
</script>

<style scoped>
</style>

