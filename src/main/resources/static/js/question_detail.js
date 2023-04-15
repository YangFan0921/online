let questionApp = new Vue({
    el:"#questionApp",
    data:{
        question:{},
    },
    methods:{
        loadQuestions(){
            let qid = location.search;
            // console.log(qid)
            if (!qid){
                alert("必须选择一个问题")
                return
            }
            // "?153"  -->  "153"
            qid = qid.substring(1)
            axios.get("/questions/"+qid).then(function (r) {
                // console.log(r.data)
                questionApp.question = r.data;
                addDuration(questionApp.question)
            }).catch(function (e){
                console.log(e)
            })
        },
    },
    created(){
        this.loadQuestions();
    }
})


let postAnswerApp = new Vue({
    el:"#postAnswerApp",
    data:{
        message:"",
        hasError:false
    },
    methods:{
        postAnswer(){
            //判断url上有id
            let qid=location.search;
            if(!qid){
                postAnswerApp.hasError=true;
                postAnswerApp.message="必须指定问题id";
                return;
            }
            qid=qid.substring(1);
            //判断富文本编辑器中有内容
            let content=$("#summernote").val();
            if(!content){
                postAnswerApp.hasError=true;
                postAnswerApp.message="回答内容不能为空";
                return;
            }
            //定义form表单对象
            let form=new FormData();
            form.append("questionId",qid);
            form.append("content",content);
            axios.post("/answers",form).then(function (r) {
                // console.log(r.data)
                let answer = r.data;
                answersApp.answers.push(answer);
                $("#summernote").summernote("reset");
                postAnswerApp.hasError=false;
                answer.duration="刚刚";
            }).catch(function (e) {
                console.log(e)
            })
        }
    },
    created(){}
})

let answersApp = new Vue({
    el:"#answersApp",
    data:{
        answers:[],
        editMessage:'',
        postCommentMessage:'',
        editError:false,
        postCommentError:false,
    },
    methods:{
        loadAnswers(){
            let qid=location.search;
            if(!qid){
                alert("必须指定问题id");
                return;
            }
            qid=qid.substring(1);
            axios.get("/answers/question/"+qid).then(function (r) {
                // console.log(r.data)
                answersApp.answers = r.data;
                let answers = answersApp.answers;
                for(let i=0;i<answers.length;i++){
                    addDuration(answers[i]);
                }

            }).catch(function (e) {
                console.log(e)
            })
        },
        postComment(answerId){
            if(!answerId){
                return;
            }
            //获得当前评论区域中的textarea
            let textarea=$("#addComment"+answerId+" textarea")
            let content=textarea.val();
            if(!content){
                answersApp.postCommentError = true
                answersApp.postCommentMessage = "评论内容不能为空"
                return;
            }
            let form=new FormData();
            form.append("answerId",answerId);
            form.append("content",content);
            axios.post("/comments",form).then(function (r) {
                console.log(r.data);
                //清空新增的文字
                textarea.val("");
                //将弹出的输入框折叠隐藏
                $("#addComment"+answerId).collapse("hide");
                let comment=r.data;
                let answers=answersApp.answers;
                // 遍历所有回答
                for(let i=0;i<answers.length;i++){
                    //判断当前answers[i]是不是本次新增评论的回答
                    if(answers[i].id == answerId){
                        //如果是,将本次新增的评论添加到当前回答的评论数组中
                        answers[i].comments.push(comment);
                        return;
                    }
                }
            }).catch(function (e) {
                console.log(e)
            })
        },
        removeComment(commentId,index,comments){
            if(!commentId){
                return;
            }
            axios.delete("/comments/"+commentId+"/delete").then(function (r) {
                console.log(r.data)
                if(r.data!="删除成功"){
                    alert(r.data);
                }else {
                    //删除成功,从数组中移除指定位置元素
                    //删除数组中指定位置元素
                    //splice([从数组中的哪个索引开始删],[删除几个])
                    comments.splice(index,1)
                }
            }).catch(function (e) {
                console.log(e)
            })
        },
        updateComment(commentId,answerId,index,comments){
            let textarea=$("#editComment"+commentId+" textarea");
            let content=textarea.val();
            if(!content){
                answersApp.editError = true
                answersApp.editMessage = "修改评论内容不能为空"
                return;
            }
            let form=new FormData();
            form.append("answerId",answerId);
            form.append("content",content);
            axios.get("/comments/"+commentId+"/update").then(function (r) {
                console.log(r.data)
                Vue.set(comments,index,r.data);
                $("#editComment"+commentId).collapse("hide");
            }).catch(function (e) {
                console.log(e)
            })
        },
        answerSolved(answerId){
            axios.get("/answers/"+answerId+"/solved").then(function (r) {
                console.log(r.data)
                let data = r.data;
                if (data == "采纳成功"){
                    alert("采纳成功")
                    location.reload()
                }else if (data == "采纳失败,检查回答存在并且没有被采纳过"){
                    alert("采纳失败,检查回答存在并且没有被采纳过")
                }else if (data == "你不是此问题的用户"){
                    alert("你不是此问题的用户")
                }
            })
        },
    },

    created(){
        this.loadAnswers();
    }
})