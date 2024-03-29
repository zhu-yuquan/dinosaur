<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="小说" prop="novelId">
        <el-select v-model="queryParams.novelId" placeholder="请选择小说" filterable clearable size="small">
          <el-option
            v-for="item in pernovelList"
            :key="item.novelId"
            :label="item.bookName"
            :value="item.novelId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="章节名称" prop="catalogueName">
        <el-input
          v-model="queryParams.catalogueName"
          placeholder="请输入章节名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="章节编码" prop="catalogueCode">
        <el-input
          v-model="queryParams.catalogueCode"
          placeholder="请输入章节编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['novel:percatalogue:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['novel:percatalogue:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['novel:percatalogue:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['novel:percatalogue:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="percatalogueList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--<el-table-column label="章节id" align="center" prop="catalogueId" />-->
      <el-table-column label="序号" align="center">
        <template slot-scope="scope">
          <span>{{total - queryParams.pageSize*(queryParams.pageNum-1) - scope.$index}}</span>
        </template>
      </el-table-column>
      <el-table-column label="小说名称" align="center" prop="novelId" :formatter="novelFormat"/>
      <el-table-column label="章节名称" align="center" prop="catalogueName"/>
      <el-table-column label="章节编码" align="center" prop="catalogueCode"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['novel:percatalogue:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['novel:percatalogue:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改小说章节对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="小说" prop="novelId">
          <el-select v-model="form.novelId" placeholder="请选择小说" disabled>
            <el-option
              v-for="item in pernovelList"
              :key="item.novelId"
              :label="item.bookName"
              :value="item.novelId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="章节名称" prop="catalogueName">
          <el-input v-model="form.catalogueName" placeholder="请输入章节名称"/>
        </el-form-item>
        <el-form-item label="章节编码" prop="catalogueCode">
          <el-input v-model="form.catalogueCode" placeholder="请输入章节编码"/>
        </el-form-item>
        <el-form-item label="章节内容">
          <editor v-model="form.catalogueText" :min-height="192"/>
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listCatalogue,
    getCatalogue,
    delCatalogue,
    addCatalogue,
    updateCatalogue,
    exportCatalogue
  } from "@/api/novel/percatalogue";
  import {getAllListNovel} from "@/api/novel/pernovel";

  export default {
    name: "Catalogue",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 导出遮罩层
        exportLoading: false,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 小说章节表格数据
        percatalogueList: [],
        // 小说列表
        pernovelList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          novelId: 1,
          catalogueName: null,
          catalogueCode: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          novelId: [
            {required: true, message: "小说id不能为空", trigger: "change"}
          ],
        }
      };
    },
    created() {
      this.getList();
      this.getAllListNovel();
    },
    methods: {
      /** 查询小说章节列表 */
      getList() {
        this.loading = true;
        listCatalogue(this.queryParams).then(response => {
          this.percatalogueList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /** 查询小说列表 */
      getAllListNovel() {
        this.loading = true;
        getAllListNovel().then(response => {
          this.pernovelList = response.data;
          this.loading = false;
        });
      },
      novelFormat(row) {
        var novelId = row.novelId;
        var pernovelList = this.pernovelList;
        for (let i = 0; i < pernovelList.length; i++) {
          if (novelId == pernovelList[i].novelId) {
            return pernovelList[i].bookName;
          }
        }
        return "";
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          catalogueId: null,
          novelId: null,
          catalogueName: null,
          catalogueCode: null,
          catalogueText: null,
          delFlag: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.catalogueId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加小说章节";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const catalogueId = row.catalogueId || this.ids
        getCatalogue(catalogueId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改小说章节";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.catalogueId != null) {
              updateCatalogue(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addCatalogue(this.form).then(response => {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const catalogueIds = row.catalogueId || this.ids;
        this.$confirm('是否确认删除小说章节编号为"' + catalogueIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delCatalogue(catalogueIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有小说章节数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportCatalogue(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {
        });
      }
    }
  };
</script>
