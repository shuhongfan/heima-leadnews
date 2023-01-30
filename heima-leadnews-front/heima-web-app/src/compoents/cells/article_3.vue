<template>
    <div class="list-item">
        <!-- <text class="title">{{formatTitle(data.h_title)}}</text> -->
        <text class="title" v-html="data.title">3333</text>
        <div class="item-image">
            <!-- <image class="image" v-for="img in data.image" :src="formatImage(img)"></image> -->
            <image class="image" v-for="img in data.image" :src="formatImage(img)"></image>
        </div>
        <div class="item-l">
            <div class="tags">
                <text class="tags-text tags-icon">置顶</text>
                <text class="tags-text">{{data.source}}</text>
                <text class="tags-text">评论 {{data.commit}}</text>
                <text class="tags-text">{{formatDate(data.date)}}</text>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "article_3",
        props:{
            data:{
                type:Object
            }
        },
        methods : {
            formatImage:function(img){
                if(!img.startsWith('http')){
                    return "https://hmtt128.oss-cn-shanghai.aliyuncs.com/"+img;
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
        height: @list-2-height;
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
