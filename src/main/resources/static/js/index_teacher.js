
/*
显示当前用户的问题
 */
let questionsApp = new Vue({
    el:'#questionsApp',
    data: {
        questions:[],
        pageinfo:{},
    },
    methods: {
        loadQuestions:function (pageNum) {
            // let index=location.href.lastIndexOf("#");
            // let pageNum=1;
            // if(index!=-1){
            //     pageNum=location.href.substring(index+1);
            // }
            console.log("pageNum: ",pageNum)
            if(! pageNum){
                pageNum = 1;
            }
            axios({
                url: '/questions/teacher',
                method: "GET",
                params:{
                    pageNum:pageNum
                }
            }).then(function(response){
                // console.log("成功加载数据");
                // console.log(response);
                if(response.status == OK){
                    questionsApp.questions = response.data.list;
                    questionsApp.pageinfo = response.data;
                    //为question对象添加持续时间属性
                    questionsApp.updateDuration();
                    questionsApp.updateTagImage();
                    window.onhashchange=questionsApp.loadQuestions;
                }
            })
        },
        updateTagImage:function(){
            let questions = this.questions;
            for(let i=0; i<questions.length; i++){
               let tags = questions[i].tags;
               if(tags){
                   let tagImage = '/img/tags/'+tags[0].id+'.jpg';
                   // console.log(tagImage);
                   questions[i].tagImage = tagImage;
               }
            }
        },
        updateDuration:function () {
            let questions = this.questions;
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
    created:function () {
        // console.log("执行了loadQuestions方法");
        this.loadQuestions();

    }
});










