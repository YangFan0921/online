let userApp = new Vue({
    el:"#userApp",
    data:{
        user:{}
    },
    created(){
        this.loadCurrentUser();
    },
    methods:{
        loadCurrentUser(){
            axios.get("/users/me").then(function (r){
                userApp.user = r.data;
                // console.log(r.data)
            }).catch(function (e){
                console.log(e)
            })
        }
    }
})
