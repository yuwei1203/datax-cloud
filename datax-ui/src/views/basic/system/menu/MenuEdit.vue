<template>
  <el-card class="box-card" shadow="always">
    <div slot="header" class="clearfix">
      <span>{{ title }}</span>
      <el-button-group style="float: right;">
        <el-button v-hasPerm="['system:menu:edit']" size="mini" icon="el-icon-plus" round :loading="loadingOptions.loading" :disabled="loadingOptions.isDisabled" @click="submitForm">{{ loadingOptions.loadingText }}</el-button>
        <el-button size="mini" icon="el-icon-back" round @click="showCard">返回</el-button>
      </el-button-group>
    </div>
    <div class="body-wrapper">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="上级资源" prop="parentId">
          <treeselect v-model="form.parentId" :options="menuOptions" :normalizer="normalizer" placeholder="请选择上级资源" />
        </el-form-item>
        <el-form-item label="资源名称" prop="menuName">
          <el-input v-model="form.menuName" placeholder="请输入资源名称" />
        </el-form-item>
        <el-form-item label="资源类型" prop="menuType">
          <el-radio-group v-model="form.menuType">
            <el-radio
              v-for="dict in menuTypeOptions"
              :key="dict.id"
              :label="dict.itemText"
            >{{ dict.itemValue }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.menuType !== '2'" label="路由地址" prop="menuPath">
          <el-input v-model="form.menuPath" placeholder="请输入对应路由地址path" />
        </el-form-item>
        <el-form-item v-if="form.menuType !== '2'" label="路由组件" prop="menuComponent">
          <el-input v-model="form.menuComponent" placeholder="请输入对应路由组件component" />
        </el-form-item>
        <el-form-item v-if="form.menuType === '0'" label="跳转地址" prop="menuRedirect">
          <el-input v-model="form.menuRedirect" placeholder="请输入对应路由默认跳转地址redirect" />
        </el-form-item>
        <el-form-item v-if="form.menuType === '2'" label="权限标识" prop="menuPerms">
          <el-input v-model="form.menuPerms" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item v-if="form.menuType !== '2'" label="资源图标" prop="menuIcon">
          <el-input v-model="form.menuIcon" placeholder="请输入资源图标" />
        </el-form-item>
        <el-form-item v-if="form.menuType !== '2'" label="资源编码" prop="menuCode">
          <el-input v-model="form.menuCode" placeholder="请输入资源编码" />
        </el-form-item>
        <el-form-item label="排序" prop="menuSort">
          <el-input-number v-model="form.menuSort" controls-position="right" :min="1" />
        </el-form-item>
        <el-form-item label="隐藏" prop="menuHidden">
          <el-radio-group v-model="form.menuHidden">
            <el-radio
              v-for="dict in hiddenOptions"
              :key="dict.id"
              :label="dict.itemText"
            >{{ dict.itemValue }}</el-radio>
          </el-radio-group>
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
import { getMenu, listMenu, updateMenu } from '@/api/system/menu'
import { construct } from '@/utils/json-tree'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'MenuEdit',
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
      title: '资源编辑',
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
        parentId: [
          { required: true, message: '上级资源不能为空', trigger: 'blur' }
        ],
        menuName: [
          { required: true, message: '资源名称不能为空', trigger: 'blur' }
        ],
        menuSort: [
          { required: true, message: '资源排序不能为空', trigger: 'blur' }
        ]
      },
      // 状态数据字典
      statusOptions: [],
      // 类型数据字典
      menuTypeOptions: [],
      // 资源树选项
      menuOptions: [],
      hiddenOptions: []
    }
  },
  created() {
    console.log('id:' + this.data.id)
    this.getDicts('sys_common_status').then(response => {
      if (response.success) {
        this.statusOptions = response.data
      }
    })
    this.getDicts('sys_menu_type').then(response => {
      if (response.success) {
        this.menuTypeOptions = response.data
      }
    })
    this.getDicts('sys_yes_no').then(response => {
      if (response.success) {
        this.hiddenOptions = response.data
      }
    })
    this.getMenuTreeSelect()
  },
  mounted() {
    this.getMenu(this.data.id)
  },
  methods: {
    showCard() {
      this.$emit('showCard', this.showOptions)
    },
    getMenuTreeSelect: function() {
      listMenu().then(response => {
        if (response.success) {
          const { data } = response
          this.menuOptions = []
          const menu = { id: '0', menuName: '主类目', children: [] }
          menu.children = construct(data, {
            id: 'id',
            pid: 'parentId',
            children: 'children'
          })
          this.menuOptions.push(menu)
        }
      })
    },
    /** 转换资源数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.menuName,
        children: node.children
      }
    },
    /** 获取详情 */
    getMenu: function(id) {
      getMenu(id).then(response => {
        if (response.success) {
          this.form = response.data
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
          updateMenu(this.form).then(response => {
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
