<template>
  <div class="todo" style="padding: 10px">
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
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="dueDate" label="截止日期" />
      <el-table-column prop="completed" label="完成情况" />
      <el-table-column prop="userId" label="用户ID" />
      <el-table-column label="操作" >
        <template #default="scope">
          <el-button plain @click="loadtodo(scope.row.id)">
            查看事件
          </el-button>
          <el-button plain @click="loadUser(scope.row.id)">
            查看用户信息
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

    <el-dialog
        v-model="dialogVisible"
        title="提示"
        width="30%"
    >
      <el-form :model="form" label-width="120px">
        <el-form-item label="标题">
          <el-input v-model="form.title" style="width: 80%" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="事件描述">
          <el-input v-model="form.description" style="width: 80%" placeholder="请输入事件描述" />
        </el-form-item>
        <el-form-item label="截止日期">
          <el-date-picker
              v-model="form.dueDate"
              type="datetime"
              placeholder="选择日期"
              style="width: 80%"
          />
        </el-form-item>
        <el-form-item label="完成情况">
          <el-switch v-model="form.completed" />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input-number v-model="form.userId" :min="1" style="width: 80%" placeholder="请输入用户ID" />
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

    <div class="pagination">
      <el-dialog v-model="dialogTableVisible" title="事件描述" width="50%">
        <el-table :data="todosData">
          <el-table-column property="description" />
        </el-table>
      </el-dialog>
    </div>

    <div>
      <el-dialog v-model="dialogUservisible" title="用户信息" width="80%">
        <el-table :data="todosData" >
          <el-table-column property="id" label="用户ID"/>
          <el-table-column property="username" label="姓名"/>
          <el-table-column property="nickName" label="昵称"/>
          <el-table-column property="gender" label="性别"/>
          <el-table-column property="age" label="年龄"/>
          <el-table-column property="address" label="地址"/>
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


    </div>
  </div>
</template>

<script>

// @ is an alias to /src
import {ComponentSize, dayjs} from "element-plus";
import {ref} from "vue";
import request from "@/utils/request";

export default{
  name:'Todo',

  components:{

  },
  data() {
    return {
      form: {},

      dialogVisible:false,
      dialogTableVisible:false,
      dialogUservisible:false,
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
      request.get("/todos",{
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
      this.form = {
        title: '',
        description: '',
        dueDate: null, // 确保初始化为空字符串或其他默认值
        completed: false,
        userId: null
      };
    },
    loadtodo(Id) {
        request.get(`/todos/${Id}`)
            .then(res => {
              console.log("Response:", res);
              if (res.code === "0") {
                this.todosData = [res.data]; // 将单个对象包装成数组
                console.log("todosData:", this.todosData); // 打印 todosData
                this.dialogTableVisible = true;
              } else {
                console.error("获取用户及其待办事件失败:", res.msg);
              }
            })
            .catch(err => {
              console.error("请求失败:", err);
            });
      },
    loadUser(Id){
      request.get(`/todos/${Id}`)
          .then(res => {
            console.log("Response:", res);
            if (res.code === "0") {
              this.todosData = [res.data.user]; // 将单个对象包装成数组
              console.log("todosData:", this.todosData); // 打印 todosData
              this.dialogUservisible = true;
            } else {
              console.error("获取用户及其待办事件失败:", res.msg);
            }
          })
          .catch(err => {
            console.error("请求失败:", err);
          });
    },
    save(){
      this.form.dueDate = dayjs(this.form.dueDate).format('YYYY-MM-DD HH:mm:ss');
      console.log(this.form.dueDate);
      if(this.form.id){
        request.put('/todos', this.form).then(res=>{
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
        request.post('/todos', this.form).then(res=>{
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
      request.delete("/todos/"+id).then(res=>{
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