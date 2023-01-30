<template>
    <div class="list-item">
        <div class="special">
            <image class="hmg" src="/static/images/buttons/hot.png"/>
            <text class="special">热门专题</text>
        </div>

        <scroller class="tab-title-list"
                  show-scrollbar="false"
                  scroll-direction="horizontal">
            <div class="special-item">
                <image class="omg" src="/static/images/timg.jpg"/>
                <div class="special-info">
                    <text class="title">{{data.title}}</text>
                    <div class="item-l">
                        <div class="tags">
                            <text class="tags-text">{{data.source}}</text>
                            <text class="tags-text">评论 {{data.commit}}</text>
                            <text class="tags-text">{{formatDate(data.date)}}</text>
                        </div>
                    </div>
                </div>
            </div>
            <div class="special-item">
                <image class="omg" src="/static/images/timg.jpg"/>
                <div class="special-info">
                    <text class="title">{{data.title}}</text>
                    <div class="item-l">
                        <div class="tags">
                            <text class="tags-text">{{data.source}}</text>
                            <text class="tags-text">评论 {{data.commit}}</text>
                            <text class="tags-text">{{formatDate(data.date)}}</text>
                        </div>
                    </div>
                </div>
            </div>
        </scroller>
        <div class="more">
            <text class="more-info">查看更多热点事件</text>
            <image class="jmg" src="/static/images/buttons/jiatou.png"/>
        </div>
    </div>
</template>

<script>
    export default {
        name: "article_special",
        props:{
            data:{
                type:Object
            }
        },
        methods : {
            formatImage:function(img){
                if(img.indexOf('http')){
                    return "http://www.baidu.com/"+img;
                }else{
                    return img;
                }
            },
            formatDate:function(time){
                return this.$date.format13(time);
            },
            formatTitle:function(title){
                if(title.length>20){
                    return title.substring(0,19);
                }
                return title;
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/article';
    .list-item{
        height: 550px;
        margin-right: 0px;
        padding-right: 0px;
    }
    .tab-title-list {
        flex-direction: row;
        flex: 1;
    }
    .special{
        font-size: 42px;
        font-weight: bold;
        color:#000000;
        margin-bottom: 10px;
        flex-direction: row;
        align-items: center;
    }
    .special-info{
        flex-direction: column;
        position: absolute;
        bottom:50px;
    }
    .title{
        width: 650px;
    }
    .title,.tags-text{
        color:#ffffff;
    }
    .special-item{
        margin-right: 30px;
        border-radius: 15px;
        box-shadow: 5px -15px 5px #aaaaaa;
    }
    .omg {
        width: 666px;
        height: 370px;
        border-radius: 15px;
    }
    .more{
        width: 750px;
        flex-direction: row;
        justify-content:center;
        align-items: center;
    }
    .more-info{
        color: #666666;
    }
    .hmg{
        width: 48px;
        height: 63px;
        margin-right: 20px;
    }
    .jmg{
        width: 35px;
        height: 35px;
        margin-left: 15px;
    }
    .item-image{
        flex-direction: row;
        padding: 6px 10px 0px;
        justify-content: space-around;
    }
    .image{
        width: 225px;
    }
</style>
