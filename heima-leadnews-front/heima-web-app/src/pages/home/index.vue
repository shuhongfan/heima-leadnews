<template>
  <div class="wrapper" @touchmove.stop>
    <div class="top-body"><Home_Bar/></div>
    <div class="content-body" @touchmove.stop>
      <wxc-tab-page ref="wxc-tab-page" :tab-titles="tabTitles" :tab-styles="tabStyles" title-type="text" :tab-page-height="tabPageHeight" @wxcTabPageCurrentTabSelected="wxcTabPageCurrentTabSelected">
        <list @touchmove.self v-for="(v,index) in tabList" :key="index" class="item-container" :style="{ height: (tabPageHeight - tabStyles.height-12) + 'px' }">
          <!-- 下来刷新最新 -->
          <refresh @refresh='loadnew'  :display="shownew?'show':'hide'" class="loading">
            <loading-indicator class="loading-icon"></loading-indicator>
            <text class="loading-text">{{load_new_text}}</text>
          </refresh>
          <!-- 列表项，并绑定显示事件 -->
          <cell v-for="(item,key) in v" class="cell" @appear="show(item.id)" :key="key">
            <wxc-pan-item :ext-id="'1-' + (v) + '-' + (key)" @wxcPanItemClicked="wxcPanItemClicked(item)" @wxcPanItemPan="wxcPanItemPan">
              <Item0 v-if="item.type==0" :data="item"/>
              <Item1 v-if="item.type==1" :data="item"/>
                <Item3 v-if="item.type==2" :data="item"/>
              <Item3 v-if="item.type==3" :data="item"/>
            </wxc-pan-item>
          </cell>
          <!-- 上来加载更多 -->
          <loading @loading="loadmore" :display="showmore?'show':'hide'" class="loading">
            <loading-indicator class="loading-icon"></loading-indicator>
            <text class="loading-text">{{load_more_text}}</text>
          </loading>
        </list>

      </wxc-tab-page>
    </div>
  </div>
</template>

