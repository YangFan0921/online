let loginMsgApp = new Vue({
    el:"#fail",
    data:{
        msgStatus:false,
        msg:"",

    },
    created(){
        let rst = window.location.search.substring(1)
        if (rst === "fail"){
            this.msgStatus = true
                axios.get("/fail").then(function (r) {
                    console.log("r",r)
                    this.msg = r.data
                    document.getElementById('spanMsg').innerHTML = this.msg
                    console.log("msg",this.msg)
                }).catch(function (e) {
                    console.log("e",e)
                })
        }
    },
    methods:{


    }


})