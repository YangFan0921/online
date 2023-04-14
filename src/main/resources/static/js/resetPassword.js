// var header = new Vue({
//     el:"#header",
//     data:{
//         user:{}
//     },
//     methods:{
//         loadAvatar(){
//             axios.get("/users/myInfo").then(function (r) {
//                 // console.log(r.data)
//                 header.user = r.data;
//             })
//
//         }
//     },
//     created(){
//         this.loadAvatar();
//     }
// })

var resetPasswordApp = new Vue({
    el:"#resetPasswordApp",
    data:{
        user:{},
        
        oldPassword:'',
        newPassword:'',
        confirmPassword:'',
        message: '',
        hasError: false
    },
    methods:{
        resetPassword(){
            let data = new FormData($("form")[0]);
            if(this.newPassword != this.confirmPassword){
                this.message = "确认密码不一致！";
                this.hasError = true;
                return;
            }
            axios.post("/users/resetPassword",data).then(function(r){
                console.log(typeof(r.data));
                this.message = r.data
                console.log(this.message);
                if(r.data == '原密码错误'){
                    resetPasswordApp.message = "原密码错误";
                    resetPasswordApp.hasError = true;
                }else if(r.data == "修改成功"){
                    // alert("修改成功，请重新登录")
                    // this.$message("密码修改成功，请重新登录")
                    alert("修改成功，请重新登录")
                    axios.get("/logout").then(function(r){
                        location.href="/login.html"
                    })
                }
                
        })
        }
        
    },
    created(){

    }
})

