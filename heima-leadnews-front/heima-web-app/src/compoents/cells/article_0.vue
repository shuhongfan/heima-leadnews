<template>
    <div class="list-item">
        <text class="title" v-html="data.title">2222</text>
        <div class="tags">
            <text class="tags-text tags-icon">置顶</text>
            <text class="tags-text">{{data.source}}</text>
            <text class="tags-text">评论 {{data.commit}}</text>
            <text class="tags-text date">{{formatDate(data.date)}}</text>
        </div>
    </div>
</template>

<script>
    export default {
        name: "article_0",
        props:{
            data:{
                type:Object
            }
        },
        methods : {
            formatDate:function(time){
                return this.$date.format13(time);
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/article';
    .list-item{
        padding-top: 8px;
        padding-bottom:20px;
    }
    .title{
        flex: none;
    }
</style>
