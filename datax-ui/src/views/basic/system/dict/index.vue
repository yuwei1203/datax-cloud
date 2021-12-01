<template>
  <div class="app-container">
    <transition name="el-zoom-in-center">
      <dict-list v-if="options.showList" @showCard="showCard"></dict-list>
    </transition>
    <transition name="el-zoom-in-top">
      <dict-add v-if="options.showAdd" :data="options.data" @showCard="showCard"></dict-add>
    </transition>
    <transition name="el-zoom-in-top">
      <dict-edit v-if="options.showEdit" :data="options.data" @showCard="showCard"></dict-edit>
    </transition>
    <transition name="el-zoom-in-bottom">
      <dict-detail v-if="options.showDetail" :data="options.data" @showCard="showCard"></dict-detail>
    </transition>
    <transition name="el-zoom-in-center">
      <dict-item-list v-if="options.showItemList" :data="options.data" @showCard="showCard"></dict-item-list>
    </transition>
    <transition name="el-zoom-in-top">
      <dict-item-add v-if="options.showItemAdd" :data="options.data" @showCard="showCard"></dict-item-add>
    </transition>
    <transition name="el-zoom-in-top">
      <dict-item-edit v-if="options.showItemEdit" :data="options.data" @showCard="showCard"></dict-item-edit>
    </transition>
    <transition name="el-zoom-in-bottom">
      <dict-item-detail v-if="options.showItemDetail" :data="options.data" @showCard="showCard"></dict-item-detail>
    </transition>
  </div>
</template>

<script>
import DictList from './DictList'
import DictAdd from './DictAdd'
import DictEdit from './DictEdit'
import DictDetail from './DictDetail'
import DictItemList from './item/DictItemList'
import DictItemAdd from './item/DictItemAdd'
import DictItemEdit from './item/DictItemEdit'
import DictItemDetail from './item/DictItemDetail'

export default {
  name: 'Dict',
  components: { DictList, DictAdd, DictEdit, DictDetail, DictItemList, DictItemAdd, DictItemEdit, DictItemDetail },
  data () {
    return {
      options: {
        data: {},
        showList: true,
        showAdd: false,
        showEdit: false,
        showDetail: false,
        showItemList: false,
        showItemAdd: false,
        showItemEdit: false,
        showItemDetail: false
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
