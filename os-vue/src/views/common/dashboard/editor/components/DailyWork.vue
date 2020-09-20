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
      <el-form ref="dataForm" :model="dataForm" label-width="80px">
        <el-form-item v-show="false" label="ID" prop="id" />
        <el-form-item label="请假类型" prop="leaveType">
          <el-select v-model="dataForm.data.leaveType" placeholder="请选择请假类型" style="width:100%">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              :disabled="item.disabled"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="申请内容" prop="remark">
          <el-input
            v-model="dataForm.data.content"
            maxlength="140"
            rows="4"
            show-word-limit="true"
            type="textarea"
            placeholder="请输入申请内容"
          />
        </el-form-item>
        <el-form-item label="开始时间" prop="createTime">
          <el-date-picker
            v-model="dataForm.data.createTime"
            style="width:100%"
            type="date"
            format="yyyy-MM-dd"
            placeholder="选择用户操作日期"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="updateTime">
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
      },
      options: [{
        value: '1',
        label: '事假'
      }, {
        value: '2',
        label: '病假'
      }, {
        value: '3',
        label: '年假'
      }, {
        value: '4',
        label: '婚假'
      }, {
        value: '5',
        label: '丧假'
      }, {
        value: '5',
        label: '产假'
      }]
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

      if (definition.key === 'process_holiday') {
        this.holidayDialogVisible = true
      }

      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
