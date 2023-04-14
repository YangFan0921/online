var avatarApp = new Vue({
    el:"#avatarApp",
    data:{
        user:{}
    },
    methods:{
        loadAvatar(){
            axios.get("/users/myInfo").then(function (r) {
                // console.log(r.data)
                avatarApp.user = r.data;
            })
        },
        logout(){
            axios.get("/logout").then(function (r){
                console.log(r.data)
            })
        }
    },
    created(){
        this.loadAvatar();
    }
})

var userInfoApp = new Vue({
    el:"#userInfoApp",
    data:{
        user:{},
        avatarUrl:'',
        nickname:'',
        sex:'',
        birthday:'',
        selfIntroduction:'',
    },
    methods:{
        loadUserInfo(){
            axios.get("/users/myInfo").then(function (r) {
                // console.log(r.data)
                userInfoApp.user = r.data;
            })
        },
        updateMyInfo(){
            let data = new FormData($("form")[0]);
            // let form = new FormData();
            // form.append("nickname",this.nickname)
            // form.append("sex",this.sex)
            // form.append("birthday",this.birthday)
            // form.append("selfIntroduction",this.selfIntroduction)
            console.log(data)
            axios.post("/users/updateMyInfo",data).then(function (r){
                console.log(r.data)
                if (r.data == '修改成功'){
                    alert(r.data)
                    location.reload()
                }else {
                    alert("服务器忙，修改失败")
                }


            })

        },
        handleAvatarSuccess(res){
            userInfoApp.user.avatarUrl = res
        }
    },
    created(){
        this.loadUserInfo();
    }
})

