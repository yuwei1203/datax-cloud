<template>
  <div class="app-container">
    <transition name="el-zoom-in-center">
      <role-list v-if="options.showList" @showCard="showCard"></role-list>
    </transition>
    <transition name="el-zoom-in-top">
      <role-add v-if="options.showAdd" :data="options.data" @showCard="showCard"></role-add>
    </transition>
    <transition name="el-zoom-in-top">
      <role-edit v-if="options.showEdit" :data="options.data" @showCard="showCard"></role-edit>
    </transition>
    <transition name="el-zoom-in-bottom">
      <role-detail v-if="options.showDetail" :data="options.data" @showCard="showCard"></role-detail>
    </transition>
  </div>
</template>

<script>
import RoleList from './RoleList'
import RoleAdd from './RoleAdd'
import RoleEdit from './RoleEdit'
import RoleDetail from './RoleDetail'

export default {
  name: 'Role',
  components: { RoleList, RoleAdd, RoleEdit, RoleDetail },
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
