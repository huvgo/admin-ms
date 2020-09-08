<template>
  <el-popover ref="popover" placement="top-start" width="200" trigger="click">
    <el-tree
      ref="tree"
      :data="treeData"
      node-key="id"
      :default-expand-all="true"
      :expand-on-click-node="false"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
    <template slot="reference">
      <el-input v-model="menuName" placeholder="请选择上级菜单" />
    </template>
  </el-popover>
</template>

<script>
import { treeDataTranslate } from '@/utils'
import { getList } from '@/api/system/menu'

export default {
  props: {
    id: {
      type: Number,
      default() {
        return 0
      }
    }
  },
  data() {
    return {
      list: [],
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  computed: {
    menuName: function() {
      for (let i = 0; i < this.list.length; i++) {
        const item = this.list[i]
        if (parseInt(item.id) === parseInt(this.id)) {
          return item.name
        }
      }
      return ''
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      getList({ nonButton: true }).then((response) => {
        this.list = response.data
        this.treeData = treeDataTranslate(response.data, 'id')
      })
    },
    handleNodeClick(data, node, scop) {
      // this.menuName = data.name
      this.$emit('tree-popover-click', data.id)
      this.$refs.popover.showPopper = false
    }
  }
}
</script>
