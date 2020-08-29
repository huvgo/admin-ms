<template>
  <el-card class="box-card">
    <el-tree
      :data="data"
      node-key="id"
      :default-expand-all="true"
      :expand-on-click-node="false"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
  </el-card>
</template>

<script>
export default {
  props: {
    data: {
      type: Array,
      default() {
        return []
      }
    }
  },
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      loading: false
    }
  },
  created() {
  },
  methods: {
    handleNodeClick(data, node, scop) {
      this.$emit('tree-click', data.id)
    }
  }
}
</script>
