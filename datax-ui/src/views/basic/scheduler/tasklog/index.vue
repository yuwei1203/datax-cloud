<template>
  <div class="app-container">
    <transition name="el-zoom-in-center">
      <task-log-list v-if="options.showList" @showCard="showCard"></task-log-list>
    </transition>
    <transition name="el-zoom-in-bottom">
      <task-log-detail v-if="options.showDetail" :data="options.data" @showCard="showCard"></task-log-detail>
    </transition>
  </div>
</template>

<script>
import TaskLogList from './TaskLogList'
import TaskLogDetail from './TaskLogDetail'

export default {
  name: 'TaskLog',
  components: { TaskLogList, TaskLogDetail },
  data () {
    return {
      options: {
        data: {},
        showList: true,
        showDetail: false
      }
    }
  },
  methods: {
    showCard (data) {
      Object.assign(this.options, data)
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
