<template>
  <div class="app-container">
    <transition name="el-zoom-in-center">
      <task-job-list v-if="options.showList" @showCard="showCard"></task-job-list>
    </transition>
    <transition name="el-zoom-in-top">
      <task-job-add v-if="options.showAdd" :data="options.data" @showCard="showCard"></task-job-add>
    </transition>
    <transition name="el-zoom-in-top">
      <task-job-edit v-if="options.showEdit" :data="options.data" @showCard="showCard"></task-job-edit>
    </transition>
    <transition name="el-zoom-in-bottom">
      <task-job-detail v-if="options.showDetail" :data="options.data" @showCard="showCard"></task-job-detail>
    </transition>
  </div>
</template>

<script>
import TaskJobList from './TaskJobList'
import TaskJobAdd from './TaskJobAdd'
import TaskJobEdit from './TaskJobEdit'
import TaskJobDetail from './TaskJobDetail'

export default {
  name: 'TaskJob',
  components: { TaskJobList, TaskJobAdd, TaskJobEdit, TaskJobDetail },
  data () {
    return {
      options: {
        data: {},
        showList: true,
        showAdd: false,
        showEdit: false,
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
