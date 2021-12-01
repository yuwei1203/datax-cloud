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
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="form.roleCode" />
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
                :check-strictly="menuCheckStrictly"
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
                :check-strictly="deptCheckStrictly"
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
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
    </div>
  </el-card>
</template>

<script>
import { getRole } from '@/api/system/role'
import { listMenu } from '@/api/system/menu'
import { listDept } from '@/api/system/dept'
import { construct } from '@/utils/json-tree'

export default {
  name: 'RoleDetail',
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
      title: '角色详情',
      // 展示切换
      showOptions: {
        data: {},
        showList: true,
        showAdd: false,
        showEdit: false,
        showDetail: false
      },
      // 表单参数
      form: {},
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
      },
      menuCheckStrictly: false,
      deptCheckStrictly: false
    }
  },
  created() {
    console.log('id:' + this.data.id)
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
  mounted() {
    this.getRole(this.data.id)
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
    /** 获取详情 */
    getRole: function(id) {
      getRole(id).then(response => {
        if (response.success) {
          this.form = response.data
          const menuCheckedKeys = response.data.menus.map(menu => menu.id)
          const deptCheckedKeys = response.data.depts.map(dept => dept.id)
          if (menuCheckedKeys && menuCheckedKeys.length > 0) {
            this.menuCheckStrictly = true
            this.$nextTick(() => {
              this.$refs.menu.setCheckedKeys(menuCheckedKeys)
              this.menuCheckStrictly = false
            })
          }
          if (deptCheckedKeys && deptCheckedKeys.length > 0) {
            this.deptCheckStrictly = true
            this.$nextTick(() => {
              this.$refs.dept.setCheckedKeys(deptCheckedKeys)
              this.deptCheckStrictly = false
            })
          }
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
