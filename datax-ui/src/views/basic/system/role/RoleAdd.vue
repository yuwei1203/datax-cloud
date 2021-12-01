<template>
  <el-card class="box-card" shadow="always">
    <div slot="header" class="clearfix">
      <span>{{ title }}</span>
      <el-button-group style="float: right;">
        <el-button v-hasPerm="['system:role:add']" size="mini" icon="el-icon-plus" round :loading="loadingOptions.loading" :disabled="loadingOptions.isDisabled" @click="submitForm">{{ loadingOptions.loadingText }}</el-button>
        <el-button size="mini" icon="el-icon-back" round @click="showCard">返回</el-button>
      </el-button-group>
    </div>
    <div class="body-wrapper">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="form.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜单权限">
              <el-tree
                ref="menu"
                :data="menuOptions"
                show-checkbox
                node-key="id"
                empty-text="加载中，请稍后"
                :props="menuDefaultProps"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限范围" prop="dataScope">
              <el-select v-model="form.dataScope">
                <el-option
                  v-for="item in dataScopeOptions"
                  :key="item.id"
                  :label="item.itemValue"
                  :value="item.itemText"
                />
              </el-select>
            </el-form-item>
            <el-form-item v-show="form.dataScope === '2'" label="数据权限">
              <el-tree
                ref="dept"
                :data="deptOptions"
                show-checkbox
                default-expand-all
                node-key="id"
                empty-text="加载中，请稍后"
                :props="deptDefaultProps"
              />
            </el-form-item>
          </el-col>
        </el-row>
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
import { addRole } from '@/api/system/role'
import { listMenu } from '@/api/system/menu'
import { listDept } from '@/api/system/dept'
import { construct } from '@/utils/json-tree'

export default {
  name: 'RoleAdd',
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
      title: '角色新增',
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
        status: '1',
        dataScope: '1',
        // 资源id数组
        menuList: [],
        // 数据范围为自定义时数据权限id数组
        deptList: []
      },
      // 表单校验
      rules: {
        roleName: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' }
        ],
        roleCode: [
          { required: true, message: '角色编码不能为空', trigger: 'blur' }
        ]
      },
      // 状态数据字典
      statusOptions: [],
      // 数据范围数据字典
      dataScopeOptions: [],
      // 菜单列表
      menuOptions: [],
      // 部门列表
      deptOptions: [],
      menuDefaultProps: {
        children: 'children',
        label: 'menuName'
      },
      deptDefaultProps: {
        children: 'children',
        label: 'deptName'
      }
    }
  },
  created() {
    this.getDicts('sys_common_status').then(response => {
      if (response.success) {
        this.statusOptions = response.data
      }
    })
    this.getDicts('sys_data_scope').then(response => {
      if (response.success) {
        this.dataScopeOptions = response.data
      }
    })
    this.getMenuTree()
    this.getDeptTree()
  },
  methods: {
    showCard() {
      this.$emit('showCard', this.showOptions)
    },
    /** 查询菜单树结构 */
    getMenuTree() {
      listMenu().then(response => {
        if (response.success) {
          const { data } = response
          this.menuOptions = construct(data, {
            id: 'id',
            pid: 'parentId',
            children: 'children'
          })
        }
      })
    },
    /** 查询部门树结构 */
    getDeptTree() {
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
    // 所有菜单节点数据
    getMenuAllCheckedKeys() {
      // 目前被选中的菜单节点
      const checkedKeys = this.$refs.menu.getHalfCheckedKeys()
      // 半选中的菜单节点
      const halfCheckedKeys = this.$refs.menu.getCheckedKeys()
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
      return checkedKeys
    },
    // 所有部门节点数据
    getDeptAllCheckedKeys() {
      // 目前被选中的部门节点
      const checkedKeys = this.$refs.dept.getHalfCheckedKeys()
      // 半选中的部门节点
      const halfCheckedKeys = this.$refs.dept.getCheckedKeys()
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
      return checkedKeys
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.loadingOptions.loading = true
          this.loadingOptions.loadingText = '保存中...'
          this.loadingOptions.isDisabled = true
          this.form.menuList = this.getMenuAllCheckedKeys()
          this.form.deptList = this.getDeptAllCheckedKeys()
          addRole(this.form).then(response => {
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
