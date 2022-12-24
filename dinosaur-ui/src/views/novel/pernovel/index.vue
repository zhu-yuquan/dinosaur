<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="75px">
      <el-form-item label="书名" prop="bookName">
        <el-input
          v-model="queryParams.bookName"
          placeholder="请输入书名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作者" prop="bookAuthor">
        <el-input
          v-model="queryParams.bookAuthor"
          placeholder="请输入作者"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="书籍code" prop="bookCode">
        <el-input
          v-model="queryParams.bookCode"
          placeholder="请输入书籍code"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="书籍分类" prop="cateType">
        <el-select v-model="queryParams.cateType" placeholder="请选择书籍分类" clearable>
          <el-option
            v-for="item in cateTypeList"
            :key="item.value"
            :label="item.name"
            :value="item.value"
          ></el-option>
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
          v-hasPermi="['novel:pernovel:add']"
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
          v-hasPermi="['novel:pernovel:edit']"
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
          v-hasPermi="['novel:pernovel:remove']"
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
          v-hasPermi="['novel:pernovel:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pernovelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--<el-table-column label="小说id" align="center" prop="novelId" />-->
      <el-table-column label="序号" align="center">
        <template slot-scope="scope">
          <span>{{total - queryParams.pageSize*(queryParams.pageNum-1) - scope.$index}}</span>
        </template>
      </el-table-column>
      <el-table-column label="书名" align="center" prop="bookName"/>
      <el-table-column label="作者" align="center" prop="bookAuthor"/>
      <el-table-column label="书籍状态" align="center" prop="bookStatus"/>
      <el-table-column label="分类名称" align="center" prop="cateName"/>
      <el-table-column label="书籍code" align="center" prop="bookCode"/>
      <el-table-column label="书籍图片" align="center" prop="bookImage">
        <template width="90" slot-scope="scope">
          <el-image fit="contain" style="width:80px;height:80px;border:none;" :src="scope.row.bookImage"
                    v-show="scope.row.bookImage"/>
        </template>
      </el-table-column>
      <el-table-column label="书籍简介" align="center" prop="bookIntro" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['novel:pernovel:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdateCatalogue(scope.row)"
            v-hasPermi="['novel:pernovel:edit']"
          >更新章节
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="voicePrompt(scope.row)"
            v-hasPermi="['novel:pernovel:query']"
          >语音介绍
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="getReptile(scope.row)"
            v-hasPermi="['novel:pernovel:edit']"
          >reptile
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['novel:pernovel:remove']"
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

    <!-- 添加或修改小说对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="书名" prop="bookName">
          <el-input v-model="form.bookName" placeholder="请输入书名"/>
        </el-form-item>
        <el-form-item label="作者" prop="bookAuthor">
          <el-input v-model="form.bookAuthor" placeholder="请输入作者"/>
        </el-form-item>
        <el-form-item label="书籍code" prop="bookCode">
          <el-input v-model="form.bookCode" placeholder="请输入书籍code"/>
        </el-form-item>
        <el-form-item label="书籍状态" prop="bookStatus">
          <el-input v-model="form.bookStatus" placeholder="请输入书籍状态"/>
        </el-form-item>
        <el-form-item label="书籍分类" prop="cateType">
          <el-select v-model="form.cateType" placeholder="请选择书籍分类" clearable>
            <el-option
              v-for="item in cateTypeList"
              :key="item.value"
              :label="item.name"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分类名称" prop="cateName">
          <el-input v-model="form.cateName" placeholder="请输入分类名称"/>
        </el-form-item>
        <el-form-item label="书籍图片">
          <imageUpload v-model="form.bookImage"/>
        </el-form-item>
        <el-form-item label="书籍简介" prop="bookIntro">
          <el-input v-model="form.bookIntro" :rows="3" type="textarea" placeholder="请输入内容"/>
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
    listNovel,
    getReptile,
    updateCatalogueList,
    getNovel,
    delNovel,
    addNovel,
    updateNovel,
    exportNovel
  } from "@/api/novel/pernovel";

  export default {
    name: "Novel",
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
        // 小说表格数据
        pernovelList: [],
        cateTypeList: [
          {name: "完本", value: 0},
          {name: "玄幻", value: 1},
          {name: "武侠", value: 2},
          {name: "都市", value: 3},
          {name: "历史", value: 4},
          {name: "网游", value: 5},
          {name: "科幻", value: 6},
          {name: "女生", value: 7}
        ],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          bookName: null,
          bookAuthor: null,
          bookCode: null,
          bookStatus: null,
          cateType: null,
          cateName: null,
          bookImage: null,
          bookIntro: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {}
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询小说列表 */
      getList() {
        this.loading = true;
        listNovel(this.queryParams).then(response => {
          this.pernovelList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 爬虫
      getReptile(row) {
        console.log("reptile------")
        getReptile().then(response => {
          console.log("reptile" + response)
        });
      },
      // 阅读
      voicePrompt(row) {
        var content = "小说"+ row.bookName+ ",作者" + row.bookAuthor;
        content += row.cateName + row.bookStatus;
        content += "写的是" + row.bookIntro;
        new Audio("http://tts.baidu.com/text2audio?lan=zh&ie=UTF-8&spd=6&text=" + encodeURI(content)).play();
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          novelId: null,
          bookName: null,
          bookAuthor: null,
          bookCode: null,
          bookStatus: null,
          cateType: null,
          cateName: null,
          bookImage: null,
          bookIntro: null,
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
        this.ids = selection.map(item => item.novelId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加小说";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const novelId = row.novelId || this.ids
        getNovel(novelId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改小说";
        });
      },
      /** 更新小说章节按钮操作 */
      handleUpdateCatalogue(row) {
        const novelId = row.novelId;
        this.$confirm('是否确认更新"' + row.bookName + '"小说的章节数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return updateCatalogueList({novelId: novelId});
        }).then(() => {
          this.msgSuccess("数据更新较慢，请等待更新");
        }).catch(() => {
        });

      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.novelId != null) {
              updateNovel(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addNovel(this.form).then(response => {
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
        const novelIds = row.novelId || this.ids;
        this.$confirm('是否确认删除小说编号为"' + novelIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delNovel(novelIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有小说数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportNovel(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {
        });
      }
    }
  };
</script>
