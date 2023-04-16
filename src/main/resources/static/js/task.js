let App = new Vue({
    el:"#app",
    data:{
        user:{},
        noAnswerQuestions:[],
        unSolveQuestions:[],
        solvedQuestions:[],
        pageinfo:{}
    },
    methods:{
        loadNoAnswerQuestions(pageNum){
            if (!pageNum) {
                pageNum = 1;
            }
            axios({
                url: '/notice/task/noAnswer',
                method: "GET",
                params: {
                    pageNum: pageNum
                }
            }).then(function (response) {
                // console.log("成功加载数据");
                // console.log(response);
                if (response.status == OK) {
                    if (response.data=='不允许访问'){
                        // console.log("else OK",response.status)
                        window.location.href="../error/error.html";
                    }
                    // console.log(response.data)
                    App.noAnswerQuestions = response.data.list;
                    App.pageinfo = response.data;
                    window.onhashchange = App.loadNoAnswerQuestions;
                }
            }).catch(function (r) {
                console.log(r.data)
            })
        },
        loadUnSolveQuestion(pageNum){
            if (!pageNum) {
                pageNum = 1;
            }
            axios({
                url: '/notice/task/unSolve',
                method: "GET",
                params: {
                    pageNum: pageNum
                }
            }).then(function (response) {
                // console.log("成功加载数据");
                // console.log(response);
                if (response.status == OK) {
                    // console.log(response.data)
                    App.unSolveQuestions = response.data.list;
                    App.pageinfo = response.data;
                    window.onhashchange = App.loadUnSolveQuestion;
                }
            }).catch(function (r) {
                console.log(r.data)
            })
        },
        loadSolvedQuestion(pageNum){
            if (!pageNum) {
                pageNum = 1;
            }
            axios({
                url: '/notice/task/solved',
                method: "GET",
                params: {
                    pageNum: pageNum
                }
            }).then(function (response) {
                // console.log("成功加载数据");
                // console.log(response);
                if (response.status == OK) {
                    // console.log(response.data)
                    App.solvedQuestions = response.data.list;
                    App.pageinfo = response.data;
                    window.onhashchange = App.loadSolvedQuestion;
                }
            }).catch(function (r) {
                console.log(r.data)
            })
        },

        getMyInfo(){
            axios.get("/users/me").then(function (r) {
                App.user = r.data
            })
        },
    },
    created(){
        this.loadNoAnswerQuestions();
        this.getMyInfo();
    }
})