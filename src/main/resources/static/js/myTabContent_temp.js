Vue.component("myTabContentApp",{
    props:["tabContents"],
    template:`
        <div id="myTabContent" class="tab-content col-12">
            <div class="tab-pane fade show active row" id="commentNotice" role="tabpanel" aria-labelledby="noAnswer-tab">
              <div class="list-group list-group-flush">
                <h6 class="text-dark my-2" style="display: none" v-show="tabContents.length==0">暂无新消息！</h6>
                <a href="../notice/all.html" style="display: none" class="text-info" v-show="tabContents.length==0">查看历史消息</a>
                <!--只显示10条消息-->
                <div class="list-group-item list-group-item-action" v-for="tabContent in tabContents">
                  <span v-text="tabContent.userNickName">张三</span>
                  <strong style="display: none">回答了你的问题</strong>
                  <strong style="display: none">评论了您的回答</strong>
                  <strong style="display: none">评论了您的提问</strong>
                  <strong>向您提问</strong>
                  <a href="" class="text-info" v-text="tabContent.title" v-bind:href="'/question/detail_teacher.html?'+tabContent.id">Java中方法重载和重写的区别</a>
                </div>
              </div>
              <div class="col-12 mt-2 " v-show="tabContents.length > 10">
                <a href="../notice/all.html" class="text-info">查看全部消息</a>
              </div>
            </div>
            <div class="tab-pane fade row" id="announcement" role="tabpanel" aria-labelledby="unSolve-tab">
              <p class="text-black mt-2">暂无公告！</p>
            </div>
          </div>
    `
})