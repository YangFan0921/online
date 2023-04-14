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