<script>
  import Home_Bar from "@/compoents/bars/home_bar"
  import WxcTabPage from "@/compoents/tabs/home_tabs"
  import {Utils, BindEnv,WxcPanItem } from 'weex-ui'
  import Item0 from '../../compoents/cells/article_0.vue'
  import Item1 from '../../compoents/cells/article_1.vue'
  import Item3 from '../../compoents/cells/article_3.vue'
  import Config from './config'
  import Api from '@/apis/home/api'

  const modal = weex.requireModule("modal")

  export default {
    name: 'HeiMa-Home',
    components: {Home_Bar,WxcTabPage, Item0,Item1,Item3,WxcPanItem},
    data: () => ({
      api:null,// API
      shownew:true,//是否显示loadnew动画
      showmore:false,//是否显示loadmore动画
      tabTitles: Config.tabTitles,//频道配置
      tabStyles: Config.tabStyles,//频道样式
      tabList: [...Array(Config.tabTitles.length).keys()].map(i => []),//列表数据集合
      tabPageHeight: 1334,//列表总高度
      params:{
        loaddir:1,
        index:0,
        tag:"__all__",
        size:10,
        maxBehotTime:0,
        minBehotTime:20000000000000
      },//列表数据请求参数
      ashow : {},//列表展示行为记录表
      timer : null,//定时函数

      token: '',
      equipmentId: ''
    }),
    computed:{
      // 渲染加载最新和更多的国际化语言
      load_new_text:function(){return this.$lang.load_new_text},
      load_more_text:function(){return this.$lang.load_more_text}
    },
    mounted(){
      // 激活推荐按钮
      this.$refs['wxc-tab-page'].setPage(1,null,true);
    },
    destroyed(){
      clearInterval(this.timer)
    },
    created () {
      console.log('aaaaaa')
      // 初始化高度，顶部菜单高度120+顶部bar 90
      this.tabPageHeight = Utils.env.getPageHeight()-222;
      Api.setVue(this);
      let _this = this;
      // 每隔5秒提交一次数据
      // this.timer = setInterval(function(){
      //   let result = Api.saveShowBehavior(_this.ashow);
      //   if(result){
      //     result.then((d)=>{
      //       // 标记已经处理完成
      //       let ids=d.data;
      //       for(let i=0;i<ids.length;i++){
      //         _this.ashow[ids[i].id]=false;
      //       }
      //     });
      //   }
      // },5000);

      this.$store.getToken().then(token => {
        this.token = token
      }).catch((e)=>{
        console.log(e)
      })
      this.$store.getEquipmentId().then(equipmentId=> {
        this.equipmentId = equipmentId
      }).catch((e)=>{
        console.log(e)
      })
      this.loadChannels();
    },
    methods: {
      // 列表项在可见区域展示后的事件处理
      show:function(id){
        if(this.ashow[id]==undefined){
          this.ashow[id]=true;
        }
      },
      loadChannels:function(){
       this.tabTitles = [{title: '动态',id:'__dyna__'},
        {title: '推荐',id:'__all__'}];
        Api.loadchannels(this.params).then((d)=>{
          for(let i=0;i<d.data.length;i++){
            this.tabTitles.push({title:d.data[i].name,id:d.data[i].id});
          }
        }).catch((e)=>{
          console.log(e)
        })
      },
      // 上拉加载更多
      loadmore:function(){
        this.showmore=true;
        this.params.loaddir=2
        this.load();
      },
      // 下来刷新数据
      loadnew:function(){
        this.shownew=true;
        this.params.loaddir=0
        this.load();
      },
      // 正常加载数据
      load : function(){
        Api.loaddata(this.params).then((d)=>{
          this.tanfer(d.data,d.host);
        }).catch((e)=>{
          console.log(e)
        })
      },
      // 列表数据转换成View需要的Model对象
      tanfer : function(data,host){
        if(data.length==0){
          this.showmore=false;
          this.shownew=false;
          modal.toast({message:'没有数据了...',duration:3})
          return ;
        }
        let arr = []
        for(let i=0;i<data.length;i++){
          let ims = []
          if(data[i].images){
              let imagesArr = data[i].images.replace(/[\[\]]/ig,'').split(',')
              for(var j = 0;j<imagesArr.length;j++){
                  // ims.push(host+imagesArr[j])
                  ims.push(imagesArr[j])
              }
          }
          const { id: articleId, title, comment, authorId, authorName, publishTime } = data[i]
          const querystr = `?token=${this.token}&equipmentId=${this.equipmentId}&articleId=${articleId}&title=${title}&authorId=${authorId}&authorName=${authorName}&publishTime=${publishTime}`
          let tmp = {
            id: articleId,
            title: title,
            comment: comment,
            authorId: authorId,
            source: authorName,
            date: publishTime,
            type: ims.length==2?1:ims.length,
            image: ims,
            icon: '\uf06d',
            staticUrl: `${data[i].staticUrl}${querystr}`
          }
          let time = new Date(data[i].publishTime).getTime();
          if(this.params.maxBehotTime<time){
            this.params.maxBehotTime=time;
          }
          if(this.params.minBehotTime>time){
            this.params.minBehotTime=time;
          }
          arr.push(tmp);
        }
        // this.$router.push({
        //   name:'article-info',
        //   params:arr[0]
        // })
        let newList = [...Array(this.tabTitles.length).keys()].map(i => []);
        if(this.params.loaddir!=0){
          arr = this.tabList[this.params.index].concat(arr);
        }else{
          arr=arr.concat(this.tabList[this.params.index]);
        }
        newList[this.params.index] = arr;
        this.tabList = newList;
        this.showmore=false;
        this.shownew=false;
      },
      // 频道页切换事件
      wxcTabPageCurrentTabSelected (e) {
        this.params.loaddir=1
        this.params.index=e.page
        this.params.tag = this.tabTitles[e.page]['id'];
        this.params.maxBehotTime=0
        this.params.minBehotTime=20000000000000
        this.shownew=true
        this.load();
      },
      // 兼容回调
      wxcPanItemPan (e) {
        if (BindEnv.supportsEBForAndroid()) {
          this.$refs['wxc-tab-page'].bindExp(e.element);
        }
      },
      // 列表项点击事件
      wxcPanItemClicked(item){
        this.$router.push({
          name:'article-info',
          params:item
        })
        console.log(item.staticUrl)
      }
    }
  };
</script>

<style lang="less" scoped>
  @import '../../styles/article';
  .wrapper{
    background-color: @body-background;
    font-size: @font-size;
    font-family: @font-family;
    flex-direction : column;
    flex-wrap:wrap;
  }
  .top-body{
  }
  .content-body{
    flex: 1;
    flex-direction : column;
  }
  .item-container {
    width: 750px;
    background-color: #ffffff;
  }
  .cell {
    background-color: #ffffff;
  }
</style>
