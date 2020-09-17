<template>
  <div class="app-container">
    <el-card class="box-card">
      <el-row>
        <el-button
          v-for="(item,index) in holidayList"
          :key="index"
          type="primary"
          plain
          round
          @click="handleholidayAdd()"
        >{{ item.name }}</el-button>
      </el-row>
    </el-card>
    <el-dialog :visible.sync="holidayDialogVisible" :title="'新增'">

      <el-form ref="dataForm" :model="dataForm" label-width="80px" label-position="left">
        <el-form-item v-show="false" label="ID" prop="id" />
        <el-form-item label="流程实例ID" prop="processId">
          <el-input v-model="dataForm.processId" placeholder="请输入流程实例ID" />
        </el-form-item>
        <el-form-item label="流程标识" prop="processKey">
          <el-input v-model="dataForm.processKey" placeholder="请输入流程标识" />
        </el-form-item>
        <el-form-item label="流程名称" prop="processName">
          <el-input v-model="dataForm.processName" placeholder="请输入流程名称" />
        </el-form-item>
        <el-form-item label="流程定义ID" prop="processDefinitionId">
          <el-input v-model="dataForm.processDefinitionId" placeholder="请输入流程定义ID" />
        </el-form-item>
        <el-form-item
          label="流程状态（0已提交；1审批中；2审批
通过；3审批不通过；4撤销）"
          prop="processState"
        >
          <el-input
            v-model="dataForm.processState"
            placeholder="请输入流程状态（0已提交；1审批中；2审批
通过；3审批不通过；4撤销）"
          />
        </el-form-item>
        <el-form-item label="申请人ID" prop="userId">
          <el-input v-model="dataForm.userId" placeholder="请输入申请人ID" />
        </el-form-item>
        <el-form-item label="申请人" prop="username">
          <el-input v-model="dataForm.username" placeholder="请输入申请人" />
        </el-form-item>
        <el-form-item label="申请时间" prop="procApplyTime">
          <el-date-picker
            v-model="dataForm.procApplyTime"
            style="width:100%"
            type="date"
            format="yyyy-MM-dd"
            placeholder="选择用户操作日期"
          />
        </el-form-item>
        <el-form-item label="当前节点审批人ID" prop="procCurrNodeUserId">
          <el-input v-model="dataForm.procCurrNodeUserId" placeholder="请输入当前节点审批人ID" />
        </el-form-item>
        <el-form-item label="当前节点审批人" prop="procCurrNodeUserName">
          <el-input v-model="dataForm.procCurrNodeUserName" placeholder="请输入当前节点审批人" />
        </el-form-item>
        <el-form-item label="结束流程时间" prop="procEndTime">
          <el-date-picker
            v-model="dataForm.procEndTime"
            style="width:100%"
            type="date"
            format="yyyy-MM-dd"
            placeholder="选择用户操作日期"
          />
        </el-form-item>
        <el-form-item label="" prop="timeOfEntry">
          <el-date-picker
            v-model="dataForm.timeOfEntry"
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
import { getList as getHolidayList } from '@/api/form/process'
import { add } from '@/api/process/process'

export default {
  data() {
    return {
      holidayDialogVisible: false,
      holidayList: [],
      dataForm: {
        id: '',
        userId: '',
        remark: '',
        time: '',
        createTime: '',
        updateTime: ''
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
      getHolidayList(this.queryParam).then((response) => {
        this.holidayList = response.data
      })
    },
    dataFormSubmit() {
      add(this.holidayForm).then((response) => {
        this.dialogVisible = false
        this.fetchData()
        this.$message({ message: response.userTips, type: 'success' })
      })
    },
    handleholidayAdd() {
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
