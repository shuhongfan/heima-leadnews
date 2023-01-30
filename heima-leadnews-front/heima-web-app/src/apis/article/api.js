function Api(){
    var vue;
}
Api.prototype = {
    setVue : function(vue){
        this.vue = vue;
    },
    // 保存展现行为数据
    loadinfo : function(articleId){
        let url = this.vue.$config.urls.get('load_article_info')
        return new Promise((resolve, reject) => {
            this.vue.$request.post(url,{articleId:articleId}).then((d)=>{
                resolve(d);
            }).catch((e)=>{
                reject(e);
            })
        })
    },
    // 加载文章关系信息
    loadbehavior: function(articleId,authorId){
        let url = this.vue.$config.urls.get('load_article_behavior')
        return this.vue.$store.getEquipmentId().then(equipmentId=>{
            return new Promise((resolve, reject) => {
                this.vue.$request.post(url,{equipmentId:equipmentId,articleId:articleId,authorId:authorId}).then((d)=>{
                    resolve(d);
                }).catch((e)=>{
                    reject(e);
                })
            })
        }).catch(e=>{
            return new Promise((resolve, reject) => {
                reject(e);
            })
        })
    },
    // 喜欢、点赞
    like : function(data){
        let url = this.vue.$config.urls.get('like_behavior')
        return this.vue.$store.getEquipmentId().then(equipmentId=>{
            return new Promise((resolve, reject) => {
                this.vue.$request.post(url,{equipmentId:equipmentId,articleId:data.articleId,type:0,operation:data.operation}).then((d)=>{
                    resolve(d);
                }).catch((e)=>{
                    reject(e);
                })
            })
        }).catch(e=>{
            return new Promise((resolve, reject) => {
                reject(e);
            })
        })
    },
    // 不喜欢
    unlike : function(data){
        let url = this.vue.$config.urls.get('unlike_behavior')
        return this.vue.$store.getEquipmentId().then(equipmentId=>{
            return new Promise((resolve, reject) => {
                this.vue.$request.post(url,{equipmentId:equipmentId,articleId:data.articleId,type:data.type}).then((d)=>{
                    resolve(d);
                }).catch((e)=>{
                    reject(e);
                })
            })
        }).catch(e=>{
            return new Promise((resolve, reject) => {
                reject(e);
            })
        })
    },
    // 不喜欢
    read : function(data){
        let url = this.vue.$config.urls.get('read_behavior')
        return this.vue.$store.getEquipmentId().then(equipmentId=>{
            return new Promise((resolve, reject) => {
                this.vue.$request.post(url,{
                    equipmentId:equipmentId,
                    articleId:data.articleId,
                    count:1,
                    readDuration:data.readDuration,
                    percentage:data.percentage,
                    loadDuration:data.loadDuration
                }).then((d)=>{
                    resolve(d);
                }).catch((e)=>{
                    reject(e);
                })
            })
        }).catch(e=>{
            return new Promise((resolve, reject) => {
                reject(e);
            })
        })
    },
    // 收藏
    collection : function(data){
        let url = this.vue.$config.urls.get('collection_behavior')
        return this.vue.$store.getEquipmentId().then(equipmentId=>{
            return new Promise((resolve, reject) => {
                this.vue.$request.post(url,{
                    equipmentId:equipmentId,
                    entryId:data.articleId,
                    published_time:data.publishedTime,
                    type:0,
                    operation:data.operation
                }).then((d)=>{
                    resolve(d);
                }).catch((e)=>{
                    reject(e);
                })
            })
        }).catch(e=>{
            return new Promise((resolve, reject) => {
                reject(e);
            })
        })
    },
    // 转发
    forward : function(data){
        let url = this.vue.$config.urls.get('forward_behavior')
        return this.vue.$store.getEquipmentId().then(equipmentId=>{
            return new Promise((resolve, reject) => {
                this.vue.$request.post(url,{
                    equipmentId:equipmentId,
                    articleId:data.articleId
                }).then((d)=>{
                    resolve(d);
                }).catch((e)=>{
                    reject(e);
                })
            })
        }).catch(e=>{
            return new Promise((resolve, reject) => {
                reject(e);
            })
        })
    },
    // 分享
    share : function(data){
        let url = this.vue.$config.urls.get('share_behavior')
        return this.vue.$store.getEquipmentId().then(equipmentId=>{
            return new Promise((resolve, reject) => {
                this.vue.$request.post(url,{
                    equipmentId:equipmentId,
                    articleId:data.articleId,
                    type:data.type
                }).then((d)=>{
                    resolve(d);
                }).catch((e)=>{
                    reject(e);
                })
            })
        }).catch(e=>{
            return new Promise((resolve, reject) => {
                reject(e);
            })
        })
    }
    ,
    // 关注
    follow : function(data){
        let url = this.vue.$config.urls.get('user_follow')
        return new Promise((resolve, reject) => {
            this.vue.$request.post(url,{
                authorId:data.authorId,
                operation:data.operation,
                articleId:data.articleId
            }).then((d)=>{
                resolve(d);
            }).catch((e)=>{
                reject(e);
            })
        })
    },
    // 文章评论列表
    loadComment: function (articleId, minLikes) {
        let url = this.vue.$config.urls.get('load_comment')
        return new Promise((resolve, reject) => {
        this.vue.$request
            .post(url, { articleId: articleId, minLikes: minLikes })
            .then((d) => {
            resolve(d)
            })
            .catch((e) => {
            reject(e)
            })
        })
    },
    // 文章评论点赞
    likeComment: function (commentId, operation) {
        let url = this.vue.$config.urls.get('like_comment')
        return new Promise((resolve, reject) => {
        this.vue.$request
            .post(url, { commentId: commentId, operation: operation })
            .then((d) => {
            resolve(d)
            })
            .catch((e) => {
            reject(e)
            })
        })
    },
    // 保存文章评论
    saveComment: function (articleId, content) {
        let url = this.vue.$config.urls.get('save_comment')
        return new Promise((resolve, reject) => {
        this.vue.$request
            .post(url, { articleId: articleId, content: content })
            .then((d) => {
            resolve(d)
            })
            .catch((e) => {
            reject(e)
            })
        })
    },
    // 文章评论回复列表
    loadCommentRepay: function (commentId, minLikes) {
        let url = this.vue.$config.urls.get('load_comment_repay')
        return new Promise((resolve, reject) => {
        this.vue.$request
            .post(url, { commentId: commentId, minLikes: minLikes })
            .then((d) => {
            resolve(d)
            })
            .catch((e) => {
            reject(e)
            })
        })
    },
    // 文章评论回复点赞
    likeCommentRepay: function (commentRepayId, operation) {
        let url = this.vue.$config.urls.get('like_comment_repay')
        return new Promise((resolve, reject) => {
        this.vue.$request
            .post(url, { commentRepayId: commentRepayId, operation: operation })
            .then((d) => {
            resolve(d)
            })
            .catch((e) => {
            reject(e)
            })
        })
    },
    // 保存文章评论回复
    saveCommentRepay: function (commentId, content) {
        let url = this.vue.$config.urls.get('save_comment_repay')
        return new Promise((resolve, reject) => {
        this.vue.$request
            .post(url, { commentId: commentId, content: content })
            .then((d) => {
            resolve(d)
            })
            .catch((e) => {
            reject(e)
            })
        })
    }
}

export default new Api()
