<template>
  <el-card class="box-card" shadow="always">
    <el-row type="flex" justify="space-between">
      <el-col :span="12">
        <el-button-group>
          <el-button
            v-hasPerm="['system:dept:add']"
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
      :data="deptTree"
      row-key="id"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      border
      tooltip-effect="dark"
      :size="tableSize"
      :height="tableHeight"
      default-expand-all
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
              v-hasPerm="['system:dept:add']"
              size="mini"
              type="text"
              icon="el-icon-plus"
              @click="handleAdd(scope.row)"
            >新增</el-button>
            <el-button
              v-hasPerm="['system:dept:edit']"
              size="mini"
              type="text"
              icon="el-icon-edit-outline"
              @click="handleEdit(scope.row)"
            >修改</el-button>
            <el-button
              v-hasPerm="['system:dept:detail']"
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleDetail(scope.row)"
            >详情</el-button>
            <el-button
              v-hasPerm="['system:dept:remove']"
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
import { listDept, delDept, delDepts } from '@/api/system/dept'
import { construct } from '@/utils/json-tree'

export default {
  name: 'DeptList',
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
        { prop: 'deptName', label: '部门名称', show: true },
        { prop: 'deptNo', label: '部门编码', show: true },
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
      // 部门表格数据
      deptTree: [],
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
  },
  mounted() {
    this.initCols()
  },
  methods: {
    /** 查询部门列表 */
    getList() {
      this.loading = true
      listDept().then(response => {
        const { data } = response
        const deptList = data
        // 插件转tree
        this.deptTree = construct(deptList, {
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
        delDept(row.id).then(response => {
          if (response.success) {
            this.$message.success('删除成功')
            this.getList()
          }
        })
      }).catch(() => {
      })
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
