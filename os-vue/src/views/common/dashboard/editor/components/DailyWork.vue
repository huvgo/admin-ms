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
      <el-form ref="holidayForm" :model="holidayForm" label-width="80px" label-position="left">
        <el-form-item v-show="false" label="ID" prop="id" />
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="holidayForm.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="holidayForm.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="创建时间" prop="createTime">
          <el-date-picker
            v-model="holidayForm.createTime"
            style="width:100%"
            type="date"
            format="yyyy-MM-dd"
            placeholder="选择用户操作日期"
          />
        </el-form-item>
        <el-form-item label="更新时间" prop="updateTime">
          <el-date-picker
            v-model="holidayForm.updateTime"
            style="width:100%"
            type="date"
            format="yyyy-MM-dd"
            placeholder="选择用户操作日期"
          />
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
import { getList as getHolidayList } from '@/api/form/process'

export default {
  data() {
    return {
      holidayDialogVisible: false,
      holidayList: [],
      holidayForm: {
        id: '',
        userId: '',
        remark: '',
        createTime: '',
        updateTime: ''
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
