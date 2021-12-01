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
        <el-form-item label="登录用户" prop="userName">
          <el-input v-model="form.userName" />
        </el-form-item>
        <el-form-item label="操作系统" prop="opOs">
          <el-input v-model="form.opOs" />
        </el-form-item>
        <el-form-item label="浏览器类型" prop="opBrowser">
          <el-input v-model="form.opBrowser" />
        </el-form-item>
        <el-form-item label="登录IP地址" prop="opIp">
          <el-input v-model="form.opIp" />
        </el-form-item>
        <el-form-item label="登录时间" prop="opDate">
          <el-input v-model="form.opDate" />
        </el-form-item>
      </el-form>
    </div>
  </el-card>
</template>

<script>
import { getLog } from '@/api/monitor/loginlog'

export default {
  name: 'LoginLogDetail',
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
      title: '登录日志详情',
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
