Vue.component("noticeCountApp",{
    props:["user","type","counts","clickTabContent"],
    template:`
        <div id="noticeCount">
          <button type="button" class="btn btn-default btn-xs fa fa-bell-o" data-toggle="modal" data-target="#noticeModal" v-show="type==1" @click="clickTabContent">
            <span class="badge badge-warning badge-pill" v-text="counts" v-show="counts > 0">2</span>
          </button>
        </div>
    `
})