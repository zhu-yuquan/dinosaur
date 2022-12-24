<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="父级id" prop="parentId">
        <el-input
          v-model="queryParams.parentId"
          placeholder="请输入父级id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="相册所属人id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入相册所属人id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="相册名称" prop="photoName">
        <el-input
          v-model="queryParams.photoName"
          placeholder="请输入相册名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="可见状态" prop="visibleType">
        <el-select v-model="queryParams.visibleType" placeholder="请选择可见状态" clearable size="small">
          <el-option label="公开" value="0"/>
          <el-option label="朋友可见" value="1"/>
          <el-option label="私人可见" value="3"/>
        </el-select>
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
          v-hasPermi="['novel:perphoto:add']"
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
          v-hasPermi="['novel:perphoto:edit']"
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
          v-hasPermi="['novel:perphoto:remove']"
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
          v-hasPermi="['novel:perphoto:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="perphotoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" align="center" prop="photoId"/>
      <el-table-column label="父级" align="center" prop="parentId"/>
      <el-table-column label="相册所属人" align="center" prop="userId"/>
      <el-table-column label="相册名称" align="center" prop="photoName"/>
      <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip="true"/>
      <el-table-column label="是否公开" align="center" prop="visibleType"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['novel:perphoto:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['novel:perphoto:remove']"
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

    <!-- 添加或修改相册对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="相册所属人" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择相册所属人" clearable>
            <el-option label="管理员" value="1"/>
          </el-select>
        </el-form-item>
        <el-form-item label="相册名称" prop="photoName">
          <el-input v-model="form.photoName" placeholder="请输入相册名称"/>
        </el-form-item>
        <el-form-item label="可见状态" prop="visibleType">
          <el-select v-model="form.visibleType" placeholder="请选择可见状态" clearable>
            <el-option label="公开" :value="0"/>
            <el-option label="朋友可见" :value="1"/>
            <el-option label="私人可见" :value="3"/>
          </el-select>
        </el-form-item>
        <el-form-item label="删除状态" prop="delFlag">
          <el-select v-model="form.delFlag" placeholder="请选择删除状态" clearable>
            <el-option label="正常" :value="N"/>
            <el-option label="删除" :value="Y"/>
          </el-select>
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
  import {listPhoto, getPhoto, delPhoto, addPhoto, updatePhoto, exportPhoto} from "@/api/novel/perphoto";

  export default {
    name: "Photo",
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
        // 相册表格数据
        perphotoList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          parentId: null,
          userId: null,
          photoName: null,
          visibleType: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          userId: [
            {required: true, message: "相册所属人不能为空", trigger: "blur"}
          ],
          photoName: [
            {required: true, message: "相册名称不能为空", trigger: "blur"}
          ],
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询相册列表 */
      getList() {
        this.loading = true;
        listPhoto(this.queryParams).then(response => {
          this.perphotoList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          photoId: null,
          parentId: null,
          userId: null,
          photoName: null,
          remark: null,
          visibleType: null,
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
        this.ids = selection.map(item => item.photoId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加相册";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const photoId = row.photoId || this.ids
        getPhoto(photoId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改相册";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.photoId != null) {
              updatePhoto(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addPhoto(this.form).then(response => {
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
        const photoIds = row.photoId || this.ids;
        this.$confirm('是否确认删除相册编号为"' + photoIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delPhoto(photoIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有相册数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportPhoto(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {
        });
      }
    }
  };
</script>
