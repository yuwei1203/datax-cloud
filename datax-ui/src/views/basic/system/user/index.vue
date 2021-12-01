<template>
  <div class="app-container">
    <transition name="el-zoom-in-center">
      <user-list v-if="options.showList" @showCard="showCard"></user-list>
    </transition>
    <transition name="el-zoom-in-top">
      <user-add v-if="options.showAdd" :data="options.data" @showCard="showCard"></user-add>
    </transition>
    <transition name="el-zoom-in-top">
      <user-edit v-if="options.showEdit" :data="options.data" @showCard="showCard"></user-edit>
    </transition>
    <transition name="el-zoom-in-bottom">
      <user-detail v-if="options.showDetail" :data="options.data" @showCard="showCard"></user-detail>
    </transition>
  </div>
</template>

<script>
import UserList from './UserList'
import UserAdd from './UserAdd'
import UserEdit from './UserEdit'
import UserDetail from './UserDetail'

export default {
  name: 'User',
  components: { UserList, UserAdd, UserEdit, UserDetail },
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
