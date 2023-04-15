let hotQuestionsApp = new Vue({
    el:"#hotQuestions",
    data:{
        questions:[]
    },
    methods:{
        loadHotQuestions(){
            axios.get("/questions/hotQuestions").then(function (r) {
                // console.log(r.data)
                hotQuestionsApp.questions = r.data
            })
        }
    },
    created(){
        this.loadHotQuestions();
    }
})