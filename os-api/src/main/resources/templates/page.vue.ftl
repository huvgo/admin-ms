<template>
    <div class="app-container">
        <el-card class="box-card">
            <el-form :inline="true" :model="queryParam" @keyup.enter.native="fetchData()">
                <#list fields as field>
                    <#if field.condition>
                        <el-form-item>
                            <el-input v-model="queryParam.${field.name}" placeholder="${field.comment}" clearable/>
                        </el-form-item>
                    </#if>
                </#list>
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
                    fit
                    highlight-current-row
                    @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" header-align="center" align="center" width="50"/>
                <#list fields as field>
                    <el-table-column label="${field.comment}" prop="${field.name}"/>
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
        </el-card>
        <el-dialog :visible.sync="dialogVisible" :title="'新增'">
            <el-form ref="dataForm" :model="dataForm" label-width="80px" label-position="left">
                <el-form-item v-show="false" label="ID" prop="id"/>
                <#list fields as field>
                    <#if field.name = "id">
                        <el-form-item v-show="false" label="ID" prop="id"/>
                    <#elseif field.element = "2">
                        <el-form-item label="${field.comment}" prop="${field.name}">
                            <el-select v-model="dataForm.${field.name}" style="width:100%" placeholder="请选择">
                                <el-option
                                        v-for="item in options"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                    <#elseif field.element = "3">
                        <el-form-item label="${field.comment}" prop="${field.name}">
                            <el-date-picker
                                    v-model="dataForm.${field.name}"
                                    style="width:100%"
                                    type="date"
                                    format="yyyy-MM-dd"
                                    placeholder="选择用户操作日期"
                            />
                        </el-form-item>
                    <#elseif field.element = "4">
                        <el-form-item label="${field.comment}" prop="${field.name}">
                            <el-switch v-model="dataForm.${field.name}"/>
                        </el-form-item>
                    <#else>
                        <el-form-item label="${field.comment}" prop="${field.name}">
                            <el-input v-model="dataForm.${field.name}" placeholder="请输入${field.comment}"/>
                        </el-form-item>
                    </#if>
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
    import {add, del, getList, update} from '@/api/'

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
                    <#list fields as field>
                    <#if field.condition>
                    ${field.name}:
                        '',
                    </#if>
                    </#list>
                    currentPage
        :
            1,
                pageSize
        :
            10
        },
            dataForm: {
                <#list fields as field>
                ${field.name}:
                    ''<#if field_has_next>,
                </#if>
                </#list>
            }
        ,
            ids: [],
                list
        :
            null,
                listLoading
        :
            true,
                total
        :
            0
        }
        },
        created() {
            this.fetchData()
        },
        methods: {
            fetchData() {
                this.listLoading = true
                getList(this.queryParam).then((response) = > {
                    this.list = response.data.records
                    this.total = response.data.total
                    this.listLoading = false
                }
            )
            },
            dataFormSubmit() {
                let request
                if (this.dataForm.id) {
                    request = update(this.dataForm)
                } else {
                    request = add(this.dataForm)
                }
                request.then((response) = > {
                    this.dialogVisible = false
                    this.fetchData()
                    this.$message({message: response.message, type: 'success'})
                }
            )
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
                this.$nextTick(() = > {
                    this.$refs['dataForm'].resetFields()
                }
            )
            },
            handleDelete({$index, row}) {
                del([row.id]).then((response) = > {
                    this.fetchData()
                    this.$message({message: response.message, type: 'success'})
                }
            )
            },
            handleBatchDelete() {
                del(this.ids).then((response) = > {
                    this.fetchData()
                    this.$message({message: response.message, type: 'success'})
                }
            )
            },
            handleEdit(scope) {
                this.dialogVisible = true
                this.$nextTick(() = > {
                    this.dataForm = JSON.parse(JSON.stringify(scope.row))
                }
            )
            },
            // 多选
            handleSelectionChange(ids) {
                this.ids = ids.map(item = > {
                    return item.id
                }
            )
            }
        }
    }
</script>

<style scoped>
    .el-dialog {
        padding: 10px 20px;
    }

    .el-card {
        border: 0 solid #ffffff;
    }
</style>
