<template>
  <div class="app-container">
    <transition name="el-zoom-in-center">
      <post-list v-if="options.showList" @showCard="showCard"></post-list>
    </transition>
    <transition name="el-zoom-in-top">
      <post-add v-if="options.showAdd" :data="options.data" @showCard="showCard"></post-add>
    </transition>
    <transition name="el-zoom-in-top">
      <post-edit v-if="options.showEdit" :data="options.data" @showCard="showCard"></post-edit>
    </transition>
    <transition name="el-zoom-in-bottom">
      <post-detail v-if="options.showDetail" :data="options.data" @showCard="showCard"></post-detail>
    </transition>
  </div>
</template>

<script>
import PostList from './PostList'
import PostAdd from './PostAdd'
import PostEdit from './PostEdit'
import PostDetail from './PostDetail'

export default {
  name: 'Post',
  components: { PostList, PostAdd, PostEdit, PostDetail },
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
