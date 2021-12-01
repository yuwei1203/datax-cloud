<template>
  <el-card class="box-card" shadow="always">
    <div slot="header" class="clearfix">
      <span>{{ title }}</span>
      <el-button-group style="float: right;">
        <el-button v-hasPerm="['system:user:edit']" size="mini" icon="el-icon-plus" round :loading="loadingOptions.loading" :disabled="loadingOptions.isDisabled" @click="submitForm">{{ loadingOptions.loadingText }}</el-button>
        <el-button size="mini" icon="el-icon-back" round @click="showCard">返回</el-button>
      </el-button-group>
    </div>
    <div class="body-wrapper">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名称" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名称" />
        </el-form-item>
        <el-form-item label="用户昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入用户昵称" />
        </el-form-item>
        <el-form-item label="电子邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入电子邮箱" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker
            v-model="form.birthday"
            type="date"
            placeholder="请输入出生日期"
          />
        </el-form-item>
        <el-form-item label="归属部门" prop="deptId">
          <treeselect v-model="form.deptId" :options="deptOptions" :normalizer="normalizer" placeholder="请选择归属部门" />
        </el-form-item>
        <el-form-item label="岗位">
          <el-select v-model="form.postList" multiple placeholder="请选择岗位" @change="$set(form, form.postList, $event)">
            <el-option
              v-for="item in postOptions"
              :key="item.id"
              :label="item.postName"
              :value="item.id"
              :disabled="item.status === '0'"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.roleList" multiple placeholder="请选择角色" @change="$set(form, form.roleList, $event)">
            <el-option
              v-for="item in roleOptions"
              :key="item.id"
              :label="item.roleName"
              :value="item.id"
              :disabled="item.status === '0'"
            />
          </el-select>
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
import { getUser, updateUser } from '@/api/system/user'
import { listDept } from '@/api/system/dept'
import { listPost } from '@/api/system/post'
import { listRole } from '@/api/system/role'
import { construct } from '@/utils/json-tree'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'UserEdit',
  components: { Treeselect },
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
      title: '用户编辑',
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
      form: {},
      // 表单校验
      rules: {
        username: [
          { required: true, message: '用户名称不能为空', trigger: 'blur' }
        ],
        nickname: [
          { required: true, message: '用户昵称不能为空', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '电子邮箱不能为空', trigger: 'blur' },
          {
            type: 'email',
            message: '请输入正确的电子邮箱',
            trigger: ['blur', 'change']
          }
        ],
        phone: [
          { required: true, message: '手机号码不能为空', trigger: 'blur' },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: '请输入正确的手机号码',
            trigger: 'blur'
          }
        ],
        deptId: [
          { required: true, message: '归属部门不能为空', trigger: 'blur' }
        ]
      },
      // 状态数据字典
      statusOptions: [],
      // 部门树选项
      deptOptions: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: []
    }
  },
  created() {
    console.log('id:' + this.data.id)
    this.getDicts('sys_common_status').then(response => {
      if (response.success) {
        this.statusOptions = response.data
      }
    })
    this.getDeptTreeSelect()
    this.getPostList()
    this.getRoleList()
  },
  mounted() {
    this.getUser(this.data.id)
  },
  methods: {
    showCard() {
      this.$emit('showCard', this.showOptions)
    },
    getDeptTreeSelect: function() {
      listDept().then(response => {
        if (response.success) {
          const { data } = response
          this.deptOptions = construct(data, {
            id: 'id',
            pid: 'parentId',
            children: 'children'
          })
        }
      })
    },
    /** 转换部门数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.deptName,
        children: node.children
      }
    },
    getPostList() {
      listPost().then(response => {
        if (response.success) {
          this.postOptions = response.data
        }
      })
    },
    getRoleList() {
      listRole().then(response => {
        if (response.success) {
          this.roleOptions = response.data
        }
      })
    },
    /** 获取详情 */
    getUser: function(id) {
      getUser(id).then(response => {
        if (response.success) {
          this.form = response.data
          this.form.postList = response.data.posts.map(post => post.id)
          this.form.roleList = response.data.roles.map(role => role.id)
        }
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.loadingOptions.loading = true
          this.loadingOptions.loadingText = '保存中...'
          this.loadingOptions.isDisabled = true
          updateUser(this.form).then(response => {
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
