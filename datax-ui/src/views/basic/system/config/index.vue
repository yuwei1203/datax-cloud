<template>
  <div class="app-container">
    <transition name="el-zoom-in-center">
      <config-list v-if="options.showList" @showCard="showCard"></config-list>
    </transition>
    <transition name="el-zoom-in-top">
      <config-add v-if="options.showAdd" :data="options.data" @showCard="showCard"></config-add>
    </transition>
    <transition name="el-zoom-in-top">
      <config-edit v-if="options.showEdit" :data="options.data" @showCard="showCard"></config-edit>
    </transition>
    <transition name="el-zoom-in-bottom">
      <config-detail v-if="options.showDetail" :data="options.data" @showCard="showCard"></config-detail>
    </transition>
  </div>
</template>

<script>
import ConfigList from './ConfigList'
import ConfigAdd from './ConfigAdd'
import ConfigEdit from './ConfigEdit'
import ConfigDetail from './ConfigDetail'

export default {
  name: 'Config',
  components: { ConfigList, ConfigAdd, ConfigEdit, ConfigDetail },
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
