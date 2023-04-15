Vue.component("navbarNavAltMarkupApp",{
    props:["user"],
    template:`
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
          <div class="navbar-nav">
            <a class="nav-item nav-link active" href="../index.html">首页</a>
            <a class="nav-item nav-link active" href="../personal/myQuestion.html">提问</a>
            <a class="nav-item nav-link active" href="../personal/task.html" v-show="user.type">任务</a>
            <a class="nav-item nav-link active" href="../personal/collect.html">收藏</a>
          </div>
        </div>
    `
})