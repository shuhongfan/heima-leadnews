<template>
    <div class="wrapper" @touchmove.stop>
        <div class="top-body" @touchmove.self><Home_Bar @onSubmit="onSubmit" :value="keyword"/></div>
        <div class="content-body" @touchmove.stop>
            <wxc-tab-page ref="wxc-tab-page" :showMore="false" :tab-titles="tabTitles" :tab-styles="tabStyles" title-type="text" :tab-page-height="tabPageHeight" @wxcTabPageCurrentTabSelected="wxcTabPageCurrentTabSelected">
                <list @touchmove.self v-for="(v,index) in tabList"  :key="index" class="item-container" :style="{ height: (tabPageHeight - tabStyles.height) + 'px' }">
                    <!-- 列表项，并绑定显示事件 -->
                    <cell v-for="(item,key) in v" class="cell" :key="key">
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
    import Home_Bar from "@/compoents/bars/search_result_top"
    import WxcTabPage from "@/compoents/tabs/home_tabs"
    import { Utils, BindEnv,WxcPanItem } from 'weex-ui'
    import Item0 from '../../compoents/cells/article_0.vue'
    import Item1 from '../../compoents/cells/article_1.vue'
    import Item3 from '../../compoents/cells/article_3.vue'
    import Config from './config'
    import Api from '@/apis/search_result/api'

    export default {
        name: 'HeiMa-Home',
        components: {Home_Bar,WxcTabPage, Item0,Item1,Item3,WxcPanItem},
        props:{
            keyword:''//当前搜索的关键字
        },
        data: () => ({
            api:null,// API
            showmore:true,//是否显示loadmore动画
            tabTitles: Config.tabTitles,//频道配置
            tabStyles: Config.tabStyles,//频道样式
            tabList: [...Array(Config.tabTitles.length).keys()].map(i => []),//列表数据集合
            tabPageHeight: 1334,//列表总高度
            params:{
                tag:"__all__",
                keyword:'',
                pageNum:1,
                pageSize:20,
                index:0,
                minBehotTime:20000000000000
            },

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
            this.$refs['wxc-tab-page'].setPage(0,null,true);
        },
        created () {
            // 初始化高度，顶部菜单高度120+顶部bar 90
            this.tabPageHeight = Utils.env.getPageHeight()-110;
            this.params.keyword = this.keyword;
            Api.setVue(this);

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
        },
        methods: {
            // 上拉加载更多
            loadmore:function(){
                this.showmore=true;
                this.params.pageNum=this.params.pageNum+1
                this.load();
            },
            // 正常加载数据
            load : function(){
                Api.article_search(this.params).then((d)=>{
                    this.tanfer(d.data);
                }).catch((e)=>{
                    console.log(e)
                })
            },
            // 列表数据转换成View需要的Model对象
            tanfer : function(data){
                if(data.length==200){
                    this.showmore=false;
                    modal.toast({message:'没有数据了...',duration:3})
                    return ;
                }
                let arr = []
                for(let i=0;i<data.length;i++){
                    let ims = []
                    if(data[i].images){
                        ims = data[i].images.replace(/[\[\]]/ig,'').split(',')

                    }
                    const { id: articleId, title, comment, authorId, authorName, publishTime } = data[i]
                    const querystr = `?token=${this.token}&equipmentId=${this.equipmentId}&articleId=${articleId}&title=${title}&authorId=${authorId}&authorName=${authorName}&publishTime=${publishTime}`
                    let tmp = {
                        id: articleId,
                        // title:data[i].h_title,
                       title:data[i].title,
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
                    if(this.params.minBehotTime>time){
                        this.params.minBehotTime=time;
                    }
                    arr.push(tmp);
                }
                let newList = [...Array(this.tabTitles.length).keys()].map(i => []);
                if(this.params.pageNum!=1){
                    arr = this.tabList[this.params.index].concat(arr);
                }else{
                    arr=arr.concat(this.tabList[this.params.index]);
                }
                newList[this.params.index] = arr;
                this.tabList = newList;
                this.showmore=false;
            },
            // 分类页切换事件
            wxcTabPageCurrentTabSelected (e) {
                this.params.pageNum=1
                this.params.index=e.page
                this.params.tag = Config.tabTitles[e.page]['id'];
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
            },
            onSubmit : function(val){
                this.params.keyword = val;
                this.tabList = [...Array(this.tabTitles.length).keys()].map(i => []);
                this.load();
            }
        }
    }
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
