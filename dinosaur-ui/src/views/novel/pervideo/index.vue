<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="视频名" prop="videoName">
        <el-input
          v-model="queryParams.videoName"
          placeholder="请输入视频名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作者" prop="videoAuthor">
        <el-input
          v-model="queryParams.videoAuthor"
          placeholder="请输入作者"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="视频code" prop="videoCode">
        <el-input
          v-model="queryParams.videoCode"
          placeholder="请输入视频code"
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
          v-hasPermi="['novel:pervideo:add']"
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
          v-hasPermi="['novel:pervideo:edit']"
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
          v-hasPermi="['novel:pervideo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['novel:pervideo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pervideoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center">
        <template slot-scope="scope">
          <span>{{total - queryParams.pageSize*(queryParams.pageNum-1) - scope.$index}}</span>
        </template>
      </el-table-column>
      <el-table-column label="视频名" align="center" prop="videoName" />
      <!--<el-table-column label="作者" align="center" prop="videoAuthor" />-->
      <el-table-column label="视频code" align="center" prop="videoCode" />
      <!--<el-table-column label="视频状态" align="center" prop="videoStatus" />-->
      <!--<el-table-column label="视频封面图片" align="center" prop="videoImage" />-->
<!--      <el-table-column label="视频链接地址" align="center" prop="videoUrl" show-overflow-tooltip/>-->
      <el-table-column label="视频简介" align="center" prop="videoIntro" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['novel:pervideo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="handleView(scope.row)"
            v-hasPermi="['novel:pervideo:view']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['novel:pervideo:remove']"
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

    <!-- 添加或修改视频对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="视频名" prop="videoName">
          <el-input v-model="form.videoName" placeholder="请输入视频名" />
        </el-form-item>
        <el-form-item label="作者" prop="videoAuthor">
          <el-input v-model="form.videoAuthor" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="视频code" prop="videoCode">
          <el-input v-model="form.videoCode" placeholder="请输入视频code" />
        </el-form-item>
        <el-form-item label="视频封面图片">
          <imageUpload v-model="form.videoImage"/>
        </el-form-item>
        <el-form-item label="视频链接地址" prop="videoUrl">
          <el-input v-model="form.videoUrl" placeholder="请输入视频链接地址" />
        </el-form-item>
        <el-form-item label="视频简介" prop="videoIntro">
          <el-input v-model="form.videoIntro" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="视频分类" prop="cateStr">
          <el-input v-model="form.cateStr" placeholder="请输入视频分类" />
        </el-form-item>
        <el-form-item label="分类名称" prop="cateName">
          <el-input v-model="form.cateName" placeholder="请输入分类名称" />
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
import { listPervideo, getPervideo, delPervideo, addPervideo, updatePervideo } from "@/api/novel/pervideo";

export default {
  name: "Pervideo",
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 视频表格数据
      pervideoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        videoName: null,
        videoAuthor: null,
        videoCode: null,
        videoStatus: null,
        videoImage: null,
        videoUrl: null,
        videoIntro: null,
        cateStr: null,
        cateName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询视频列表 */
    getList() {
      this.loading = true;
      listPervideo(this.queryParams).then(response => {
        this.pervideoList = response.rows;
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
        videoId: null,
        videoName: null,
        videoAuthor: null,
        videoCode: null,
        videoStatus: "0",
        videoImage: null,
        videoUrl: null,
        videoIntro: null,
        cateStr: null,
        cateName: null,
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
      this.ids = selection.map(item => item.videoId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加视频";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const videoId = row.videoId || this.ids
      getPervideo(videoId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改视频";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.videoId != null) {
            updatePervideo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPervideo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 查看详情 */
    handleView(row) {
      const videoId = row.videoId;
      this.$router.push("/pervideo/detail/"+ videoId);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const videoIds = row.videoId || this.ids;
      this.$modal.confirm('是否确认删除视频编号为"' + videoIds + '"的数据项？').then(function() {
        return delPervideo(videoIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('novel/pervideo/export', {
        ...this.queryParams
      }, `pervideo_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
