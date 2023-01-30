<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, viewport-fit=cover">
    <title>黑马头条</title>
    <!-- 引入样式文件 -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/vant@2.12.20/lib/index.css">
    <!-- 页面样式 -->
    <link rel="stylesheet" href="../../../plugins/css/index.css">
</head>

<body>
<div id="app">
    <div class="article">
        <van-row>
            <van-col span="24" class="article-title" v-html="title"></van-col>
        </van-row>

        <van-row type="flex" align="center" class="article-header">
            <van-col span="3">
                <van-image round class="article-avatar" src="https://p3.pstatp.com/thumb/1480/7186611868"></van-image>
            </van-col>
            <van-col span="16">
                <div v-html="authorName"></div>
                <div>{{ publishTime | timestampToDateTime }}</div>
            </van-col>
            <van-col span="5">
                <van-button round :icon="relation.isfollow ? '' : 'plus'" type="info" class="article-focus"
                            :text="relation.isfollow ? '取消关注' : '关注'" :loading="followLoading" @click="handleClickArticleFollow">
                </van-button>
            </van-col>
        </van-row>

        <van-row class="article-content">
            <#if content??>
                <#list content as item>
                    <#if item.type='text'>
                        <van-col span="24" class="article-text">${item.value}</van-col>
                    <#else>
                        <van-col span="24" class="article-image">
                            <van-image width="100%" src="${item.value}"></van-image>
                        </van-col>
                    </#if>
                </#list>
            </#if>
        </van-row>

        <van-row type="flex" justify="center" class="article-action">
            <van-col>
                <van-button round :icon="relation.islike ? 'good-job' : 'good-job-o'" class="article-like"
                            :loading="likeLoading" :text="relation.islike ? '取消赞' : '点赞'" @click="handleClickArticleLike"></van-button>
                <van-button round :icon="relation.isunlike ? 'delete' : 'delete-o'" class="article-unlike"
                            :loading="unlikeLoading" @click="handleClickArticleUnlike">不喜欢</van-button>
            </van-col>
        </van-row>

        <!-- 文章评论列表 -->
        <van-list v-model="commentsLoading" :finished="commentsFinished" finished-text="没有更多了"
                  @load="onLoadArticleComments">
            <van-row id="#comment-view" type="flex" class="article-comment" v-for="(item, index) in comments" :key="index">
                <van-col span="3">
                    <van-image round src="https://p3.pstatp.com/thumb/1480/7186611868" class="article-avatar"></van-image>
                </van-col>
                <van-col span="21">
                    <van-row type="flex" align="center" justify="space-between">
                        <van-col class="comment-author" v-html="item.authorName"></van-col>
                        <van-col>
                            <van-button round :icon="item.operation === 0 ? 'good-job' : 'good-job-o'" size="normal"
                                        @click="handleClickCommentLike(item)">{{ item.likes || '' }}
                            </van-button>
                        </van-col>
                    </van-row>

                    <van-row>
                        <van-col class="comment-content" v-html="item.content"></van-col>
                    </van-row>
                    <van-row type="flex" align="center">
                        <van-col span="10" class="comment-time">
                            {{ item.createdTime | timestampToDateTime }}
                        </van-col>
                        <van-col span="3">
                            <van-button round size="normal" v-html="item.reply" @click="showCommentRepliesPopup(item.id)">回复 {{
                                item.reply || '' }}
                            </van-button>
                        </van-col>
                    </van-row>
                </van-col>
            </van-row>
        </van-list>
    </div>
    <!-- 文章底部栏 -->
    <van-row type="flex" justify="space-around" align="center" class="article-bottom-bar">
        <van-col span="13">
            <van-field v-model="commentValue" placeholder="写评论">
                <template #button>
                    <van-button icon="back-top" @click="handleSaveComment"></van-button>
                </template>
            </van-field>
        </van-col>
        <van-col span="3">
            <van-button icon="comment-o" @click="handleScrollIntoCommentView"></van-button>
        </van-col>
        <van-col span="3">
            <van-button :icon="relation.iscollection ? 'star' : 'star-o'" :loading="collectionLoading"
                        @click="handleClickArticleCollection"></van-button>
        </van-col>
        <van-col span="3">
            <van-button icon="share-o"></van-button>
        </van-col>
    </van-row>

    <!-- 评论Popup 弹出层 -->
    <van-popup v-model="showPopup" closeable position="bottom"
               :style="{ width: '750px', height: '60%', left: '50%', 'margin-left': '-375px' }">
        <!-- 评论回复列表 -->
        <van-list v-model="commentRepliesLoading" :finished="commentRepliesFinished" finished-text="没有更多了"
                  @load="onLoadCommentReplies">
            <van-row id="#comment-reply-view" type="flex" class="article-comment-reply"
                     v-for="(item, index) in commentReplies" :key="index">
                <van-col span="3">
                    <van-image round src="https://p3.pstatp.com/thumb/1480/7186611868" class="article-avatar"></van-image>
                </van-col>
                <van-col span="21">
                    <van-row type="flex" align="center" justify="space-between">
                        <van-col class="comment-author" v-html="item.authorName"></van-col>
                        <van-col>
                            <van-button round :icon="item.operation === 0 ? 'good-job' : 'good-job-o'" size="normal"
                                        @click="handleClickCommentReplyLike(item)">{{ item.likes || '' }}
                            </van-button>
                        </van-col>
                    </van-row>

                    <van-row>
                        <van-col class="comment-content" v-html="item.content"></van-col>
                    </van-row>
                    <van-row type="flex" align="center">
                        <!-- TODO: js计算时间差 -->
                        <van-col span="10" class="comment-time">
                            {{ item.createdTime | timestampToDateTime }}
                        </van-col>
                    </van-row>
                </van-col>
            </van-row>
        </van-list>
        <!-- 评论回复底部栏 -->
        <van-row type="flex" justify="space-around" align="center" class="comment-reply-bottom-bar">
            <van-col span="13">
                <van-field v-model="commentReplyValue" placeholder="写评论">
                    <template #button>
                        <van-button icon="back-top" @click="handleSaveCommentReply"></van-button>
                    </template>
                </van-field>
            </van-col>
            <van-col span="3">
                <van-button icon="comment-o"></van-button>
            </van-col>
            <van-col span="3">
                <van-button icon="star-o"></van-button>
            </van-col>
            <van-col span="3">
                <van-button icon="share-o"></van-button>
            </van-col>
        </van-row>
    </van-popup>
</div>

<!-- 引入 Vue 和 Vant 的 JS 文件 -->
<script src=" https://cdn.jsdelivr.net/npm/vue/dist/vue.min.js">
</script>
<script src="https://cdn.jsdelivr.net/npm/vant@2.12.20/lib/vant.min.js"></script>
<!-- 引入 Axios 的 JS 文件 -->
<#--<script src="https://unpkg.com/axios/dist/axios.min.js"></script>-->
<script src="../../../plugins/js/axios.min.js"></script>
<!-- 页面逻辑 -->
<script src="../../../plugins/js/index.js"></script>
</body>

</html>