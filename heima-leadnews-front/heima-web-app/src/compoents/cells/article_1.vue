<template>
    <div class="list-item">
        <div class="list-lr">
            <div class="item-l">
                <!-- <text class="title" :style="flexStyle(data.h_title)">{{formatTitle(data.h_title)}}</text> -->
                <text class="title" v-html="data.title">1111</text>
                <div class="tags">
                    <text class="tags-text tags-icon">置顶</text>
                    <text class="tags-text">{{data.source}}</text>
                    <text class="tags-text">评论 {{data.commit}}</text>
                </div>
            </div>
            <div class="item-r">
                <!-- <image class="image" :src="formatImage(data.image[0])"/> -->
                <image class="image" :src="formatImage(data.image[0])"/>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "article_1",
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
                if(title.length>32){
                    return title.substring(0,31);
                }
                return title;
            },
            flexStyle : function(title){
                if(title.length>12){
                    return {flex:1}
                }
                return {flex:'none',marginBottom:'-5px'};
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/article';
    .list-lr{
        flex-direction: row;
        justify-content: space-around;
    }
    .item-l{
        flex: 1;
    }
    .item-r{
        width: 180px;
        margin: @list-tb-margin @list-lr-margin;
    }
</style>
