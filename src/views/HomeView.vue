<template>
  <div class="home" style="padding: 10px">
<!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>
<!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>

    <el-table :data="tableData" stripe border style="width: 100%">
      <el-table-column prop="id" label="ID" sortable />
      <el-table-column prop="username" label="姓名" />
      <el-table-column prop="nickName" label="昵称" />
      <el-table-column prop="age" label="年龄" />
      <el-table-column prop="gender" label="性别" />
      <el-table-column prop="address" label="地址" />
      <el-table-column label="操作" >
        <template #default="scope">
          <el-button plain @click="loadtodo(scope.row.id)">
            代办事件
          </el-button>

          <el-button  type="text"  @click="handleEdit(scope.row)">
            编辑
          </el-button>

            <el-popconfirm title="确认删除吗？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button type="text">删除</el-button>
              </template>
            </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-dialog v-model="dialogTableVisible" title="代办事件" width="80%">
        <el-table :data="todosData">
          <el-table-column property="title" label="标题"  />
          <el-table-column property="description" label="描述" />
          <el-table-column property="completed" label="完成状态" />
          <el-table-column label="截止日期">
            <template #default="scope">
              {{ formatDueDate(scope.row.dueDate) }}
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>
    </div>
    <div style="margin: 10px 0">
      <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 40]"
          :size="size"
          :disabled="disabled"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
      <el-dialog
          v-model="dialogVisible"
          title="提示"
          width="30%"
      >
        <el-form label-width="120px">
          <el-form-item :modal="form" label="用户名">
            <el-input v-model="form.username" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item :modal="form" label="昵称">
            <el-input v-model="form.nickName" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item :modal="form" label="年龄">
            <el-input v-model="form.age" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item :modal="form" label="性别">
            <el-radio v-model="form.gender" label="男">男</el-radio>
            <el-radio v-model="form.gender" label="女">女</el-radio>
            <el-radio v-model="form.gender" label="未知">未知</el-radio>
          </el-form-item>
          <el-form-item :modal="form" label="地址">
            <el-input type="textarea" v-model="form.address" style="width: 80%"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="save">
              确认
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>

// @ is an alias to /src
import {ComponentSize} from "element-plus";
import {ref} from "vue";
import request from "@/utils/request";

export default{
  name:'Home',

  components:{

  },
  data() {
    return {
      form: {},

      dialogVisible:false,
      dialogTableVisible:false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      size : ref<ComponentSize>('default'),
      background : ref(false),
      disabled : ref(false),
      total : ref(0),
      tableData : [

      ],
      todosData:[]
    }
},
  created(){
    this.load()
  },
  methods:{
    load(){
      request.get("/user",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search,
        }
      }).then(res=>{
        console.log(res)
        this.tableData = res.data.records;
        this.total = res.data.total;
      })
    },
    add(){
      this.dialogVisible = true;
      this.form={};
    },
    loadtodo(userId) {
      request.get(`/user/${userId}/todos`)
          .then(res => {
            console.log(res);
            if (res.code === "0") {
              if (Array.isArray(res.data.todos)) {
                this.todosData = res.data.todos.map(todo => ({
                  ...todo,
                  dueDate: this.formatDueDate(todo.dueDate)
                }));
              } else {
                console.error("todos 数据格式错误，不是数组:", res.data.todos);
                this.todosData = [];
              }
              this.dialogTableVisible = true;
            } else {
              console.error("获取用户及其待办事件失败:", res.msg);
            }
          })
          .catch(err => {
            console.error("请求失败:", err);
          });
    },
    formatDueDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleString(); // 使用本地化格式
    },

  save(){
      if(this.form.id){
        request.put('/user', this.form).then(res=>{
          console.log(res)
          if(res.code === '0'){
            this.$message({
              type:'success',
              message: "更新成功"
            })
          }
          else{
            this.$message({
              type:'error',
              message: res.data.msg
            })
          }
        })
        this.load()
        this.dialogVisible = false;
        window.location.reload()
      }
      else{
        request.post('/user', this.form).then(res=>{
          console.log(res)
          if(res.code === '0'){
            this.$message({
              type:'success',
              message: "新增成功"
            })
          }
          else{
            this.$message({
              type:'error',
              message: res.msg
            })
          }
        })
        this.load()
        this.dialogVisible = false;
        window.location.reload()
      }

    },
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogVisible = true;
    },
    handleDelete(id){
      console.log(id)
      request.delete("/user/"+id).then(res=>{
        console.log(res)
        if(res.code === '0'){
          this.$message({
            type:'success',
            message: "删除成功"
          })
        }
        else{
          this.$message({
            type:'error',
            message: res.msg
          })
        }
        this.load()
      })

    },
    handleSizeChange(val) {
      this.load()
      console.log(`${val} items per page`)
    },
    handleCurrentChange(val)  {
      this.load()
      console.log(`current page: ${val}`)
    }
  }


}

</script>

<style scoped>

</style>