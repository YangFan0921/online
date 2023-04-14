let resetPasswordByPhoneApp = new Vue({
    el:"#resetPasswordByPhoneApp",
    data:{
        user:{},
        
        username:'',
        password:'',
        confirmPassword:'',
        message:'',
        hasError:false,
    },
    methods: {
        resetPasswordByPhone(){
            let data = new FormData($("form")[0]);
            console.log(data);
            if(this.password != this.confirmPassword){
                this.message = "确认密码不一致！";
                this.hasError = true;
                return;
            }
            axios.post("/users/resetPasswordByPhone",data).then(function(r){
                // console.log(r.data);
                if(r.data == "此用户不存在"){
                    resetPasswordByPhoneApp.message = r.data
                    resetPasswordByPhoneApp.hasError = true
                    // console.log(r.data);
                }else if(r.data == "修改成功"){
                    alert("修改成功")
                    location.href="/login.html"
                }
            })

        }
    },
    created() {
        
    },
})