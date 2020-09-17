<template>
  <div class="app-container">
    <daily-work />
  </div>
</template>

<script>

import DailyWork from './components/DailyWork'

export default {
  name: 'DashboardEditor',
  components: { DailyWork },
  data() {
    return {

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

<style lang="scss" scoped>

</style>
