// 初始化 Vue 实例
new Vue({
  el: '#app',
  data() {
    return {
      // Minio模板应该写真实接口地址
      baseUrl: 'http://192.168.200.150:51601', //'http://172.16.17.191:5001',
      token: '',
      equipmentId: '',
      articleId: '',
      title: '',
      authorId: 0,
      authorName: '',
      publishTime: '',
      relation: {
        islike: false,
        isunlike: false,
        iscollection: false,
        isfollow: false,
        isforward: false
      },
      followLoading: false,
      likeLoading: false,
      unlikeLoading: false,
      collectionLoading: false,
      // 评论
      comments: [],
      commentsLoading: false,
      commentsFinished: false,
      commentValue: '',
      currentCommentId: '',
      // 评论回复
      commentReplies: [],
      commentRepliesLoading: false,
      commentRepliesFinished: false,
      commentReplyValue: '',
      showPopup: false
    }
  },
  filters: {
    // TODO: js计算时间差
    timestampToDateTime: function (value) {
      if (!value) return ''

      const date = new Date(value)
      const Y = date.getFullYear() + '-'
      const M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
      const D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' '
      const h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':'
      const m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':'
      const s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds())

      return Y + M + D + h + m + s
    }
  },
  created() {
    this.token = this.getQueryVariable('token')
    this.equipmentId = this.getQueryVariable('equipmentId')
    this.articleId = this.getQueryVariable('articleId')
    this.title = this.getQueryVariable('title')
    const authorId = this.getQueryVariable('authorId')
    if (authorId) {
      this.authorId = parseInt(authorId, 10)
    }
    this.authorName = this.getQueryVariable('authorName')
    const publishTime = this.getQueryVariable('publishTime')
    if (publishTime) {
      this.publishTime = parseInt(publishTime, 10)
    }
    this.loadArticleBehavior()
	this.readArticleBehavior()
  },
  
  methods: {
    // 加载文章评论
    async loadArticleComments(index = 1, minDate = 20000000000000) {
      const url = `${this.baseUrl}/comment/api/v1/comment/load`
      const data = { articleId: this.articleId, index: index, minDate: minDate }
      const config = { headers: { 'token': this.token } }

      try {
        const { status, data: { code, errorMessage, data: comments } } = await axios.post(url, data, config)
        if (status !== 200) {
          vant.Toast.fail('当前系统正在维护，请稍后重试')
          return
        }
        if (code !== 200) {
          vant.Toast.fail(errorMessage)
          return
        }
        if (comments.length) {
          this.comments = this.comments.concat(comments)
        }

        // 加载状态结束
        this.commentsLoading = false;

        // 数据全部加载完成
        if (!comments.length) {
          this.commentsFinished = true
        }
      } catch (err) {
        this.commentsLoading = false
        this.commentsFinished = true
        console.log('err: ' + err)
      }
    },
    // 滚动加载文章评论
    onLoadArticleComments() {
      let index = undefined
      let minDate = undefined
      if (this.comments.length) {
        index = 2
        minDate = this.comments[this.comments.length - 1].createdTime
      }
      this.loadArticleComments(index, minDate)
    },
    // 加载文章行为
    async loadArticleBehavior() {
      const url = `${this.baseUrl}/article/api/v1/article/load_article_behavior/`
      const data = { equipmentId: this.equipmentId, articleId: this.articleId, authorId: this.authorId }
      const config = { headers: { 'token': this.token } }

      try {
        const { status, data: { code, errorMessage, data: relation } } = await axios.post(url, data, config)
        if (status !== 200) {
          vant.Toast.fail('当前系统正在维护，请稍后重试')
          return
        }
        if (code !== 200) {
          vant.Toast.fail(errorMessage)
          return
        }
        this.relation = relation
      } catch (err) {
        console.log('err: ' + err)
      }
    },
	//阅读文章行为
	async readArticleBehavior(){
		const url = `${this.baseUrl}/behavior/api/v1/read_behavior`
		const data = {equipmentId:this.equipmentId,articleId:this.articleId,count:1,readDuration:0,percentage:0,loadDuration:0}
		const config = {headers:{'token':this.token}}
		
		try{
			const {status,data:{code,errorMessage}} = await axios.post(url,data,config)
			if(status !== 200){
				vant.Toast.fail("当前系统正在维护，请稍后重试")
				return
			}
			if(code !== 0){
				vant.Toast.fail(errorMessage)
				return
			}
		}catch (err){
			console.log('err: '+ err)
		}
	},
    // 关注/取消关注
    async handleClickArticleFollow() {
      const url = `${this.baseUrl}/user/api/v1/user/user_follow/`
      const data = { authorId: this.authorId, operation: this.relation.isfollow ? 1 : 0, articleId: this.articleId }
      const config = { headers: { 'token': this.token } }

      this.followLoading = true
      try {
        const { status, data: { code, errorMessage } } = await axios.post(url, data, config)
        if (status !== 200) {
          vant.Toast.fail('当前系统正在维护，请稍后重试')
          return
        }
        if (code !== 200) {
          vant.Toast.fail(errorMessage)
          return
        }
        this.relation.isfollow = !this.relation.isfollow
        vant.Toast.success(this.relation.isfollow ? '成功关注' : '成功取消关注')
      } catch (err) {
        console.log('err: ' + err)
      }
      this.followLoading = false
    },
    // 点赞/取消赞
    async handleClickArticleLike() {
      const url = `${this.baseUrl}/behavior/api/v1/likes_behavior/`
      const data = { equipmentId: this.equipmentId, articleId: this.articleId, type: 0, operation: this.relation.islike ? 1 : 0 }
      const config = { headers: { 'token': this.token } }

      this.likeLoading = true
      try {
        const { status, data: { code, errorMessage } } = await axios.post(url, data, config)
        if (status !== 200) {
          vant.Toast.fail('当前系统正在维护，请稍后重试')
          return
        }
        if (code !== 200) {
          vant.Toast.fail(errorMessage)
          return
        }
        this.relation.islike = !this.relation.islike
        vant.Toast.success(this.relation.islike ? '点赞操作成功' : '取消点赞操作成功')
      } catch (err) {
        console.log('err: ' + err)
      }
      this.likeLoading = false
    },
    // 不喜欢/取消不喜欢
    async handleClickArticleUnlike() {
      const url = `${this.baseUrl}/behavior/api/v1/un_likes_behavior/`
      const data = { equipmentId: this.equipmentId, articleId: this.articleId, type: this.relation.isunlike ? 1 : 0 }
      const config = { headers: { 'token': this.token } }

      this.unlikeLoading = true
      try {
        const { status, data: { code, errorMessage } } = await axios.post(url, data, config)
        if (status !== 200) {
          vant.Toast.fail('当前系统正在维护，请稍后重试')
          return
        }
        if (code !== 200) {
          vant.Toast.fail(errorMessage)
          return
        }
        this.relation.isunlike = !this.relation.isunlike
        vant.Toast.success(this.relation.isunlike ? '不喜欢操作成功' : '取消不喜欢操作成功')
      } catch (err) {
        console.log('err: ' + err)
      }
      this.unlikeLoading = false
    },
    // 提交评论
    async handleSaveComment() {
      if (!this.commentValue) {
        vant.Toast.fail('评论内容不能为空')
        return
      }
      if (this.commentValue.length > 140) {
        vant.Toast.fail('评论字数不能超过140字')
        return
      }
      const url = `${this.baseUrl}/comment/api/v1/comment/save`
      const data = { articleId: this.articleId, content: this.commentValue }
      const config = { headers: { 'token': this.token } }

      try {
        const { status, data: {  code, errorMessage } } = await axios.post(url, data, config)
        if (status !== 200) {
          vant.Toast.fail('当前系统正在维护，请稍后重试')
          return
        }
        if (code !== 200) {
          vant.Toast.fail(errorMessage)
          return
        }
        vant.Toast.success('评论成功')
        this.commentValue = ''
		this.comments = []
        this.loadArticleComments()
		this.commentsFinished = false;
      } catch (err) {
        console.log('err: ' + err)
      }
    },
    // 页面滚动到评论区
    handleScrollIntoCommentView() {
      document.getElementById('#comment-view').scrollIntoView({ behavior: 'smooth' })
    },
    // 收藏/取消收藏
    async handleClickArticleCollection() {
      const url = `${this.baseUrl}/article/api/v1/collection_behavior/`
      const data = { equipmentId: this.equipmentId, entryId: this.articleId, publishedTime: this.publishTime, type: 0, operation: this.relation.iscollection ? 1 :0 }
      const config = { headers: { 'token': this.token } }

      this.collectionLoading = true
      try {
        const { status, data: { code, errorMessage } } = await axios.post(url, data, config)
        if (status !== 200) {
          vant.Toast.fail('当前系统正在维护，请稍后重试')
          return
        }
        if (code !== 200) {
          vant.Toast.fail(errorMessage)
          return
        }
        this.relation.iscollection = !this.relation.iscollection
        vant.Toast.success(this.relation.iscollection ? '收藏操作成功' : '取消收藏操作成功')
      } catch (err) {
        console.log('err: ' + err)
      }
      this.collectionLoading = false
    },
    // 评论点赞
    async handleClickCommentLike(comment) {
      const commentId = comment.id
      const operation = comment.operation === 0 ? 1 : 0

      const url = `${this.baseUrl}/comment/api/v1/comment/like`
      const data = { commentId: comment.id, operation: operation }
      const config = { headers: { 'token': this.token } }

      try {
        const { status, data: { code, errorMessage, data: { likes } } } = await axios.post(url, data, config)
        if (status !== 200) {
          vant.Toast.fail('当前系统正在维护，请稍后重试')
          return
        }
        if (code !== 200) {
          vant.Toast.fail(errorMessage)
          return
        }
        const item = this.comments.find((item) => {
          return item.id === commentId
        })
        item.operation = operation
        item.likes = likes
        vant.Toast.success((operation === 0 ? '点赞' : '取消点赞') + '操作成功！')
      } catch (err) {
        console.log('err: ' + err)
      }
    },
    // 弹出评论回复Popup
    showCommentRepliesPopup(commentId) {
      this.showPopup = true;
      this.currentCommentId = commentId
      this.commentReplies = []
      this.commentRepliesFinished = false
    },
    // 加载评论回复
    async loadCommentReplies(minDate = 20000000000000) {
      const url = `${this.baseUrl}/comment/api/v1/comment_repay/load`
      const data = { commentId: this.currentCommentId, 'minDate':  minDate}
      const config = { headers: { 'token': this.token } }

      try {
        const { status, data: { code, errorMessage, data: commentReplies } } = await axios.post(url, data, config)
        if (status !== 200) {
          vant.Toast.fail('当前系统正在维护，请稍后重试')
          return
        }
        if (code !== 200) {
          vant.Toast.fail(errorMessage)
          return
        }
        if (commentReplies.length) {
          this.commentReplies = this.commentReplies.concat(commentReplies)
        }

        // 加载状态结束
        this.commentRepliesLoading = false;

        // 数据全部加载完成
        if (!commentReplies.length) {
          this.commentRepliesFinished = true
        }
      } catch (err) {
        this.commentRepliesLoading = false
        this.commentRepliesFinished = true
        console.log('err: ' + err)
      }
    },
    // 滚动加载评论回复
    onLoadCommentReplies() {
      let minDate = undefined
      if (this.commentReplies.length) {
        minDate = this.commentReplies[this.commentReplies.length - 1].createdTime
      }
      this.loadCommentReplies(minDate)
    },
    // 提交评论回复
    async handleSaveCommentReply() {
      if (!this.commentReplyValue) {
        vant.Toast.fail('评论内容不能为空')
        return
      }
      if (this.commentReplyValue.length > 140) {
        vant.Toast.fail('评论字数不能超过140字')
        return
      }
      const url = `${this.baseUrl}/comment/api/v1/comment_repay/save`
      const data = { commentId: this.currentCommentId, content: this.commentReplyValue }
      const config = { headers: { 'token': this.token } }

      try {
        const { status, data: {  code, errorMessage } } = await axios.post(url, data, config)
        if (status !== 200) {
          vant.Toast.fail('当前系统正在维护，请稍后重试')
          return
        }
        if (code !== 200) {
          vant.Toast.fail(errorMessage)
          return
        }
        vant.Toast.success('评论成功')
        this.commentReplyValue = ''
		this.commentReplies = []
		this.comments = []
        // 刷新评论回复列表
        this.loadCommentReplies()
        // 刷新文章评论列表
        this.loadArticleComments()
      } catch (err) {
        console.log('err: ' + err)
      }
    },
    // 评论回复点赞
    async handleClickCommentReplyLike(commentReply) {
      const commentReplyId = commentReply.id
      const operation = commentReply.operation === 0 ? 1 : 0

      const url = `${this.baseUrl}/comment/api/v1/comment_repay/like`
      const data = { commentRepayId: commentReplyId, 'operation': operation }
      const config = { headers: { 'token': this.token } }

      try {
        const { status, data: { code, errorMessage, data: { likes } } } = await axios.post(url, data, config)
        if (status !== 200) {
          vant.Toast.fail('当前系统正在维护，请稍后重试')
          return
        }
        if (code !== 200) {
          vant.Toast.fail(errorMessage)
          return
        }
        const item = this.commentReplies.find((item) => {
          return item.id === commentReplyId
        })
        item.operation = operation
        item.likes = likes
        vant.Toast.success((operation === 0 ? '点赞' : '取消点赞') + '操作成功！')
      } catch (err) {
        console.log('err: ' + err)
      }
    },
    getQueryVariable(aVariable) {
      const query = decodeURI(window.location.search).substring(1)
      const array = query.split('&')
      for (let i = 0; i < array.length; i++) {
        const pair = array[i].split('=')
        if (pair[0] == aVariable) {
          return pair[1]
        }
      }
      return undefined
    },
    // onSelect(option) {
    //   vant.Toast(option.name);
    //   this.showShare = false;
    // }
  }
})
