<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>代码生成</span>
      </div>
      <div class="component-item">
        <el-form ref="form" :model="form" label-width="100px">
          <el-form-item label="模块">
            <el-input v-model="form.moduleName" placeholder="模块名称，例如：sys" />
          </el-form-item>
          <el-form-item label="移除表前缀">
            <el-input v-model="form.tablePrefix" placeholder="移除前缀，例如：sys_" />
          </el-form-item>
          <el-form-item label="高级设置">
            <el-switch v-model="otherSetting" active-color="#13ce66" inactive-color="#ff4949" />
          </el-form-item>
          <el-form-item v-if="otherSetting" label="作者">
            <el-input v-model="form.author" placeholder="注释中的作者信息" />
          </el-form-item>
          <el-form-item v-if="otherSetting" label="项目路径">
            <el-input v-model="form.projectPath" placeholder="项目路径，例如：com.company.project.modules" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">立即生成</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>

export default {
  data() {
    return {
      form: {
        moduleName: '',
        tablePrefix: '',
        author: '',
        projectPath: ''
      },
      list: [],
      listLoading: false,
      otherSetting: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
    }
  }
}
</script>

<style scoped>
</style>

