<script>
import {Lock, User} from "@element-plus/icons-vue";
import request from "@/utils/request";

export default {
  components: {Lock, User},
  data(){
    return{
      form:{},
      rules: {
        username: [
          {required:true,message:'请输入用户名',trigger:'blur'}
        ],
        password: [
            {required:true,trigger:'blur',message:'请输入密码'}
        ],
        confirm: [
          {required:true,message:'请再次输入密码',trigger:'blur'}
        ]
      }
    }
  },
  methods:{
    register(){
      this.$refs.form.validate(valid => {
        if (valid) {
          if(this.form.password!==this.form.confirm){
            this.$message({
              type:'error',
              message: '两次输入密码不一致'
            })
            return;
          }
          request.post("/user/register",this.form).then(res=>{
            console.log("完整响应数据:", res); // 打印完整响应
            if(res.code === '0'){
              this.$message({
                type:'success',
                message: "注册成功"
              })
              this.$router.push('/login');
            }
            else{
              this.$message({
                type:'error',
                message: res.data.msg
              })
            }
          })
        }
      })

    }
  }
}

</script>

<template>
  <div style="width:100%;height:100vh;background-color: darkslateblue;overflow:hidden;">
    <div style="width: 400px; margin: 150px auto">
      <div style="color: #cccccc;font-size: 30px;text-align: center;padding: 30px 0;">
        欢迎注册
      </div>
      <el-form ref="form" :model="form" size="default" :rules="rules">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名">
            <template v-slot:prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" show-password>
            <template v-slot:prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="confirm">
          <el-input v-model="form.confirm" placeholder="请再次输入密码" show-password>
            <template v-slot:prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="register">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>

</style>