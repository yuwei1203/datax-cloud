<template>
  <div class="app-container">
    <transition name="el-zoom-in-center">
      <menu-list v-if="options.showList" @showCard="showCard"></menu-list>
    </transition>
    <transition name="el-zoom-in-top">
      <menu-add v-if="options.showAdd" :data="options.data" @showCard="showCard"></menu-add>
    </transition>
    <transition name="el-zoom-in-top">
      <menu-edit v-if="options.showEdit" :data="options.data" @showCard="showCard"></menu-edit>
    </transition>
    <transition name="el-zoom-in-bottom">
      <menu-detail v-if="options.showDetail" :data="options.data" @showCard="showCard"></menu-detail>
    </transition>
  </div>
</template>

<script>
import MenuList from './MenuList'
import MenuAdd from './MenuAdd'
import MenuEdit from './MenuEdit'
import MenuDetail from './MenuDetail'

export default {
  name: 'Menu',
  components: { MenuList, MenuAdd, MenuEdit, MenuDetail },
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
