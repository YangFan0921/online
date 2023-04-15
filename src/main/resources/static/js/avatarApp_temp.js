Vue.component("avatarApp",{
    props:["user","logout"],
    template:`
        <div class="dropdown   py-1 mr-3 " id="avatarApp">
          <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown"
                  aria-haspopup="true" aria-expanded="false">
            <img style="width: 30px;height: 30px;border-radius: 50%;"
                 :src="user.avatarUrl">
          </button>
          <div class="dropdown-menu dropdown-menu-sm-left text-sm border-0" aria-labelledby="dropdownMenu2">
            <a class="dropdown-item " href="../personal/userInfo.html">账号设置</a>
            <div class="dropdown-divider"></div>
            <button data-toggle="modal" data-target="#invitecodeModal" class="dropdown-item btn-default">查看邀请码
            </button>
            <div class="dropdown-divider"></div>
            <a class="dropdown-item " href="../login.html?logout" @click="logout">注销登录</a>
          </div>
        </div>
    `
})