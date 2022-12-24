<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文件绝对路径" prop="absolutePath">
        <el-input
          v-model="queryParams.absolutePath"
          placeholder="请输入文件绝对路径"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="业务分类" prop="bizType">
        <el-select v-model="queryParams.bizType" placeholder="请选择业务分类" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="原文件名称" prop="oldFileName">
        <el-input
          v-model="queryParams.oldFileName"
          placeholder="请输入原文件名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="纬度" prop="latitude">
        <el-input
          v-model="queryParams.latitude"
          placeholder="请输入纬度"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经度" prop="longitude">
        <el-input
          v-model="queryParams.longitude"
          placeholder="请输入经度"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="新文件名称" prop="newFileName">
        <el-input
          v-model="queryParams.newFileName"
          placeholder="请输入新文件名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属Id" prop="ownerId">
        <el-input
          v-model="queryParams.ownerId"
          placeholder="请输入所属Id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属类型" prop="ownerType">
        <el-select v-model="queryParams.ownerType" placeholder="请选择所属类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="文件大小" prop="size">
        <el-input
          v-model="queryParams.size"
          placeholder="请输入文件大小"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="排序" prop="seq">
        <el-input
          v-model="queryParams.seq"
          placeholder="请输入排序"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择文件类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="上传时间" prop="uploadTime">
        <el-date-picker clearable size="small"
          v-model="queryParams.uploadTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择上传时间">
        </el-date-picker>
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
          v-hasPermi="['novel:cmsupload:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['novel:cmsupload:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['novel:cmsupload:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
		  :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['novel:cmsupload:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cmsuploadList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!--<el-table-column label="文件id" align="center" prop="uploadId" />-->
      <el-table-column label="序号" align="center">
        <template slot-scope="scope">
          <span>{{total - queryParams.pageSize*(queryParams.pageNum-1) - scope.$index}}</span>
        </template>
      </el-table-column>
      <el-table-column label="文件绝对路径" align="center" width="200" prop="absolutePath">
        <template slot-scope="scope" >
          <el-image fit="contain" style="width:200px;height:200px;border:none;" :src="scope.row.absolutePath" v-show="scope.row.absolutePath"/>
        </template>
      </el-table-column>
      <el-table-column label="业务分类" align="center" prop="bizType" />
      <el-table-column label="原文件名称" align="center" prop="oldFileName" />
      <!--<el-table-column label="纬度" align="center" prop="latitude" />-->
      <!--<el-table-column label="经度" align="center" prop="longitude" />-->
      <el-table-column label="新文件名称" align="center" prop="newFileName" />
      <el-table-column label="所属Id" align="center" prop="ownerId" />
      <el-table-column label="所属类型" align="center" prop="ownerType" />
      <el-table-column label="文件大小" align="center" prop="size" />
      <el-table-column label="排序" align="center" prop="seq" />
      <el-table-column label="文件类型" align="center" prop="type" />
      <el-table-column label="上传时间" align="center" prop="uploadTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.uploadTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['novel:cmsupload:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['novel:cmsupload:remove']"
          >删除</el-button>
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

    <!-- 添加或修改文件对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件绝对路径" prop="absolutePath">
          <el-input v-model="form.absolutePath" placeholder="请输入文件绝对路径" />
        </el-form-item>
        <el-form-item label="业务分类" prop="bizType">
          <el-select v-model="form.bizType" placeholder="请选择业务分类">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="原文件名称" prop="oldFileName">
          <el-input v-model="form.oldFileName" placeholder="请输入原文件名称" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="请输入纬度" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="请输入经度" />
        </el-form-item>
        <el-form-item label="新文件名称" prop="newFileName">
          <el-input v-model="form.newFileName" placeholder="请输入新文件名称" />
        </el-form-item>
        <el-form-item label="所属Id" prop="ownerId">
          <el-input v-model="form.ownerId" placeholder="请输入所属Id" />
        </el-form-item>
        <el-form-item label="所属类型" prop="ownerType">
          <el-select v-model="form.ownerType" placeholder="请选择所属类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="文件大小" prop="size">
          <el-input v-model="form.size" placeholder="请输入文件大小" />
        </el-form-item>
        <el-form-item label="排序" prop="seq">
          <el-input v-model="form.seq" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="文件类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择文件类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="上传时间" prop="uploadTime">
          <el-date-picker clearable size="small"
            v-model="form.uploadTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择上传时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
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
import { listUpload, getUpload, delUpload, addUpload, updateUpload, exportUpload } from "@/api/novel/cmsupload";

export default {
  name: "Upload",
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
      // 文件表格数据
      cmsuploadList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        absolutePath: null,
        bizType: null,
        oldFileName: null,
        latitude: null,
        longitude: null,
        newFileName: null,
        ownerId: null,
        ownerType: null,
        size: null,
        seq: null,
        type: null,
        uploadTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        absolutePath: [
          { required: true, message: "文件绝对路径不能为空", trigger: "blur" }
        ],
        oldFileName: [
          { required: true, message: "原文件名称不能为空", trigger: "blur" }
        ],
        newFileName: [
          { required: true, message: "新文件名称不能为空", trigger: "blur" }
        ],
        ownerId: [
          { required: true, message: "所属Id不能为空", trigger: "blur" }
        ],
        ownerType: [
          { required: true, message: "所属类型不能为空", trigger: "change" }
        ],
        size: [
          { required: true, message: "文件大小不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "文件类型不能为空", trigger: "change" }
        ],
        uploadTime: [
          { required: true, message: "上传时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询文件列表 */
    getList() {
      this.loading = true;
      listUpload(this.queryParams).then(response => {
        this.cmsuploadList = response.rows;
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
        uploadId: null,
        absolutePath: null,
        bizType: null,
        oldFileName: null,
        latitude: null,
        longitude: null,
        newFileName: null,
        ownerId: null,
        ownerType: null,
        size: null,
        seq: null,
        type: null,
        uploadTime: null,
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
      this.ids = selection.map(item => item.uploadId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加文件";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const uploadId = row.uploadId || this.ids
      getUpload(uploadId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改文件";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.uploadId != null) {
            updateUpload(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUpload(this.form).then(response => {
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
      const uploadIds = row.uploadId || this.ids;
      this.$confirm('是否确认删除文件编号为"' + uploadIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delUpload(uploadIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有文件数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportUpload(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
