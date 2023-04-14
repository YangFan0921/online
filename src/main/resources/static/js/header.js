let NoticeCountApp = new Vue({
    el:"#noticeCount",
    data:{
        counts : 0,
        user:{},
        type:0
    },
    methods:{
        loadInfo(){
            axios.get("/users/myInfo").then(function (r) {
                // console.log(r.data)
                NoticeCountApp.user = r.data;
            })
        },
        loadNoticeCount(){
            axios.get("/notice/counts").then(function (r) {
                NoticeCountApp.counts = r.data
                // console.log("counts:",r.data)
                NoticeCountApp.type = NoticeCountApp.user.type
                // console.log(NoticeCountApp.user)
                // console.log(NoticeCountApp.type)
            })
        }

    },
    created(){
        this.loadInfo();
        this.loadNoticeCount()
    }
})

let myTabContentApp = new Vue({
    el:"#myTabContent",
    data:{
        tabContents:[]
    },
    methods:{
        loadTabContent(){
         axios.get("/notice/tabContent").then(function (r) {
             // console.log(r.data)
             myTabContentApp.tabContents = r.data
         })
        }
    },
    created(){
        this.loadTabContent()
    }
})

let avatarApp = new Vue({
    el:"#avatarApp",
    data:{
        counts : 0,
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