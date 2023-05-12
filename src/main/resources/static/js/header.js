let navbarNavAltMarkupApp = new Vue({
    el:"#navbarNavAltMarkup",
    data:{
        user:{}
    },
    methods:{
        loadNavbarNavAltMarkup(){
            axios.get("/users/me").then(function (r){
                navbarNavAltMarkupApp.user = r.data;
                // console.log(r.data)
            }).catch(function (e){
                console.log(e)
            })
        }
    },
    created(){
        this.loadNavbarNavAltMarkup();
    }
})

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
                NoticeCountApp.type = NoticeCountApp.user.type
                // NoticeCountApp.user.avatarUrl = "http://localhost:8899/"+NoticeCountApp.user.avatarUrl
                // console.log("user.type",NoticeCountApp.user.type)
                // console.log("type",NoticeCountApp.type)
            })
        },
        loadNoticeCount(){
            axios.get("/notice/counts").then(function (r) {
                NoticeCountApp.counts = r.data
                // console.log("counts:",r.data)

            })
        },
        clickTabContent(){
            myTabContentApp.loadTabContent()
        }

    },
    created(){
        this.loadInfo();
        this.loadNoticeCount()
    }
})

let avatarApp = new Vue({
    el:"#avatarApp",
    data:{
        // counts : 0,
        user:{}
    },
    methods:{
        loadAvatar(){
            axios.get("/users/myInfo").then(function (r) {
                // console.log(r.data)
                avatarApp.user = r.data;
                // avatarApp.user.avatarUrl = "http://localhost:8899/"+avatarApp.user.avatarUrl
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
             console.log(r.data)
         })
        }
    },
    created(){
        // this.loadTabContent()
    }
})

