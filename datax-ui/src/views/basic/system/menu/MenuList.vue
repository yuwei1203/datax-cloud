<template>
  <el-card class="box-card" shadow="always">
    <el-row type="flex" justify="space-between">
      <el-col :span="12">
        <el-button-group>
          <el-button
            v-hasPerm="['system:menu:add']"
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
          >新增</el-button>
        </el-button-group>
      </el-col>
      <el-col :span="12">
        <div class="right-toolbar">
          <el-tooltip content="密度" effect="dark" placement="top">
            <el-dropdown trigger="click" @command="handleCommand">
              <el-button circle size="mini">
                <svg-icon class-name="size-icon" icon-class="colum-height" />
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="medium">正常</el-dropdown-item>
                <el-dropdown-item command="small">中等</el-dropdown-item>
                <el-dropdown-item command="mini">紧凑</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-tooltip>
          <el-tooltip content="刷新" effect="dark" placement="top">
            <el-button circle size="mini" @click="handleRefresh">
              <svg-icon class-name="size-icon" icon-class="shuaxin" />
            </el-button>
          </el-tooltip>
          <el-tooltip content="列设置" effect="dark" placement="top">
            <el-popover placement="bottom" width="100" trigger="click">
              <el-checkbox-group v-model="checkedTableColumns" @change="handleCheckedColsChange">
                <el-checkbox
                  v-for="(item, index) in tableColumns"
                  :key="index"
                  :label="item.prop"
                >{{ item.label }}</el-checkbox>
              </el-checkbox-group>
              <span slot="reference">
                <el-button circle size="mini">
                  <svg-icon class-name="size-icon" icon-class="shezhi" />
                </el-button>
              </span>
            </el-popover>
          </el-tooltip>
        </div>
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="menuTree"
      row-key="id"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      border
      tooltip-effect="dark"
      :size="tableSize"
      :height="tableHeight"
      style="width: 100%;margin: 15px 0;"
    >
      <template v-for="(item, index) in tableColumns">
        <el-table-column
          v-if="item.show"
          :key="index"
          :prop="item.prop"
          :label="item.label"
          :formatter="item.formatter"
          align="center"
          show-overflow-tooltip
        />
      </template>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-popover
            placement="left"
            trigger="click"
          >
            <el-button
              v-hasPerm="['system:menu:add']"
              size="mini"
              type="text"
              icon="el-icon-plus"
              @click="handleAdd(scope.row)"
            >新增</el-button>
            <el-button
              v-hasPerm="['system:menu:edit']"
              size="mini"
              type="text"
              icon="el-icon-edit-outline"
              @click="handleEdit(scope.row)"
            >修改</el-button>
            <el-button
              v-hasPerm="['system:menu:detail']"
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleDetail(scope.row)"
            >详情</el-button>
            <el-button
              v-hasPerm="['system:menu:remove']"
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-button slot="reference">操作</el-button>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
import { listMenu, delMenu, delMenus } from '@/api/system/menu'
import { construct } from '@/utils/json-tree'

export default {
  name: 'MenuList',
  data() {
    return {
      tableHeight: document.body.offsetHeight - 240 + 'px',
      // 展示切换
      showOptions: {
        data: {},
        showList: true,
        showAdd: false,
        showEdit: false,
        showDetail: false
      },
      // 遮罩层
      loading: true,
      // 表格头
      tableColumns: [
        { prop: 'menuName', label: '资源名称', show: true },
        {
          prop: 'menuIcon',
          label: '图标',
          show: true,
          formatter: this.menuIconFormatter
        },
        { prop: 'menuCode', label: '资源编码', show: true },
        { prop: 'menuPerms', label: '权限标识', show: true },
        { prop: 'menuPath', label: '资源路由地址', show: true },
        { prop: 'menuComponent', label: '资源组件路径', show: true },
        {
          prop: 'menuType',
          label: '菜单类型',
          show: true,
          formatter: this.menuTypeFormatter
        },
        {
          prop: 'status',
          label: '状态',
          show: true,
          formatter: this.statusFormatter
        },
        { prop: 'createTime', label: '创建时间', show: true }
      ],
      // 默认选择中表格头
      checkedTableColumns: [],
      tableSize: 'medium',
      // 资源表格数据
      menuTree: [],
      // 类型数据字典
      menuTypeOptions: [],
      // 状态数据字典
      statusOptions: []
    }
  },
  created() {
    this.getDicts('sys_menu_type').then(response => {
      if (response.success) {
        this.menuTypeOptions = response.data
      }
    })
    this.getDicts('sys_common_status').then(response => {
      if (response.success) {
        this.statusOptions = response.data
      }
    })
    this.getList()
  },
  mounted() {
    this.initCols()
  },
  methods: {
    /** 查询资源列表 */
    getList() {
      this.loading = true
      listMenu().then(response => {
        const { data } = response
        const menuList = data
        // 插件转tree
        this.menuTree = construct(menuList, {
          id: 'id',
          pid: 'parentId',
          children: 'children'
        })
        this.loading = false
      })
    },
    initCols() {
      this.checkedTableColumns = this.tableColumns.map(col => col.prop)
    },
    handleCheckedColsChange(val) {
      this.tableColumns.forEach(col => {
        if (!this.checkedTableColumns.includes(col.prop)) {
          col.show = false
        } else {
          col.show = true
        }
      })
    },
    handleCommand(command) {
      this.tableSize = command
    },
    /** 刷新列表 */
    handleRefresh() {
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.showOptions.data = {}
      if (row !== undefined) {
        this.showOptions.data.parentId = row.id
      }
      this.showOptions.showList = false
      this.showOptions.showAdd = true
      this.showOptions.showEdit = false
      this.showOptions.showDetail = false
      this.$emit('showCard', this.showOptions)
    },
    /** 修改按钮操作 */
    handleEdit(row) {
      this.showOptions.data.id = row.id || this.ids[0]
      this.showOptions.showList = false
      this.showOptions.showAdd = false
      this.showOptions.showEdit = true
      this.showOptions.showDetail = false
      this.$emit('showCard', this.showOptions)
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.showOptions.data.id = row.id || this.ids[0]
      this.showOptions.showList = false
      this.showOptions.showAdd = false
      this.showOptions.showEdit = false
      this.showOptions.showDetail = true
      this.$emit('showCard', this.showOptions)
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('选中数据将被永久删除, 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delMenu(row.id).then(response => {
          if (response.success) {
            this.$message.success('删除成功')
            this.getList()
          }
        })
      }).catch(() => {
      })
    },
    menuIconFormatter(row, column, cellValue, index) {
      const menuIcon = row.menuIcon
      if (menuIcon) {
        return <svg-icon icon-class={menuIcon} />
      }
    },
    menuTypeFormatter(row, column, cellValue, index) {
      const dictText = this.selectDictLabel(this.menuTypeOptions, cellValue)
      if (cellValue === '0') {
        return <el-tag type='primary'>{dictText}</el-tag>
      } else if (cellValue === '1') {
        return <el-tag type='warning'>{dictText}</el-tag>
      } else if (cellValue === '2') {
        return <el-tag type='info'>{dictText}</el-tag>
      }
    },
    statusFormatter(row, column, cellValue, index) {
      const dictText = this.selectDictLabel(this.statusOptions, cellValue)
      if (cellValue === '1') {
        return <el-tag type='success'>{dictText}</el-tag>
      } else {
        return <el-tag type='warning'>{dictText}</el-tag>
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.right-toolbar {
  float: right;
}
.el-card ::v-deep .el-card__body {
  height: calc(100vh - 170px);
}
</style>
