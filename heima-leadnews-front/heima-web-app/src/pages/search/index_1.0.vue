<template>
    <div class="art-page" @touchmove.stop>
        <div class="art-top"><TopBar @onBlur="onBlur" @onInput="onInput"/></div>
        <scroller @touchmove.self class="scroller" :style="{'height':scrollerHeight}">
            <template v-for="item in data.history">
                <SearchHistory @onClickText="doSearch" @onDeleteHistory="onDeleteHistory" :id="item.id" :title="item.keyword"/>
            </template>
            <a href="#" class="all-search">
                <text class="all-search-text">全部搜索记录</text>
            </a>
        </scroller>
        <div class="art-tip" v-if="showTip" ref="tip"><SearchTip @onSelect="doSearch" :search="data.keyword" :data="data.tip"/></div>
    </div>
</template>

<script>
    import TopBar from '@/compoents/bars/search_top'
    import SearchHistory from '@/compoents/cells/search_0'
    import SearchTip from '@/compoents/inputs/search_tip'
    import HotCell from '@/compoents/cells/search_1'
    import Title from '@/compoents/titles/title'
    import Api from '@/apis/search/api'
    import { Utils } from 'weex-ui'
    const modal = weex.requireModule("modal")
    export default {
        name: "index",
        components:{TopBar,SearchHistory,Title,HotCell,SearchTip},
        data(){
            return {
                scrollerHeight:'500px',
                showTip:false,
                icon : {
                    hot : '\uf06d',
                    other:'\uf17d'
                },
                data : {
                    keyword:'',//当前输入的关键字
                    history : [],//搜索历史
                    tip : [],// 联想词
                    hot : []//热搜关键字
                }
            }
        },
        created(){
            Api.setVue(this)
        },
        mounted(){
            this.scrollerHeight=(Utils.env.getPageHeight()-180)+'px';
            this.load_search_history()
            this.load_hot_keywords()
        },
        methods:{
            doSearch : function(val){
                this.$router.push({name:'search_result',params:{'keyword':val}})
            },
            // 加载搜索历史
            load_search_history : function(){
                Api.load_search_history().then(data=>{
                    if(data.code==200){
                        this.data.history = data.data
                    }else{
                        modal.toast({message: data.errorMessage,duration: 3})
                    }
                }).catch((e)=>{
                    console.log(e)
                })
            },
            // 删除历史搜搜关键字
            onDeleteHistory : function(id){
                let _this = this;
                modal.confirm({message:'确认要删除吗？'},function(button) {
                    if(button=='OK') {
                        Api.del_search(id).then(data => {
                            if (data.code == 200) {
                                modal.toast({message: '删除成功', duration: 3})
                                _this.load_search_history()
                            } else {
                                modal.toast({message: data.errorMessage, duration: 3})
                            }
                        }).catch((e) => {
                            console.log(e)
                        })
                    }
                })
            },
            //用户输入时，提示联想词
            onInput : function(val){
                Api.associate_search(val).then(data => {
                    if (data.code == 200) {
                        this.data.keyword=val
                        this.showTip = true
                        this.data.tip=data.data
                    }
                })
            },
            // 加载热搜关键字
            load_hot_keywords : function(){
                Api.load_hot_keywords().then(data=>{
                    if(data.code==200){
                        // 需要转换数据格式
                        let newData=[]
                        let temp = []
                        for(var i=0;i<data.data.length;i++){
                            if(i>0&&i%2==0){
                                newData.push(temp)
                                temp = []
                            }
                            temp.push(data.data[i])
                        }
                        this.data.hot = newData
                    }else{
                        modal.toast({message: data.errorMessage,duration: 3})
                    }
                }).catch((e)=>{
                    console.log(e)
                })
            },
            // 失去焦点，关闭联想词
            onBlur : function(){
                this.showTip=false
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .art-page{
        width: 750px;
        flex-direction: column;
        background-color: #ffffff;
    }
    .art-tip{
        position: absolute;
        top: 100px;
        width: 750px;
        z-index: 999;
    }
    .art-top{
        top: 0px;
        z-index: 999;
        position: fixed;
        height: 90px;
    }
    .scroller{
        flex: 1;
        flex-direction: column;
        width: 750px;
        padding-top: 90px;
    }
    .all-search{
        font-size: 36px;
        align-items: center;
        padding: 18px 20px;
        background-color: #ffffff;
    }
    .all-search-text{
        color: #bdbdbd;
        font-family: "微软雅黑";
        font-size: 32px;
    }
    .item{
        flex-direction: row;
    }
</style>
