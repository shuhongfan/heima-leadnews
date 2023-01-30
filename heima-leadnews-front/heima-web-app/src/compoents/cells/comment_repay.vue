<template>
    <div class="cwapper">
        <div class="cleft"><image :src="image" class="head"></image></div>
        <div class="cright">
            <div class="row">
                <text class="author">{{author}}</text>
                
            </div>
            <div class="comment">{{comment}}</div>
            <div class="row">
                <text class="time">{{formatDate(updatedTime)}}</text>
                <image class="img" :src="operation === 0 ? '/static/images/buttons/like_1.png' : '/static/images/buttons/like_0.png'" @click="likeCommentRepay"></image>
                <text class="like" @click="likeCommentRepay">{{like > 0 ? like : ''}}</text>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "comment_repay",
        props:["id", "author", "like", "image", "comment", "updatedTime", "operation"],
        methods: {
            formatDate:function(time){
                return this.$date.format13(time);
            },
            likeCommentRepay: function() {
                let tempOperation = this.operation === 0 ? 1 : 0
                this.$emit("likeCommentRepay", this.id, tempOperation)
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .cwapper{
        // width: 750px;
        flex-direction: row;
        margin-bottom: 20px;
    }
    .cleft{
        width: 56px;
    }
    .cright{
        flex: 1;
        padding-left: 15px;
        padding-right: 45px;
        flex-direction: column;
    }
    .row{
        flex-direction: row;
        align-items: center;
    }
    .author{
        flex: 1;
        font-size: 25px;
        color: #43649c;
    }
    .like{
        margin-left: 5px;
        font-size: 25px;
        color: #b5b5b5;
    }
    .head{
        width: 48px;
        height: 48px;
        border-radius: 48px;
    }
    .icon{
        color: #b5b5b5;
        font-size: 32px;
    }
    .comment{
        flex: 1;
        font-size: 32px;
        color: #3A3A3A;
        margin: 15px 0px;
    }
    .time{
        font-size: 21px;
        color: #212121;
    }
    .replay{
        background-color: #f7f7f7;
        border-radius: 11px;
        margin-left: 20px;
        padding: 5px 10px;
        font-size: 21px;
        color: #212121;
        vertical-align: center;
    }
    .img {
        width: 20px;
        height: 20px;
        margin-left: 350px;
    }
</style>
