<template>
  <el-row :gutter="20">
    <el-col :span="4">
      <el-card class="box-card tree-wrapper" shadow="always">
        <el-tree
          ref="dept"
          :data="deptOptions"
          default-expand-all
          node-key="id"
          empty-text="加载中，请稍后"
          :props="defaultProps"
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
        />
      </el-card>
    </el-col>
    <el-col :span="20">
      <el-card class="box-card" shadow="always">
        <el-form ref="queryForm" :model="queryParams" :inline="true">
          <el-form-item label="用户名称" prop="username">
            <el-input
              v-model="queryParams.username"
              placeholder="请输入用户名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <template v-if="advanced">
            <el-form-item label="用户昵称" prop="nickname">
              <el-input
                v-model="queryParams.nickname"
                placeholder="请输入用户昵称"
                clearable
                size="small"
              />
            </el-form-item>
          </template>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            <el-button type="text" @click="toggleAdvanced">
              {{ advanced ? '收起' : '展开' }}
              <i
                :class="{ 'el-icon-arrow-up': advanced, 'el-icon-arrow-down': !advanced }"
              />
            </el-button>
          </el-form-item>
        </el-form>

        <el-row type="flex" justify="space-between">
          <el-col :span="12">
            <el-button-group>
              <el-button
                v-hasPerm="['system:user:add']"
                type="primary"
                icon="el-icon-plus"
                size="mini"
                @click="handleAdd"
              >新增</el-button>
              <el-button
                v-hasPerm="['system:user:edit']"
                type="success"
                icon="el-icon-edit-outline"
                size="mini"
                :disabled="single"
                @click="handleEdit"
              >修改</el-button>
              <el-button
                v-hasPerm="['system:user:detail']"
                type="info"
                icon="el-icon-view"
                size="mini"
                :disabled="single"
                @click="handleDetail"
              >详情</el-button>
              <el-button
                v-hasPerm="['system:user:remove']"
                type="danger"
                icon="el-icon-delete"
                size="mini"
                :disabled="multiple"
                @click="handleBatchDelete"
              >删除</el-button>
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
                  <el-checkbox-group
                    v-model="checkedTableColumns"
                    @change="handleCheckedColsChange"
                  >
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
          :data="userList"
          border
          tooltip-effect="dark"
          :size="tableSize"
          :height="tableHeight"
          style="width: 100%;margin: 15px 0;"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="序号" width="55" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
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
                  v-hasPerm="['system:user:edit']"
                  size="mini"
                  type="text"
                  icon="el-icon-edit-outline"
                  @click="handleEdit(scope.row)"
                >修改</el-button>
                <el-button
                  v-hasPerm="['system:user:detail']"
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleDetail(scope.row)"
                >详情</el-button>
                <el-button
                  v-hasPerm="['system:user:reset:password']"
                  size="mini"
                  type="text"
                  icon="el-icon-edit-outline"
                  @click="handlePassword(scope.row)"
                >重置密码</el-button>
                <el-button
                  v-hasPerm="['system:user:remove']"
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

        <el-pagination
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :current-page.sync="queryParams.pageNum"
          :page-size.sync="queryParams.pageSize"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import { pageUser, delUser, delUsers, resetPassword } from '@/api/system/user'
import { listDept } from '@/api/system/dept'
import { construct } from '@/utils/json-tree'

export default {
  name: 'UserList',
  data() {
    return {
      tableHeight: document.body.offsetHeight - 310 + 'px',
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
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 表格头
      tableColumns: [
        { prop: 'username', label: '用户名称', show: true },
        { prop: 'nickname', label: '用户昵称', show: true },
        { prop: 'email', label: '电子邮箱', show: true },
        { prop: 'phone', label: '手机号码', show: true },
        {
          prop: 'dept',
          label: '所属部门',
          show: true,
          formatter: this.deptFormatter
        },
        {
          prop: 'posts',
          label: '所属岗位',
          show: true,
          formatter: this.postFormatter
        },
        {
          prop: 'roles',
          label: '所属角色',
          show: true,
          formatter: this.roleFormatter
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
      // 用户表格数据
      userList: [],
      // 总数据条数
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        username: '',
        nickname: '',
        deptId: ''
      },
      // 高级搜索 展开/关闭
      advanced: false,
      // 部门列表
      deptOptions: [],
      defaultProps: {
        children: 'children',
        label: 'deptName'
      },
      // 状态数据字典
      statusOptions: []
    }
  },
  created() {
    this.getDicts('sys_common_status').then(response => {
      if (response.success) {
        this.statusOptions = response.data
      }
    })
    this.getList()
    this.getDeptTree()
  },
  mounted() {
    this.initCols()
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true
      pageUser(this.queryParams).then(response => {
        this.loading = false
        if (response.success) {
          const { data } = response
          this.userList = data.data
          this.total = data.total
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
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        username: '',
        nickname: '',
        deptId: ''
      }
      this.handleQuery()
    },
    /** 刷新列表 */
    handleRefresh() {
      this.getList()
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.showOptions.data = {}
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
    handlePassword(row) {
      this.$confirm('确认重置该用户密码, 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        resetPassword({ id: row.id }).then(response => {
          if (response.success) {
            this.$message.success('重置密码成功')
          }
        })
      }).catch(() => {
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('选中数据将被永久删除, 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delUser(row.id).then(response => {
          if (response.success) {
            this.$message.success('删除成功')
            this.getList()
          }
        })
      }).catch(() => {
      })
    },
    /** 批量删除按钮操作 */
    handleBatchDelete() {
      if (!this.ids.length) {
        this.$message({
          message: '请先选择需要操作的数据',
          type: 'warning'
        })
      }
      this.$message.warning('不支持批量删除')
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.queryParams.pageNum = 1
      this.queryParams.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.queryParams.pageNum = val
      this.getList()
    },
    /** 节点单击事件 */
    handleNodeClick(data) {
      this.queryParams.deptId = data.id
      this.getList()
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    deptFormatter(row, column, cellValue, index) {
      return row.dept.deptName
    },
    roleFormatter(row, column, cellValue, index) {
      return row.roles.map(item => item.roleName).join(',')
    },
    postFormatter(row, column, cellValue, index) {
      return row.posts.map(item => item.postName).join(',')
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
.tree-wrapper {
  overflow-y: auto;
}
</style>
