<template>
  <div class="app-container">
    <el-card class="box-card">
      <el-row>
        <el-button
          v-for="(item,index) in definitionList"
          :key="index"
          type="primary"
          plain
          round
          @click="handleholidayAdd(item)"
        >{{ item.name }}</el-button>
      </el-row>
    </el-card>
    <el-dialog :visible.sync="holidayDialogVisible" :title="'新增'">
      <el-form ref="dataForm" :model="dataForm" label-width="80px" label-position="left">
        <el-form-item v-show="false" label="ID" prop="id" />
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="dataForm.data.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dataForm.data.content" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="创建时间" prop="createTime">
          <el-date-picker
            v-model="dataForm.data.createTime"
            style="width:100%"
            type="date"
            format="yyyy-MM-dd"
            placeholder="选择用户操作日期"
          />
        </el-form-item>
        <el-form-item label="更新时间" prop="updateTime">
          <el-date-picker
            v-model="dataForm.data.updateTime"
            style="width:100%"
            type="date"
            format="yyyy-MM-dd"
            placeholder="选择用户操作日期"
          />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="holidayDialogVisible=false">取消</el-button>
        <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getList as getDefinitionList } from '@/api/form/process'
import { add } from '@/api/process/process'

export default {
  data() {
    return {
      holidayDialogVisible: false,
      definitionList: [],
      definition: {},
      dataForm: {
        id: '',
        processDefinitionId: '',
        userId: '',
        deptId: '',
        currNodeUserId: '',
        currNodeUserName: '',
        data: {},
        status: '',
        createTime: '',
        updateTime: '',
        endTime: ''
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      getDefinitionList(this.queryParam).then((response) => {
        this.definitionList = response.data
      })
    },
    dataFormSubmit() {
      add(this.dataForm).then((response) => {
        this.dialogVisible = false
        this.fetchData()
        this.$message({ message: response.userTips, type: 'success' })
      })
    },
    handleholidayAdd(definition) {
      this.definition = definition
      this.dataForm.processDefinitionId = this.definition.id

      this.holidayDialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
