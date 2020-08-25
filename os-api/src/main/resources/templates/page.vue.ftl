<template>
    <div class="app-container">
        <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
            <el-form-item>
                <el-input v-model="queryParam.id" placeholder="ID" clearable />
            </el-form-item>
            <el-form-item>
                <el-button @click="fetchData()">查询</el-button>
                <el-button type="primary" @click="handleAdd()">新增</el-button>
                <el-button type="danger" :disabled="ids.length <= 0" @click="handleBatchDelete()">批量删除</el-button>
            </el-form-item>
        </el-form>

        <el-table
                v-loading="listLoading"
                :data="list"
                element-loading-text="Loading"
                border
                fit
                highlight-current-row
                @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" header-align="center" align="center" width="50" />
            <#list table.fields as field>
                <el-table-column label="${field.comment}">
                    <template slot-scope="scope">{{ scope.row.${field.propertyName} }}</template>
                </el-table-column>
            </#list>
            <el-table-column align="center" label="操作" width="150">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope)">修改</el-button>
                    <el-button type="danger" size="mini" @click="handleDelete(scope)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                :current-page="queryParam.currentPage"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="queryParam.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
        />

        <el-dialog :visible.sync="dialogVisible" :title="'新增'">
            <el-form ref="dataForm" :model="dataForm" label-width="80px" label-position="left">
                <el-form-item v-show="false" label="ID" prop="id" />
                <#list table.fields as field>
                    <el-form-item label="${field.comment}" prop="${field.propertyName}">
                        <el-input v-model="dataForm.${field.propertyName}" placeholder="请输入${field.comment}" />
                    </el-form-item>
                </#list>
            </el-form>
            <div style="text-align:right;">
                <el-button type="danger" @click="dialogVisible=false">取消</el-button>
                <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    import { add, del, update, getList } from '@/api/${entity?lower_case}'

    export default {
        filters: {
            statusFilter(status) {
                const statusMap = {
                    published: 'success',
                    draft: 'gray',
                    deleted: 'danger'
                }
                return statusMap[status]
            }
        },
        data() {
            return {
                dialogVisible: false,
                queryParam: {
                    currentPage: 1,
                    pageSize: 10
                },
                dataForm: {
                    <#list table.fields as field>
                    ${field.propertyName}: ''<#if field_has_next>,</#if>
                    </#list>
                },
                ids: [],
                list: null,
                listLoading: true,
                total: 0
            }
        },
        created() {
            this.fetchData()
        },
        methods: {
            fetchData() {
                this.listLoading = true
                getList(this.queryParam).then((response) => {
                    this.list = response.data.records
                    this.total = response.data.total
                    this.listLoading = false
                })
            },
            dataFormSubmit() {
                let request
                if (this.dataForm.id) {
                    request = update(this.dataForm)
                } else {
                    request = add(this.dataForm)
                }
                request.then((response) => {
                    this.dialogVisible = false
                    this.fetchData()
                    this.$message({ message: response.message, type: 'success' })
                })
            },
            handleSizeChange(pageSize) {
                this.queryParam.pageSize = pageSize
                this.fetchData()
            },
            handleCurrentChange(currentPage) {
                this.queryParam.currentPage = currentPage
                this.fetchData()
            },
            handleAdd() {
                this.dialogVisible = true
                this.$nextTick(() => {
                    this.$refs['dataForm'].resetFields()
                })
            },
            handleDelete({ $index, row }) {
                del([row.id]).then((response) => {
                    this.fetchData()
                    this.$message({ message: response.message, type: 'success' })
                })
            },
            handleBatchDelete() {
                del(this.ids).then((response) => {
                    this.fetchData()
                    this.$message({ message: response.message, type: 'success' })
                })
            },
            handleEdit(scope) {
                this.dialogVisible = true
                this.$nextTick(() => {
                    this.dataForm = JSON.parse(JSON.stringify(scope.row))
                })
            },
            // 多选
            handleSelectionChange(ids) {
                this.ids = ids.map(item => {
                    return item.id
                })
            }
        }
    }
</script>

<style scoped>
    .el-dialog{
        padding: 10px 20px;
    }
    .el-card{
        border: 0 solid #ffffff;
    }
</style>