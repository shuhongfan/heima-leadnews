<template>
  <div class="art-page" @touchmove.stop>
    <div class="art-top"><TopBar :text="reply + '评论回复'" /></div>
    <scroller @touchmove.self class="scroller" ref="scroller" @scroll="scroller" :style="{height:scrollerHeight+'px'}" >
      <div class="comment" ref="comment">
        <list>
            <cell v-for="(item, index) in commentRepays" :key="index">
              <CommentRepay
                :id="item.id"
                :author="item.authorName"
                :comment="item.content"
                :like="item.likes"
                :image="item.image"
                :updatedTime="item.updatedTime"
                :operation="item.operation"
                @likeCommentRepay="likeCommentRepay" />
            </cell>
            <!-- 上来加载更多 -->
            <loading @loading="loadMoreCommentRepay" :display="showmore?'show':'hide'" class="loading">
                <loading-indicator class="loading-icon"></loading-indicator>
                <text class="loading-text">{{load_more_text}}</text>
            </loading>
        </list>
      </div>
    </scroller>
    <div class="art-bottom">
      <BottomBar :forward="test.isforward"
                  :collection="relation.iscollection"
                  @onSubmit="saveCommentRepay"
                  @clickForward="forward"
                  @clickCollection="collection" />
    </div>
  </div>
</template>

<script>
import { WxcButton ,Utils } from 'weex-ui'
import TopBar from '@/compoents/bars/article_top_bar'
import BottomBar from '@/compoents/bars/article_bottom_bar'
import CommentRepay from '@/compoents/cells/comment_repay'
import Api from '@/apis/article/api'

const modal = weex.requireModule("modal")

export default {
  name: 'index',
  components:{TopBar, CommentRepay, BottomBar},
  props:["id", "reply"],
  data () {
    return {
      scrollerHeight:'500px',
      commentRepays: [], //文章评论回复
      test : {
          isforward : false
      },
      relation:{
          islike: false,
          isunlike: false,
          iscollection: false,
          isfollow: false,
          isforward:false
      },//关系
      time : {
          timer:null,//定时器
          timerStep:100,//定时器步长
          readDuration:0,//阅读时长
          percentage:0,//阅读比例
          loadDuration:0,//加载时长
          loadOff:true//加载完成控制
      },//时间相关属性
      minBehotTime: 20000000000000,
      showmore: false, // 是否显示loadmore动画
    }
  },
  created () {
    Api.setVue(this);
    this.loadCommentRepay();
   },
  destroyed () { },
  computed:{
      // 渲染加载最新和更多的国际化语言
      load_more_text:function(){return this.$lang.load_more_text}
  },
  mounted () { 
    this.scrollerHeight=(Utils.env.getPageHeight()-177)+'px';
  },
  methods: {
    scroller: function(e){
        let y = Math.abs(e.contentOffset.y)+(Utils.env.getPageHeight()-180)
        let height = e.contentSize.height
        this.time.percentage = Math.max(parseInt((y*100)/height),this.time.percentage)
    },
    loadCommentRepay() {
      this.minBehotTime = 20000000000000
      Api.loadCommentRepay(this.id, this.minBehotTime).then(d => {
          if (d.code==200) {
              this.commentRepays = d.data
          } else {
              modal.toast({message: d.errorMessage, duration: 3})
          }
      }).catch((e)=>{
          console.log(e)
      })
    },
    // 上拉加载更多
    loadMoreCommentRepay: function() {
        this.showmore = true;
        this.minBehotTime = this.commentRepays[this.commentRepays.length - 1].updatedTime

        Api.loadCommentRepay(this.id, this.minBehotTime).then(d => {
            this.showmore = false;
            if (d.code == 200) {
                if (d.data.length == 0) {
                    modal.toast({message: '没有数据了...', duration: 3})
                } else {
                    this.commentRepays = this.commentRepays.concat(d.data)
                }
            } else {
                modal.toast({message: d.errorMessage, duration: 3})
            }
        }).catch((e)=>{
            console.log(e)
        })
    },
    likeCommentRepay(commentRepayId, operation) {
      Api.likeCommentRepay(commentRepayId, operation).then(d => {
          if (d.code == 200) {
              let item = this.commentRepays.find((commentRepay) => {
                  return commentRepay.id === commentRepayId
              });
              item.operation = operation
              item.likes = d.data.likes

              let message = (operation === 0 ? "点赞" : "取消点赞" ) + "操作成功！"
              modal.toast({ message: message, duration: 3 })
          } else {
              modal.toast({message: d.errorMessage,duration: 3})
          }
      }).catch((e)=>{
          console.log(e)
      })
    },
    saveCommentRepay(content) {
      Api.saveCommentRepay(this.id, content).then(d => {
          if (d.code==200) {
              modal.toast({ message: '评论回复成功',duration:3})
              // 重新加载文章评论回复列表
              this.loadCommentRepay()
          } else {
              modal.toast({message: d.errorMessage,duration: 3})
          }
      }).catch((e)=>{
          console.log(e)
      })
    },
    // 转发
    forward : function(){
        this.tip()
        // Api.forward({articleId:this.id}).then(d=>{
        //     this.test.isforward = !this.test.isforward
        // }).catch((e)=>{
        //     console.log(e)
        // })
    },
    // 收藏
    collection : function(){
      this.tip()
        // Api.collection({articleId:this.id,publishedTime:this.date,operation:this.relation.iscollection?1:0}).then(d=>{
        //     if(d.code==0){
        //         this.relation.iscollection = !this.relation.iscollection
        //         let message = (this.relation.iscollection?"收藏":"取消收藏")+"操作成功！";
        //         modal.toast({ message:message,duration:3})
        //     }else{
        //         modal.toast({message: d.errorMessage,duration: 3})
        //     }
        // }).catch((e)=>{
        //     console.log(e)
        // })
    },
    tip: function(){
        modal.toast({ message:'该功能暂未实现！',duration:3})
    }
  },
}
</script>

<style scoped>
  .art-page{
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      width: 750px;
      flex-direction: column;
      font-family: "微软雅黑";
  }
  .art-top{}
  .art-bottom{
      bottom: 0;
      position: fixed;
      width: 750px;
  }
  .scroller{
      flex: 1;
      flex-direction: column;
      width: 750px;
      padding: 0px 29px;
      margin: 0px 0px 87px;
  }
  .comment {
    margin-top: 20px;
  }
</style>
