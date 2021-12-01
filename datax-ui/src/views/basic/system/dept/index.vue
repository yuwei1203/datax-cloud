<template>
  <div class="app-container">
    <transition name="el-zoom-in-center">
      <dept-list v-if="options.showList" @showCard="showCard"></dept-list>
    </transition>
    <transition name="el-zoom-in-top">
      <dept-add v-if="options.showAdd" :data="options.data" @showCard="showCard"></dept-add>
    </transition>
    <transition name="el-zoom-in-top">
      <dept-edit v-if="options.showEdit" :data="options.data" @showCard="showCard"></dept-edit>
    </transition>
    <transition name="el-zoom-in-bottom">
      <dept-detail v-if="options.showDetail" :data="options.data" @showCard="showCard"></dept-detail>
    </transition>
  </div>
</template>

<script>
import DeptList from './DeptList'
import DeptAdd from './DeptAdd'
import DeptEdit from './DeptEdit'
import DeptDetail from './DeptDetail'

export default {
  name: 'Dept',
  components: { DeptList, DeptAdd, DeptEdit, DeptDetail },
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
