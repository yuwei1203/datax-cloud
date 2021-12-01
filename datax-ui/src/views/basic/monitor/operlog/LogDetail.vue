<template>
  <el-card class="box-card" shadow="always">
    <div slot="header" class="clearfix">
      <span>{{ title }}</span>
      <el-button-group style="float: right;">
        <el-button size="mini" icon="el-icon-back" round @click="showCard">返回</el-button>
      </el-button-group>
    </div>
    <div class="body-wrapper">
      <el-form ref="form" :model="form" label-width="80px" disabled>
        <el-form-item label="所属模块" prop="module">
          <el-input v-model="form.module" />
        </el-form-item>
        <el-form-item label="日志标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="form.userName" />
        </el-form-item>
        <el-form-item label="请求IP" prop="remoteAddr">
          <el-input v-model="form.remoteAddr" />
        </el-form-item>
        <el-form-item label="请求URI" prop="requestUri">
          <el-input v-model="form.requestUri" />
        </el-form-item>
        <el-form-item label="方法类名" prop="className">
          <el-input v-model="form.className" />
        </el-form-item>
        <el-form-item label="方法名称" prop="methodName">
          <el-input v-model="form.methodName" />
        </el-form-item>
        <el-form-item label="请求参数" prop="params">
          <el-input v-model="form.params" />
        </el-form-item>
        <el-form-item label="请求耗时" prop="time">
          <el-input v-model="form.time" />
        </el-form-item>
        <el-form-item label="浏览器名称" prop="browser">
          <el-input v-model="form.browser" />
        </el-form-item>
        <el-form-item label="操作系统" prop="os">
          <el-input v-model="form.os" />
        </el-form-item>
        <el-form-item label="错误类型" prop="exCode">
          <el-input v-model="form.exCode" />
        </el-form-item>
        <el-form-item label="错误信息" prop="exMsg">
          <el-input v-model="form.exMsg" />
        </el-form-item>
        <el-form-item label="操作时间" prop="createTime">
          <el-input v-model="form.createTime" />
        </el-form-item>
      </el-form>
    </div>
  </el-card>
</template>

<script>
import { getLog } from '@/api/monitor/operlog'

export default {
  name: 'OperLogDetail',
  props: {
    data: {
      type: Object,
      default: function() {
        return {}
      }
    }
  },
  data() {
    return {
      title: '操作日志详情',
      // 展示切换
      showOptions: {
        data: {},
        showList: true,
        showDetail: false
      },
      // 表单参数
      form: {}
    }
  },
  created() {
    console.log('id:' + this.data.id)
  },
  mounted() {
    this.getLog(this.data.id)
  },
  methods: {
    showCard() {
      this.$emit('showCard', this.showOptions)
    },
    /** 获取详情 */
    getLog: function(id) {
      getLog(id).then(response => {
        if (response.success) {
          this.form = response.data
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.el-card ::v-deep .el-card__body {
  height: calc(100vh - 230px);
  overflow-y: auto;
}
</style>
