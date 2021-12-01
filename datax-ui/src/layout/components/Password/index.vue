<template>
  <el-dialog title="修改密码" width="30%" center append-to-body :show-close="false" :close-on-click-modal="false" :visible.sync="dialogVisible">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input v-model="form.oldPassword" type="password" placeholder="请输入旧密码" />
      </el-form-item>
      <el-form-item label="新密码" prop="password">
        <el-input v-model="form.password" type="password" placeholder="请输入新密码" />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" type="password" placeholder="请确认密码" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="loadingOptions.loading" :disabled="loadingOptions.disabled" @click="submitForm()">保存</el-button>
        <el-button @click="dialogVisible = false">取消</el-button>
      </span>
  </el-dialog>
</template>

<script>
import { updatePassword } from '@/api/system/user'

export default {
  name: 'ChangePassword',
  props: {
    visible: {
      type: Boolean,
      default: function() {
        return false
      }
    }
  },
  data() {
    // 确认密码验证
    const validatePass = (rule, value, callback) => {
      if (value.length < 1) {
        return callback(new Error('确认密码不能为空'))
      } else if (value !== this.form.password) {
        return callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      // 保存按钮
      loadingOptions: {
        loading: false,
        disabled: false
      },
      form: {
        oldPassword: undefined,
        password: undefined,
        confirmPassword: undefined
      },
      rules: {
        oldPassword: [
          { required: true, message: '旧密码不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '新密码不能为空', trigger: 'blur' },
          { min: 6, max: 16, message: '长度在6到16个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validatePass, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.loadingOptions.loading = true
          this.loadingOptions.disabled = true
          updatePassword(this.form).then(response => {
            if (response.success) {
              this.$message.success('保存成功')
              setTimeout(() => {
                // 2秒后跳转登录页
                this.dialogVisible = false
                this.$emit('handlePasswordFinished')
              }, 2000)
            } else {
              this.$message.error('保存失败')
              this.loadingOptions.loading = false
              this.loadingOptions.disabled = false
            }
          }).catch(() => {
            this.loadingOptions.loading = false
            this.loadingOptions.disabled = false
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
