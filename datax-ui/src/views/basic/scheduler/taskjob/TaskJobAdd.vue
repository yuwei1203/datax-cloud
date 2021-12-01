<template>
  <el-card class="box-card" shadow="always">
    <div slot="header" class="clearfix">
      <span>{{ title }}</span>
      <el-button-group style="float: right;">
        <el-button v-hasPerm="['scheduler:job:add']" size="mini" icon="el-icon-plus" round :loading="loadingOptions.loading" :disabled="loadingOptions.isDisabled" @click="submitForm">{{ loadingOptions.loadingText }}</el-button>
        <el-button size="mini" icon="el-icon-back" round @click="showCard">返回</el-button>
      </el-button-group>
    </div>
    <div class="body-wrapper">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="任务名称" prop="jobName">
          <el-input v-model="form.jobName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="Spring Bean名称" prop="beanName">
          <el-input v-model="form.beanName" placeholder="请输入Spring Bean名称" />
        </el-form-item>
        <el-form-item label="方法名称" prop="methodName">
          <el-input v-model="form.methodName" placeholder="请输入方法名称" />
        </el-form-item>
        <el-form-item label="方法参数" prop="methodParams">
          <el-input v-model="form.methodParams" placeholder="请输入方法参数" />
        </el-form-item>
        <el-form-item label="cron表达式" prop="cronExpression">
          <el-input v-model="form.cronExpression" placeholder="请输入cron表达式" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.id"
              :label="dict.itemText"
            >{{ dict.itemValue }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
    </div>
  </el-card>
</template>

<script>
import { addJob } from '@/api/scheduler/job'

export default {
  name: 'TaskJobAdd',
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
      title: '任务新增',
      // 展示切换
      showOptions: {
        data: {},
        showList: true,
        showAdd: false,
        showEdit: false,
        showDetail: false
      },
      // 保存按钮
      loadingOptions: {
        loading: false,
        loadingText: '保存',
        isDisabled: false
      },
      // 表单参数
      form: {
        status: '1'
      },
      // 表单校验
      rules: {
        jobName: [
          { required: true, message: '任务名称不能为空', trigger: 'blur' }
        ],
        beanName: [
          { required: true, message: 'Spring Bean名称不能为空', trigger: 'blur' }
        ],
        methodName: [
          { required: true, message: '方法名称不能为空', trigger: 'blur' }
        ],
        cronExpression: [
          { required: true, message: 'cron表达式不能为空', trigger: 'blur' }
        ]
      },
      // 状态数据字典
      statusOptions: []
    }
  },
  created() {
    this.getDicts('sys_job_status').then(response => {
      if (response.success) {
        this.statusOptions = response.data
      }
    })
  },
  methods: {
    showCard() {
      this.$emit('showCard', this.showOptions)
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.loadingOptions.loading = true
          this.loadingOptions.loadingText = '保存中...'
          this.loadingOptions.isDisabled = true
          addJob(this.form).then(response => {
            if (response.success) {
              this.$message.success('保存成功')
              setTimeout(() => {
                // 2秒后跳转列表页
                this.$emit('showCard', this.showOptions)
              }, 2000)
            } else {
              this.$message.error('保存失败')
              this.loadingOptions.loading = false
              this.loadingOptions.loadingText = '保存'
              this.loadingOptions.isDisabled = false
            }
          }).catch(() => {
            this.loadingOptions.loading = false
            this.loadingOptions.loadingText = '保存'
            this.loadingOptions.isDisabled = false
          })
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
