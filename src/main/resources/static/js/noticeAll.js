let myTabContentsApp = new Vue({
    el:"#myTabContents",
    data:{
        user:{},
        tabContents:[],
        pageinfo:{}
    },
    methods:{
        loadMyTabContents(pageNum) {
            if (!pageNum) {
                pageNum = 1;
            }
            axios({
                url: '/notice',
                method: "GET",
                params: {
                    pageNum: pageNum
                }
            }).then(function (response) {
                // console.log("成功加载数据");
                // console.log(response);
                if (response.status == OK) {
                    if (response.data == "不允许访问"){
                        // console.log("else OK",response.status)
                        window.location.href="../error/error.html";
                    }
                    console.log("OK",response.data)
                    console.log("status",response.status)
                    myTabContentsApp.tabContents = response.data.list;
                    myTabContentsApp.pageinfo = response.data;
                    //为question对象添加持续时间属性
                    myTabContentsApp.updateDuration();
                    window.onhashchange = myTabContentsApp.loadMyTabContents;
                }
            }).catch(function (r) {
                console.log(r.data)
            })
        },
        getMyInfo(){
            axios.get("/users/me").then(function (r) {
                myTabContentsApp.user = r.data
            })
        },
        updateDuration:function () {
            let questions = this.tabContents;
            for(let i=0; i<questions.length; i++){
                //创建问题时候的时间毫秒数
                let createtime = new Date(questions[i].createtime).getTime();
                //当前时间毫秒数
                let now = new Date().getTime();
                let duration = now - createtime;
                if (duration < 1000*60){ //一分钟以内
                    questions[i].duration = "刚刚";
                }else if(duration < 1000*60*60){ //一小时以内
                    questions[i].duration =
                        (duration/1000/60).toFixed(0)+"分钟以前";
                }else if (duration < 1000*60*60*24){
                    questions[i].duration =
                        (duration/1000/60/60).toFixed(0)+"小时以前";
                }else if(duration < 1000*60*60*24*30){
                    questions[i].duration =
                        (duration/1000/60/60/24).toFixed(0)+"天以前";
                }else {
                    questions[i].duration =
                        (duration/1000/60/60/24/30).toFixed(0)+"月以前";
                }
            }
        }
    },
    created(){
        this.loadMyTabContents();
        this.getMyInfo();
    }
})